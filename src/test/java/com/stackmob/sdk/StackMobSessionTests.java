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
    private StackMob stackmob = new StackMob("7f1aebc7-0fb8-4265-bfea-2c42c08a3bf0", "81573b21-b948-4339-baa3-dbffe0ca4503", "user", 0);

    @Test
    public void testSessionInitializedCorrectly() {
        StackMobSession session = stackmob.getSession();
        assertEquals("7f1aebc7-0fb8-4265-bfea-2c42c08a3bf0", session.getKey());
        assertEquals("81573b21-b948-4339-baa3-dbffe0ca4503", session.getSecret());
        assertEquals("user", session.getUserObjectName());
        assertEquals(0, session.getApiVersionNumber());
    }
}