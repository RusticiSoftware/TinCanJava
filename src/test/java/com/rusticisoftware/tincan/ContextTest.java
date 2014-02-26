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

import static com.rusticisoftware.tincan.TestUtils.*;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.UUID;

import lombok.Data;
import org.junit.Test;

/**
 * ContextTest Class Description
 */
@Data
public class ContextTest {
    @Test
    public void serializeDeserialize() throws Exception {
        Context ctx = new Context();
        ctx.setContextActivities(new ContextActivities());
        ctx.getContextActivities().setParent(new ArrayList<Activity>());
        ctx.getContextActivities().getParent().add(new Activity("http://example.com/activity"));
        
        ctx.setExtensions(new Extensions());
        ctx.getExtensions().put("http://example.com/extension", "extensionValue");
        
        ctx.setInstructor(getAgent("Instructor", "mbox", "mailto:instructor@example.com"));
        ctx.setLanguage("en-US");
        ctx.setPlatform("iPhone 5");
        ctx.setRegistration(UUID.randomUUID());
        ctx.setRevision("1.0.4");

        StatementRef ref = new StatementRef();
        ref.setId(UUID.randomUUID());

        ctx.setStatement(ref);

        ctx.setTeam(getAgent("Group", "mbox", "mailto:group@example.com"));
        
        assertSerializeDeserialize(ctx);
    }
}
