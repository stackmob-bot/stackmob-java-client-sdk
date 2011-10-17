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

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Map;
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
import static org.junit.Assert.*;

public class StackMobTests extends StackMobTestCommon {

    public class Game {

        //public for the benefit of Gson
        public List<String> players;
        public String game_id;
        public long createddate;
        public long lastmoddate;
        public String name;

        //this ctor is used for gson deserialization
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

        public void delete(StackMob stackmob) {
            assertTrue(game_id != null);
            stackmob.delete("game", game_id, new StackMobCallback() {
                @Override
                public void success(String responseBody) {}

                @Override
                public void failure(StackMobException e) {
                    fail("attempted to delete game " + game_id + " but failed: " + e.getMessage());
                }
            });
        }
    }

    //for capturing JSON errors with GSON
    public class Error {
        public String error;
    }

    @Test
    public void testLoginShouldBeSucessful() {
        Map<String, String> params = new HashMap<String, String>();
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
        Map<String, String> params = new HashMap<String, String>();
        params.put("username", "idonotexist");
        params.put("password", "ghost");

        StackMobCallback callback = new StackMobCallback() {
            @Override
            public void success(String responseBody) {
                Error err = new Gson().fromJson(responseBody, Error.class);
                assertNotNull(err.error);
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
        Map<String, String> params = new HashMap<String, String>();
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
        Game game = new Game(Arrays.asList("one", "two"), "one");
        stackmob.post("game", game, EmptyCallback);
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

    private List<Game> getGamesFromJSON(String json) {
        assertNotNull(json);
        Type collectionType = new TypeToken<List<Game>>() {}.getType();
        List<Game> games = new Gson().fromJson(json, collectionType);
        assertNotNull(games);
        return games;
    }

    @Test
    public void testGetWithArguments() {
        Game game = new Game(Arrays.asList("one", "two"), "one");
        stackmob.post("game", game, EmptyCallback);
        StackMobCallback callback = new StackMobCallback() {
            @Override
            public void success(String responseBody) {
                List<Game> games = getGamesFromJSON(responseBody);
                assertTrue(games.size() >= 1);
                Game game = games.get(0);
                assertEquals("one", game.name);
                game.delete(stackmob);
            }
            @Override
            public void failure(StackMobException e) {
                fail(e.getMessage());
            }
        };

        Map<String, String> arguments = new HashMap<String, String>();
        arguments.put("name", "one");
        stackmob.get("game", arguments, callback);
    }

    @Test
    public void testGetWithQuery() {
        Long creationTime = System.currentTimeMillis();
        Game game = new Game(Arrays.asList("one", "two"), "one");
        stackmob.post("game", game, EmptyCallback);
        StackMobCallback callback = new StackMobCallback() {
            @Override
            public void success(String responseBody) {
                List<Game> games = getGamesFromJSON(responseBody);
                assertTrue(games.size() >= 1);
                for(Game game: games) {
                    game.delete(stackmob);
                }
            }

            @Override
            public void failure(StackMobException e) {
                fail(e.getMessage());
            }
        };

        StackMobQuery q = new StackMobQuery("game").field("lastmoddate").isGreaterThanOrEqualTo(creationTime).getQuery();
        stackmob.get(q, callback);
    }

    @Test
    public void testPostWithRequestObject() {
        StackMobCallback callback = new StackMobCallback() {
            @Override
            public void success(String responseBody) {
                Gson gson = new Gson();
                Game game = gson.fromJson(responseBody, Game.class);
                assertEquals("newGame", game.name);
                game.delete(stackmob);
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
    public void testPUT() {
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
                        assertNotNull(game.name);
                        assertEquals(newName, game.name);
                    }
                    @Override
                    public void failure(StackMobException e) {
                        fail(e.getMessage());
                    }
                });
                game.delete(stackmob);
            }
            @Override
            public void failure(StackMobException e) {
                fail(e.getMessage());
            }
        });
    }
}