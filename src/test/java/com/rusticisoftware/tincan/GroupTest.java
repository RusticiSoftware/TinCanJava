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

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class GroupTest {
    @Test
    public void testGetObjectType() throws Exception {
        Group mock = new Group();
        assertEquals(mock.getObjectType(), "Group");
    }
    
    @Test
    public void serializeDeserialize() throws Exception {
        List<Agent> members = new ArrayList<Agent>();
        for (int i = 0; i < 10; i++) {
            members.add(
                getAgent("Member " + i, "mbox", "mailto:member" + i + "@example.com"));
        }
        Group group = new Group();
        group.setName("Group");
        group.setMbox("mailto:group@example.com");
        group.setMembers(members);
        
        assertSerializeDeserialize(group);
    }
}
