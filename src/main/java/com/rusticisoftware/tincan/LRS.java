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

import com.rusticisoftware.tincan.documents.*;
import com.rusticisoftware.tincan.lrsresponses.*;

import java.util.List;
import java.util.UUID;

/**
 * Define the interface that must be satisfied to talk to an LRS
 */
public interface LRS {
    AboutLRSResponse about();

    StatementLRSResponse saveStatement(Statement statement);
    StatementsResultLRSResponse saveStatements(List<Statement> statements);
    StatementLRSResponse retrieveStatement(String id);
    StatementLRSResponse retrieveVoidedStatement(String id);
    StatementLRSResponse retrieveStatement(String id, boolean attachments);
    StatementLRSResponse retrieveVoidedStatement(String id, boolean attachments);
    StatementsResultLRSResponse queryStatements(StatementsQueryInterface query);
    StatementsResultLRSResponse moreStatements(String moreURL);

    ProfileKeysLRSResponse retrieveStateIds(Activity activity, Agent agent, UUID registration);
    StateLRSResponse retrieveState(String id, Activity activity, Agent agent, UUID registration);
    LRSResponse saveState(StateDocument state);
    LRSResponse updateState(StateDocument state);
    LRSResponse deleteState(StateDocument state);
    LRSResponse clearState(Activity activity, Agent agent, UUID registration);

    ActivityLRSResponse retrieveActivity(Activity activity);
    ProfileKeysLRSResponse retrieveActivityProfileIds(Activity activity);
    ActivityProfileLRSResponse retrieveActivityProfile(String id, Activity activity);
    LRSResponse saveActivityProfile(ActivityProfileDocument profile);
    LRSResponse updateActivityProfile(ActivityProfileDocument profile);
    LRSResponse deleteActivityProfile(ActivityProfileDocument profile);

    PersonLRSResponse retrievePerson(Agent agent);
    ProfileKeysLRSResponse retrieveAgentProfileIds(Agent agent);
    AgentProfileLRSResponse retrieveAgentProfile(String id, Agent agent);
    LRSResponse saveAgentProfile(AgentProfileDocument profile);
    LRSResponse updateAgentProfile(AgentProfileDocument profile);
    LRSResponse deleteAgentProfile(AgentProfileDocument profile);
}