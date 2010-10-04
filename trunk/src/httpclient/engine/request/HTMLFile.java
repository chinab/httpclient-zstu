package httpclient.engine.request;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;

public class HTMLFile extends HTMLEditorKit.ParserCallback{

    private String path;

    public HTMLFile(String path){
        this.path = path;
    }

    @Override
    public void handleText(char[] data, int pos) {
        System.out.println(data);
    }

    @Override
    public void handleStartTag(HTML.Tag t, MutableAttributeSet a, int pos) {
        System.out.println("<" + t + (a.toString().length()>0 ? " " + a : "") + ">");

    }

    @Override
    public void handleEndTag(HTML.Tag t, int pos) {
        System.out.println("</" + t + ">");
    }
    
    @Override
    public void handleSimpleTag(HTML.Tag t, MutableAttributeSet a, int pos) {
        System.out.println("<" + t + (a.toString().length()>0 ? " " + a : a) + "/>");
    }


    public void parse() throws IOException{
        try{

            Reader reader = new FileReader(path);
            new ParserDelegator().parse(reader, this, false);

        } catch(FileNotFoundException e){
            throw new IOException("can't find HTML file for POST request."
                    + " please make sure that path to file is correct.");
        }
    }
}
