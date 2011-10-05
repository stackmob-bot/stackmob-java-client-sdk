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
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.junit.Test;
import static org.junit.Assert.*;

import com.stackmob.sdk.callback.StackMobCallback;
import com.stackmob.sdk.exception.StackMobException;
import com.stackmob.sdk.net.HttpVerb;

public class StackMobRequestTest {
    private StackMob stackmob = new StackMob("7f1aebc7-0fb8-4265-bfea-2c42c08a3bf0",
            "81573b21-b948-4339-baa3-dbffe0ca4503",
            "androidtest",
            "fithsaring.mob1",
            "stackmob.com",
            "user",
            0);

    @Test
    public void testListapiSecureGetRequest() {
        StackMobRequest request = new StackMobRequest(stackmob.getSession(), "lisapi", new StackMobCallback() {
            @Override
            public void success(String responseBody) {
                assertNotNull(responseBody);
            }
            @Override
            public void failure(StackMobException e) {
                fail(e.getMessage());
            }
        });

        request.sendRequest();
    }

    @Test
    public void testListapiSecurePostRequest() {
        StackMobRequest request = new StackMobRequest(stackmob.getSession(), "listapi", HttpVerb.POST, new StackMobCallback() {
            @Override
            public void success(String responseBody) {
                assertNotNull(responseBody);
            }
            @Override
            public void failure(StackMobException e) {
                fail(e.getMessage());
            }
        });

        request.sendRequest();
    }

    @Test
    public void testListapiRegularGetRequest() {
        StackMobRequest request = new StackMobRequest(stackmob.getSession(), "listapi", new StackMobCallback() {
            @Override
            public void success(String responseBody) {
                assertNotNull(responseBody);
            }
            @Override
            public void failure(StackMobException e) {
                fail(e.getMessage());
            }
        });

        request.sendRequest();
    }

    @Test
    public void testListapiRegularPostRequest() {
        StackMobRequest request = new StackMobRequest(stackmob.getSession(), "listapi", HttpVerb.POST, new StackMobCallback() {
            @Override
            public void success(String responseBody) {
                assertNotNull(responseBody);
            }
            @Override
            public void failure(StackMobException e) {
                fail(e.getMessage());
            }
        });

        request.sendRequest();
    }

    @Test
    public void testInexistentMethodShouldFail() {
        StackMobRequest request = new StackMobRequest(stackmob.getSession(), "inexistent", new StackMobCallback() {
            @Override
            public void success(String responseBody) {
                fail("Inexistent method should fail");
            }
            @Override
            public void failure(StackMobException e) {
                assertNotNull(e.getMessage());
            }
        });

        request.sendRequest();
    }
}