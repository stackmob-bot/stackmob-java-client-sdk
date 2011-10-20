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

package com.stackmob.sdk.net;

import java.net.URISyntaxException;
import java.util.Map;

public class HttpRedirectHelper {
    public static final int RedirectStatusCode = 302;

    /**
     * get the new request that represents the redirect if there is one
     * @param headers the headers from which to get the new loc
     * @return the new request to make. NOTE: if a redirect happened, setURI will be called on oldRequest and the result is returned, so your original request will be modified in place.
     * @throws Exception if there was a problem getting the redirect location
     * @throws URISyntaxException if there is a problem parsing the redirect URL
     */
    public static String getNewLocation(Map<String, String> headers) throws Exception {

        String loc = null;
        for(String key : headers.keySet()) {
            if(key.toLowerCase().equals("location")) {
                loc = headers.get(key);
            }
        }
        if(loc == null) {
            throw new Exception("redirect location requested but it was non existent in headers");
        }
        return loc;
    }

    public static boolean isRedirected(int responseCode) {
        return responseCode == RedirectStatusCode;
    }

}
