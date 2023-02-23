package co.antonis.gwt.example.client;

import co.antonis.gwt.example.client.ui.UiPanelRequest;
import co.antonis.gwt.example.client.utilities.Log;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

import java.util.logging.Logger;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class App implements EntryPoint {
    Logger log = Log.getLogger();

    /**
     * The message displayed to the user when the server cannot be reached or
     * returns an error.
     */
    private static final String SERVER_ERROR = "An error occurred while "
            + "attempting to contact the server. Please check your network "
            + "connection and try again.";

    /**
     * Create a remote service proxy to talk to the server-side Greeting service.
     */

    @Override
    public void onModuleLoad() {
        log.info("onModuleLoad, adding uiMain");
        UiPanelRequest uiMain = new UiPanelRequest("GET", "http://localhost:8080/json");
        RootPanel.get("rootPanel").add(uiMain);
        log.info("onModuleLoad, adding uiMain (completed");
    }

}
