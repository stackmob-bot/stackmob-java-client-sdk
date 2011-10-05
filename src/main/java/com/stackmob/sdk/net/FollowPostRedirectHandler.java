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

package com.stackmob.sdk.net;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultRedirectHandler;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;

public class FollowPostRedirectHandler extends DefaultRedirectHandler {

  /**
   * HttpClient is compliant with the requirements of the HTTP specification
   * (RFC 2616) and does not automatically redirect other methods than GET and
   * HEAD. We have to override this method to automatically follow redirects
   * when using the POST method.
   */
  @Override
  public boolean isRedirectRequested(final HttpResponse response, final HttpContext context) {
    if (response == null) {
      throw new IllegalArgumentException("HTTP response may not be null");
    }

    int statusCode = response.getStatusLine().getStatusCode();
    switch (statusCode) {
      case HttpStatus.SC_MOVED_TEMPORARILY:
      case HttpStatus.SC_MOVED_PERMANENTLY:
      case HttpStatus.SC_TEMPORARY_REDIRECT:
        HttpRequest request = (HttpRequest) context.getAttribute(ExecutionContext.HTTP_REQUEST);
        String method = request.getRequestLine().getMethod();
        return method.equalsIgnoreCase(HttpGet.METHOD_NAME)
            || method.equalsIgnoreCase(HttpHead.METHOD_NAME)
            || method.equalsIgnoreCase(HttpPost.METHOD_NAME);
      case HttpStatus.SC_SEE_OTHER:
        return true;
      default:
        return false;
    }
  }

}
