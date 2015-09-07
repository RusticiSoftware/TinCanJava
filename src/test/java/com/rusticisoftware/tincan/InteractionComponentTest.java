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

import org.junit.Test;

/**
 * InteractionComponentTest Class Description
 */
public class InteractionComponentTest {
    @Test
    public void serializeDeserialize() throws Exception {
        InteractionComponent comp = new InteractionComponent();
        comp.setId("choice1");
        comp.setDescription(new LanguageMap());
        comp.getDescription().put("en-US", "Some choice");
        assertSerializeDeserialize(comp);
    }
}
