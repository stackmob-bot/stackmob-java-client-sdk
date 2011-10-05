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

package com.stackmob.java.sdk.api;

public class StackMobSession {

  private String key;
  private String secret;
  private String appName;
  private String subDomain;
  private String domain;
  private String userObjectName;
  private int apiVersionNumber;

  private String twitterConsumerKey;
  private String twitterConsumerSecret;

  private String facebookAppId;

  public StackMobSession(String key, String secret, String appName,
      String subDomain, String domain, String userObjectName,
      int apiVersionNumber) {

    this.key = key;
    this.secret = secret;
    this.appName = appName;
    this.subDomain = subDomain;
    this.domain = domain;
    this.userObjectName = userObjectName;
    this.apiVersionNumber = apiVersionNumber;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getSecret() {
    return secret;
  }

  public void setSecret(String secret) {
    this.secret = secret;
  }

  public String getAppName() {
    return appName;
  }

  public void setAppName(String appName) {
    this.appName = appName;
  }

  public String getSubDomain() {
    return subDomain;
  }

  public void setSubDomain(String subDomain) {
    this.subDomain = subDomain;
  }

  public String getDomain() {
    return domain;
  }

  public void setDomain(String domain) {
    this.domain = domain;
  }

  public String getUserObjectName() {
    return userObjectName;
  }

  public void setUserObjectName(String userObjectName) {
    this.userObjectName = userObjectName;
  }

  public int getApiVersionNumber() {
    return apiVersionNumber;
  }

  public void setApiVersionNumber(int apiVersionNumber) {
    this.apiVersionNumber = apiVersionNumber;
  }

  public String getTwitterConsumerKey() {
    return twitterConsumerKey;
  }

  public void setTwitterConsumerKey(String twitterConsumerKey) {
    this.twitterConsumerKey = twitterConsumerKey;
  }

  public String getTwitterConsumerSecret() {
    return twitterConsumerSecret;
  }

  public void setTwitterConsumerSecret(String twitterConsumerSecret) {
    this.twitterConsumerSecret = twitterConsumerSecret;
  }

  public void setFacebookAppId(String facebookAppId) {
    this.facebookAppId = facebookAppId;
  }

  public String getFacebookAppId() {
    return facebookAppId;
  }


}
