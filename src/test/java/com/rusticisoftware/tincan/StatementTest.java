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
import java.util.List;
import java.util.UUID;

import org.joda.time.DateTime;
import org.junit.Test;

/**
 * Description
 */
public class StatementTest {

    @Test
    public void serializeDeserialize() throws Exception {
        
        List<StatementTarget> statementTargets = new ArrayList<StatementTarget>();
        statementTargets.add(new Activity("http://example.com/activity"));
        statementTargets.add(getAgent("Target", "mbox", "mailto:target@example.com"));
        statementTargets.add(new StatementRef(UUID.randomUUID()));
        
        SubStatement sub = new SubStatement();
        sub.setActor(getAgent("Sub", "mbox", "mailto:sub@example.com"));
        sub.setVerb(new Verb("http://example.com/verb"));
        sub.setObject(new Activity("http://example.com/sub-activity"));
        statementTargets.add(sub);
        
        
        Statement st = new Statement();
        st.setActor(getAgent("Joe", "mbox", "mailto:joe@example.com"));

        st.setAttachments(new ArrayList<Attachment>());
        Attachment att = new Attachment();
        att.setSha2("abc");
        st.getAttachments().add(att);
        
        st.setAuthority(getAgent("Authority", "mbox", "mailto:authority@example.com"));
        
        st.setContext(new Context());
        st.getContext().setLanguage("en-US");
        
        st.setId(UUID.randomUUID());
        
        st.setResult(new Result());
        st.getResult().setCompletion(true);
        
        st.setStored(new DateTime());
        st.setTimestamp(new DateTime());
        st.setVerb(new Verb("http://example.com/verb"));
        
        for (StatementTarget target : statementTargets) {
            st.setObject(target);
            assertSerializeDeserialize(st);
        }
    }
}
