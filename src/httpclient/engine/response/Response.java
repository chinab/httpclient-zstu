package httpclient.engine.response;

import httpclient.engine.ui.InfoWindow;
import httpclient.engine.ui.BrowserWindow;
import httpclient.engine.PreferenceList;
import httpclient.engine.general.*;
import httpclient.engine.general.MIMETypes;
import httpclient.engine.general.cookie.CookieProcessor;
import java.io.*;
import javax.swing.JFileChooser;

public class Response implements HTTPHeaders{

    private InputStream in;
    private String resourceName;
    private StartingLine startingLine;
    private HeaderList headerList;
    private BrowserWindow browserWindow;
    private InfoWindow infoWindow;
    private PreferenceList preferences;
    private CookieProcessor cp;

    public Response(InputStream is, String resourceName, CookieProcessor cp,
            PreferenceList preferences, BrowserWindow browserWindow,
            InfoWindow infoWindow){
        this.resourceName = resourceName;
        this.browserWindow = browserWindow;
        this.infoWindow = infoWindow;
        this.in = is;
        this.preferences = preferences;
        this.cp = cp;
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

    private ByteArrayOutputStream readMessage(int contentLength, boolean progressDisabled)
            throws IOException {
        int b;
        int downloaded = 0;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        while(contentLength!=downloaded) {
            b=in.read();
            if(b==-1){
                break;
            }
            baos.write(b);
            downloaded++;
            if(!progressDisabled){
                float p = (float)downloaded/(float)contentLength*100;
                browserWindow.setProgress(Math.round(p));
            }
        }
        return baos;
    }

    private ByteArrayOutputStream readChunkedMessage(boolean progressDisabled)
            throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // read first chunk size and CRLF
        int chunkSize;
        try{
            chunkSize = Integer.parseInt(readLine().trim(), 16);
        } catch (NumberFormatException e){
            throw new IOException("can't read first chunk size");
        }
        while(chunkSize>0){
            // read chunk data
            readMessage(chunkSize, progressDisabled).writeTo(baos);
            // read CRLF
            readLine();
            // read chunk size and CRLF
            try{
                chunkSize = Integer.parseInt(readLine().trim(), 16);
            } catch(NumberFormatException e){
                throw new IOException("can't read chunk size");
            }
        }
        return baos;
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

    public int getStatusCode(){
        return startingLine.getStatusCode();
    }

    public String getRealm(){
        String www_auth = headerList.getHeader(HEADER_WWW_AUTHENTICATE).getValue();
        return www_auth.substring(www_auth.indexOf('=')+1);
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
        if(!preferences.getRequestType().equals(
                PreferenceList.HTTP_REQUEST_HEAD)){
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
            if(headerList.hasHeader(HEADER_TRANSFER_ENCODING)){
                if(headerList.getHeader(
                        HEADER_TRANSFER_ENCODING).getValue().equals("chunked")){
                    progressDisabled = false;
                    chunked = true;
                }
            }
            ByteArrayOutputStream message;

            if(chunked){
                message = readChunkedMessage(progressDisabled);
            } else {
                message = readMessage(contentLength, progressDisabled);
            }

            /* cookies */
            cp.setCookies(headerList.getCookies());


            // show text or save to file
            if(headerList.hasHeader(HEADER_CONTENT_TYPE)){
                if(MIMETypes.isTextType(headerList.getHeader(
                        HEADER_CONTENT_TYPE).getValue())){
                    String charset = headerList.getCharset();
                    System.out.println(charset);
                    if(!charset.isEmpty()){
                        browserWindow.showMessage(
                                message.toString(charset.toUpperCase()));
                    }
                    browserWindow.showMessage(message.toString());
                } else {
                    File currentDir = new File(".");
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setCurrentDirectory(currentDir);
                    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    fileChooser.setDialogTitle("Save " + resourceName + " to...");
                    int returnVal = fileChooser.showDialog(browserWindow.getComponent(), "Save");
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        File outDir = fileChooser.getSelectedFile();
                        FileOutputStream fos = new FileOutputStream(
                                outDir.getAbsolutePath()
                                + File.separatorChar + resourceName);
                        message.writeTo(fos);
                        fos.close();
                    } 
                }
            }
            message.reset();
            message = null;
            System.gc();
        } else {
            browserWindow.showMessage("");
        }
        browserWindow.setProgress(100);
    }
}
