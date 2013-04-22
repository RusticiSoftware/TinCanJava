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
package com.rusticisoftware.tincan;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import lombok.NoArgsConstructor;
import com.rusticisoftware.tincan.json.Mapper;
import com.rusticisoftware.tincan.json.StringOfJSON;

/**
 * Extensions model class
 */
@NoArgsConstructor
public class Extensions {
    private final HashMap<URL,JsonNode> _map = new HashMap<URL,JsonNode>();

    public Extensions(JsonNode jsonNode) throws MalformedURLException {
        Iterator<Map.Entry<String,JsonNode>> items = jsonNode.fields();
        while(items.hasNext()) {
            Map.Entry<String,JsonNode> item = items.next();

            this.put(new URL(item.getKey()), item.getValue());
        }
    }

    public Extensions(StringOfJSON jsonStr) throws IOException {
        this(jsonStr.toJSONNode());
    }

    public ObjectNode toJSONNode(TCAPIVersion version) {
        ObjectNode node = Mapper.getInstance().createObjectNode();

        for (Map.Entry<URL,JsonNode> entry : this._map.entrySet()) {
            node.put(entry.getKey().toString(), entry.getValue());
        }

        return node;
    }

    public Object put(URL key, JsonNode val) {
        return this._map.put(key, val);
    }

    public Object put(String key, JsonNode val) throws MalformedURLException {
        return this.put(new URL(key), val);
    }

    public Object put(URL key, Object val) {
        JsonNode storeVal = Mapper.getInstance().valueToTree(val);
        return this.put(key, storeVal);
    }

    public Object put(URL key, StringOfJSON val) {
        JsonNode storeVal = Mapper.getInstance().valueToTree(val);
        return this.put(key, storeVal);
    }

    public Object put(String key, Object val) throws MalformedURLException {
        return this.put(new URL(key), val);
    }

    public JsonNode get(URL key) {
        return this._map.get(key);
    }

    public JsonNode get(String key) throws MalformedURLException {
        return this.get(new URL(key));
    }
}
