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

package com.stackmob.sdk.api;

import com.stackmob.sdk.callback.StackMobRedirectedCallback;
import com.stackmob.sdk.callback.StackMobCallback;
import java.util.Map;

public class StackMobUserBasedRequest extends StackMobRequest {
    public StackMobUserBasedRequest(StackMobSession session, String method, StackMobCallback callback, StackMobRedirectedCallback redirCB) {
        super(session, method, callback, redirCB);
        isSecure = true;
    }

    public StackMobUserBasedRequest(StackMobSession session, String method, Map<String, String> params, StackMobCallback callback, StackMobRedirectedCallback redirCB) {
        super(session, method, params, callback, redirCB);
        isSecure = true;
    }

    @Override
    protected String getPath() {
        return "/" + session.getUserObjectName() + "/" + methodName;
    }

    @Override
    public StackMobUserBasedRequest setUrlFormat(String urlFormat) {
        this.urlFormat = urlFormat;
        return this;
    }
}
