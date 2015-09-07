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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.rusticisoftware.tincan.json.JSONBase;
import com.rusticisoftware.tincan.json.Mapper;

/**
 * Language map
 */
public class LanguageMap extends JSONBase implements Iterable<Map.Entry<String, String>> {
    private final HashMap<String,String> _map = new HashMap<String, String>();

    public LanguageMap() {
    }

    private class LanguageMapIterator implements Iterator<Map.Entry<String, String>> {
        private Iterator<Map.Entry<String, String>> iterator;

        public LanguageMapIterator() {
            iterator = _map.entrySet().iterator();
        }
        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public Entry<String, String> next() {
            return iterator.next();
        }

        @Override
        public void remove() throws UnsupportedOperationException {
            throw new UnsupportedOperationException(
                "LanguageMap iterator does not implement the remove method");
        }
    }
    public LanguageMap(JsonNode jsonNode) {
        this();

        Iterator<Map.Entry<String,JsonNode>> items = jsonNode.fields();
        while(items.hasNext()) {
            Map.Entry<String,JsonNode> item = items.next();

            this.put(item.getKey(), item.getValue().textValue());
        }
    }

    public ObjectNode toJSONNode(TCAPIVersion version) {
        ObjectNode node = Mapper.getInstance().createObjectNode();

        for (Map.Entry<String, String> entry : this._map.entrySet()) {
            node.put(entry.getKey(), entry.getValue());
        }

        return node;
    }

    public String put(String key, String val) {
        return this._map.put(key, val);
    }
    
    public String put(Map.Entry<String, String> entry) {
        return this.put(entry.getKey(), entry.getValue());
    }

    public String get(String key) {
        return this._map.get(key);
    }

    public boolean containsKey(String key) {
        return this._map.containsKey(key);
    }

    public boolean containsValue(String value) {
        return this._map.containsValue(value);
    }

    public Map.Entry<String, String> findFirstValue(String value) {
        Map.Entry<String, String> retVal = null;
        Iterator<Map.Entry<String,String>> it = this.iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> n = it.next();
            if (n.getValue().equalsIgnoreCase(value)) {
                retVal = n;
                break;
            }
        }
        return retVal;
    }

    @Override
    public Iterator<Entry<String, String>> iterator() {
        return new LanguageMapIterator();
    }
}
