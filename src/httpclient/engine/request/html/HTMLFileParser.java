package httpclient.engine.request.html;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;

public class HTMLFileParser extends HTMLEditorKit.ParserCallback{

    private String path;
    private boolean isInForm;
    private boolean isInTextArea;
    private String action;
    private String arguments;

    private InputList inputList = new InputList();
    private String textAreaName;

    public HTMLFileParser(String path){
        this.path = path;
    }

    @Override
    public void handleText(char[] data, int pos) {
       // System.out.println(data);
        if(isInTextArea){
            //System.out.println(data);
            String text = new String(data);
            arguments += "&" + textAreaName + "=" + text;
            inputList.add(new Input(textAreaName, text, "textarea"));
        }
    }

    @Override
    public void handleStartTag(HTML.Tag t, MutableAttributeSet a, int pos) {
       // System.out.println("<" + t + (a.toString().length()>0 ? " " + a : "") + ">");
        String tagName = t.toString();
        if(tagName.equals("form")){
            if(a.getAttribute(HTML.Attribute.METHOD).equals("post")){
                action = a.getAttribute(HTML.Attribute.ACTION).toString();
                //System.out.println(action);
                isInForm = true;
            }
        }
        if(isInForm){
            if(tagName.equals("textarea")){
                String name = a.getAttribute(HTML.Attribute.NAME).toString();
                if(name!=null){
                    
                    textAreaName = name;
                    isInTextArea = true;
                }
            }
        }
    }

    @Override
    public void handleEndTag(HTML.Tag t, int pos) {
      //  System.out.println("</" + t + ">");
        String tagName = t.toString();
        if(t.toString().equals("form")){
            isInForm = false;
        }
        if(tagName.equals("textarea")){
            isInTextArea = false;
        }
    }
    
    @Override
    public void handleSimpleTag(HTML.Tag t, MutableAttributeSet a, int pos) {
       // System.out.println("<" + t + (a.toString().length()>0 ? " " + a : a) + "/>");
        String tagName = t.toString();
        if(isInForm){
            if(tagName.equals("input")){
                String type = a.getAttribute(HTML.Attribute.TYPE).toString();
                if(type.equals("text") || type.equals("file")){
                    //remember name
                    String name = a.getAttribute(HTML.Attribute.NAME).toString();
                    //remember value
                    String value = a.getAttribute(HTML.Attribute.VALUE).toString();
                    arguments += "&" + name + "=" + value;
                    inputList.add(new Input(name, value, type));
                }
                if(a.getAttribute(HTML.Attribute.TYPE).equals("file")){

                }
                if(a.getAttribute(HTML.Attribute.TYPE).equals("submit")){
                    // finish processing
                }
            }
        }
    }


    public void parse() throws IOException{
        arguments = "";
        try{
            Reader reader = new FileReader(path);
            new ParserDelegator().parse(reader, this, false);

        } catch(FileNotFoundException e){
            throw new IOException("can't find HTML file for POST request."
                    + " please make sure that path to file is correct.");
        }
    }

    public InputList getInputList(){
        return inputList;
    }

    public String getArguments(){
        return arguments.substring(1);
    }
    public String getAction(){
        return action;
    }
}
