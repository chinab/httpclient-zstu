package httpclient.engine.ui;

import httpclient.engine.general.HeaderList;
import httpclient.engine.response.StartingLine;

public interface InfoWindow {

    public void updateStatus(StartingLine sl);
    public void updateHeaders(HeaderList headerList);
    
}
