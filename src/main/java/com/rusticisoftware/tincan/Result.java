package com.rusticisoftware.tincan;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.Duration;
import org.joda.time.format.ISOPeriodFormat;
import com.rusticisoftware.tincan.json.JSONBase;
import com.rusticisoftware.tincan.json.Mapper;
import java.net.MalformedURLException;

/**
 * Result Model class
 */
@Data
@NoArgsConstructor
public class Result extends JSONBase {
    private Score score;
    private Boolean success;
    private Boolean completion;
    private Duration duration;
    private String response;
    private Extensions extensions;

    public Result(JsonNode jsonNode) throws MalformedURLException {
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
            this.setDuration(new Duration(durationNode.textValue()));
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
            node.put("duration", ISOPeriodFormat.alternate().print(this.getDuration().toPeriod()));
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
