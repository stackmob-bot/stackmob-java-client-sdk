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

public class StackMobPushToken {

    public static enum TokenType {
        iOS("ios"),
        Android("android");

        private String type;
        TokenType(String type) {
            this.type = type;
        }

        public String toString() {
            return type;
        }
    }

    private String tokenString;
    private Long registeredMilliseconds;
    private TokenType type;

    public StackMobPushToken(String token, TokenType type) {
        this.tokenString = token;
        this.registeredMilliseconds = System.currentTimeMillis();
        this.type = type;
    }

    public StackMobPushToken(String token, TokenType type, Long registeredMS) {
        this(token, type);
        this.registeredMilliseconds = registeredMS;
    }

    public String getToken() {
        return tokenString;
    }

    public TokenType getTokenType() {
        return type;
    }

    public Long getRegisteredMilliseconds() {
        return registeredMilliseconds;
    }
}
