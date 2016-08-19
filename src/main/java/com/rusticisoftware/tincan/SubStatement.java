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

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.rusticisoftware.tincan.internal.StatementBase;
import com.rusticisoftware.tincan.json.StringOfJSON;

/**
 * SubStatement Class used when including a statement like object in another statement's 'object' property
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class SubStatement extends StatementBase implements StatementTarget {
    private final String objectType = "SubStatement";

    public SubStatement (JsonNode jsonNode) throws MalformedURLException, URISyntaxException, IOException, NoSuchAlgorithmException {
        super(jsonNode);
    }

    public SubStatement (StringOfJSON jsonStr) throws Exception {
        super(jsonStr);
    }

    @Override
    public ObjectNode toJSONNode(TCAPIVersion version) {
        ObjectNode node = super.toJSONNode(version);
        node.put("objectType", this.getObjectType());
        return node;
    }
}
