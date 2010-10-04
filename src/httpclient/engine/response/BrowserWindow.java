package httpclient.engine.response;

public interface BrowserWindow {

    public void setProgress(int value);
    public void showMessage(String value);
    public void showError(String message);

}
