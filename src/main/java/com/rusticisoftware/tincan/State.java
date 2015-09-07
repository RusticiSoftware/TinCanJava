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

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.UUID;

import org.joda.time.DateTime;

/**
 * State Class
 */
public class State {
    // TODO: need SHA1 of the contents?
    private String id;
    private DateTime updated;
    private byte[] contents;
    private Agent agent;
    private URI activityId;
    private UUID registration;

	public State() {
	}
    public State(String id, byte[] contents, URI activityId, Agent agent, UUID registration) {
        this.setId(id);
        this.setContents(contents);
        this.setAgent(agent);
        this.setActivityId(activityId);
        this.setRegistration(registration);
    }

    public State(String id, byte[] contents, String activityId, Agent agent, UUID registration) throws URISyntaxException {
        this(id, contents, new URI(activityId), agent, registration);
    }

    public State(String id, byte[] contents, URI activityId, Agent agent) {
        this(id, contents, activityId, agent, null);
    }

    public State(String id, byte[] contents, String activityId, Agent agent) throws URISyntaxException {
        this(id, contents, new URI(activityId), agent, null);
    }

    public State(String id, String contents, URI activityId, Agent agent, UUID registration) {
        this(id, contents.getBytes(Charset.forName("UTF-8")), activityId, agent, registration);
    }

    public State(String id, String contents, String activityId, Agent agent, UUID registration) throws URISyntaxException {
        this(id, contents.getBytes(Charset.forName("UTF-8")), new URI(activityId), agent, registration);
    }

    public State(String id, String contents, URI activityId, Agent agent) {
        this(id, contents.getBytes(Charset.forName("UTF-8")), activityId, agent, null);
    }

    public State(String id, String contents, String activityId, Agent agent) throws URISyntaxException {
        this(id, contents.getBytes(Charset.forName("UTF-8")), new URI(activityId), agent, null);
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public DateTime getUpdated() {
		return updated;
	}

	public void setUpdated(DateTime updated) {
		this.updated = updated;
	}

	public byte[] getContents() {
		return contents;
	}

	public void setContents(byte[] contents) {
		this.contents = contents;
	}

	public Agent getAgent() {
		return agent;
	}

	public void setAgent(Agent agent) {
		this.agent = agent;
	}

	public URI getActivityId() {
		return activityId;
	}

	public void setActivityId(URI activityId) {
		this.activityId = activityId;
	}

	public UUID getRegistration() {
		return registration;
	}

	public void setRegistration(UUID registration) {
		this.registration = registration;
	}
}
