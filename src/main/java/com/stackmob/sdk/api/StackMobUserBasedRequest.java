package com.stackmob.sdk.api;

import com.stackmob.sdk.callback.StackMobCallback;
import com.stackmob.sdk.net.HttpVerb;

import java.util.Map;

/**
 * com.stackmob.sdk.api
 * <p/>
 * Copyright 2011 StackMob
 * <p/>
 * User: aaron
 * Date: 10/5/11
 * Time: 1:40 PM
 */
public class StackMobUserBasedRequest extends StackMobRequest {
    public StackMobUserBasedRequest(StackMobSession session, String method, StackMobCallback callback) {
        super(session, method, callback);
    }

    public StackMobUserBasedRequest(StackMobSession session, String method, Map<String, Object> params, StackMobCallback callback) {
        super(session, method, params, callback);
    }

    @Override
    protected String getPath() {
        return "/" + session.getUserObjectName() + "/" + methodName;
    }
}
