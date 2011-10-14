package com.stackmob.sdk.net;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;

import java.io.IOException;

public class NoopResponseHandler implements ResponseHandler<HttpResponse> {
    public HttpResponse handleResponse(final HttpResponse response) throws HttpResponseException, IOException {
        return response;
    }
}
