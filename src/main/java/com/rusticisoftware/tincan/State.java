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

import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.UUID;

/**
 * State Class
 */
@Data
@NoArgsConstructor
public class State {
    // TODO: need SHA1 of the contents?
    private String id;
    private DateTime updated;
    private byte[] contents;
    private Agent agent;
    private URL activityId;
    private UUID registration;

    public State(String id, byte[] contents, URL activityId, Agent agent, UUID registration) {
        this.setId(id);
        this.setContents(contents);
        this.setAgent(agent);
        this.setActivityId(activityId);
        this.setRegistration(registration);
    }

    public State(String id, byte[] contents, String activityId, Agent agent, UUID registration) throws MalformedURLException {
        this(id, contents, new URL(activityId), agent, registration);
    }

    public State(String id, byte[] contents, URL activityId, Agent agent) {
        this(id, contents, activityId, agent, null);
    }

    public State(String id, byte[] contents, String activityId, Agent agent) throws MalformedURLException {
        this(id, contents, new URL(activityId), agent, null);
    }

    public State(String id, String contents, URL activityId, Agent agent, UUID registration) {
        this(id, contents.getBytes(Charset.forName("UTF-8")), activityId, agent, registration);
    }

    public State(String id, String contents, String activityId, Agent agent, UUID registration) throws MalformedURLException {
        this(id, contents.getBytes(Charset.forName("UTF-8")), new URL(activityId), agent, registration);
    }

    public State(String id, String contents, URL activityId, Agent agent) {
        this(id, contents.getBytes(Charset.forName("UTF-8")), activityId, agent, null);
    }

    public State(String id, String contents, String activityId, Agent agent) throws MalformedURLException {
        this(id, contents.getBytes(Charset.forName("UTF-8")), new URL(activityId), agent, null);
    }
}
