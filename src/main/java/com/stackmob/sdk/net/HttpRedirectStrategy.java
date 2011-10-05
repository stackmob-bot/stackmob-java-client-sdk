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
import org.apache.http.client.RedirectStrategy;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.RequestWrapper;
import org.apache.http.protocol.HttpContext;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Arrays;

import java.net.URI;

public class HttpRedirectStrategy implements RedirectStrategy {
    public static final int HTTP_REDIRECT_STATUS_CODE = 302; // StackMob uses the FOUND redirect (302)
    private StackMobRedirectedCallback redirectedCallback;

    public HttpRedirectStrategy(StackMobRedirectedCallback redirectedCallback) {
      this.redirectedCallback = redirectedCallback;
    }

    public HttpUriRequest getRedirect(HttpRequest request, HttpResponse response, HttpContext context)
        throws ProtocolException {
        RequestWrapper wrapper = new RequestWrapper(request);
        if(response.getStatusLine().getStatusCode() == HTTP_REDIRECT_STATUS_CODE) {
            List<Header> headers = Arrays.asList(response.getAllHeaders());
            Header newLocHeader = null;
            for(Header h : headers) {
                if(h.getName().equals("Location")) {
                    newLocHeader = h;
                }
            }
            if(newLocHeader ==  null) {
                throw new ProtocolException("301 given for redirect, but no location given");
            }

            try {
                wrapper.setURI(new URI(newLocHeader.getValue()));
                redirectedCallback.redirected(request, response, wrapper);
            }
            catch(URISyntaxException e) {
                throw new ProtocolException("problem with location headers: " + e.getMessage());
            }
        }

        return wrapper;
    }

    public boolean isRedirected(HttpRequest request, HttpResponse response, HttpContext context) {
        return response.getStatusLine().getStatusCode() == HTTP_REDIRECT_STATUS_CODE;
    }
}
