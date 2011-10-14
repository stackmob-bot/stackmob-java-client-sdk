package com.stackmob.sdk;

import com.stackmob.sdk.callback.StackMobCallback;
import com.stackmob.sdk.exception.StackMobException;

public class StackMobTestCommon {
    public static final String API_KEY = "YOUR_API_KEY_HERE";
    public static final String API_SECRET = "YOUR_API_SECRET_HERE";
    public static final String USER_OBJECT_NAME = "user";
    public static final Integer API_VERSION_NUM = 0;

    public static final StackMobCallback EmptyCallback = new StackMobCallback() {
        @Override public void success(String responseBody) {}
        @Override public void failure(StackMobException e) {}
    };
}
