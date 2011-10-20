# Stackmob Java SDK (Release Notes)

### 0.1.5 (10/20/2011)
* Added the StackMobQuery class to assist with building complex query operations (ie: <, >, <=, =>, IN)
	* See [Javadoc](http://stackmob.github.com/stackmob-java-client-sdk/javadoc/0.1.5/apidocs) and [README](http://stackmob.github.com/stackmob-java-client-sdk/javadoc/0.1.5/apidocs) for more details
* Overhaul of OAuth signing process & removed httpclient & signpost dependencies
* Fixed bug with login where it was not correctly saving the login cookie
* Changed StackMobRedirectCallback interface. See [Javadoc](http://stackmob.github.com/stackmob-java-client-sdk/javadoc/0.1.5/apidocs/com/stackmob/sdk/callback/StackMobRedirectedCallback.html) for more.

### 0.1.4 (10/17/2011)
* StackMob Push REST API support

### 0.1.3 (10/13/2011)
* Android compatability fixes
* Fixed SSL hostname verification issues for HTTPS
* Simplified redirect handling

### 0.1.1 (10/6/2011)
* idential functionality to 0.1.0. this release was done to correct a problem with the previous release

### 0.1.0 (10/6/2011)
* Initial version of the StackMob Java SDK
  * Basic GET, POST, PUT, DELETE functionality
  * login/logout functionality
  * Twitter & Facebook functionality
  * ability to follow cluster redirects given by the StackMob platform
  * ability to cache cluster redirects given by the StackMob platform



