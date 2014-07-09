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
    AboutLRSResponse about() throws Exception;

    StatementLRSResponse saveStatement(Statement statement) throws Exception;
    StatementsResultLRSResponse saveStatements(List<Statement> statements) throws Exception;
    StatementLRSResponse retrieveStatement(String id) throws Exception;
    StatementLRSResponse retrieveVoidedStatement(String id) throws Exception;
    StatementsResultLRSResponse queryStatements(StatementsQueryInterface query) throws Exception;
    StatementsResultLRSResponse moreStatements(String moreURL) throws Exception;

    ProfileKeysLRSResponse retrieveStateIds(Activity activity, Agent agent, UUID registration) throws Exception;
    StateLRSResponse retrieveState(String id, Activity activity, Agent agent, UUID registration) throws Exception;
    LRSResponse saveState(StateDocument state) throws Exception;
    LRSResponse deleteState(StateDocument state) throws Exception;
    LRSResponse clearState(Activity activity, Agent agent, UUID registration) throws Exception;

    ProfileKeysLRSResponse retrieveActivityProfileIds(Activity activity) throws Exception;
    ActivityProfileLRSResponse retrieveActivityProfile(String id, Activity activity) throws Exception;
    LRSResponse saveActivityProfile(ActivityProfileDocument profile) throws Exception;
    LRSResponse deleteActivityProfile(ActivityProfileDocument profile) throws Exception;

    ProfileKeysLRSResponse retrieveAgentProfileIds(Agent agent) throws Exception;
    AgentProfileLRSResponse retrieveAgentProfile(String id, Agent agent) throws Exception;
    LRSResponse saveAgentProfile(AgentProfileDocument profile) throws Exception;
    LRSResponse deleteAgentProfile(AgentProfileDocument profile) throws Exception;
}