/**
 * Copyright 2011 StackMob
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.stackmob.sdk.api;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Modifier;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.GsonBuilder;
import com.stackmob.sdk.callback.StackMobRedirectedCallback;
import com.stackmob.sdk.push.StackMobPushToken;
import com.stackmob.sdk.push.StackMobPushTokenDeserializer;
import com.stackmob.sdk.push.StackMobPushTokenSerializer;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import com.google.gson.Gson;
import com.stackmob.sdk.callback.StackMobCallback;
import com.stackmob.sdk.exception.StackMobException;
import com.stackmob.sdk.net.HttpHelper;
import com.stackmob.sdk.net.HttpVerb;

public class StackMobRequest {

    public static final String DEFAULT_URL_FORMAT = "api.mob1.stackmob.com";
    protected static final String SECURE_SCHEME = "https";
    protected static final String REGULAR_SCHEME = "http";

    protected final StackMobSession session;
    protected final String sessionKey;
    protected final StackMobRedirectedCallback redirectedCallback;
    protected final String sessionSecret;

    protected String methodName;
    protected String urlFormat = DEFAULT_URL_FORMAT;
    protected Boolean isSecure = false;
    protected HttpVerb httpMethod = HttpVerb.GET;
    protected Map<String, String> params;
    protected Object requestObject;

    protected Gson gson;

    //default to doing nothing
    protected StackMobCallback callback = new StackMobCallback() {
        @Override
        public void success(String s) {}
        @Override
        public void failure(StackMobException e) {}
    };

    //default constructor - not available for public consumption
    private StackMobRequest(StackMobSession session, String method, StackMobRedirectedCallback cb) {
        this.session = session;
        this.sessionKey = session.getKey();
        this.sessionSecret = session.getSecret();
        this.methodName = method;
        this.redirectedCallback = cb;

        GsonBuilder gsonBuilder = new GsonBuilder()
                                  .registerTypeAdapter(StackMobPushToken.class, new StackMobPushTokenDeserializer())
                                  .registerTypeAdapter(StackMobPushToken.class, new StackMobPushTokenSerializer())
                                  .excludeFieldsWithModifiers(Modifier.PRIVATE, Modifier.PROTECTED, Modifier.TRANSIENT, Modifier.STATIC);
        gson = gsonBuilder.create();
    }

    public StackMobRequest(StackMobSession session, String method, StackMobCallback callback, StackMobRedirectedCallback redirCB) {
        this(session, method, redirCB);
        this.callback = callback;
    }

    public StackMobRequest(StackMobSession session, String method, Map<String, String> args, StackMobCallback callback, StackMobRedirectedCallback redirCB) {
        this(session, method, callback, redirCB);
        this.params = args;
    }

    public StackMobRequest(StackMobSession session, String method, HttpVerb verb, Object requestObject, StackMobCallback callback, StackMobRedirectedCallback redirCB) {
        this(session, method, verb, callback, redirCB);
        this.requestObject = requestObject;
    }

    public StackMobRequest(StackMobSession session, String method, HttpVerb verb, StackMobCallback callback, StackMobRedirectedCallback redirCB) {
        this(session, method, callback, redirCB);
        this.methodName = method;
        this.httpMethod = verb;
    }



    public StackMobRequest setUrlFormat(String urlFmt) {
        this.urlFormat = urlFmt;
        return this;
    }

    public void sendRequest() {
        try {
            String response = null;

            switch(httpMethod) {
                case GET:
                    response = sendGetRequest();
                    break;
                case POST:
                    response = sendPostRequest();
                    break;
                case PUT:
                    response = sendPutRequest();
                    break;
                case DELETE:
                    response = sendDeleteRequest();
                    break;
            }

            callback.success(response);

        }
        catch (StackMobException e) {
            callback.failure(e);
        }
    }

    private String formatQueryString(List<NameValuePair> nameValuePairs) {
        //return URLEncodedUtils.format(nameValuePairs, HTTP.UTF_8);
        String ret = URLEncodedUtils.format(nameValuePairs, HTTP.UTF_8);
        return ret;
//        return ret.replace("%5B", "[").replace("%5D", "]");//replace is a hack to make sure that the encoder does not encode [ or ]
        /*
        String url = "";
        for(NameValuePair nameValuePair : nameValuePairs) {
            url = OAuth.addQueryParameters(url, nameValuePair.getName(), nameValuePair.getValue());
        }
        return url.substring(1);
        */
    }

    private String sendGetRequest() throws StackMobException {
        URI uri;
        String ret;

        try {
            String query = null;
            if (null != params) {
                query = formatQueryString(getParamsForRequest());
            }

            uri = URIUtils.createURI(getScheme(), getHost(), -1, getPath(), query, null);
            if(session.getAppName() != null) {
                ret = HttpHelper.doGet(uri, sessionKey, sessionSecret, session.getAppName(), session.getApiVersionNumber(), redirectedCallback);
            }
            else {
                ret = HttpHelper.doGet(uri, sessionKey, sessionSecret, session.getApiVersionNumber(), redirectedCallback);
            }
        }
        catch (URISyntaxException e) {
            throw new StackMobException(e.getMessage());
        }

        return ret;
    }

    private String sendPostRequest() throws StackMobException {
        URI uri;
        String ret;

        try {
            uri = URIUtils.createURI(getScheme(), getHost(), -1, getPath(), null, null);

            HttpEntity entity = null;
            if (null != params) {
                entity = new UrlEncodedFormEntity(getParamsForRequest(), HTTP.UTF_8);
            }
            else if (null != requestObject) {
                entity = new StringEntity(gson.toJson(requestObject), HTTP.UTF_8);
            }

            if(session.getAppName() != null) {
                ret = HttpHelper.doPost(uri, entity, sessionKey, sessionSecret, session.getAppName(), session.getApiVersionNumber(), redirectedCallback);
            }
            else {
                ret = HttpHelper.doPost(uri, entity, sessionKey, sessionSecret, session.getApiVersionNumber(), redirectedCallback);
            }

        }
        catch (URISyntaxException e) {
            throw new StackMobException(e.getMessage());
        }
        catch (UnsupportedEncodingException e) {
            throw new StackMobException(e.getMessage());
        }
        return ret;
    }

    private String sendPutRequest() throws StackMobException {
        URI uri;
        String ret;

        try {
            uri = URIUtils.createURI(getScheme(), getHost(), -1, getPath(),null, null);

            HttpEntity entity = null;
            if (null != params) {
                entity = new UrlEncodedFormEntity(getParamsForRequest(), HTTP.UTF_8);
            }
            else if (null != requestObject) {
                entity = new StringEntity(gson.toJson(requestObject), HTTP.UTF_8);
            }
            if(session.getAppName() != null) {
                ret = HttpHelper.doPut(uri, entity, sessionKey, sessionSecret, session.getAppName(), session.getApiVersionNumber(), redirectedCallback);
            }
            else {
                ret = HttpHelper.doPut(uri, entity, sessionKey, sessionSecret, session.getApiVersionNumber(), redirectedCallback);
            }
        }
        catch (URISyntaxException e) {
            throw new StackMobException(e.getMessage());
        }
        catch (UnsupportedEncodingException e) {
            throw new StackMobException(e.getMessage());
        }

        return ret;
    }

    private String sendDeleteRequest() throws StackMobException {
        URI uri;
        String ret;

        try {
            String query = null;
            if (null != params) {
                query = URLEncodedUtils.format(getParamsForRequest(), HTTP.UTF_8);
            }

            uri = URIUtils.createURI(getScheme(), getHost(), -1, getPath(), query, null);
            if(session.getAppName() != null) {
                ret = HttpHelper.doDelete(uri, sessionKey, sessionSecret, session.getAppName(), session.getApiVersionNumber(), redirectedCallback);
            }
            else {
                ret = HttpHelper.doDelete(uri, sessionKey, sessionSecret, session.getApiVersionNumber(), redirectedCallback);
            }
        }
        catch (URISyntaxException e) {
            throw new StackMobException(e.getMessage());
        }

        return ret;
    }

    protected String getPath() {
        if(methodName.startsWith("/")) {
            return methodName;
        }
        else {
            return "/" + methodName;
        }
    }

    private String getScheme() {
        if (isSecure) {
            return SECURE_SCHEME;
        }
        else {
            return REGULAR_SCHEME;
        }
    }

    private String getHost() {
        return urlFormat;
    }

    private List<NameValuePair> getParamsForRequest() {
        if (null == params) {
            return null;
        }

        List<NameValuePair> ret = new ArrayList<NameValuePair>(params.size());
        for (String key : params.keySet()) {
            ret.add(new BasicNameValuePair(key, params.get(key)));
        }

        return ret;
    }
}