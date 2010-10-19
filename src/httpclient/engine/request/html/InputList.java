package httpclient.engine.request.html;

import java.util.ArrayList;
import java.util.Iterator;

public class InputList implements Iterable<Input> {

    private ArrayList<Input> inputs = new ArrayList<Input>();

    public void add(Input input){
        inputs.add(input);
    }

    public boolean hasFiles(){
        for(Input i: inputs){
            if(i.getType().equals("file")) return true;
        }
        return false;
    }

    public int size(){
        return inputs.size();
    }

    public Input get(int i){
        return inputs.get(i);
    }

    public Iterator<Input> iterator(){
        Iterator<Input> iinp = inputs.iterator();
        return iinp;
    }
}
