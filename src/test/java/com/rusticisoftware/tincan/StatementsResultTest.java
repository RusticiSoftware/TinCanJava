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
import static com.rusticisoftware.tincan.TestUtils.getAgent;

import java.util.ArrayList;

import org.junit.Test;

/**
 * Description
 */
public class StatementsResultTest {
    @Test
    public void serializeDeserialize() throws Exception {

        StatementsResult result = new StatementsResult();
        result.setStatements(new ArrayList<Statement>());
        Statement st = new Statement();
        st.setActor(getAgent("Joe", "mbox", "mailto:joe@example.com"));
        st.setVerb(new Verb("http://example.com/verb"));
        st.setObject(new Activity("http://example.com/activity"));
        result.getStatements().add(st);
        result.getStatements().add(st);
        result.setMoreURL("/statements?continueToken=abc");
        
        assertSerializeDeserialize(result);
    }
}
