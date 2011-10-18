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

package com.stackmob.sdk;

import com.stackmob.sdk.api.StackMob;
import com.stackmob.sdk.api.StackMobRequest;
import com.stackmob.sdk.api.StackMobSession;
import com.stackmob.sdk.callback.StackMobRedirectedCallback;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.*;

import com.stackmob.sdk.callback.StackMobCallback;
import com.stackmob.sdk.exception.StackMobException;
import com.stackmob.sdk.net.HttpVerb;

import static com.stackmob.sdk.StackMobTestCommon.*;

public class StackMobRequestTests extends StackMobTestCommon {
    private StackMobRedirectedCallback redirectedCallback = new StackMobRedirectedCallback() {
      @Override
      public void redirected(HttpRequest origRequest, HttpResponse response, HttpRequest newRequest) {
        //do nothing
      }
    };

    private StackMobSession session = stackmob.getSession();

    @Test
    @Ignore("currently fails with Internal Server Error")
    public void testListapiSecureGetRequest() {
        StackMobRequest request = new StackMobRequest(session, "lisapi", new StackMobCallback() {
            @Override
            public void success(String responseBody) {
                assertNotNull(responseBody);
            }
            @Override
            public void failure(StackMobException e) {
                fail(e.getMessage());
            }
        }, redirectedCallback);

        request.sendRequest();
    }

    @Test
    public void testListapiSecurePostRequest() {
        StackMobRequest request = new StackMobRequest(session, "listapi", HttpVerb.POST, new StackMobCallback() {
            @Override
            public void success(String responseBody) {
                assertNotNull(responseBody);
            }
            @Override
            public void failure(StackMobException e) {
                fail(e.getMessage());
            }
        }, redirectedCallback);

        request.sendRequest();
    }

    @Test
    public void testListapiRegularGetRequest() {
        StackMobRequest request = new StackMobRequest(session, "listapi", new StackMobCallback() {
            @Override
            public void success(String responseBody) {
                assertNotNull(responseBody);
            }
            @Override
            public void failure(StackMobException e) {
                fail(e.getMessage());
            }
        }, redirectedCallback);

        request.sendRequest();
    }

    @Test
    public void testListapiRegularPostRequest() {
        StackMobRequest request = new StackMobRequest(session, "listapi", HttpVerb.POST, new StackMobCallback() {
            @Override
            public void success(String responseBody) {
                assertNotNull(responseBody);
            }
            @Override
            public void failure(StackMobException e) {
                fail(e.getMessage());
            }
        }, redirectedCallback);

        request.sendRequest();
    }

    @Test
    public void testInexistentMethodShouldFail() {
        StackMobRequest request = new StackMobRequest(session, "inexistent", new StackMobCallback() {
            @Override
            public void success(String responseBody) {
                fail("Inexistent method should fail");
            }
            @Override
            public void failure(StackMobException e) {
                assertNotNull(e.getMessage());
            }
        }, redirectedCallback);

        request.sendRequest();
    }
}