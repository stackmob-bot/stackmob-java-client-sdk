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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import org.junit.Test;
import org.junit.Ignore;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.stackmob.sdk.api.*;
import com.stackmob.sdk.callback.StackMobCallback;
import com.stackmob.sdk.exception.StackMobException;

public class StackMobTest {
    private StackMob stackmob = new StackMob("7f1aebc7-0fb8-4265-bfea-2c42c08a3bf0",
            "81573b21-b948-4339-baa3-dbffe0ca4503", "androidtest",
            "stackmob",
            "stackmob.com",
            "user",
            0);

    public static class Game {

        //public for the benefit of Gson
        public List<String> players;
        public String game_id;
        public long createdDate;
        public long lastModDate;
        public String name;

        //for Gson deserialization
        public Game(List<String> players, String gameId, long createdDate, long lastModDate, String name) {
          this(players, name);
          this.game_id = gameId;
          this.createdDate = createdDate;
          this.lastModDate = lastModDate;
        }

        public Game(List<String> players, String name) {
            this.players = players;
            this.name = name;
        }
    }

    @Test
    public void testSingleton() {
        assertNotNull(stackmob);
    }

    @Test
    public void testLoginShouldBeSucessful() {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("username", "admin");
        params.put("password", "1234");

        StackMobCallback callback = new StackMobCallback() {
            @Override
            public void success(String responseBody) {
                assertNotNull(responseBody);
            }
            @Override
            public void failure(StackMobException e) {
                fail(e.getMessage());
            }
        };

        stackmob.login(params, callback);
    }

    @Test
    public void testLoginShouldFail() {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("username", "idonotexist");
        params.put("password", "ghost");

        StackMobCallback callback = new StackMobCallback() {
            @Override
            public void success(String responseBody) {
                fail("Login shouldn't succeed when login credentials are wrong");
            }
            @Override
            public void failure(StackMobException e) {
                assertEquals("Unauthorized", e.getMessage());
            }
        };

        stackmob.login(params, callback);
    }

    @Test
    public void testLogoutShouldBeSucessful() {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("username", "admin");
        params.put("password", "1234");

        stackmob.login(params, new StackMobCallback() {
            @Override
            public void success(String responseBody) {
            }

            @Override
            public void failure(StackMobException e) {
                fail(e.getMessage());
            }
        });

        StackMobCallback callback = new StackMobCallback() {
            @Override
            public void success(String responseBody) {
                assertNotNull(responseBody);
            }
            @Override
            public void failure(StackMobException e) {
                fail(e.getMessage());
            }
        };

        stackmob.logout(callback);
    }

    @Test
    public void startSessionTest() {
        stackmob.startSession(new StackMobCallback() {
            @Override
            public void success(String responseBody) {
                assertNotNull(responseBody);
            }

            @Override
            public void failure(StackMobException e) {
                fail(e.getMessage());
            }
        });
    }

    @Test
    @Ignore("endsession is currently returning 404")
    public void endSessionTest() {
        stackmob.endSession(new StackMobCallback() {
            @Override
            public void success(String responseBody) {
                System.out.println("endsession: " + responseBody);
                assertNotNull(responseBody);
            }
            @Override
            public void failure(StackMobException e) {
                fail(e.getMessage());
            }
        });
    }

    @Test
    public void testGetWithoutArguments() {
        StackMobCallback callback = new StackMobCallback() {
            @Override
            public void success(String responseBody) {
                assertNotNull(responseBody);
                Gson gson = new Gson();
                Type collectionType = new TypeToken<List<Game>>() {}.getType();
                List<Game> games = gson.fromJson(responseBody, collectionType);
                assertNotNull(games);
                assertFalse(games.isEmpty());
            }
            @Override
            public void failure(StackMobException e) {
                fail(e.getMessage());
            }
        };

        stackmob.get("game", callback);
    }

    @Test
    public void testGetWithArguments() {
        StackMobCallback callback = new StackMobCallback() {
            @Override
            public void success(String responseBody) {
                assertNotNull(responseBody);
                Gson gson = new Gson();
                Type collectionType = new TypeToken<List<Game>>() {}.getType();
                List<Game> games = gson.fromJson(responseBody, collectionType);
                assertNotNull(games);
                assertFalse(games.size() > 1);
                Game game = games.get(0);
                assertEquals("one", game.name);
            }
            @Override
            public void failure(StackMobException e) {
                fail(e.getMessage());
            }
        };

        HashMap<String, Object> arguments = new HashMap<String, Object>();
        arguments.put("name", "one");
        stackmob.get("game", arguments, callback);
    }

    @Test
    public void testPostWithRequestObject() {
        StackMobCallback callback = new StackMobCallback() {
            @Override
            public void success(String responseBody) {
                Gson gson = new Gson();
                Game game = gson.fromJson(responseBody, Game.class);
                assertEquals("newGame", game.name);
                }
            @Override
            public void failure(StackMobException e) {
                fail(e.getMessage());
            }
        };

        Game game = new Game(new ArrayList<String>(), "newGame");
        game.name = "newGame";
        stackmob.post("game", game, callback);
    }

    @Test
    public void testDeleteWithId() {
        Game game = new Game(new ArrayList<String>(), "gameToDelete");

        stackmob.post("game", game, new StackMobCallback() {
            @Override
            public void success(String responseBody) {
                Gson gson = new Gson();
                Game game = gson.fromJson(responseBody, Game.class);
                stackmob.delete("game", game.game_id, new StackMobCallback() {
                    @Override
                    public void success(String responseBody) {
                        assertNotNull(responseBody);
                    }

                    @Override
                    public void failure(StackMobException e) {
                        fail(e.getMessage());
                    }
                });
            }

            @Override
            public void failure(StackMobException e) {
                fail(e.getMessage());
            }
        });
    }

    @Test
    public void testPutWithIdAndObjectRequest() {
        final String oldName = "oldGameName";
        final String newName = "newGameName";
        Game game = new Game(new ArrayList<String>(), oldName);

        stackmob.post("game", game, new StackMobCallback() {
            @Override
            public void success(String responseBody) {
                Gson gson = new Gson();
                Game game = gson.fromJson(responseBody, Game.class);
                game.name = newName;
                stackmob.put("game", game.game_id, game, new StackMobCallback() {
                    @Override
                    public void success(String responseBody) {
                        Gson gson = new Gson();
                        Game game = gson.fromJson(responseBody, Game.class);
                        assertNotNull(game);
                        assertEquals(newName, game.name);
                    }
                    @Override
                    public void failure(StackMobException e) {
                        fail(e.getMessage());
                    }
                });
            }
            @Override
            public void failure(StackMobException e) {
                fail(e.getMessage());
            }
        });
    }
}