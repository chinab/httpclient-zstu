package httpclient.engine.ui;

import java.awt.Component;

public interface BrowserWindow {

    public void setProgress(int value);
    public void showMessage(String value);
    public void showError(String message);
    public Component getComponent();
    public void enableGoButton(boolean setEnabled);

}
