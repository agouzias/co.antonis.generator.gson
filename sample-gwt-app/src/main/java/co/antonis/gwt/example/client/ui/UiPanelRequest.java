package co.antonis.gwt.example.client.ui;

import co.antonis.generator.model.sample.PojoParent;
import co.antonis.gwt.example.client.generated.SerializationGWTJsonTypes;
import co.antonis.gwt.example.client.generated.SerializationGWTJson_Sample;
import co.antonis.gwt.example.client.ui.components.ace.AceTextAreaView;
import co.antonis.gwt.example.client.service.RestClientGWT;
import co.antonis.gwt.example.client.utilities.Log;
import com.google.gson.JsonObject;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;

import java.util.Arrays;
import java.util.logging.Logger;

public class UiPanelRequest extends Composite {
    interface UiMainUiBinder extends UiBinder<HTMLPanel, UiPanelRequest> {
    }

    Logger log = Log.getLogger();

    private static UiMainUiBinder ourUiBinder = GWT.create(UiMainUiBinder.class);
    @UiField
    TextBox uiTextURL;
    @UiField
    TextBox uiTextParamA, uiTextParamB, uiTextParamC, uiTextParamD, uiTextParamE;
    @UiField
    Button buttonSubmit, buttonConvertJsonToPojo;

    @UiField
    InlineHTML uiTextResponseStatus, uiTextResponseCode, uTextResponseCookie;
    @UiField
    HTMLPanel uiContainerTextBody, uiContainerTextConverted;
    @UiField
    CheckBox uiCheckSendAsParam1;
    @UiField
    CheckBox uiCheckBoxMethod;
    AceTextAreaView uiTextResponseBody, uiTextConvertedStructure;

    public UiPanelRequest(String method, String url) {
        log.info("Request Panel initializing for " + method);
        initWidget(ourUiBinder.createAndBindUi(this));
        uiCheckBoxMethod.setValue("GET".equals(method));
        uiTextURL.setText(url);

        uiTextResponseBody = new AceTextAreaView();
        uiTextResponseBody.setSize("100%", "500px");
        uiContainerTextBody.add(uiTextResponseBody);

        uiTextConvertedStructure = new AceTextAreaView();
        uiTextConvertedStructure.setSize("100%", "500px");
        uiContainerTextConverted.add(uiTextConvertedStructure);

        int count = 1;
        for (TextBox box : Arrays.asList(uiTextParamA, uiTextParamB, uiTextParamC, uiTextParamD))
            box.getElement().setAttribute("placeholder", "param " + (count++));

    }

    @UiHandler("buttonConvertJsonToPojo")
    public void buttonSubmitConvert(ClickEvent event) {
        log.info("Converting json of text [" + uiTextResponseBody.getText() + "]");
        long start = System.currentTimeMillis();
        PojoParent pojoParent = convertPojo(uiTextResponseBody.getText(), PojoParent.class);
        long diff = System.currentTimeMillis() - start;

        log.info("Diff Convert:" + diff + "ms");
        uiTextConvertedStructure.setText(pojoParent.toString());
    }

    public static <T> T convertPojo(String json, Class<T> clazz) {
        return SerializationGWTJsonTypes.fromJson(clazz, json);
    }

    @UiHandler("buttonSubmit")
    public void buttonSubmitClick(ClickEvent event) {
        String[] keys = new String[]{};
        String[] values = new String[]{};

        if (uiCheckSendAsParam1.getValue()) {
            keys = new String[]{"param"};

            Log.info("Using Input Json as object/param, converting to POJO");
            PojoParent pojo = SerializationGWTJson_Sample.toPojoParent(uiTextResponseBody.getText());

            Log.info("Converted POJO to JSON");
            JSONObject jsonConverted = SerializationGWTJson_Sample.fromPojoParent(pojo);

            Log.info("Converted POJO to JSON");

            values = new String[]{jsonConverted != null ? jsonConverted.toString() : null};
        }

        log.info("onClick [" + (uiCheckBoxMethod.getValue() ? RequestBuilder.GET : RequestBuilder.POST) + "][" + uiTextURL.getText() + "]");
        try {
            RestClientGWT.request(
                    uiCheckBoxMethod.getValue() ? RequestBuilder.GET : RequestBuilder.POST,
                    uiTextURL.getText(),
                    keys,
                    values,
                    new RequestCallback() {
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
