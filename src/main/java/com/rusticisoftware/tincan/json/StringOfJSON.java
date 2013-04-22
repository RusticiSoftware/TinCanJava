/*
    Copyright 2013 Rustici Software

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/
package com.rusticisoftware.tincan.json;

import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;

/**
 * StringOfJSON Class Description
 */
public class StringOfJSON {
    private String source;

    public StringOfJSON(String json) {
        this.source = json;
    }

    public JsonNode toJSONNode() throws IOException {
        if (this.source == null) {
            return null;
        }
        return Mapper.getInstance().readValue(this.source, JsonNode.class);
    }

    @Override
    public String toString() {
        return this.source;
    }
}
