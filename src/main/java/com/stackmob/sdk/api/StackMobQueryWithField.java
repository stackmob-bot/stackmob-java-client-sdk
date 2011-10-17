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

import java.util.List;

public class StackMobQueryWithField {
    private String field;
    private StackMobQuery q;

    public StackMobQueryWithField(String field, StackMobQuery q) {
        this.field = field;
        this.q = q;
    }

    public StackMobQuery getQuery() {
        return this.q;
    }

    public String getField() {
        return this.field;
    }

    public StackMobQueryWithField field(String f) {
        if(this.field.equals(f)) {
            return this;
        }
        else {
            return new StackMobQueryWithField(f, this.q);
        }
    }

    public StackMobQueryWithField isIn(List<String> values) {
        this.q = this.q.fieldIsIn(this.field, values);
        return this;
    }

    public StackMobQueryWithField isLessThan(String val) {
        this.q = this.q.fieldIsLessThan(this.field, val);
        return this;
    }

    public StackMobQueryWithField isLessThan(Integer val) {
        return isLessThan(val.toString());
    }

    public StackMobQueryWithField isLessThan(Long val) {
        return isLessThan(val.toString());
    }

    public StackMobQueryWithField isLessThan(Boolean val) {
        return isLessThan(val.toString());
    }

    public StackMobQueryWithField isGreaterThan(String val) {
        this.q = this.q.fieldIsGreaterThan(this.field, val);
        return this;
    }

    public StackMobQueryWithField isGreaterThan(Integer val) {
        return isGreaterThan(val.toString());
    }

    public StackMobQueryWithField isGreaterThan(Long val) {
        return isGreaterThan(val.toString());
    }

    public StackMobQueryWithField isGreaterThan(Boolean val) {
        return isGreaterThan(val.toString());
    }

    public StackMobQueryWithField isLessThanOrEqualTo(String val) {
        this.q = this.q.fieldIslessThanOrEqualTo(this.field, val);
        return this;
    }

    public StackMobQueryWithField isLessThanOrEqualTo(Integer val) {
        return isLessThanOrEqualTo(val.toString());
    }

    public StackMobQueryWithField isLessThanOrEqualTo(Long val) {
        return isLessThanOrEqualTo(val.toString());
    }

    public StackMobQueryWithField isLessThanOrEqualTo(Boolean val) {
        return isLessThanOrEqualTo(val.toString());
    }

    public StackMobQueryWithField isGreaterThanOrEqualTo(String val) {
        this.q = this.q.fieldIsGreaterThanOrEqualTo(this.field, val);
        return this;
    }

    public StackMobQueryWithField isGreaterThanOrEqualTo(Integer val) {
        return isGreaterThanOrEqualTo(val.toString());
    }

    public StackMobQueryWithField isGreaterThanOrEqualTo(Long val) {
        return isGreaterThanOrEqualTo(val.toString());
    }

    public StackMobQueryWithField isGreaterThanOrEqualTo(Boolean val) {
        return isGreaterThanOrEqualTo(val.toString());
    }
}
