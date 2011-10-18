package com.stackmob.sdk.push;

/**
 * com.stackmob.sdk.push
 * <p/>
 * Copyright 2011 StackMob
 * <p/>
 * User: aaron
 * Date: 10/17/11
 * Time: 2:23 PM
 */

import com.google.gson.JsonObject;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import java.lang.reflect.Type;

public class StackMobPushTokenSerializer implements JsonSerializer<StackMobPushToken>{
    public JsonElement serialize(StackMobPushToken token, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.add("type", new JsonPrimitive(token.getTokenType().toString()));
        object.add("token", new JsonPrimitive(token.getToken()));
        object.add("registered_milliseconds", new JsonPrimitive(token.getRegisteredMilliseconds()));
        return object;
    }
}