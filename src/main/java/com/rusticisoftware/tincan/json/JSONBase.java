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

import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import com.rusticisoftware.tincan.TCAPIVersion;

/**
 * JSONBase Class Description
 */
public abstract class JSONBase implements JSON {
    @Override
    public abstract ObjectNode toJSONNode(TCAPIVersion version);

    @Override
    public ObjectNode toJSONNode() {
        TCAPIVersion version = TCAPIVersion.latest();
        return this.toJSONNode(version);
    }

    @Override
    public String toJSON(TCAPIVersion version, Boolean pretty) throws IOException {
        ObjectWriter writer = Mapper.getWriter(pretty);

        return writer.writeValueAsString(this.toJSONNode(version));
    }


    @Override
    public String toJSON(TCAPIVersion version) throws IOException {
        return this.toJSON(version, false);
    }

    @Override
    public String toJSON(Boolean pretty) throws IOException {
        return this.toJSON(TCAPIVersion.latest(), pretty);
    }

    @Override
    public String toJSON() throws IOException {
        return this.toJSON(TCAPIVersion.latest(), false);
    }
}
