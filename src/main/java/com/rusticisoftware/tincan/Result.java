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

import java.net.URISyntaxException;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import org.joda.time.Duration;
import org.joda.time.Period;
import org.joda.time.format.ISOPeriodFormat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.rusticisoftware.tincan.json.JSONBase;
import com.rusticisoftware.tincan.json.Mapper;

/**
 * Result Model class
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class Result extends JSONBase {
    private Score score;
    private Boolean success;
    private Boolean completion;
    private Period duration;
    private String response;
    private Extensions extensions;

    public Result(JsonNode jsonNode) throws URISyntaxException {
        this();

        JsonNode scoreNode = jsonNode.path("score");
        if (! scoreNode.isMissingNode()) {
            this.setScore(new Score(scoreNode));
        }

        JsonNode successNode = jsonNode.path("success");
        if (! successNode.isMissingNode()) {
            this.setSuccess(successNode.booleanValue());
        }

        JsonNode completionNode = jsonNode.path("completion");
        if (! completionNode.isMissingNode()) {
            this.setCompletion(completionNode.booleanValue());
        }

        JsonNode durationNode = jsonNode.path("duration");
        if (! durationNode.isMissingNode()) {
            this.setDuration(new Period(durationNode.textValue()));
        }

        JsonNode responseNode = jsonNode.path("response");
        if (! responseNode.isMissingNode()) {
            this.setResponse(responseNode.textValue());
        }

        JsonNode extensionsNode = jsonNode.path("extensions");
        if (! extensionsNode.isMissingNode()) {
            this.setExtensions(new Extensions(extensionsNode));
        }
    }

    @Override
    public ObjectNode toJSONNode(TCAPIVersion version) {
        ObjectMapper mapper = Mapper.getInstance();
        ObjectNode node = mapper.createObjectNode();

        if (this.score != null) {
            node.put("score", this.getScore().toJSONNode(version));
        }
        if (this.success != null) {
            node.put("success", this.getSuccess());
        }
        if (this.completion != null) {
            node.put("completion", this.getCompletion());
        }
        if (this.duration != null) {
            node.put("duration", ISOPeriodFormat.standard().print(this.getDuration()));
        }
        if (this.response != null) {
            node.put("response", this.getResponse());
        }
        if (this.extensions != null) {
            node.put("extensions", this.getExtensions().toJSONNode(version));
        }

        return node;
    }
}
