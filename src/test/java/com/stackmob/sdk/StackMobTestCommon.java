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

import com.google.gson.Gson;
import com.stackmob.sdk.api.StackMob;
import com.stackmob.sdk.testobjects.Error;
import static org.junit.Assert.*;

public class StackMobTestCommon {
    public static final String API_KEY = "YOUR_API_KEY_HERE";
    public static final String API_SECRET = "YOUR_API_SECRET_HERE";

    public static final String USER_OBJECT_NAME = "user";
    public static final Integer API_VERSION_NUM = 0;
    protected static final Gson gson = new Gson();
    protected final StackMob stackmob;

    public StackMobTestCommon() {
        assertFalse("YOUR_API_KEY_HERE".equals(API_KEY));
        assertFalse("YOUR_API_SECRET_HERE".equals(API_SECRET));
        stackmob = new StackMob(API_KEY, API_SECRET, USER_OBJECT_NAME, API_VERSION_NUM);
    }

    public static void assertNotError(String responseBody) {
        try {
            Error err = gson.fromJson(responseBody, Error.class);
            assertNull("request failed with error: " + err.error, err.error);
        }
        catch (Exception e) {
            //do nothing
        }
    }

    public static void assertError(String responseBody) {
        Error err = Error.decodeFromJson(responseBody, Error.class);
        assertNotNull(err.error);
    }
}
