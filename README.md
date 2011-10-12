# StackMob Java Client SDK

With the StackMob Java Client SDK, you can integrate StackMob into any Java / JVM application.

Here are some example usages:

* Connect your Android app to your StackMob app (there is also an [https://github.com/stackmob/Stackmob_Android](Android SDK) that provides more Android specific functionality)
* Connect your Java command line utility to your StackMob app
* Connect your Tomcat, JBoss, etc... app to your StackMob app

Hopefully you can see the pattern here. With this library, you can connect almost any JVM to your StackMob app and access the same app data as with the [https://github.com/stackmob/StackMob_iOS](iOS), [https://github.com/stackmob/Stackmob_Android](Android) and [https://github.com/stackmob/stackmob-ruby](Ruby) SDKs.

## Getting Started

### With Maven

Using Maven is the best way to install with the Java SDK because it makes it easy for you to keep up to date. Here's how:

1. ``` git clone git@github.com:stackmob/stackmob-java-sdk.git ```
2. ``` cd stackmob-java-sdk ```
3. ``` mvn install ```
4. add this to your pom.xml file:

```xml
<dependency>
    <groupId>com.stackmob</groupId>
    <artifactId>stackmob-java-sdk</artifactId>
    <version>0.1.0-SNAPSHOT</version>
    <scope>compile</scope>
</dependency>
```

Starting very soon, the stackmob-java-sdk JAR will be in Maven Central, at which point steps 1-3 will become unnecessary. We will update these docs when that is complete.

### Without Maven

If you don't use Maven, you can do this:

1. download [this JAR](/Users/aaron/code/stackmob-java-sdk/target/stackmob-java-sdk-0.1.0-SNAPSHOT.jar)
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
[javadoc](http://stackmob.github.com/stackmob-java-sdk/javadoc/0.1.0/apidocs/) for more.


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