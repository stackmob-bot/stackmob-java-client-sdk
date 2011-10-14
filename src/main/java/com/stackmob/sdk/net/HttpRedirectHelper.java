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

import com.stackmob.sdk.callback.StackMobRedirectedCallback;
import org.apache.http.*;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.protocol.HttpContext;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Arrays;

public class HttpRedirectHelper {
    public static final int RedirectStatusCode = HttpStatus.SC_MOVED_TEMPORARILY;//302

    /**
     * get the new request that represents the redirect if there is one
     * @param oldRequest the original request that you made
     * @param response the response that came back
     * @return the new request to make. NOTE: if a redirect happened, setURI will be called on oldRequest and the result is returned, so your original request will be modified in place.
     * @throws ProtocolException if there is a problem parsing headers to get the redirect URL
     * @throws URISyntaxException if there is a problem parsing the redirect URL
     */
    public static HttpRequestBase getRedirect(HttpRequestBase oldRequest, HttpResponse response) throws ProtocolException, URISyntaxException {
        if(!isRedirected(response)) {
            return oldRequest;
        }

        List<Header> newLocHeaders = Arrays.asList(response.getHeaders(HttpHeaders.LOCATION));
        if (newLocHeaders.size() < 1) {
            throw new ProtocolException(RedirectStatusCode + " given for redirect, but no location given");
        }
        oldRequest.setURI(new URI(newLocHeaders.get(0).getValue()));
        return oldRequest;
    }

    public static boolean isRedirected(HttpResponse response) {
        return response.getStatusLine().getStatusCode() == RedirectStatusCode;
    }

}
