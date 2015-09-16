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
 * ScoreTest Class Description
 */
public class ScoreTest {
    
    @Test
    public void serializeDeserialize() throws Exception {
        Score sc = new Score();
        sc.setMax(100.0);
        sc.setMin(0.0);
        sc.setRaw(80.0);
        sc.setScaled(0.8);
        assertSerializeDeserialize(sc);
    }
}
