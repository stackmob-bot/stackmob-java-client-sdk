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

package com.stackmob.sdk;

import com.stackmob.sdk.net.HttpRedirectHelper;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.*;

public class HttpRedirectHelperTests extends StackMobTestCommon {

    private static final int RedirectErrorCode = 302;
    private static final String RedirectedLoc = "http://redirected.com";


    @Test
    public void isRedirected() throws Exception {
        assertTrue(HttpRedirectHelper.isRedirected(RedirectErrorCode));
    }

    @Test
    public void getRedirectedBasic() throws Exception {
        Map<String, String> redirectHeaders = new HashMap<String, String>();
        redirectHeaders.put("Location", RedirectedLoc);
        String newLoc = HttpRedirectHelper.getNewLocation(redirectHeaders);
        assertEquals(RedirectedLoc, newLoc);
    }
}
