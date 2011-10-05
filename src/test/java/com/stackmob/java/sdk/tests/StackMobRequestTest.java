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

package com.stackmob.java.sdk.tests;

import com.stackmob.java.sdk.api.StackMob;
import com.stackmob.java.sdk.api.StackMobRequest;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import com.stackmob.java.sdk.callback.StackMobCallback;
import com.stackmob.java.sdk.exception.StackMobException;
import com.stackmob.java.sdk.net.HttpVerb;

public class StackMobRequestTest {

  @BeforeClass
  public static void onlyOnce() {
    StackMob stackmob = StackMob.getInstance();
    stackmob.setApplication("7f1aebc7-0fb8-4265-bfea-2c42c08a3bf0",
        "81573b21-b948-4339-baa3-dbffe0ca4503", "androidtest",
        "fithsaring.mob1", "stackmob.com", "user", 0);
  }

  @Test
  public void testListapiSecureGetRequest() {

    StackMobRequest request = new StackMobRequest(StackMob
        .getInstance().getSession());

    request.methodName = "listapi";
    request.isSecure = true;
    request.isUserBased = false;
    request.httpMethod = HttpVerb.GET;
    request.callback = new StackMobCallback() {
      
      public void success(String responseBody) {
        assertNotNull(responseBody);
      }
      
      public void failure(StackMobException e) {
        fail(e.getMessage());
      }
    };

    request.sendRequest();
  }

  @Test
  public void testListapiSecurePostRequest() {

    StackMobRequest request = new StackMobRequest(StackMob
        .getInstance().getSession());

    request.methodName = "listapi";
    request.isSecure = true;
    request.isUserBased = false;
    request.httpMethod = HttpVerb.POST;
    request.callback = new StackMobCallback() {
      
      public void success(String responseBody) {
        assertNotNull(responseBody);
      }
      
      public void failure(StackMobException e) {
        fail(e.getMessage());
      }
    };

    request.sendRequest();
  }

  @Test
  public void testListapiRegularGetRequest() {

    StackMobRequest request = new StackMobRequest(StackMob
        .getInstance().getSession());

    request.methodName = "listapi";
    request.isSecure = false;
    request.isUserBased = false;
    request.httpMethod = HttpVerb.GET;
    request.callback = new StackMobCallback() {
      
      public void success(String responseBody) {
        assertNotNull(responseBody);
      }
      
      public void failure(StackMobException e) {
        fail(e.getMessage());
      }
    };

    request.sendRequest();
  }

  @Test
  public void testListapiRegularPostRequest() {

    StackMobRequest request = new StackMobRequest(StackMob
        .getInstance().getSession());

    request.methodName = "listapi";
    request.isSecure = false;
    request.isUserBased = false;
    request.httpMethod = HttpVerb.POST;
    request.callback = new StackMobCallback() {
      
      public void success(String responseBody) {
        assertNotNull(responseBody);
      }
      
      public void failure(StackMobException e) {
        fail(e.getMessage());
      }
    };

    request.sendRequest();
  }

  @Test
  public void testInexistentMethodShouldFail() {

    StackMobRequest request = new StackMobRequest(StackMob
        .getInstance().getSession());

    request.methodName = "inexistent";
    request.isSecure = true;
    request.isUserBased = false;
    request.httpMethod = HttpVerb.GET;
    request.callback = new StackMobCallback() {
      
      public void success(String responseBody) {
        fail("Inexistent method should fail");
      }
      
      public void failure(StackMobException e) {
        assertNotNull(e.getMessage());
      }
    };

    request.sendRequest();
  }
}
