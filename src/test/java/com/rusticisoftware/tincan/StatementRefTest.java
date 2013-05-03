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

import static com.rusticisoftware.tincan.TestUtils.assertSerializeDeserialize;
import static org.junit.Assert.assertEquals;

import java.util.UUID;

import lombok.Data;
import org.junit.Test;

/**
 * StatementRefTest Class Description
 */
@Data
public class StatementRefTest {
    
    @Test
    public void testGetObjectType() throws Exception {
        StatementRef ref = new StatementRef();
        assertEquals("StatementRef", ref.getObjectType());
    }

    @Test
    public void serializeDeserialize() throws Exception {
        StatementRef ref = new StatementRef();
        ref.setId(UUID.randomUUID());
        assertSerializeDeserialize(ref);
    }
    
}
