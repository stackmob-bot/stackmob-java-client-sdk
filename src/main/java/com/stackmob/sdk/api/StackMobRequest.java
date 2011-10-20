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
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.GsonBuilder;
import com.stackmob.sdk.callback.StackMobRedirectedCallback;
import com.stackmob.sdk.push.StackMobPushToken;
import com.stackmob.sdk.push.StackMobPushTokenDeserializer;
import com.stackmob.sdk.push.StackMobPushTokenSerializer;
import com.stackmob.sdk.util.Pair;

import com.google.gson.Gson;
import com.stackmob.sdk.callback.StackMobCallback;
import com.stackmob.sdk.exception.StackMobException;
import com.stackmob.sdk.net.HttpVerb;
import org.scribe.model.Response;
import org.scribe.oauth.OAuthService;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.builder.ServiceBuilder;

import com.stackmob.sdk.net.StackMobApi;
import com.stackmob.sdk.net.HttpRedirectHelper;

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

    private OAuthService oAuthService;

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

        oAuthService = new ServiceBuilder().provider(StackMobApi.class).apiKey(sessionKey).apiSecret(sessionSecret).build();
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

    private String sendGetRequest() throws StackMobException {
        try {
            String query = null;
            if (null != params) {
                query = formatQueryString(getParamsForRequest());
            }

            URI uri = createURI(getScheme(), getHost(), getPath(), query);
            OAuthRequest req = getOAuthRequest(HttpVerb.GET, uri.toString());
            return sendRequest(req);
        }
        catch (URISyntaxException e) {
            throw new StackMobException(e.getMessage());
        }
    }

    private String sendPostRequest() throws StackMobException {
        try {
            URI uri = createURI(getScheme(), getHost(), getPath(), "");
            String payload = getPayload();
            OAuthRequest req = getOAuthRequest(HttpVerb.POST, uri.toString(), payload);
            return sendRequest(req);
        }
        catch (URISyntaxException e) {
            throw new StackMobException(e.getMessage());
        }
    }

    private String sendPutRequest() throws StackMobException {
        try {
            URI uri = createURI(getScheme(), getHost(), getPath(), "");
            String payload = getPayload();
            OAuthRequest req = getOAuthRequest(HttpVerb.PUT, uri.toString(), payload);
            return sendRequest(req);
        }
        catch (URISyntaxException e) {
            throw new StackMobException(e.getMessage());
        }
    }

    private String sendDeleteRequest() throws StackMobException {
        try {
            String query = null;
            if (null != params) {
                query = formatQueryString(getParamsForRequest());
            }

            URI uri = createURI(getScheme(), getHost(), getPath(), query);
            OAuthRequest req = getOAuthRequest(HttpVerb.DELETE, uri.toString());
            return sendRequest(req);
        }
        catch (URISyntaxException e) {
            throw new StackMobException(e.getMessage());
        }
    }

    private URI createURI(String scheme, String host, String path, String query) throws URISyntaxException {
        StringBuilder uriBuilder = new StringBuilder().append(scheme).append("://").append(host);
        if(!path.startsWith("/")) {
            uriBuilder.append("/");
        }
        uriBuilder.append(path);

        if(query != null && query.length() > 0) {
            uriBuilder.append("?").append(query);
        }

        return new URI(uriBuilder.toString());
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

    private String percentEncode(String s) throws UnsupportedEncodingException {
        return URLEncoder.encode(s, "UTF-8").replace("+", "%20");
    }

    private String formatQueryString(Map<String, String> params) {
        StringBuilder formatBuilder = new StringBuilder();
        boolean first = true;
        for(String key : params.keySet()) {
            if(!first) {
                formatBuilder.append("&");
            }
            first = false;
            String value = params.get(key);
            try {
                formatBuilder.append(percentEncode(key)).append("=").append(percentEncode(value));
            }
            catch(UnsupportedEncodingException e) {
                //do nothing
            }
        }
        return formatBuilder.toString();
    }


    private Map<String, String> getParamsForRequest() {
        Map<String, String> ret = new HashMap<String, String>();
        if (null != params) {
            ret = params;
        }
        return ret;
    }

    private String getPayload() {
        String payload = "";
        if(null != params) {
            payload = formatQueryString(getParamsForRequest());
        }
        else if(null != requestObject) {
            payload = gson.toJson(requestObject);
        }
        return payload;
    }

    private OAuthRequest getOAuthRequest(HttpVerb method, String url) {
        OAuthRequest oReq = new OAuthRequest(Verb.valueOf(method.toString()), url);
        int apiVersion = session.getApiVersionNumber();
        final String contentType = "application/vnd.stackmob+json;";
        final String accept = contentType + " version="+apiVersion;
        String userAgentIntermediate = "StackMob Java Client; " + apiVersion;
        if(session.getAppName() != null) {
            userAgentIntermediate += "/"+session.getAppName();
        }
        final String userAgent = userAgentIntermediate;
        List<Pair<String, String>> headers = new ArrayList<Pair<String, String>>();
        headers.add(new Pair<String, String>("Content-Type", contentType));
        headers.add(new Pair<String, String>("Accept", accept));
        headers.add(new Pair<String, String>("User-Agent", userAgent));
        for(Pair<String, String> header: headers) {
            oReq.addHeader(header.getFirst(), header.getSecond());
        }

        oAuthService.signRequest(new Token("", ""), oReq);
        return oReq;
    }

    private OAuthRequest getOAuthRequest(HttpVerb method, String url, String payload) {
        OAuthRequest req = getOAuthRequest(method, url);
        req.addPayload(payload);
        return req;
    }

    private String sendRequest(OAuthRequest req) {
        Response ret = req.send();
        if(HttpRedirectHelper.isRedirected(ret.getCode())) {
            try {
                String newLocation = HttpRedirectHelper.getNewLocation(ret.getHeaders());
                HttpVerb verb = HttpVerb.valueOf(req.getVerb().toString());
                OAuthRequest newReq = getOAuthRequest(verb, newLocation);
                if(req.getBodyContents() != null && req.getBodyContents().length() > 0) {
                    newReq = getOAuthRequest(verb, newLocation, req.getBodyContents());
                }
                //does NOT protect against circular redirects
                redirectedCallback.redirected(req.getUrl(), ret.getHeaders(), ret.getBody(), newReq.getUrl());
                return sendRequest(newReq);
            }
            catch(Exception e) {
                return ret.getBody();
            }
        }
        else {
            return ret.getBody();
        }
    }

}