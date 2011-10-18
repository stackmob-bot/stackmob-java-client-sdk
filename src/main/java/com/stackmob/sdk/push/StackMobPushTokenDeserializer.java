package com.stackmob.sdk.push;

import com.google.gson.*;

import java.lang.reflect.Type;

public class StackMobPushTokenDeserializer implements JsonDeserializer<StackMobPushToken> {
    public StackMobPushToken deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        JsonObject obj = json.getAsJsonObject();

        JsonPrimitive tokenStringPrimitive = obj.getAsJsonPrimitive("token");
        String tokenString = tokenStringPrimitive.getAsString();

        JsonPrimitive tokenTypePrimitive = obj.getAsJsonPrimitive("type");
        StackMobPushToken.TokenType tokenType = StackMobPushToken.TokenType.valueOf(tokenTypePrimitive.getAsString());

        JsonPrimitive registeredMSPrimitive = obj.getAsJsonPrimitive("registered_milliseconds");
        long registeredMS = registeredMSPrimitive.getAsInt();

        return new StackMobPushToken(tokenString, tokenType, registeredMS);
    }
}
