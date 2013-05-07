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

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.UUID;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.rusticisoftware.tincan.json.JSONBase;
import com.rusticisoftware.tincan.json.Mapper;

/**
 * Context Class Description
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class Context extends JSONBase {
    private UUID registration;
    private Agent instructor;
    private Agent team;
    private ContextActivities contextActivities;
    private String revision;
    private String platform;
    private String language;
    private SubStatement statement;
    private Extensions extensions;

    public Context(JsonNode jsonNode) throws MalformedURLException, URISyntaxException {
        this();

        JsonNode registrationNode = jsonNode.path("registration");
        if (! registrationNode.isMissingNode()) {
            this.setRegistration(UUID.fromString(registrationNode.textValue()));
        }

        // TODO: check these for Group
        JsonNode instructorNode = jsonNode.path("instructor");
        if (! instructorNode.isMissingNode()) {
            this.setInstructor(Agent.fromJson(instructorNode));
        }

        JsonNode teamNode = jsonNode.path("team");
        if (! teamNode.isMissingNode()) {
            this.setTeam(Agent.fromJson(teamNode));
        }

        JsonNode contextActivitiesNode = jsonNode.path("contextActivities");
        if (! contextActivitiesNode.isMissingNode()) {
            this.setContextActivities(new ContextActivities(contextActivitiesNode));
        }

        JsonNode revisionNode = jsonNode.path("revision");
        if (! revisionNode.isMissingNode()) {
            this.setRevision(revisionNode.textValue());
        }

        JsonNode platformNode = jsonNode.path("platform");
        if (! platformNode.isMissingNode()) {
            this.setPlatform(platformNode.textValue());
        }

        JsonNode languageNode = jsonNode.path("language");
        if (! languageNode.isMissingNode()) {
            this.setLanguage(languageNode.textValue());
        }

        JsonNode statementNode = jsonNode.path("statement");
        if (! statementNode.isMissingNode()) {
            this.setStatement(new SubStatement(statementNode));
        }

        JsonNode extensionsNode = jsonNode.path("extensions");
        if (! extensionsNode.isMissingNode()) {
            this.setExtensions(new Extensions(extensionsNode));
        }
    }

    @Override
    public ObjectNode toJSONNode(TCAPIVersion version) {
        ObjectNode node = Mapper.getInstance().createObjectNode();

        if (this.registration != null) {
            node.put("registration", this.getRegistration().toString());
        }
        if (this.instructor != null) {
            node.put("instructor", this.getInstructor().toJSONNode(version));
        }
        if (this.team != null) {
            node.put("team", this.getTeam().toJSONNode(version));
        }
        if (this.contextActivities != null) {
            node.put("contextActivities", this.getContextActivities().toJSONNode(version));
        }
        if (this.revision != null) {
            node.put("revision", this.getRevision());
        }
        if (this.platform != null) {
            node.put("platform", this.getPlatform());
        }
        if (this.language != null) {
            node.put("language", this.getLanguage());
        }
        if (this.statement != null) {
            node.put("statement", this.getStatement().toJSONNode(version));
        }
        if (this.extensions != null) {
            node.put("extensions", this.getExtensions().toJSONNode(version));
        }

        return node;
    }
}
