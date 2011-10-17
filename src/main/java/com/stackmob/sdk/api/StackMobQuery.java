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

package com.stackmob.sdk.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A class that builds queries to execute on the StackMob platform. Example usage:
 * <code>
 *     //this code:
 *     StackMobQuery query = new StackMobQuery("user").field("age").isGreaterThan(20).isLessThanOrEqualTo(40).field("friend").in(Arrays.asList("joe", "bob", "alice").getQuery();
 *     //is identical to this code:
 *     StackMobQuery query = new StackMobQuery("user").fieldIsGreaterThan("user", 20).fieldIsLessThanOrEqualTo("user", 40).fieldIsIn("user", Arrays.asList("joe", "bob", "alice");
 * </code>
 *
 * A few helpful notes about this object:
 * <ul>
 *     <li>this class is not thread safe. make sure to synchronize all calls</li>
 *     <li>calling field("field") on a StackMobQuery will return a StackMobQueryWithField object, which helps you build up part of part of your query on a specific field</li>
 *     <li>
 *         you can chain together operators on a StackMobQueryWithField.
 *         when you're done, call field("field") or getQuery() to get a new StackMobQueryWithField object or the resulting StackMobQuery object (respectively)
 *     </li>
 *     <li>you can only operate on one field at a time, but you can call field("field") as many times as you want on either a StackMobQuery or StackMobQueryWithField object</li>
 *     <li>
 *         you can call methods like fieldIsGreaterThan("field", "value") or fieldIsLessThanOrEqualTo("field", "value") directly on a StackMobQuery object.
 *         the above code sample shows 2 queries that are equivalent. the first line uses StackMobQueryWithField objects, and the second uses direct calls on StackMobQuery
 *     </li>
 * </ul>
 */
public class StackMobQuery {

    private String objectName;
    private Map<String, String> args = new HashMap<String, String>();

    public static enum Operator {
        LT("lt"),
        GT("gt"),
        LTE("lte"),
        GTE("gte"),
        IN("in");

        private String operator;

        Operator(String operator) {
            this.operator = operator;
        }

        public String getOperatorForURL() {
            return "["+operator+"]";
        }
    }

    public StackMobQuery(String objectName) {
        this.objectName = objectName;
    }

    public String getObjectName() {
        return objectName;
    }

    public Map<String, String> getArguments() {
        return args;
    }

    public StackMobQueryWithField field(String field) {
        return new StackMobQueryWithField(field, this);
    }

    /**
     * add an "IN" to your query. test whether the given field's value is in the given list of possible values
     * @param field the field whose value to test
     * @param values the values against which to match
     * @return the new query that resulted from adding this operation
     */
    public StackMobQuery fieldIsIn(String field, List<String> values) {
        StringBuilder builder = new StringBuilder();
        //equivalent of values.join(",");
        boolean first = true;
        for(String val: values) {
            if(!first) {
                builder.append(",");
            }
            first = false;
            builder.append(val);
        }

        putInMap(field, Operator.IN, builder.toString());
        return this;
    }

    /**
     * same as {@link #fieldIsLessThan(String, String)}, except works with Strings
     * @param field the field whose value to test
     * @param val the value against which to test
     * @return the new query that resulted from adding this operation
     */
    public StackMobQuery fieldIsLessThan(String field, String val) {
        return putInMap(field, Operator.LT, val);
    }

    /**
     * same as {@link #fieldIsLessThan(String, String)}, except applies "<=" instead of "<"
     * @param field the field whose value to test
     * @param val the value against which to test
     * @return the new query that resulted from adding this operation
     */
    public StackMobQuery fieldIslessThanOrEqualTo(String field, String val) {
        return putInMap(field, Operator.LTE, val);
    }

    /**
     * same as {@link #fieldIsLessThan(String, String)}, except applies ">" instead of "<"
     * @param field the field whose value to test
     * @param val the value against which to test
     * @return the new query that resulted from adding this operation
     */
    public StackMobQuery fieldIsGreaterThan(String field, String val) {
        return putInMap(field, Operator.GT, val);
    }

    /**
     * same as {@link #fieldIsLessThan(String, String)}, except applies ">=" instead of "<"
     * @param field the field whose value to test
     * @param val the value against which to test
     * @return the new query that resulted from adding this operation
     */
    public StackMobQuery fieldIsGreaterThanOrEqualTo(String field, String val) {
        return putInMap(field, Operator.GTE, val);
    }

    private StackMobQuery putInMap(String field, Operator operator, String value) {
        args.put(field+operator.getOperatorForURL(), value);
        return this;
    }

    private StackMobQuery putInMap(String field, Operator operator, int value) {
        putInMap(field, operator, Integer.toString(value));
        return this;
    }
}