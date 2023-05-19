package co.antonis.gwt.example.client.service;


import co.antonis.gwt.example.client.model.exception.InvalidUserException;
import co.antonis.gwt.example.client.model.others.InfoMessageStructure;
import co.antonis.gwt.example.client.utilities.Log;
import com.google.gwt.http.client.*;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class RestClientGWT {
    public static String Prefix = "http://localhost:8383";
    //since it resides at daqc i am moving on step up
    public static String Method = "../api/v1/daq/execute";
    public static boolean DEBUG = true;

    //region Callback Helpers
    public interface Converter<T> {
        T convert(String text);
    }

    public static <T> RequestCallback callbackConvert(AsyncCallback<T> async, Converter<T> convert) {
        return new RestClientGWT.CallbackWrapper(async) {
            @Override
            public void onResponse(Request request, Response response) {
                if (async != null) {
                    T convertObject = null;
                    boolean hasErrorOnConvert = false;
                    try {
                        //Check for Null string
                        convertObject = response.getText() == null || "null".equals(response.getText()) ? null : convert.convert(response.getText());
                    } catch (Exception exc) {
                        async.onFailure(exc);
                        hasErrorOnConvert = true;
                    }
                    if (!hasErrorOnConvert)
                        async.onSuccess(convertObject);
                }
            }
        };
    }


    public abstract static class CallbackWrapper implements RequestCallback {
        AsyncCallback asyncCallbackMain;

        /*The callback simple is used as helper in order to implement it AS "lamda"*/
        /*CallbackSimple RestClientGWT.RestClientGWT.callbackConvert;*/

        public CallbackWrapper(AsyncCallback asyncCallbackDefault/*, CallbackSimple callback*/) {
            this.asyncCallbackMain = asyncCallbackDefault;
        }

        @Override
        public void onResponseReceived(Request request, Response response) {
            Log.info("[BCB] Rest code [" + response.getStatusCode() + "]");
            if (DEBUG) {
                Log.info("-----------------");
                Log.info(response.getText());
                Log.info("-----------------");
            }

            if (response.getStatusCode() == 0) {
                asyncCallbackMain.onFailure(new Throwable("Server unresponsive"));
            }

            //Check for invalid user (common error)
            if (response.getStatusCode() == Response.SC_UNAUTHORIZED) {
                try {
                    InfoMessageStructure message = new InfoMessageStructure("");//UtilitiesGWTJackson.InfoMessageMapper.read(response.getText());
                    asyncCallbackMain.onFailure(new InvalidUserException(message.getMessage(), InvalidUserException.UserExceptionType.valueOf(message.getMessage())));
                } catch (Exception exc) {
                    //In case of parsing error
                    asyncCallbackMain.onFailure(new InvalidUserException());
                }
            } else if (response.getStatusCode() == Response.SC_OK) {
                try {
                    Log.info("[BCB] Response OK, calling WrapperCallback/Convertion");
                    onResponse(request, response);
                } catch (Exception exc) {
                    Log.error("", "[BCB] problematic implementation", exc);
                }
            }
        }

        public abstract void onResponse(Request request, Response response);

        @Override
        public void onError(Request request, Throwable throwable) {
            Log.info("[BCB] onError " + (throwable != null ? throwable.getMessage() : "n/a"));
            if (this.asyncCallbackMain != null) {
                this.asyncCallbackMain.onFailure(throwable);
            }
        }
    }

    public static RequestCallback CallBackLog = new RequestCallback() {
        @Override
        public void onResponseReceived(Request request, Response response) {
            Log.info("RestClientGWT. [Response Received] " + response.getStatusText());
        }

        @Override
        public void onError(Request request, Throwable throwable) {
            Log.info("RestClientGWT. [Response] onError");
        }
    };

    //endregion

    //region Core Rest Calls
    public static void executeMethodSafe(TypeDaqRest method, String[] parameters, RequestCallback callback/*AsyncCallback async, CallbackSimple convertCallback*/) {
        try {
            executeMethod(method, parameters, callback);
        } catch (Exception exc) {
            if (callback != null)
                callback.onError(null, exc);
        }
    }

    public static void executeMethod(TypeDaqRest method, String[] parameters, RequestCallback callback) throws Exception {
        String url = RestClientGWT.Prefix + RestClientGWT.Method;
        String boundaryInParameters = "--BOUNDARY--";
        String payload = "";

        if (parameters != null) {
            for (String parameter : parameters)
                payload += parameter + boundaryInParameters;
        }

        requestPost(url,
                new String[]{"method", "payload", "boundary"},
                new String[]{method.name(), payload, boundaryInParameters},
                callback);
    }

    public static void requestPost(String url, String[] keys, String[] values, RequestCallback callback) throws Exception {
        Log.info("RestClient: " + url + ", params " + keys.length);
        String boundary = "--GWT--[" + System.currentTimeMillis() + "]--";
        String contentType = "multipart/form-data; boundary=" + boundary;
        RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, url);
        builder.setHeader("Content-type", contentType);
        String requestData = createPayload(keys, values, boundary);
        builder.setRequestData(requestData);
        builder.setCallback(callback != null ? callback : CallBackLog);
        builder.send();
    }

    public static void request(RequestBuilder.Method method, String url, String[] keys, String[] values, RequestCallback callback) throws Exception {
        Log.info("RestClient: " + url + ", params " + keys.length);
        RequestBuilder builder;
        if (method == RequestBuilder.POST) {
            String boundary = "--GWT--[" + System.currentTimeMillis() + "]--";
            String contentType = "multipart/form-data; boundary=" + boundary;
            builder = new RequestBuilder(RequestBuilder.POST, url);
            builder.setHeader("Content-type", contentType);
            String requestData = createPayload(keys, values, boundary);
            builder.setRequestData(requestData);
        } else {
            StringBuilder urlBuilder = new StringBuilder(url);
            if (keys.length > 0)
                urlBuilder.append("?");
            for (int i = 0; i < keys.length; i++)
                urlBuilder.append(i != 0 ? "&" : "").append(keys[i]).append("=").append(values[i]);
            builder = new RequestBuilder(RequestBuilder.GET, URL.encode(urlBuilder.toString()));
        }
        //todo fix url
        builder.setCallback(callback != null ? callback : CallBackLog);
        builder.send();
    }

    public static String createPayload(String[] keys, String[] values, String boundary) {
        String body = "";
        for (int i = 0; i < keys.length; i++) {
            body += "--" + boundary
                    + "\r\nContent-Disposition: form-data; name=\"" + keys[i] + "\""
                    /* + "\r\nContent-type: " + formHash[key].type*/
                    + "\r\n\r\n" + values[i] + "\r\n";
        }
        body += "--" + boundary + "--\r\n";
        return body;
    }
    //endregion

    //region Examples
    public static void requestExampleHardCoded() throws Exception {
        String prefix = "http://localhost:8282";
        RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, prefix + "/api/v1/user/login");
        builder.setHeader("Content-type", "multipart/form-data; boundary=----WebKitFormBoundarybrrF9ABSxr7uB9pI");
        String requestData = "";
        requestData = "------WebKitFormBoundarybrrF9ABSxr7uB9pI\r\n" +
                "Content-Disposition: form-data; name=\"user\"\r\n" +
                "\r\n" +
                "agouzias\r\n" +
                "------WebKitFormBoundarybrrF9ABSxr7uB9pI\r\n" +
                "Content-Disposition: form-data; name=\"pass\"\r\n" +
                "\r\n" +
                "@ntonis1234\r\n" +
                "------WebKitFormBoundarybrrF9ABSxr7uB9pI\r\n" +
                "Content-Disposition: form-data; name=\"passTOTP\"\r\n" +
                "\r\n" +
                "\r\n" +
                "------WebKitFormBoundarybrrF9ABSxr7uB9pI--\r\n";
        builder.setRequestData(requestData);
        builder.setHeader("Content-length", requestData.length() + "");
        builder.setCallback(new RequestCallback() {
            @Override
            public void onResponseReceived(Request request, Response response) {
                Log.info("Response " + response.getStatusText());
            }

            @Override
            public void onError(Request request, Throwable throwable) {
                Log.info("Error");
            }
        });
        builder.send();

    }

    public static void requestLogin() throws Exception {
        requestPost("/api/v1/user/login", new String[]{"user", "pass"}, new String[]{"agouzias", "@ntonis1234"}, CallBackLog);
    }

    public static void requestPost() throws Exception {
        requestPost("/api/v1/daq/execute", new String[]{"boundary", "method", "payload", "payload2"}, new String[]{"agouzias", "agouzias1", "agouzias2"}, CallBackLog);
    }
    //endregion
}
