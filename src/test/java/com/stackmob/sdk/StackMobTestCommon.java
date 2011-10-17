package com.stackmob.sdk;

import com.stackmob.sdk.callback.StackMobCallback;
import com.stackmob.sdk.exception.StackMobException;
import com.stackmob.sdk.api.StackMob;

public class StackMobTestCommon {
    public static final String API_KEY = "cd2c9ccd-bd89-48e7-85b8-34ff9bebd3b0";
    public static final String API_SECRET = "ae3078d5-f7fa-4d32-8e0f-6f342830e08c";
    public static final String USER_OBJECT_NAME = "user";
    public static final Integer API_VERSION_NUM = 0;

    protected static final StackMobCallback EmptyCallback = new StackMobCallback() {
        @Override public void success(String responseBody) {}
        @Override public void failure(StackMobException e) {}
    };
    protected static StackMob stackmob = new StackMob(API_KEY, API_SECRET, USER_OBJECT_NAME, API_VERSION_NUM);
}
