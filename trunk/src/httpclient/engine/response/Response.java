package httpclient.engine.response;

import httpclient.engine.general.*;
import httpclient.gui.*;
import java.io.*;

public class Response implements HTTPHeaders{

    private InputStream in;
    private StartingLine startingLine;
    private HeaderList headerList;
    private String message;
    private BrowserWindow browserWindow;
    private InfoWindow infoWindow;

    public Response(InputStream is, BrowserWindow browserWindow,
            InfoWindow infoWindow){
        this.browserWindow = browserWindow;
        this.infoWindow = infoWindow;
        this.in = is;
    }

    private String readLine() throws IOException{
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int b;
        boolean crlfFound = false;
        while((b=in.read())>0){
            if(b=='\r'){
                if((b=in.read())=='\n'){
                    crlfFound = true;
                    break;
                }
                baos.write('\r');
            }
            baos.write(b);
        }
        if(baos.size()==0 && !crlfFound) {
            throw new IOException("no bytes to read!");
        }
        return baos.toString();
    }

    private String readMessage(int contentLength, boolean progressDisabled)
            throws IOException {
        int b;
        int downloaded = 0;
        String messageBody = "";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        while(contentLength!=downloaded) {
            b=in.read();
            if(b==-1){
                System.out.println(b);
                break;
            }
            baos.write(b);
            downloaded++;
            if(!progressDisabled){
                float p = (float)downloaded/(float)contentLength*100;
                browserWindow.setProgressValue(Math.round(p));
            }
        }
        messageBody = baos.toString();
        return messageBody;
    }

    private String readChunkedMessage(boolean progressDisabled) throws IOException {
        StringBuilder sb = new StringBuilder();
        // read first chunk size and CRLF
        int chunkSize;
        try{
            chunkSize = Integer.parseInt(readLine().trim(), 16);
        } catch (NumberFormatException e){
            throw new IOException("can't read first chunk size");
        }
        while(chunkSize>0){
            // read chunk data
            String chunkData = readMessage(chunkSize, progressDisabled);
            sb.append(chunkData);
            // read CRLF
            readLine();
            // read chunk size and CRLF
            try{
                chunkSize = Integer.parseInt(readLine().trim(), 16);
            } catch(NumberFormatException e){
                throw new IOException("can't read chunk size");
            }
        }
        return sb.toString();
    }

    private StartingLine parseStartingLine(final String line)
            throws IOException {

        int minor, major, code;
        String version;

        String[] splittedLine = line.split(" ", 3);
        if(splittedLine.length!=3){
            throw new IOException("wrong format of starting line");
        }
        if(splittedLine[0].charAt(4)=='/'){
            version = splittedLine[0].substring(5, splittedLine[0].length());
        } else {
            throw new IOException("wrong version format");
        }
        try{
            minor = Integer.parseInt(version.split("\\.", 2)[0]);
            major = Integer.parseInt(version.split("\\.", 2)[1]);
        } catch (NumberFormatException exc){
            throw new IOException("wrong version format");
        }
        try{
            code = Integer.parseInt(splittedLine[1]);
        } catch (NumberFormatException exc){
            throw new IOException("wrong status code format");
        }
        return new StartingLine(major, minor, code, splittedLine[2]);
    }

    private Header parseHeader(String header) throws IOException {
        String name, value;
        int colonIndex;
        colonIndex = header.indexOf(':');
        name = header.substring(0, colonIndex).toLowerCase();
        value = header.substring(colonIndex+1, header.length()).trim();
        return new Header(name, value);
    }

   public void read() throws IOException {
        // get starting line
        String newLine = readLine();
        startingLine = parseStartingLine(newLine);
        infoWindow.updateStatus(startingLine);
        // get headers
        headerList = new HeaderList();
        String completeHeader = "";
        newLine = readLine();
        while(newLine.length()!=0){ // пока новая строка не пустая
            completeHeader += newLine; // плюсуем новую строку к прошлой
            newLine = readLine(); // читаем новую строку
            if(!newLine.startsWith(" ")){ //если на новой строке новый хедер
                // парсим прошлую строку
                headerList.addHeader(parseHeader(completeHeader));
                completeHeader = "";
            } 
        }
        // update header table
        infoWindow.updateHeaders(headerList);
        // progressbar routine
        boolean progressDisabled = false;
        boolean chunked = false;
        int contentLength = -1;
        if(!headerList.hasHeader(HEADER_CONTENT_LENGTH)){
            progressDisabled = true;
        } else {
            contentLength = Integer.parseInt(
                    headerList.getHeader(HEADER_CONTENT_LENGTH).getValue());
        }
        if(headerList.hasHeader(TRANSFER_ENCODING)){
            if(headerList.getHeader(
                    TRANSFER_ENCODING).getValue().equals("chunked")){
                progressDisabled = false;
                chunked = true;
            }
        }
        if(chunked){
            message = readChunkedMessage(progressDisabled);
        } else {
            message = readMessage(contentLength, progressDisabled);
        }

        browserWindow.setTextPaneValue(message);
        browserWindow.setProgressValue(100);
    } 
}
