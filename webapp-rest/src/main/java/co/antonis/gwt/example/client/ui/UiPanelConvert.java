package co.antonis.gwt.example.client.ui;

import co.antonis.gwt.example.client.generated.SerializationGWTUtilities;
import co.antonis.gwt.example.client.model.others.InfoMessageStructure;
import co.antonis.gwt.example.client.ui.components.ace.AceTextAreaView;
import co.antonis.gwt.example.client.utilities.Log;
import co.antonis.generator.model.samplePojo.StructureGenerator;
import co.antonis.generator.model.samplePojo.PojoParent;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

import java.util.logging.Logger;

public class UiPanelConvert extends Composite {
    interface UiPanelConvertUiBinder extends UiBinder<HTMLPanel, UiPanelConvert> {
    }
    Logger log = Log.getLogger();

    private static UiPanelConvertUiBinder ourUiBinder = GWT.create(UiPanelConvertUiBinder.class);
    @UiField
    HTMLPanel uiContainerJson, uiContainerStructure;
    @UiField
    Button uiButtonJsonFrom;
    @UiField
    Button uiButtonToJson;
    AceTextAreaView uiTextJson, uiTextStructure;

    public UiPanelConvert() {
        initWidget(ourUiBinder.createAndBindUi(this));

        uiTextStructure = new AceTextAreaView();
        uiTextStructure.setSize("100%", "200px");
        uiContainerJson.add(uiTextStructure);

        uiTextJson = new AceTextAreaView();
        uiTextJson.setSize("100%", "200px");
        uiContainerStructure.add(uiTextJson);
    }

    @UiHandler("uiButtonToJson")
    public void onButtonToJson(ClickEvent event) {
        log.info("Converting To Json of auto generated structure [" + uiTextStructure.getText() + "]");
        PojoParent structure = StructureGenerator.generatePojoParent();
        InfoMessageStructure info = SerializationGWTUtilities.toInfoMessageStructure(uiTextStructure.getText());
        log.info("Output " + info.toString(false));
        uiTextJson.setText(info.toString(false));
    }

}
