package co.antonis.gwt.example.client.utilities;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Utilities {

    public static void showDialog(String title, String text){
        final DialogBox dialogBox = new DialogBox();
        VerticalPanel vPanel = new VerticalPanel();
        vPanel.add(new HTMLPanel(text));
        vPanel.add(new Button("Close", (ClickHandler) clickEvent -> dialogBox.hide()));
        dialogBox.setWidget(vPanel);
        dialogBox.setText(title);
        dialogBox.show();
    }
}
