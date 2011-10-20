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
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import com.stackmob.sdk.api.StackMobQuery;
import com.stackmob.sdk.testobjects.*;
import org.junit.Test;

import com.google.gson.reflect.TypeToken;
import com.stackmob.sdk.callback.StackMobCallback;
import com.stackmob.sdk.exception.StackMobException;
import static org.junit.Assert.*;

public class StackMobTests extends StackMobTestCommon {

    @Test public void login() throws Exception {
        final String username = "testUser";
        final String password = "1234";
        new User(username, password).delete(stackmob, true);

        User user = StackMobObject.create(stackmob, new User(username, password), User.class);
        user.password = password;
        Map<String, String> params = new HashMap<String, String>();
        params.put("username", user.username);
        params.put("password", user.password);

        StackMobCallback callback = new StackMobCallback() {
            @Override
            public void success(String responseBody) {
                assertNotNull(responseBody);
                assertNotError(responseBody);
            }
            @Override
            public void failure(StackMobException e) {
                fail(e.getMessage());
            }
        };

        stackmob.login(params, callback);
        user.delete(stackmob);
    }

    @Test public void loginShouldFail() {
        final String username = "doesntexist";
        final String password = "doesntexist";

        new User(username, password).delete(stackmob, true);

        Map<String, String> params = new HashMap<String, String>();
        params.put("username", username);
        params.put("password", password);


        StackMobCallback callback = new StackMobCallback() {
            @Override
            public void success(String responseBody) {
                assertError(responseBody);
            }
            @Override
            public void failure(StackMobException e) {
                assertEquals("Unauthorized", e.getMessage());
            }
        };

        stackmob.login(params, callback);
    }

    @Test public void logout() throws Exception {
        final String username = "username";
        final String password = "1234";

        new User(username, password).delete(stackmob, true);

        User user = StackMobObject.create(stackmob, new User(username, password), User.class);
        user.password = password;

        Map<String, String> params = new HashMap<String, String>();
        params.put("username", user.username);
        params.put("password", user.password);

        stackmob.login(params, new StackMobCallback() {
            @Override
            public void success(String responseBody) {
                assertNotError(responseBody);
            }

            @Override
            public void failure(StackMobException e) {
                fail(e.getMessage());
            }
        });

        stackmob.logout(new StackMobCallback() {
            @Override
            public void success(String responseBody) {
                assertNotError(responseBody);
                assertNotNull(responseBody);
            }
            @Override
            public void failure(StackMobException e) {
                fail(e.getMessage());
            }
        });

        user.delete(stackmob);
    }

    @Test public void startSession() {
        stackmob.startSession(new StackMobCallback() {
            @Override
            public void success(String responseBody) {
                assertNotError(responseBody);
                assertNotNull(responseBody);
            }

            @Override
            public void failure(StackMobException e) {
                fail(e.getMessage());
            }
        });
    }

    @Test public void getWithoutArguments() throws Exception {
        Game game = StackMobObject.create(stackmob, new Game(Arrays.asList("one", "two"), "one"), Game.class);

        stackmob.get("game", new StackMobCallback() {
            @Override
            public void success(String responseBody) {
                assertNotError(responseBody);
                assertNotNull(responseBody);
                Type collectionType = new TypeToken<List<Game>>() {}.getType();
                List<Game> games = gson.fromJson(responseBody, collectionType);
                assertNotNull(games);
                assertFalse(games.isEmpty());
            }
            @Override
            public void failure(StackMobException e) {
                fail(e.getMessage());
            }
        });
        game.delete(stackmob);
    }

