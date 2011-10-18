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
