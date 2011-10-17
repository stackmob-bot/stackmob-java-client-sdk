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
                    if (!urlFormat.equalsIgnoreCase(uri.getHost())) {
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

    /**
     * create a new StackMob object. this is the preferred constructor
     * @param apiKey the api key for your app
     * @param apiSecret the api secret for your app
     * @param userObjectName the name of your app's user object. if you do not have a user object, pass the empty strinrg here and do not use the login, logout, facebook or twitter methods, as they will fail
     * @param appName the name of your application
     * @param apiVersionNumber the version of your app's API that you want to use with this StackMob session. pass 0 for sandbox
     */
    public StackMob(String apiKey, String apiSecret, String userObjectName, String appName, Integer apiVersionNumber) {
        setSession(new StackMobSession(apiKey, apiSecret, userObjectName, appName, apiVersionNumber));
    }

    /**
    * create a new StackMob object
    * @param apiKey the api key for your app
    * @param apiSecret the api secret for your app
    * @param userObjectName the name of your app's user object. if you do not have a user object, pass the empty string here and do not use the login, logout, facebook or twitter methods, as they will fail
    * @param apiVersionNumber the version of your app's API that you want to use with this StackMob session. pass 0 for sandbox
    */
    public StackMob(String apiKey, String apiSecret, String userObjectName, Integer apiVersionNumber) {
        setSession(new StackMobSession(apiKey, apiSecret, userObjectName, apiVersionNumber));
    }

    /**
    * create a new StackMob object. use this constructor if you do your own caching of URLs for redirection
     * @param apiKey the api key for your app
     * @param apiSecret the api secret for your app
     * @param userObjectName the name of your app's user object
     * @param apiVersionNumber the version number of your app's API that you want to use with this object. pass 0 for sandbox
     * @param urlFormat the format of URLs to use. for instance: api.mob1.stackmob.com
     * @param redirectedCallback callback to be called if the StackMob platform issues a redirect. you should use this callback to cache the new URLs. here is a sample callback:
     * <code>
     * new StackMobRedirectedCallback() {
     *   public void redirected(HttpRequest origRequest, HttpResponse response, HttpRequest newRequest) {
     *       try {
     *           URI uri = new URI(newRequest.getRequestLine().getUri());
     *           cache(uri.getHost);
     *       }
     *        catch (URISyntaxException e) {
     *           handleException(e);
     *       }
     *   }
     * }
     * }
     * </code>
     * note that this callback may be called in a background thread
     */
    public StackMob(String apiKey,
                  String apiSecret,
                  String userObjectName,
                  Integer apiVersionNumber,
                  String urlFormat,
                  StackMobRedirectedCallback redirectedCallback) {
        this(apiKey, apiSecret, userObjectName, apiVersionNumber);
        this.userRedirectedCallback = redirectedCallback;
        this.urlFormat = urlFormat;
    }

    /**
     * call the login method on StackMob
     * @param params parameters to pass to the login method
     * @param callback callback to be called when the server returns. may happen in a separate thread
     */
    public void login(Map<String, String> params, StackMobCallback callback) {
        new StackMobUserBasedRequest(session, "login", params, callback, redirectedCallback).setUrlFormat(urlFormat).sendRequest();
    }

    /**
     * call the logout method on StackMob
     * @param callback callback to be called when the server returns. may happen in a separate thread
     */
    public void logout(StackMobCallback callback) {
        new StackMobUserBasedRequest(this.session, "logout", callback, redirectedCallback).setUrlFormat(urlFormat).sendRequest();
    }

    /**
     * call the startsession method on StackMob
     * @param callback callback to call when the method completes
     */
    public void startSession(StackMobCallback callback) {
        new StackMobRequest(this.session, "startsession", HttpVerb.POST, callback, redirectedCallback).setUrlFormat(urlFormat).sendRequest();
    }

    /**
     * call the endsession method on stackmob
     * @param callback callback to be called when the server returns. may happen in a separate thread
     */
    public void endSession(StackMobCallback callback) {
        new StackMobRequest(this.session, "endsession", HttpVerb.POST, callback, redirectedCallback).setUrlFormat(urlFormat).sendRequest();
    }

    /**
     * call the twitterlogin method on stackmob
     * @param token the twitter session key (this is a per user key - different from the consumer key)
     * @param secret the twitter session secret (this is a per user secret - different from the consumer secret)
     * @param callback callback to be called when the server returns. may happen in a separate thread
     */
    public void twitterLogin(String token, String secret, StackMobCallback callback) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("tw_tk", token);
        params.put("tw_ts", secret);
        new StackMobUserBasedRequest(this.session, "twitterlogin", params, callback, redirectedCallback).setUrlFormat(urlFormat).sendRequest();
    }

    /**
     * call the twitterStatusUpdate method on StackMob
     * @param message the message to send. must be <= 140 characters
     * @param callback callback to be called when the server returns. may happen in a separate thread
     */
    public void twitterStatusUpdate(String message, StackMobCallback callback) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("tw_st", message);
        new StackMobUserBasedRequest(this.session, "twitterStatusUpdate", params, callback, redirectedCallback).setUrlFormat(urlFormat).sendRequest();
    }

    /**
     * create a new user on stackmob and associate it with an existing twitter user
     * @param token the twitter session key (this is a per user key - different from the consumer key)
     * @param secret the twitter session secret (this is a per user secret - different from the consumer secret)
     * @param username the username that the user should have
     * @param callback callback to be called when the server returns. may happen in a separate thread
     */
    public void registerWithTwitterToken(String token, String secret, String username, StackMobCallback callback) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("tw_tk", token);
        params.put("tw_ts", secret);
        params.put("username", username);
        new StackMobUserBasedRequest(this.session, "createUserWithTwitter", params, callback, redirectedCallback).setUrlFormat(urlFormat).sendRequest();
    }

    /**
     * link an existing stackmob user with an existing twitter user
     * @param token the twitter session key (this is a per user key - different from the consumer key)
     * @param secret the twitter session secret (this is a per user secret - different from the consumer secret)
     * @param callback callback to be called when the server returns. may happen in a separate thread
     */
    public void linkUserWithTwitterToken(String token, String secret, StackMobCallback callback) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("tw_tk", token);
        params.put("tw_ts", secret);

        new StackMobUserBasedRequest(this.session, "linkUserWithTwitter", params, callback, redirectedCallback).setUrlFormat(urlFormat).sendRequest();
    }

    /**
     * login into facebook on the StackMob platform
     * @param token the facebook user token
     * @param callback callback to be called when the server returns. may happen in a separate thread
     */
    public void facebookLogin(String token, StackMobCallback callback) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("fb_at", token);

        new StackMobUserBasedRequest(this.session, "facebookLogin", params, callback, redirectedCallback).setUrlFormat(urlFormat).sendRequest();
    }

    /**
     * create a new StackMob user and link that user with a facebook account
     * @param token the facebook user token
     * @param username the StackMob username that the new user should have
     * @param callback callback to be called when the server returns. may happen in a separate thread
     */
    public void registerWithFacebookToken(String token, String username, StackMobCallback callback) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("fb_at", token);
        params.put("username", username);

        new StackMobUserBasedRequest(this.session, "createUserWithFacebook", params, callback, redirectedCallback).setUrlFormat(urlFormat).sendRequest();
    }

    /**
     * link an existing StackMob user with a Facebook user
     * @param token the Facebook user token
     * @param callback callback to be called when the server returns. may happen in a separate thread
     */
    public void linkUserWithFacebookToken(String token, StackMobCallback callback) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("fb_at", token);

        new StackMobUserBasedRequest(this.session, "linkUserWithFacebook", params, callback, redirectedCallback).setUrlFormat(urlFormat).sendRequest();
    }

    /**
     * post a message to facebook
     * @param msg the message to post
     * @param callback callback to be called when the server returns. may happen in a separate thread. will not post to FB and will return nothing if there is no user logged into FB
     */
    public void facebookPostMessage(String msg, StackMobCallback callback) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("message", msg);

        new StackMobUserBasedRequest(this.session, "postFacebookMessage", params, callback, redirectedCallback).setUrlFormat(urlFormat).sendRequest();
    }

    /**
     * get facebook user info for the current user
     * @param callback callback to be called when the server returns. may happen in a separate thread. will return nothing if there is no currently logged in FB user
     */
    public void getFacebookUserInfo(StackMobCallback callback) {
        get("getFacebookUserInfo", callback);
    }

    /**
     * register a user for C2DM push notifications
     * @param username the StackMob username to register
     * @param registrationID the C2DM registration ID. see http://code.google.com/android/c2dm/#registering for detail on how to get this ID
     * @param callback callback to be called when the server returns. may happen in a separate thread
     */
    public void registerForPushWithUser(String username, String registrationID, StackMobCallback callback) {
        RegistrationIDAndUser tokenAndUser = new RegistrationIDAndUser(registrationID, username);
        post("/push/register_device_token_universal", tokenAndUser, callback);
    }

    /**
     * do a get request on the StackMob platform
     * @param path the path to get
     * @param callback callback to be called when the server returns. may happen in a separate thread
     */
    public void get(String path, StackMobCallback callback) {
        new StackMobRequest(this.session, path, callback, redirectedCallback).setUrlFormat(urlFormat).sendRequest();
    }

    /**
     * do a get request on the StackMob platform
     * @param path the path to get
     * @param arguments arguments to be encoded into the query string of the get request
     * @param callback callback to be called when the server returns. may happen in a separate thread
     */
    public void get(String path, Map<String, String> arguments, StackMobCallback callback) {
        new StackMobRequest(this.session, path, arguments, callback, redirectedCallback).setUrlFormat(urlFormat).sendRequest();
    }

    public void get(StackMobQuery query, StackMobCallback callback) {
        this.get("/"+query.getObjectName(), query.getArguments(), callback);
    }

    public void get(StackMobQueryWithField query, StackMobCallback callback) {
        this.get(query.getQuery(), callback);
    }

    /**
     * do a post request on the StackMob platform
     * @param path the path to get
     * @param requestObject the object to serialize and send in the POST body. this object will be serialized with Gson
     * @param callback callback to be called when the server returns. may happen in a separate thread
     */
    public void post(String path, Object requestObject, StackMobCallback callback) {
        new StackMobRequest(this.session, path, HttpVerb.POST, requestObject, callback, redirectedCallback).setUrlFormat(urlFormat).sendRequest();
    }

    /**
     * do a put request on the StackMob platform
     * @param path the path to put
     * @param id the id of the object to put
     * @param requestObject the object to serialize and send in the PUT body. this object will be serialized with Gson
     * @param callback callback to be called when the server returns. may happen in a separate thread
     */
    public void put(String path, String id, Object requestObject, StackMobCallback callback) {
        new StackMobRequest(this.session, path + "/" + id, HttpVerb.PUT, requestObject, callback, redirectedCallback).setUrlFormat(urlFormat).sendRequest();
    }

    /**
     * do a delete request to the stackmob platform
     * @param path the path to delete
     * @param id the id of the object to put
     * @param callback callback to be called when the server returns. may happen in a separate thread
     */
    public void delete(String path, String id, StackMobCallback callback) {
        new StackMobRequest(this.session, path + "/" + id, HttpVerb.DELETE, callback, redirectedCallback).setUrlFormat(urlFormat).sendRequest();
    }

    private void setSession(StackMobSession session) {
        this.session = session;
        HttpHelper.setVersion(this.session.getApiVersionNumber());
    }

    /**
     * get the session that this StackMob object contains
     * @return the session
     */
    public StackMobSession getSession() {
        return session;
    }

}
