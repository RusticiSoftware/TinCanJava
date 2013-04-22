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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

/**
 * Mapper Class provides access to a Jackson ObjectMapper singleton
 */
public class Mapper {
    private static class LazyHolder {
        private static final ObjectMapper INSTANCE = new ObjectMapper();
    }

    public static ObjectMapper getInstance() {
        return LazyHolder.INSTANCE;
    }

    public static ObjectWriter getWriter(Boolean pretty) {
        ObjectMapper mapper = getInstance();

        ObjectWriter writer;
        if (pretty) {
            writer = mapper.writer().withDefaultPrettyPrinter();
        }
        else {
            writer = mapper.writer();
        }

        return writer;
    }
}
