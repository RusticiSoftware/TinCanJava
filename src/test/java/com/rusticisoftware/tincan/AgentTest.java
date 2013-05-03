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

import static org.junit.Assert.assertEquals;
import static com.rusticisoftware.tincan.TestUtils.*;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;

import com.rusticisoftware.tincan.json.StringOfJSON;

/**
 * Description
 */
public class AgentTest {
    
    @Test
    public void serializeDeserialize() throws Exception {
        Map<String, String> ids = new LinkedHashMap<String, String>();
        ids.put("mbox", "mailto:joeuser@example.com");
        ids.put("openid", "http://openid.org/joeuser");
        ids.put("mbox_sha1sum", "b623062e19c5608ab0e1342e5011d48292ce00e3");
        ids.put("account", "http://example.com|joeuser");
        
        String name = "Joe User";
        for (String idType : ids.keySet()) {
            for (TCAPIVersion version : TCAPIVersion.values()) {
                Agent agent = getAgent(name, idType, ids.get(idType));
                String agentJson = agent.toJSON(version);
                Agent clone = Agent.fromJson(new StringOfJSON(agentJson).toJSONNode());
                assertEquals(agentJson, clone.toJSON(version));
            }
        }
    }
}
