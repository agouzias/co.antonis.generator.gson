package co.antonis.gwt.example.client.utilities;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;

public class DialogBoxBasic extends DialogBox {
    public Button closeButton;
    public Label textToServerLabel = new Label();
    public HTML serverResponseLabel = new HTML();

    public DialogBoxBasic(ClickHandler handler){
        setText("Remote Procedure Call");
        setAnimationEnabled(true);
        closeButton = new Button("Close");
        // We can set the id of a widget by accessing its Element
        closeButton.getElement().setId("closeButton");
        textToServerLabel = new Label();
        serverResponseLabel = new HTML();
        VerticalPanel dialogVPanel = new VerticalPanel();
        dialogVPanel.addStyleName("dialogVPanel");
        dialogVPanel.add(new HTML("<b>Sending name to the server:</b>"));
        dialogVPanel.add(textToServerLabel);
        dialogVPanel.add(new HTML("<br><b>Server replies:</b>"));
        dialogVPanel.add(serverResponseLabel);
        dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
        dialogVPanel.add(closeButton);
        setWidget(dialogVPanel);

        closeButton.addClickHandler(event -> {
            hide();
            if (handler != null)
                handler.onClick(event);
        });
    }
}
