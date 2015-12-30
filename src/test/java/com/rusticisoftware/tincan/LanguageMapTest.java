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
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Map;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Vector;

import org.junit.Test;

/**
 * Description
 */
public class LanguageMapTest {

    @Test
    public void serializeDeserialize() throws Exception {
        LanguageMap lm = new LanguageMap();
        lm.put("en-US", "Some english");
        lm.put("es-ES", "Some espanol");
        assertSerializeDeserialize(lm);
    }

    @Test
    public void fillAndIterate() {
        LanguageMap lm = new LanguageMap();
        LanguageMap lmCopy = new LanguageMap();
        lm.put("und", "Some text");
        lm.put("en-US", "Some english");
        lm.put("es-ES", "Some espanol");

        for (Map.Entry<String, String> entry : lm) {
            lmCopy.put(entry);
        }

        String lmContent = lm.toJSON();
        String lmCopyContent = lmCopy.toJSON();

        assertEquals(lmContent, lmCopyContent);

        boolean hasKey = lm.containsKey("und");
        boolean hasValue = lm.containsValue("Some english");
        Map.Entry<String, String> entry = lm.findFirstValue("Some espanol");
        ArrayList<String> arrayOfLangs = new ArrayList<String>();
        LinkedList<String> linkedListOfLangs = new LinkedList<String>();
        Vector<String> vectorOfLangs = new Vector<String>();
        lm.populateWithAvailableLanguages(arrayOfLangs);
        lm.populateWithAvailableLanguages(linkedListOfLangs);
        lm.populateWithAvailableLanguages(vectorOfLangs);

        assertTrue(hasKey);
        assertTrue(hasValue);
        assertEquals(entry.getKey(), "es-ES");
        assertEquals(arrayOfLangs.size(), 3);
        assertEquals(linkedListOfLangs.size(), 3);
        assertEquals(vectorOfLangs.size(), 3);
        for (String s : linkedListOfLangs) {
            assertTrue(lm.containsKey(s));
        }
    }
}
