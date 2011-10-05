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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.HashMap;

import com.stackmob.sdk.callback.StackMobRedirectedCallback;
import com.stackmob.sdk.callback.StackMobCallback;
import com.stackmob.sdk.net.HttpVerb;
import com.stackmob.sdk.net.HttpHelper;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;

public class StackMob {
    private StackMobSession session;
    private String urlFormat = StackMobRequest.DEFAULT_URL_FORMAT;
    private final Object urlFormatLock = new Object();

    protected static class RegistrationIDAndUser {
        public String userId;
        public Map<String, String> token = new HashMap<String, String>();

        public RegistrationIDAndUser(String registrationID, String user) {
            userId = user;
            token.put("token", registrationID);
            token.put("type", "android");
        }
    }

    protected StackMobRedirectedCallback userRedirectedCallback;

    protected StackMobRedirectedCallback redirectedCallback = new StackMobRedirectedCallback() {
      @Override
      public void redirected(HttpRequest origRequest, HttpResponse response, HttpRequest newRequest) {
        try {
            URI uri = new URI(newRequest.getRequestLine().getUri());
            synchronized(urlFormatLock) {
                if(!urlFormat.equals(uri.getHost())) {
                  urlFormat = uri.getHost();
                  userRedirectedCallback.redirected(origRequest, response, newRequest);
                }
            }
        }
        catch (URISyntaxException e) {
          //do nothing - we were unable to parse the new URL
        }
      }
    };

    public StackMob(String apiKey,
                    String apiSecret,
                    String appName,
                    String subDomain,
                    String domain,
                    String userObjectName,
                    Integer apiVersionNumber) {
        setSession(new StackMobSession(apiKey, apiSecret, appName, subDomain, domain, userObjectName, apiVersionNumber));
    }

    public StackMob(String apiKey,
                    String apiSecret,
                    String appName,
                    String subDomain,
                    String domain,
                    String userObjectName,
                    Integer apiVersionNumber,
                    String urlFormat,
                    StackMobRedirectedCallback redirectedCallback) {
        this(apiKey, apiSecret, appName, subDomain, domain, userObjectName, apiVersionNumber);
        this.userRedirectedCallback = redirectedCallback;
        this.urlFormat = urlFormat;
    }

    public void setTwitterConsumer(String consumerKey, String consumerSecret) {
        session.setTwitterConsumerKey(consumerKey);
        session.setTwitterConsumerSecret(consumerSecret);
    }

    public void setFacebookAppId(String appId) {
        session.setFacebookAppId(appId);
    }

    public void login(HashMap<String, Object> params, StackMobCallback callback) {
        new StackMobUserBasedRequest(session, "login", params, callback, redirectedCallback).setUrlFormat(urlFormat).sendRequest();
    }

    public void logout(StackMobCallback callback) {
        new StackMobUserBasedRequest(this.session, "logout", callback, redirectedCallback).setUrlFormat(urlFormat).sendRequest();
    }

    public void startSession(StackMobCallback callback) {
        new StackMobRequest(this.session, "startsession", HttpVerb.POST, callback, redirectedCallback).setUrlFormat(urlFormat).sendRequest();
    }

    public void endSession(StackMobCallback callback) {
        new StackMobRequest(this.session, "endsession", HttpVerb.POST, callback, redirectedCallback).setUrlFormat(urlFormat).sendRequest();
    }

    public void twitterLogin(String token, String secret, StackMobCallback callback) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("tw_tk", token);
        params.put("tw_ts", secret);
        new StackMobUserBasedRequest(this.session, "twitterlogin", params, callback, redirectedCallback).setUrlFormat(urlFormat).sendRequest();
    }

    public void twitterStatusUpdate(String message, StackMobCallback callback) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("tw_st", message);
        new StackMobUserBasedRequest(this.session, "twitterStatusUpdate", params, callback, redirectedCallback).setUrlFormat(urlFormat).sendRequest();
    }

    public void registerWithTwitterToken(String token, String secret, String username, StackMobCallback callback) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("tw_tk", token);
        params.put("tw_ts", secret);
        params.put("username", username);
        new StackMobUserBasedRequest(this.session, "createUserWithTwitter", params, callback, redirectedCallback).setUrlFormat(urlFormat).sendRequest();
    }

    public void linkUserWithTwitterToken(String token, String secret, StackMobCallback callback) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("tw_tk", token);
        params.put("tw_ts", secret);

        new StackMobUserBasedRequest(this.session, "linkUserWithTwitter", params, callback, redirectedCallback).setUrlFormat(urlFormat).sendRequest();
    }

    public void facebookLogin(String token, StackMobCallback callback) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("fb_at", token);

        new StackMobUserBasedRequest(this.session, "facebookLogin", params, callback, redirectedCallback).setUrlFormat(urlFormat).sendRequest();
    }

    public void registerWithFacebookToken(String token, String username, StackMobCallback callback) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("fb_at", token);
        params.put("username", username);

        new StackMobUserBasedRequest(this.session, "createUserWithFacebook", params, callback, redirectedCallback).setUrlFormat(urlFormat).sendRequest();
    }

    public void linkUserWithFacebookToken(String token, StackMobCallback callback) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("fb_at", token);

        new StackMobUserBasedRequest(this.session, "linkUserWithFacebook", params, callback, redirectedCallback).setUrlFormat(urlFormat).sendRequest();
    }

    public void facebookPostMessage(String msg, StackMobCallback callback) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("message", msg);

        new StackMobUserBasedRequest(this.session, "postFacebookMessage", params, callback, redirectedCallback).setUrlFormat(urlFormat).sendRequest();
    }

    public void getFacebookUserInfo(StackMobCallback callback) {
        get("getFacebookUserInfo", callback);
    }

    public void registerForPushWithUser(String username, String registrationID, StackMobCallback callback) {
        RegistrationIDAndUser tokenAndUser = new RegistrationIDAndUser(registrationID, username);
        post("/push/register_device_token_universal", tokenAndUser, callback);
    }

    public void get(String path, StackMobCallback callback) {
        new StackMobRequest(this.session, path, callback, redirectedCallback).setUrlFormat(urlFormat).sendRequest();
    }

    public void get(String path, HashMap<String, Object> arguments, StackMobCallback callback) {
        new StackMobRequest(this.session, path, arguments, callback, redirectedCallback).setUrlFormat(urlFormat).sendRequest();
    }

    public void post(String path, Object requestObject, StackMobCallback callback) {
        new StackMobRequest(this.session, path, HttpVerb.POST, requestObject, callback, redirectedCallback).setUrlFormat(urlFormat).sendRequest();
    }

    public void put(String path, String id, Object requestObject, StackMobCallback callback) {
        new StackMobRequest(this.session, path + "/" + id, HttpVerb.PUT, requestObject, callback, redirectedCallback).setUrlFormat(urlFormat).sendRequest();
    }

    public void delete(String path, String id, StackMobCallback callback) {
        new StackMobRequest(this.session, path + "/" + id, HttpVerb.DELETE, callback, redirectedCallback).setUrlFormat(urlFormat).sendRequest();
    }

    private void setSession(StackMobSession session) {
        this.session = session;
        HttpHelper.setVersion(this.session.getApiVersionNumber());
    }

    public StackMobSession getSession() {
        return session;
    }

}
