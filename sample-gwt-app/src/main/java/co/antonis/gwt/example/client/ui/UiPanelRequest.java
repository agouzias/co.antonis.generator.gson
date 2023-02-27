package co.antonis.gwt.example.client.ui;

import co.antonis.generator.model.sample.PojoParent;
import co.antonis.gwt.example.client.ui.components.ace.AceTextAreaView;
import co.antonis.gwt.example.client.service.RestClientGWT;
import co.antonis.gwt.example.client.utilities.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;

import java.util.logging.Logger;

public class UiPanelRequest extends Composite {
    interface UiMainUiBinder extends UiBinder<HTMLPanel, UiPanelRequest> {
    }

    Logger log = Log.getLogger();

    private static UiMainUiBinder ourUiBinder = GWT.create(UiMainUiBinder.class);
    @UiField
    TextBox uiTextURL;
    @UiField
    ListBox uiListMethod;
    @UiField
    TextBox uiTextParamA, uiTextParamB, uiTextParamC, uiTextParamD, uiTextParamE;
    @UiField
    Button buttonSubmit, buttonConvert;

    @UiField
    TextBox uiTextResponseStatus, uiTextResponseCode, uTextResponseCookie;
    @UiField
    HTMLPanel uiContainerTextBody, uiContainerTextConverted;
    AceTextAreaView uiTextResponseBody, uiTextConvertedStructure;

    public UiPanelRequest(String method, String url) {
        log.info("Request Panel initializing for " + method);
        initWidget(ourUiBinder.createAndBindUi(this));
        uiListMethod.addItem("GET");
        uiListMethod.addItem("POST");
        uiListMethod.setSelectedIndex("GET".equals(method) ? 0 : 1);
        uiTextURL.setText(url);

        uiTextResponseBody = new AceTextAreaView();
        uiTextResponseBody.setSize("100%", "500px");
        uiContainerTextBody.add(uiTextResponseBody);

        uiTextConvertedStructure = new AceTextAreaView();
        uiTextConvertedStructure.setSize("100%", "500px");
        uiContainerTextConverted.add(uiTextConvertedStructure);
    }


    @UiHandler("buttonConvert")
    public void buttonSubmitConvert(ClickEvent event) {
        log.info("Converting json of text [" + uiTextResponseBody.getText() + "]");
        long start = System.currentTimeMillis();
        PojoParent pojoParent = SerializationGWTJson.toPojoParent(uiTextResponseBody.getText());
        long diff = System.currentTimeMillis() - start;
        log.info("Diff Convertion:"+diff+"ms");
        uiTextConvertedStructure.setText(pojoParent.toString());
    }


    @UiHandler("buttonSubmit")
    public void buttonSubmitClick(ClickEvent event) {
        log.info("onClick [" + uiListMethod.getSelectedValue() + "][" + uiTextURL.getText() + "]");
        try {
            RestClientGWT.request(uiListMethod.getSelectedIndex() == 0 ? RequestBuilder.GET : RequestBuilder.POST, uiTextURL.getText(), new String[]{}, new String[]{}, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    log.info("on response received " + response.getStatusCode() + "," + response.getStatusText());
                    if (response.getStatusCode() != 200) {
                        uiTextResponseBody.setText("Error code " + response.getStatusCode());
                    } else {
                        uiTextResponseStatus.setText(response.getStatusText());
                        uiTextResponseCode.setText(response.getStatusCode() + "");
                        uiTextResponseBody.setText(response.getText());
                    }
                }

                @Override
                public void onError(Request request, Throwable throwable) {
                    log.info("on response received (error)");
                    uiTextResponseBody.setText(throwable.getMessage());
                }
            });
        } catch (Exception e) {
            log.info("failed calling rest");
        }
    }
}
