package com.stackmob.sdk.net;

import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import com.stackmob.sdk.util.Pair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class NoopResponseHandler implements ResponseHandler<Pair<HttpResponse, String>> {
    public Pair<HttpResponse, String> handleResponse(final HttpResponse response) throws IOException {
        return new Pair<HttpResponse, String>(response, EntityUtils.toString(response.getEntity()));
    }
}
