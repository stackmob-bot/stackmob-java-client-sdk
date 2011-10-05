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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
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

    public static final String DEFAULT_URL_BASE = "api.mob1.stackmob.com";
    protected static final String SECURE_SCHEME = "https";
    protected static final String REGULAR_SCHEME = "http";

    protected StackMobSession session;
    protected String sessionKey;
    protected String sessionSecret;
    protected String methodName;

    protected String url_fmt = DEFAULT_URL_BASE;

    protected Boolean isSecure = true;
    protected HttpVerb httpMethod = HttpVerb.GET;

    protected Map<String, Object> params;
    protected Object requestObject;

    //default to doing nothing
    protected StackMobCallback callback = new StackMobCallback() {
        @Override
        public void success(String s) {}
        @Override
        public void failure(StackMobException e) {}
    };

    //default constructor - not available for public consumption
    private StackMobRequest(StackMobSession session, String method) {
        this.session = session;
        this.sessionKey = session.getKey();
        this.sessionSecret = session.getSecret();
        this.methodName = method;
    }

    public StackMobRequest(StackMobSession session, String method, StackMobCallback callback) {
        this(session, method);
        this.callback = callback;
    }

    public StackMobRequest(StackMobSession session, String method, Map<String, Object> args, StackMobCallback callback) {
        this(session, method, callback);
        this.params = args;
    }

    public StackMobRequest(StackMobSession session, String method, HttpVerb verb, Object requestObject, StackMobCallback callback) {
        this(session, method, verb, callback);
        this.requestObject = requestObject;
    }

    public StackMobRequest(StackMobSession session, String method, HttpVerb verb, StackMobCallback callback) {
        this(session, method, callback);
        this.methodName = method;
        this.httpMethod = verb;
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

    private String sendGetRequest() throws StackMobException {
        URI uri;
        String ret;

        try {
            String query = null;
            if (null != params) {
                query = URLEncodedUtils.format(getParamsForRequest(), HTTP.UTF_8);
            }

            uri = URIUtils.createURI(getScheme(), getHost(), -1, getPath(), query, null);
            ret = HttpHelper.doGet(uri, sessionKey, sessionSecret);
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
                Gson gson = new Gson();
                entity = new StringEntity(gson.toJson(requestObject), HTTP.UTF_8);
            }

            ret = HttpHelper.doPost(uri, entity, sessionKey, sessionSecret);

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
                entity = new UrlEncodedFormEntity(getParamsForRequest(),HTTP.UTF_8);
            }
            else if (null != requestObject) {
                Gson gson = new Gson();
                entity = new StringEntity(gson.toJson(requestObject),HTTP.UTF_8);
            }

            ret = HttpHelper.doPut(uri, entity, sessionKey, sessionSecret);
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
            ret = HttpHelper.doDelete(uri, sessionKey, sessionSecret);

        }
        catch (URISyntaxException e) {
            throw new StackMobException(e.getMessage());
        }

        return ret;
    }

    protected String getPath() {
        return "/" + methodName;
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
        return url_fmt;
    }

    private List<NameValuePair> getParamsForRequest() {
        if (null == params) {
            return null;
        }

        List<NameValuePair> ret = new ArrayList<NameValuePair>(params.size());
        for (String key : params.keySet()) {
            ret.add(new BasicNameValuePair(key, params.get(key).toString()));
        }

        return ret;
    }

}