package co.antonis.gwt.example.client.ui.components.ace;

import co.antonis.gwt.example.client.utilities.ComponentGenerator;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.PreElement;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

public class AceTextAreaView extends Composite {


    interface AceTextAreaViewUiBinder extends UiBinder<HTMLPanel, AceTextAreaView> {
    }

    private static AceTextAreaViewUiBinder ourUiBinder = GWT.create(AceTextAreaViewUiBinder.class);
    @UiField
    PreElement containerAce;
    JavaScriptObject element;
    String id;

    public static final String Mode_javascript = "ace/mode/javascript";
    public static final String Mode_java = "ace/mode/java";
    public static final String Mode_json = "ace/mode/json";
    public static final String Theme_twilight = "ace/theme/twilight";
    public static final String Theme_eclipse = "ace/theme/eclipse";
    public static final String Theme_blue = "ace/theme/tomorrow_night_blue";

    public AceTextAreaView() {
        initWidget(ourUiBinder.createAndBindUi(this));
        id = System.currentTimeMillis()+"_";/*ComponentGenerator.randomStringId();*/
        containerAce.setAttribute("id", id);
        setTheme(Theme_blue);
        addAttachHandler(getHandler(Mode_json, null));
        setHeight("100px");
    }

    public AttachEvent.Handler getHandler(final String mode, final String height) {
        return new AttachEvent.Handler() {
            @Override
            public void onAttachOrDetach(AttachEvent attachEvent) {
                enable(mode, "", height, false);
                if (textCached != null)
                    setText(textCached);
                if (themeCached != null)
                    setTheme(themeCached);
                if(enableCached!=null)
                    setEnabled(enableCached);
            }
        };
    }

    public void setHeight(String height) {
        if (height != null) {
            super.setHeight(height);
            ComponentGenerator.styleProperty(containerAce, "height", height);
        }
    }

    /**
     * Use After on Attach
     *
     * @param mode,     the mode of the editor
     * @param text,     the initial text if any
     * @param height,   the height
     * @param readOnly, read only flag
     */
    public void enable(String mode, String text, String height, boolean readOnly) {
        element = enableEditor(id, mode, text);
        setHeight(height);
    }

    public String getText() {
        return getText(element);
    }

    String textCached = null;

    public void setText(String text) {
        try {
            setText(element, text);
            textCached = null;
        } catch (Exception exc) {
            textCached = text;
        }
    }

    String themeCached = null;

    public void setTheme(String theme) {
        try {
            setTheme(element, theme);
            themeCached = null;
        } catch (Exception exc) {
            themeCached = theme;
        }
    }


    public static native JavaScriptObject enableEditor(String id, String mode, String text)/*-{
        // Code that executes a expression for parameter
        var editor = $wnd.ace.edit(id);
        //editor.setTheme("ace/theme/twilight");
        //editor.setTheme("ace/theme/eclipse");
        editor.session.setMode(mode);
        editor.session.setValue(text);
        editor.renderer.setShowGutter(true);
        return editor;
    }-*/;


    Boolean enableCached = null;
    public void setEnabled(boolean what){
        try {
            setEnabled(element, what);
            enableCached = null;
        }catch(Exception exc){
            enableCached=what;
        }
    }

    public static native void setEnabled(JavaScriptObject editor,boolean what)/*-{
        return editor.setReadOnly(!what);
    }-*/;

    public static native String getText(JavaScriptObject editor)/*-{
        return editor.session.getValue();
    }-*/;

    public static native void setMode(JavaScriptObject editor, String mode)/*-{
        editor.session.setMode(mode);
    }-*/;

    public static native void setTheme(JavaScriptObject editor, String mode)/*-{
        editor.setTheme(mode);
    }-*/;


    public static native void setText(JavaScriptObject editor, String text)/*-{
        editor.session.setValue(text);
    }-*/;
}
