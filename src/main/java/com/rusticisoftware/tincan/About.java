/*
    Copyright 2014 Rustici Software

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
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.rusticisoftware.tincan.json.JSONBase;
import com.rusticisoftware.tincan.json.Mapper;
import com.rusticisoftware.tincan.json.StringOfJSON;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class About extends JSONBase{
    private List<TCAPIVersion> version;
    private Extensions extensions;

    public About(String str) throws IOException, URISyntaxException {
        StringOfJSON jsonStr = new StringOfJSON(str);
        init(jsonStr.toJSONNode());
    }

    public About(StringOfJSON jsonStr) throws IOException, URISyntaxException{
        init(jsonStr.toJSONNode());
    }

    public About(JsonNode jsonNode) throws URISyntaxException{
        init(jsonNode);
    }

    private void init(JsonNode jsonNode) throws URISyntaxException{

        if(jsonNode.hasNonNull("version")) {

            Iterator it = jsonNode.get("version").elements();

            while(it.hasNext()){
                if(version == null) { version = new ArrayList<TCAPIVersion>(); }
                version.add(TCAPIVersion.fromString(((JsonNode)it.next()).textValue()));
            }
        }

        if(jsonNode.hasNonNull("extensions")) {
            extensions = new Extensions(jsonNode.get("extensions"));
        }
    }

    @Override
    public ObjectNode toJSONNode(TCAPIVersion v) {
        ObjectNode result = new ObjectNode(Mapper.getInstance().getNodeFactory());
        if(!version.isEmpty()){
            ArrayNode versions = Mapper.getInstance().createArrayNode();
            for(TCAPIVersion tcapiVersion: this.getVersion()){
                versions.add(tcapiVersion.toString());
            }
            result.put("version", versions);
        }

        if(extensions != null){
            result.put("extensions", extensions.toJSONNode());
        }

        return result;
    }

}
