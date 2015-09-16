/*
    Copyright 2014 Rustici Software

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
package com.rusticisoftware.tincan.documents;

import com.rusticisoftware.tincan.Activity;
import com.rusticisoftware.tincan.Agent;

import java.util.UUID;

public class StateDocument extends Document{
    private Activity activity;
    private Agent agent;
    private UUID registration;

	public StateDocument() {
	}
	

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public Agent getAgent() {
		return agent;
	}

	public void setAgent(Agent agent) {
		this.agent = agent;
	}

	public UUID getRegistration() {
		return registration;
	}

	public void setRegistration(UUID registration) {
		this.registration = registration;
	}
}
