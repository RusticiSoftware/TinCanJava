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

import org.joda.time.DateTime;
import org.junit.Test;

/**
 * SubStatementTest Class Description
 */
public class SubStatementTest {
    
    @Test
    public void serializeDeserialize() throws Exception {
        SubStatement st = new SubStatement();
        st.setActor(getAgent("Joe", "mbox", "mailto:joe@example.com"));
        
        st.setContext(new Context());
        st.getContext().setLanguage("en-US");
        
        st.setObject(new Activity("http://example.com/activity"));
        
        st.setResult(new Result());
        st.getResult().setCompletion(true);
        
        st.setTimestamp(new DateTime());
        st.setVerb(new Verb("http://example.com/verb"));
        
        assertSerializeDeserialize(st);
    }
}
