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

import java.util.Map;
import java.util.HashMap;

import com.stackmob.sdk.callback.StackMobCallback;
import com.stackmob.sdk.net.HttpVerb;
import com.stackmob.sdk.net.HttpHelper;

public class StackMob {
    private StackMobSession session;

    protected static class RegistrationIDAndUser {
        public String userId;
        public Map<String, String> token = new HashMap<String, String>();

        public RegistrationIDAndUser(String registrationID, String user) {
            userId = user;
            token.put("token", registrationID);
            token.put("type", "android");
        }
    }

    public StackMob(String apiKey,
                    String apiSecret,
                    String appName,
                    String subDomain,
                    String domain,
                    String userObjectName,
                    Integer apiVersionNumber) {
        setSession(new StackMobSession(apiKey, apiSecret, appName, subDomain, domain, userObjectName, apiVersionNumber));
    }

    public void setTwitterConsumer(String consumerKey, String consumerSecret) {
        session.setTwitterConsumerKey(consumerKey);
        session.setTwitterConsumerSecret(consumerSecret);
    }

    public void setFacebookAppId(String appId) {
        session.setFacebookAppId(appId);
    }

    public void login(HashMap<String, Object> params, StackMobCallback callback) {
        new StackMobUserBasedRequest(session, "login", params, callback).sendRequest();
    }

    public void logout(StackMobCallback callback) {
        new StackMobUserBasedRequest(this.session, "logout", callback).sendRequest();
    }

    public void startSession(StackMobCallback callback) {
        new StackMobRequest(this.session, "startsession", HttpVerb.POST, callback).sendRequest();
    }

    public void endSession(StackMobCallback callback) {
        new StackMobRequest(this.session, "endsession", HttpVerb.POST, callback).sendRequest();
    }

    public void twitterLogin(String token, String secret, StackMobCallback callback) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("tw_tk", token);
        params.put("tw_ts", secret);
        new StackMobUserBasedRequest(this.session, "twitterlogin", params, callback).sendRequest();
    }

    public void twitterStatusUpdate(String message, StackMobCallback callback) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("tw_st", message);
        new StackMobUserBasedRequest(this.session, "twitterStatusUpdate", params, callback).sendRequest();
    }

    public void registerWithTwitterToken(String token, String secret, String username, StackMobCallback callback) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("tw_tk", token);
        params.put("tw_ts", secret);
        params.put("username", username);
        new StackMobUserBasedRequest(this.session, "createUserWithTwitter", params, callback).sendRequest();
    }

    public void linkUserWithTwitterToken(String token, String secret, StackMobCallback callback) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("tw_tk", token);
        params.put("tw_ts", secret);

        new StackMobUserBasedRequest(this.session, "linkUserWithTwitter", params, callback).sendRequest();
    }

    public void facebookLogin(String token, StackMobCallback callback) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("fb_at", token);

        new StackMobUserBasedRequest(this.session, "facebookLogin", params, callback).sendRequest();
    }

    public void registerWithFacebookToken(String token, String username, StackMobCallback callback) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("fb_at", token);
        params.put("username", username);

        new StackMobUserBasedRequest(this.session, "createUserWithFacebook", params, callback).sendRequest();
    }

    public void linkUserWithFacebookToken(String token, String secret, StackMobCallback callback) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("fb_at", token);

        new StackMobUserBasedRequest(this.session, "linkUserWithFacebook", params, callback).sendRequest();
    }

    public void facebookPostMessage(String msg, StackMobCallback callback) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("message", msg);

        new StackMobUserBasedRequest(this.session, "postFacebookMessage", params, callback).sendRequest();
    }

    public void getFacebookUserInfo(StackMobCallback callback) {
        get("getFacebookUserInfo", callback);
    }

    public void registerForPushWithUser(String username, String registrationID, StackMobCallback callback) {
        RegistrationIDAndUser tokenAndUser = new RegistrationIDAndUser(registrationID, username);
        post("/push/register_device_token_universal", tokenAndUser, callback);
    }

    public void get(String path, StackMobCallback callback) {
        new StackMobRequest(this.session, path, callback).sendRequest();
    }

    public void get(String path, HashMap<String, Object> arguments, StackMobCallback callback) {
        new StackMobRequest(this.session, path, arguments, callback).sendRequest();
    }

    public void post(String path, Object requestObject, StackMobCallback callback) {
        new StackMobRequest(this.session, path, HttpVerb.POST, requestObject, callback).sendRequest();
    }

    public void put(String path, String id, Object requestObject, StackMobCallback callback) {
        new StackMobRequest(this.session, path + "/" + id, HttpVerb.PUT, requestObject, callback).sendRequest();
    }

    public void delete(String path, String id, StackMobCallback callback) {
        new StackMobRequest(this.session, path + "/" + id, HttpVerb.DELETE, callback).sendRequest();
    }

    private void setSession(StackMobSession session) {
        this.session = session;
        HttpHelper.setVersion(this.session.getApiVersionNumber());
    }

    public StackMobSession getSession() {
        return session;
    }

}
