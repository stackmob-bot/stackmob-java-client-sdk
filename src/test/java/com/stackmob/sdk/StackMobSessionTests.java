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

import org.junit.Test;
import static org.junit.Assert.*;

import com.stackmob.sdk.api.*;

public class StackMobSessionTests {
    private StackMob stackmob = new StackMob(StackMobTestCommon.API_KEY, StackMobTestCommon.API_SECRET, StackMobTestCommon.USER_OBJECT_NAME, StackMobTestCommon.API_VERSION_NUM);

    @Test
    public void testSessionInitializedCorrectly() {
        StackMobSession session = stackmob.getSession();
        assertEquals(StackMobTestCommon.API_KEY, session.getKey());
        assertEquals(StackMobTestCommon.API_SECRET, session.getSecret());
        assertEquals(StackMobTestCommon.USER_OBJECT_NAME, session.getUserObjectName());
        assertEquals(StackMobTestCommon.API_VERSION_NUM, new Integer(session.getApiVersionNumber()));
    }
}