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

package com.stackmob.sdk.push;

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