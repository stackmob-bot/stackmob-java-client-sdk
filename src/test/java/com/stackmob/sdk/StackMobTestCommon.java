package com.stackmob.sdk;

import com.stackmob.sdk.callback.StackMobCallback;
import com.stackmob.sdk.exception.StackMobException;

public class StackMobTestCommon {
    public static final String API_KEY = "9867f87a-c1c2-4f5a-989a-e0f70032da0d";
    public static final String API_SECRET = "1f1ffead-dbbb-492f-b5ef-c25970105dab";
    public static final String USER_OBJECT_NAME = "user";
    public static final Integer API_VERSION_NUM = 0;

    public static final StackMobCallback EmptyCallback = new StackMobCallback() {
        @Override public void success(String responseBody) {}
        @Override public void failure(StackMobException e) {}
    };
}
