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

import org.junit.Test;

/**
 * ExtensionsTest Class Description
 */
public class ExtensionsTest {
    
    @Test
    public void serializeDeserialize() throws Exception {
        Extensions ext = new Extensions();
        ext.put("http://example.com/string", "extensionValue");
        ext.put("http://example.com/int", 10);
        ext.put("http://example.com/double", 1.897);
        ext.put("http://example.com/object", 
                getAgent("Random", "mbox", "mailto:random@example.com"));
        
        assertSerializeDeserialize(ext);
    }
}
