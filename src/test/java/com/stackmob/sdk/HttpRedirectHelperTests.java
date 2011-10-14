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

import com.stackmob.sdk.net.HttpRedirectHelper;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.methods.HttpGet;

import java.io.ByteArrayInputStream;
import java.net.URI;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.BasicHttpEntity;

import static org.junit.Assert.*;

public class HttpRedirectHelperTests {

    private static final String RedirectedLoc = "http://redirected.com";
    private static final ProtocolVersion protocolVer = new ProtocolVersion("http", 1, 1);


    private static HttpResponse getNewRedirectResponse() {
        BasicHttpResponse res = new BasicHttpResponse(protocolVer, HttpStatus.SC_MOVED_TEMPORARILY, "testing redirects");
        res.addHeader("Location", RedirectedLoc);
        return res;
    }

    @Test
    public void isRedirected() throws Exception {
        assertTrue(HttpRedirectHelper.isRedirected(getNewRedirectResponse()));
    }

    @Test
    public void getRedirectedBasic() throws Exception {
        HttpRequestBase req = new HttpGet(new URI("http://original.com"));
        HttpResponse redirectResponse = getNewRedirectResponse();
        HttpRequest newReq = HttpRedirectHelper.getRedirect(req, redirectResponse);
        assertEquals(RedirectedLoc, newReq.getRequestLine().getUri());
    }

    @Test
    public void getRedirectedPreservesEntity() throws Exception {
        final String entityContents = "hello world";
        HttpPost req = new HttpPost(new URI("http://original.com"));
        BasicHttpEntity entity = new BasicHttpEntity();
        ByteArrayInputStream stream = new ByteArrayInputStream(entityContents.getBytes());
        entity.setContent(stream);
        req.setEntity(entity);
        HttpResponse redirectResponse = getNewRedirectResponse();
        HttpRequest newReq = HttpRedirectHelper.getRedirect(req, redirectResponse);
        assertTrue(newReq instanceof HttpPost);
        HttpPost newReqAsPost = (HttpPost) newReq;
        assertEquals(entityContents, EntityUtils.toString(newReqAsPost.getEntity()));
    }
}
