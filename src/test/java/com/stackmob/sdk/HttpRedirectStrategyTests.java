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
import com.stackmob.sdk.net.HttpRedirectStrategy;
import com.stackmob.sdk.callback.StackMobRedirectedCallback;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.junit.Test;
import static org.junit.Assert.*;

public class HttpRedirectStrategyTests {
    private static final StackMobRedirectedCallback callback = new StackMobRedirectedCallback() {
        @Override
        public void redirected(HttpRequest origRequest, HttpResponse response, HttpRequest newRequest) {

        }
    };

    private static final HttpRedirectStrategy strategy = new HttpRedirectStrategy(callback);

    private static final ProtocolVersion protocolVer = new ProtocolVersion("http", 1, 1);
    private static final HttpRequest getRequest = new BasicHttpRequest("GET", "http://testurl.com/", protocolVer);
    private static final HttpRequest postRequest = new HttpPost();
    private static final HttpRequest putRequest = new HttpPut();
    private static final HttpRequest deleteRequest = new HttpDelete();

    private static final HttpResponse redirectResponse = new BasicHttpResponse(protocolVer, HttpStatus.SC_MOVED_TEMPORARILY, "testing redirects");
    private static final HttpContext context = new BasicHttpContext();

    @Test
    public void redirectForGet() {
        assertTrue(strategy.isRedirected(getRequest, redirectResponse, context));

    }

    @Test
    public void redirectForPost() {
        assertTrue(strategy.isRedirected(postRequest, redirectResponse, context));
    }

    @Test
    public void redirectForPut() {
        assertTrue(strategy.isRedirected(putRequest, redirectResponse, context));
    }

    @Test
    public void redirectForDelete() {
        assertTrue(strategy.isRedirected(deleteRequest, redirectResponse, context));
    }
}
