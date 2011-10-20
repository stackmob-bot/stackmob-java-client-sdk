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

package com.stackmob.sdk.testobjects;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.stackmob.sdk.StackMobTestCommon;
import com.stackmob.sdk.api.StackMob;
import com.stackmob.sdk.callback.StackMobCallback;
import com.stackmob.sdk.exception.StackMobException;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.*;

public abstract class StackMobObject {

    protected static final Gson gson = new Gson();
    private static final long MAX_LATCH_WAIT_TIME_MS = 2000L;

    public abstract String getId();
    public abstract String getName();

    public void delete(StackMob stackmob) {
        delete(stackmob, false);
    }

    public void delete(StackMob stackmob, final boolean ignoreFail) {
        final String objectId = getId();
        final String objectName = getName();
        final CountDownLatch latch = new CountDownLatch(1);
        assertNotNull(objectId);

        stackmob.delete(objectName, objectId, new StackMobCallback() {
            @Override
            public void success(String responseBody) {
                if(!ignoreFail) {
                    assertEquals("response body was " + responseBody, "Successfully deleted document", responseBody);
                }
                latch.countDown();
            }

            @Override
            public void failure(StackMobException e) {
                fail("attempted to delete " + objectName + " " + objectId + " but failed: " + e.getMessage());
                latch.countDown();
            }
        });

        try {
            assertTrue(latch.await(MAX_LATCH_WAIT_TIME_MS, TimeUnit.MILLISECONDS));
        }
        catch (InterruptedException e) {
            fail("trying to delete " + getName() + " " + objectId + " resulted in exception: " + e.getMessage());
        }
    }

    public String toString() {
        if(getId() != null) {
            return "<"+getName()+" " + getId()+">";
        }
        else {
            return "<"+getName()+">";
        }
    }

    public static <T extends StackMobObject> T decodeFromJson(final String json, final Class<T> objectClass) throws JsonSyntaxException {
        T obj = gson.fromJson(json, objectClass);
        assertNotNull(obj.getId());
        assertNotNull(obj.getName());
        return obj;
    }

    public static <T extends StackMobObject> T create(final StackMob stackmob, final T object, final Class<T> objectClass) throws InterruptedException {
        final AtomicReference<T> ref = new AtomicReference<T>();
        final CountDownLatch latch = new CountDownLatch(1);
        stackmob.post(object.getName(), object, new StackMobCallback() {
            @Override
            public void success(String responseBody) {
                StackMobTestCommon.assertNotError(responseBody);
                T obj = gson.fromJson(responseBody, objectClass);
                assertNotNull(obj.getId());
                ref.set(obj);
                latch.countDown();
            }

            @Override
            public void failure(StackMobException e) {
                fail("creating " + object.getName() + " threw an exception: " + e.getMessage());
            }
        });
        assertTrue(latch.await(MAX_LATCH_WAIT_TIME_MS, TimeUnit.MILLISECONDS));
        return ref.get();
    }
}
