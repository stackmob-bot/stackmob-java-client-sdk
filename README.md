# StackMob Java Client SDK

With the StackMob Java Client SDK, you can integrate StackMob into any Java / JVM application.

Here are some example usages:

* Connect your Android app to your StackMob app (there is also an [Android SDK](https://github.com/stackmob/Stackmob_Android) that provides Android specific functionality)
* Connect your Java command line utility to your StackMob app
* Connect your Tomcat, JBoss, etc... app to your StackMob app

Hopefully you can see the pattern here. With this library, you can connect almost any JVM to your StackMob app and access the same app data as with the [iOS](https://github.com/stackmob/StackMob_iOS), [Android](https://github.com/stackmob/Stackmob_Android) and [Ruby](https://github.com/stackmob/stackmob-ruby) SDKs.

## Getting Started

### With Maven

```xml
<dependency>
    <groupId>com.stackmob</groupId>
    <artifactId>stackmob-java-client-sdk</artifactId>
    <version>0.1.3</version>
</dependency>
```

### With SBT

```scala
libraryDependencies += "com.stackmob" % "stackmob-java-client-sdk" % "0.1.3"
```

### Without Maven

[Download this JAR](http://search.maven.org/remotecontent?filepath=com/stackmob/stackmob-java-client-sdk/0.1.3/stackmob-java-client-sdk-0.1.3.jar).
2. put it in your Java CLASSPATH

## Coding

The main interface to your app on StackMob's servers is through the com.stackmob.sdk.api.StackMob object. The following code shows how to create a new object using this object:

```java
import com.stackmob.sdk.api.StackMob;
import com.stackmob.sdk.exception.StackMobException;

final String API_KEY = "YOUR API KEY HERE";
final String API_SECRET = "YOUR API SECRET HERE";
//leave this as a blank string if you don't have a user object.
//if you leave it blank, however, you must not call login, logout or any of the twitter or facebook methods,
//so we highly recommend that you set up a user object
final String USER_OBJ_NAME = "users";
//0 for sandbox, 1 or higher for a deployed API
final Integer API_VERSION = 0;
StackMob stackmob = new StackMob(API_KEY, API_SECRET, USER_OBJ_NAME, API_VERSION);

class MyObject {
    public String primary_key;
    public long createdDate;
    public long lastModDate;
    public String objectName;

    public MyObject(String n) {
        this.objectName = n;
    }
}

MyObject object = new MyObject("test object");

//create an object
stackmob.post("MyObject", object, new StackMobCallback() {
    @Override
    public void success(String responseBody) {
        //handle the successul set
    }
    @Override
    public void failure(StackMobException e) {
        //handle a failure
    }
});
```

Once you have it, you can use the StackMob object to execute a wide range of operations against your app on StackMob's servers. Check out the
[javadoc](http://stackmob.github.com/stackmob-java-client-sdk/javadoc/0.1.1/apidocs/) for more.

## Issues
We use Github to track issues with the SDK. If you find any issues, please report them [here](https://github.com/stackmob/stackmob-java-client-sdk/issues), and include as many details as possible about the issue you encountered.

## Contributing
We encourage contributions to the StackMob SDK. If you'd like to contribute, fork this repository and make your changes. Before you submit a pull request to us with your changes, though, please keep the following in mind:

1. We strive to maintain Android compatability. Please make a best effort to ensure that your code runs on Android.
2. Be sure to test your code against live StackMob servers. To do, use com.stackmob.sdk.StackMobTestCommon in your tests so that you can change your API keys in one place.
3. If your tests must run with a specific server configuration (ie: specific object model, etc...), please include a descr

## Copyright

Copyright 2011 StackMob

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.