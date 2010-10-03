package httpclient.engine.response;

import httpclient.engine.general.HeaderList;

public interface InfoWindow {

    public void updateStatus(StartingLine sl);
    public void updateHeaders(HeaderList headerList);
    
}
