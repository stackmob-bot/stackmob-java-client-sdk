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

package com.stackmob.java.sdk.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Ignore;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.stackmob.java.sdk.api.*;
import com.stackmob.java.sdk.callback.StackMobCallback;
import com.stackmob.java.sdk.exception.StackMobException;

public class StackMobTest {
    public static class Game {
        private List<String> players;
        private String gameId;
        private long createdDate;
        private long lastModDate;
        private String name;

        public Game(List<String> players, String gameId, long createdDate, long lastModDate, String name) {
            this.players = players;
            this.gameId = gameId;
            this.createdDate = createdDate;
            this.lastModDate = lastModDate;
            this.name = name;
        }

        public List<String> getPlayers() { return this.players; }
        public String getGameId() { return this.gameId; }
        public long getCreatedDate() { return this.createdDate; }
        public long getLastModDate() { return this.lastModDate; }
    }


  @BeforeClass
  public static void onlyOnce() {
    StackMob stackmob = StackMob.getInstance();
    stackmob.setApplication("7f1aebc7-0fb8-4265-bfea-2c42c08a3bf0",
        "81573b21-b948-4339-baa3-dbffe0ca4503", "androidtest",
        "stackmob", "stackmob.com", "user", 0);
  }

  @Test
  public void testSingleton() {
    assertNotNull(StackMob.getInstance());
  }

  @Test
  public void testLoginShouldBeSucessful() {
    HashMap<String, Object> params = new HashMap<String, Object>();
    params.put("username", "admin");
    params.put("password", "1234");

    StackMobCallback callback = new StackMobCallback() {

      public void success(String responseBody) {
        assertNotNull(responseBody);
      }

      public void failure(StackMobException e) {
        fail(e.getMessage());
      }
    };

    StackMob.getInstance().login(params, callback);
  }

  @Test
  public void testLoginShouldFail() {
    HashMap<String, Object> params = new HashMap<String, Object>();
    params.put("username", "idonotexist");
    params.put("password", "ghost");

    StackMobCallback callback = new StackMobCallback() {

      public void success(String responseBody) {
        fail("Login shouldn't succeed when login credentials are wrong");
      }

      public void failure(StackMobException e) {
        assertEquals("Unauthorized", e.getMessage());
      }
    };

    StackMob.getInstance().login(params, callback);
  }

  @Test
  public void testLogoutShouldBeSucessful() {

    HashMap<String, Object> params = new HashMap<String, Object>();
    params.put("username", "admin");
    params.put("password", "1234");
    StackMob.getInstance().login(params, new StackMobCallback() {

      public void success(String responseBody) {
      }

      public void failure(StackMobException e) {
        fail(e.getMessage());
      }
    });

    StackMobCallback callback = new StackMobCallback() {

      public void success(String responseBody) {
        assertNotNull(responseBody);
      }

      public void failure(StackMobException e) {
        fail(e.getMessage());
      }
    };

    StackMob.getInstance().logout(callback);
  }

  @Test
  public void startSessionTest() {
    StackMob.getInstance().startSession(new StackMobCallback() {
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
    StackMob.getInstance().endSession(new StackMobCallback() {

      public void success(String responseBody) {
        System.out.println("endsession: " + responseBody);
        assertNotNull(responseBody);
      }

      public void failure(StackMobException e) {
        fail(e.getMessage());
      }
    });
  }

  @Test
  public void testGetWithoutArguments() {
    StackMobCallback callback = new StackMobCallback() {

      public void success(String responseBody) {
        assertNotNull(responseBody);

        Gson gson = new Gson();
        Type collectionType = new TypeToken<List<Game>>() {
        }.getType();
        List<Game> games = gson.fromJson(responseBody, collectionType);

        assertNotNull(games);
        assertFalse(games.isEmpty());
      }

      public void failure(StackMobException e) {
        fail(e.getMessage());
      }
    };

    StackMob.getInstance().get("game", callback);
  }

  @Test
  public void testGetWithArguments() {
    StackMobCallback callback = new StackMobCallback() {

      public void success(String responseBody) {
        assertNotNull(responseBody);

        Gson gson = new Gson();
        Type collectionType = new TypeToken<List<Game>>() {
        }.getType();
        List<Game> games = gson.fromJson(responseBody, collectionType);

        assertNotNull(games);
        assertFalse(games.size() > 1);

        Game game = games.get(0);
        assertEquals("one", game.name);

      }

      public void failure(StackMobException e) {
        fail(e.getMessage());
      }
    };

    HashMap<String, Object> arguments = new HashMap<String, Object>();
    arguments.put("name", "one");
    StackMob.getInstance().get("game", arguments, callback);
  }

  @Test
  public void testPostWithRequestObject() {
    StackMobCallback callback = new StackMobCallback() {

      public void success(String responseBody) {
        Gson gson = new Gson();
        Game game = gson.fromJson(responseBody, Game.class);

        assertEquals("newGame", game.name);
      }

      public void failure(StackMobException e) {
        fail(e.getMessage());
      }
    };

    Game game = new Game(new ArrayList<String>(), "newGame", 12345, 12345, "new game");
    game.name = "newGame";
    StackMob.getInstance().post("game", game, callback);
  }

  @Test
  public void testDeleteWithId() {
    Game game = new Game(new ArrayList<String>(), "gameToDelete", 12345, 12345, "game to delete");

    StackMob.getInstance().post("game", game, new StackMobCallback() {

      public void success(String responseBody) {
        Gson gson = new Gson();
        Game game = gson.fromJson(responseBody, Game.class);

        StackMob.getInstance().delete("game", game.getGameId(), new StackMobCallback() {
            public void success(String responseBody) {
                assertNotNull(responseBody);
            }
            public void failure(StackMobException e) {
                fail(e.getMessage());
            }
        });
      }

      public void failure(StackMobException e) {
        fail(e.getMessage());
      }
    });
  }

  @Test
  public void testPutWithIdAndObjectRequest() {
    Game game = new Game(new ArrayList<String>(), "gameToModifyName", 12345, 12345, "game to modify name");

    StackMob.getInstance().post("game", game, new StackMobCallback() {

      public void success(String responseBody) {
        Gson gson = new Gson();
        Game game = gson.fromJson(responseBody, Game.class);

        Game gameWithOtherName = new Game(new ArrayList<String>(), "otherName", 12345, 12345, "other named game");

        StackMob.getInstance().put("game", game.getGameId(), gameWithOtherName, new StackMobCallback() {
            public void success(String responseBody) {
                Gson gson = new Gson();
                Game game = gson.fromJson(responseBody, Game.class);
                assertNotNull(game);
                assertEquals("otherName", game.name);
            }

            public void failure(StackMobException e) {
                fail(e.getMessage());
            }
        });
      }

      public void failure(StackMobException e) {
        fail(e.getMessage());
      }
    });
  }
}
