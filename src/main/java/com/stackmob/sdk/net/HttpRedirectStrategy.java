package com.stackmob.sdk.net;

/**
 * com.stackmob.sdk.net
 * <p/>
 * Copyright 2011 StackMob
 * <p/>
 * User: aaron
 * Date: 10/5/11
 * Time: 11:40 AM
 */

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
