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

import com.google.gson.Gson;
import com.stackmob.sdk.api.StackMob;
import com.stackmob.sdk.callback.StackMobCallback;
import com.stackmob.sdk.exception.StackMobException;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.lang.reflect.Modifier;


import static org.junit.Assert.*;

public class StackMobTestCommon {
    public static final String API_KEY = "cd2c9ccd-bd89-48e7-85b8-34ff9bebd3b0";
    public static final String API_SECRET = "ae3078d5-f7fa-4d32-8e0f-6f342830e08c";
    public static final String USER_OBJECT_NAME = "user";
    public static final Integer API_VERSION_NUM = 0;
    public static final Long MAX_LATCH_WAIT_TIME_MS = 2000L;

    public StackMobTestCommon() {
        assertFalse("YOUR_API_KEY_HERE".equals(API_KEY));
        assertFalse("YOUR_API_SECRET_HERE".equals(API_SECRET));
        stackmob = new StackMob(API_KEY, API_SECRET, USER_OBJECT_NAME, API_VERSION_NUM);
    }

    protected static final Gson gson = new Gson();

    protected final StackMob stackmob;

    protected abstract static class StackMobObject {

        public abstract String getId();
        public abstract String getName();

        public void delete(StackMob stackmob) {
            final String objectId = getId();
            assertNotNull(objectId);
            delete(stackmob, getName(), objectId, false);
        }



        public String toString() {
            if(getId() != null) {
                return "<"+getName()+" " + getId()+">";
            }
            else {
                return "<"+getName()+">";
            }
        }

        public static void delete(final StackMob stackmob, final String objectName, final String objectId, final Boolean ignoreFailure) {
            final CountDownLatch latch = new CountDownLatch(1);
            stackmob.delete(objectName, objectId, new StackMobCallback() {
                @Override
                public void success(String responseBody) {
                    if(!ignoreFailure) {
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
                fail("trying to delete " + objectName + " " + objectId + " resulted in exception: " + e.getMessage());
            }
        }

        public static <T extends StackMobObject> T create(final StackMob stackmob, final T object, final Class<T> objectClass) throws InterruptedException {
            final AtomicReference<T> ref = new AtomicReference<T>();
            final CountDownLatch latch = new CountDownLatch(1);
            stackmob.post(object.getName(), object, new StackMobCallback() {
                @Override
                public void success(String responseBody) {
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

    protected static class User extends StackMobObject {

        public String username;
        public String password;
        public Long createddate;
        public Long lastmoddate;

        //this ctor is used for gson deserialization
        public User(String username, String password, long createddate, long lastmoddate) {
            this.username = username;
            this.password = password;
            this.createddate = createddate;
            this.lastmoddate = lastmoddate;
        }

        public User(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public String getName() { return "user"; }
        public String getId() { return username; }
    }

    protected static class Game extends StackMobObject {

        //public for the benefit of Gson
        public List<String> players;
        public String game_id;
        public Long createddate;
        public Long lastmoddate;
        public String name;

        public Game(List<String> players, String gameId, long createdDate, long lastModDate, String name) {
            this(players, name);
            this.game_id = gameId;
            this.createddate = createdDate;
            this.lastmoddate = lastModDate;
        }

        public Game(List<String> players, String name) {
            this.players = players;
            this.name = name;
        }

        public String getName() { return "game"; }
        public String getId() { return game_id; }
    }

    //for capturing JSON errors with GSON
    protected class Error {
        public String error;
    }

}
