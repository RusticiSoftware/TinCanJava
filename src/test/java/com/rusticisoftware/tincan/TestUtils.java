package com.rusticisoftware.tincan;

import static org.junit.Assert.assertEquals;
import java.lang.reflect.Constructor;

import com.fasterxml.jackson.databind.JsonNode;
import com.rusticisoftware.tincan.json.JSONBase;
import com.rusticisoftware.tincan.json.StringOfJSON;

public class TestUtils {
    public static Agent getAgent(String name, String idType, String idFields) {
        Agent agent = new Agent();
        agent.setName(name);
        if ("mbox".equals(idType)) {
            agent.setMbox(idFields);
        }
        else if ("openid".equals(idType)) {
            agent.setOpenID(idFields);
        }
        else if ("mbox_sha1sum".equals(idType)) {
            agent.setMboxSHA1Sum(idFields);
        }
        else if ("account".equals(idType)) {
            String[] parts = idFields.split("|");
            AgentAccount acct = new AgentAccount();
            acct.setHomePage(parts[0]);
            acct.setName(parts[1]);
            agent.setAccount(acct);
        }
        return agent;
    }
    
    public static <T extends JSONBase> void assertSerializeDeserialize(T object) throws Exception {
        for (TCAPIVersion version : TCAPIVersion.values()) {
            assertSerializeDeserialize(object, version);
        }
    }
    
    public static <T extends JSONBase> void assertSerializeDeserialize(T object, TCAPIVersion version) throws Exception {
        String objJson = object.toJSON(version);
        Constructor<? extends JSONBase> con = object.getClass().getConstructor(JsonNode.class);
        JSONBase clone = (JSONBase)con.newInstance(new StringOfJSON(objJson).toJSONNode());
        assertEquals(objJson, clone.toJSON(version));
    }
}
