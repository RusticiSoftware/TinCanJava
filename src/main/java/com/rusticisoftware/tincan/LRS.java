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

import java.util.List;
import java.util.UUID;

/**
 * Define the interface that must be satisfied to talk to an LRS
 */
public interface LRS {
    Statement retrieveStatement(String id) throws Exception;
    Statement retrieveVoidedStatement(String id) throws Exception;
    StatementsResult queryStatements(StatementsQueryInterface query) throws Exception;
    StatementsResult moreStatements(String moreURL) throws Exception;
    UUID saveStatement(Statement statement) throws Exception;
    List<String> saveStatements(List<Statement> statements) throws Exception;

    State retrieveState(String id, String activityId, Agent agent, UUID registration) throws Exception;
    void saveState(State state, String activityId, Agent agent, UUID registration) throws Exception;
}
