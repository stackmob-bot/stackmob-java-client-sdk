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

import java.util.List;
import java.util.Arrays;

public class HttpRedirectStrategy extends DefaultRedirectStrategy {
    public static final int RedirectStatusCode = HttpStatus.SC_MOVED_TEMPORARILY;//302

    private final StackMobRedirectedCallback redirectedCallback;

    public HttpRedirectStrategy(StackMobRedirectedCallback redirectedCallback) {
        this.redirectedCallback = redirectedCallback;
    }

    @Override
    public HttpUriRequest getRedirect(HttpRequest request, HttpResponse response, HttpContext context) throws ProtocolException {
        HttpUriRequest redir = super.getRedirect(request, response, context);

        if(isRedirected(request, response, context)) {
            List<Header> newLocHeaders = Arrays.asList(redir.getHeaders(HttpHeaders.LOCATION));
            if(newLocHeaders.size() < 1) {
                throw new ProtocolException(RedirectStatusCode + " given for redirect, but no location given");
            }
            redirectedCallback.redirected(request, response, redir);
        }

        return redir;
    }

    @Override
    public boolean isRedirected(HttpRequest request, HttpResponse response, HttpContext context) {
        return response.getStatusLine().getStatusCode() == RedirectStatusCode;
    }

}