    @Test public void getWithArguments() throws Exception {
        StackMobObject.create(stackmob, new Game(Arrays.asList("one", "two"), "one"), Game.class);

        Map<String, String> arguments = new HashMap<String, String>();
        arguments.put("name", "one");
        stackmob.get("game", arguments, new StackMobCallback() {
            @Override
            public void success(String responseBody) {
                assertNotError(responseBody);
                assertNotNull(responseBody);
                Type collectionType = new TypeToken<List<Game>>() {}.getType();
                List<Game> games = gson.fromJson(responseBody, collectionType);
                assertNotNull(games);
                assertTrue(games.size() >= 1);
                assertEquals("one", games.get(0).name);
                games.get(0).delete(stackmob);
            }
            @Override
            public void failure(StackMobException e) {
                fail(e.getMessage());
            }
        });
    }

    @Test
    public void getWithQuery() throws Exception {
        StackMobObject.create(stackmob, new Game(Arrays.asList("seven", "six"), "woot"), Game.class);

        StackMobQuery query = new StackMobQuery("game").fieldIsGreaterThanOrEqualTo("name", "sup");
        stackmob.get(query, new StackMobCallback() {
            @Override
            public void success(String responseBody) {
                assertNotError(responseBody);
                assertNotNull(responseBody);
                Type collectionType = new TypeToken<List<Game>>() {}.getType();
                List<Game> games = gson.fromJson(responseBody, collectionType);
                assertNotNull(games);
                assertTrue(games.size() >= 1);
                assertEquals("woot", games.get(0).name);
                games.get(0).delete(stackmob);
            }
            @Override
            public void failure(StackMobException e) {
                fail(e.getMessage());
            }
        });
    }

    @Test
    public void postWithRequestObject() throws Exception {
        Game game = new Game(Arrays.asList("one", "two"), "newGame");
        game.name = "newGame";
        stackmob.post("game", game, new StackMobCallback() {
            @Override
            public void success(String responseBody) {
                assertNotError(responseBody);
                Game game = gson.fromJson(responseBody, Game.class);
                assertEquals("newGame", game.name);
                game.delete(stackmob);
            }

            @Override
            public void failure(StackMobException e) {
                fail(e.getMessage());
            }
        });
    }

    @Test public void deleteWithId() throws Exception {
        Game game = StackMobObject.create(stackmob, new Game(new ArrayList<String>(), "gameToDelete"), Game.class);

        stackmob.delete("game", game.game_id, new StackMobCallback() {
            @Override
            public void success(String responseBody) {
                assertNotError(responseBody);
                assertNotNull(responseBody);
            }

            @Override
            public void failure(StackMobException e) {
                fail(e.getMessage());
            }
        });
    }

    @Test public void put() throws Exception {
        final String oldName = "oldGameName";
        final String newName = "newGameName";

        Game game = StackMobObject.create(stackmob, new Game(Arrays.asList("one", "two"), oldName), Game.class);

        game.name = newName;
        final Game updatedGame = new Game(Arrays.asList("modified", "modified2"), "modified_game");
        stackmob.put(game.getName(), game.getId(), updatedGame, new StackMobCallback() {
            @Override
            public void success(String responseBody) {
                assertNotError(responseBody);
                Game jsonGame = gson.fromJson(responseBody, Game.class);
                assertNotNull(jsonGame);
                assertNotNull(jsonGame.name);
                assertEquals(updatedGame.name, jsonGame.name);
                jsonGame.delete(stackmob);
            }
            @Override
            public void failure(StackMobException e) {
                fail(e.getMessage());
            }
        });
    }


    @Test public void registerToken() throws Exception {
        final String username = "testUser";
        final String password = "password";
        final String token = "testToken";

        new User(username, password).delete(stackmob, true);

        final User user = StackMobObject.create(stackmob, new User(username, password), User.class);
        user.password = password;

        stackmob.registerForPushWithUser(user.username, token, new StackMobCallback() {
            @Override
            public void success(String responseBody) {
                assertNotError(responseBody);
            }

            @Override
            public void failure(StackMobException e) {
                fail("exception thrown when trying to register for push: " + e.getMessage());
            }
        });
        user.delete(stackmob);
    }
}