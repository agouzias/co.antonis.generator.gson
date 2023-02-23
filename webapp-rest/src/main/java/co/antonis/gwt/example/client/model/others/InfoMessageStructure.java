package co.antonis.gwt.example.client.model.others;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Generic Message Structure to be used for user notifications.
 * The structure can be serialized and wrapped from other structures
 */
public class InfoMessageStructure implements Serializable {

    @Expose
    @SerializedName("t")
    protected int type;

    @Expose
    @SerializedName("msg")
    protected String message;

    @Expose
    @SerializedName("mse")
    protected String messageExtended;

    protected transient Throwable thr;

    // Info/Loading (<10)
    public static final int TypeNone = -1;
    public static final int TypeInfo = 0;
    public static final int TypeLoading = 1;

    // Error/Warning Message (>10)
    public static final int TypeWarning = 10;
    public static final int TypeError = 11;

    public InfoMessageStructure() {
    }

    public InfoMessageStructure(int type, String message, Throwable thr) {
        this.type = type;
        this.message = message;
        this.thr = thr;
    }

    public InfoMessageStructure(String message) {
        this.type = TypeInfo;
        this.message = message;
    }

    public String header() {
        switch (type) {
            case TypeInfo:
                return "Info";
            case TypeWarning:
                return "Warning";
            case TypeLoading:
                return "Loading";
            case TypeError:
                return "Error";
            default:
                return "";
        }
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public String getMessageExtended() {
        return messageExtended;
    }

    public InfoMessageStructure setMessage(String message) {
        this.message = message;
        return this;
    }

    public static InfoMessageStructure info(String message) {
        return new InfoMessageStructure(TypeInfo, message, null);
    }

    public static InfoMessageStructure info(String message, String messageExtended) {
        return new InfoMessageStructure(TypeInfo, message, null).setMessageExtended(messageExtended);
    }

    public static InfoMessageStructure error(String message) {
        return new InfoMessageStructure(TypeError, message, null);
    }

    public static InfoMessageStructure error(String message, Throwable thr) {
        return new InfoMessageStructure(TypeError, message, thr);
    }

    public InfoMessageStructure setMessageExtended(String messageExtended) {
        this.messageExtended = messageExtended;
        return this;
    }

    public static InfoMessageStructure loading(String message) {
        return new InfoMessageStructure(InfoMessageStructure.TypeLoading, message, null);
    }

    public static InfoMessageStructure warning(String message) {
        return new InfoMessageStructure(TypeWarning, message, null);
    }

    public static InfoMessageStructure warning(String message, String messageExtended) {
        return new InfoMessageStructure(TypeWarning, message, null).setMessageExtended(messageExtended);
    }

    public static InfoMessageStructure warning(String message, Throwable thr) {
        return new InfoMessageStructure(TypeWarning, message, thr);
    }

    public Throwable getThr() {
        return thr;
    }

    public static List<InfoMessageStructure> toList(String[] messages) {
        return toList(Arrays.asList(messages));
    }

    public static List<InfoMessageStructure> toList(List<String> messages) {
        List<InfoMessageStructure> list = new ArrayList<>();
        for (String message : messages)
            list.add(InfoMessageStructure.info(message));
        return list;
    }


    public static int countOf(List<InfoMessageStructure> list, int type) {
        int count = 0;
        for (InfoMessageStructure message : list) {
            if (message.getType() == type)
                count++;
        }
        return count;
    }


    public String toString(boolean isHtml) {
        String typeStr = (type == TypeInfo ? "Info: " : "Warning: ");
        if (isHtml)
            return "<strong>" + typeStr + "</strong>" + message;
        else
            return typeStr + message;

    }

    public boolean containText(String text) {
        if (text == null || text.trim().equals(""))
            return true;

        return (message != null && message.toLowerCase().indexOf(text) != -1)
                ||
                (messageExtended != null && messageExtended.toLowerCase().indexOf(text) != -1);
    }

    public boolean isError() {
        return type == TypeError;
    }

    public boolean isInfo() {
        return type == TypeInfo;
    }
}
