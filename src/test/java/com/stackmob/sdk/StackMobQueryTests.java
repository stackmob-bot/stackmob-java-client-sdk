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
import com.stackmob.sdk.api.StackMobQuery;
import com.stackmob.sdk.api.StackMobQueryWithField;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class StackMobQueryTests extends StackMobTestCommon {
    final String object = "user";
    final String field = "testField";
    final String otherField = "testField_Other";
    final String value = "testVal";
    final List<String> valueArr = Arrays.asList("one", "two", "three");

    //should return ArrayList so remove will work (many List implementations will throw otherwise)
    private ArrayList<String> getExpectedRelationalKeys() {
        return new ArrayList<String>(Arrays.asList(
                                         field + StackMobQuery.Operator.LT.getOperatorForURL(),
                                         field + StackMobQuery.Operator.GT.getOperatorForURL(),
                                         field + StackMobQuery.Operator.LTE.getOperatorForURL(),
                                         field + StackMobQuery.Operator.GTE.getOperatorForURL()));
    }

    private void assertKeysAndValuesMatch(Map<String, String> map, ArrayList<String> expectedKeys, String expectedValue) {
        assertEquals(expectedKeys.size(), map.size());
        List<String> keysClone = new ArrayList<String>(expectedKeys);
        for(String key: map.keySet()) {
            String val = map.get(key);
            assertEquals(expectedValue, val);
            assertTrue(keysClone.remove(key));
        }
        assertEquals(0, keysClone.size());
    }

    @Test public void simpleStringQuery() {
        StackMobQuery q = new StackMobQuery(object)
                          .fieldIsLessThan(field, value)
                          .fieldIsGreaterThan(field, value)
                          .fieldIslessThanOrEqualTo(field, value)
                          .fieldIsGreaterThanOrEqualTo(field, value);

        assertEquals(object, q.getObjectName());
        Map<String, String> args = q.getArguments();
        assertEquals(4, args.size());
        ArrayList<String> expectedKeys = getExpectedRelationalKeys();
        assertKeysAndValuesMatch(args, expectedKeys, value);
    }

    @Test public void inQuery() {
        StackMobQuery q = new StackMobQuery(object).fieldIsIn(field, valueArr);
        assertEquals(object, q.getObjectName());
        Map<String, String> args = q.getArguments();
        assertEquals(1, args.size());
        assertTrue(args.containsKey(field+StackMobQuery.Operator.IN.getOperatorForURL()));
        String val = args.get(field+StackMobQuery.Operator.IN.getOperatorForURL());
        List<String> valSplit = Arrays.asList(val.split(","));
        assertEquals(valueArr, valSplit);
    }

    @Test
    public void simpleQueryWithField() {
        StackMobQueryWithField q = new StackMobQuery(object).field(field);
        assertEquals(object, q.getQuery().getObjectName());
        assertEquals(field, q.getField());

        q.isGreaterThan(value).isLessThan(value).isGreaterThanOrEqualTo(value).isLessThanOrEqualTo(value);
        ArrayList<String> expectedKeys = getExpectedRelationalKeys();
        assertKeysAndValuesMatch(q.getQuery().getArguments(), expectedKeys, value);
    }

    @Test
    public void multiFieldCreation() {
        StackMobQuery q = new StackMobQuery(object);
        StackMobQueryWithField qWithField = q.field(field);
        assertTrue(qWithField == qWithField.field(field));//same field must be the same instance
        assertFalse(qWithField == qWithField.field(field + "_different"));//diff field must be a diff instance
    }

    @Test public void multiFieldQueries() {
        StackMobQuery q = new StackMobQuery(object).field(field).isLessThan(value).field(otherField).isGreaterThan(value).getQuery();
        ArrayList<String> expectedKeys = new ArrayList<String>(Arrays.asList(
                                                                            field+StackMobQuery.Operator.LT.getOperatorForURL(),
                                                                            otherField+StackMobQuery.Operator.GT.getOperatorForURL()));
        assertKeysAndValuesMatch(q.getArguments(), expectedKeys, value);
    }
}
