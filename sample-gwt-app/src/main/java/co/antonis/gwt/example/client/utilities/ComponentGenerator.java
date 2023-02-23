package co.antonis.gwt.example.client.utilities;

import com.google.gwt.dom.client.Element;

public class ComponentGenerator {
    public static Element styleProperty(Element element, String key, String value) {
        element.getStyle().setProperty(key, value);
        return element;
    }
}
