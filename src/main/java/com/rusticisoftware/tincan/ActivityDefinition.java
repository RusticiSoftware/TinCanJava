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

import static com.rusticisoftware.tincan.InteractionType.getByString;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.rusticisoftware.tincan.json.JSONBase;
import com.rusticisoftware.tincan.json.Mapper;

/**
 * Activity Definition model class
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class ActivityDefinition extends JSONBase {
    private LanguageMap name;
    private LanguageMap description;
    private URI type;
    private URI moreInfo;
    private Extensions extensions;
    private InteractionType interactionType;
    private ArrayList<String> correctResponsesPattern;
    private ArrayList<InteractionComponent> choices;
    private ArrayList<InteractionComponent> scale;
    private ArrayList<InteractionComponent> source;
    private ArrayList<InteractionComponent> target;
    private ArrayList<InteractionComponent> steps;

    public ActivityDefinition(JsonNode jsonNode) throws URISyntaxException {
        this();

        JsonNode typeNode = jsonNode.path("type");
        if (! typeNode.isMissingNode()) {
            this.setType(new URI(typeNode.textValue()));
        }

        JsonNode moreInfoNode = jsonNode.path("moreInfo");
        if (! moreInfoNode.isMissingNode()) {
            this.setMoreInfo(new URI(moreInfoNode.textValue()));
        }

        JsonNode nameNode = jsonNode.path("name");
        if (! nameNode.isMissingNode()) {
            this.setName(new LanguageMap(nameNode));
        }

        JsonNode descNode = jsonNode.path("description");
        if (! descNode.isMissingNode()) {
            this.setDescription(new LanguageMap(descNode));
        }

        JsonNode extensionsNode = jsonNode.path("extensions");
        if (! extensionsNode.isMissingNode()) {
            this.setExtensions(new Extensions(extensionsNode));
        }

        JsonNode interactionTypeNode = jsonNode.path("interactionType");
        InteractionType intType = null;
        if (! interactionTypeNode.isMissingNode()) {
            intType = getByString(interactionTypeNode.textValue());
            this.setInteractionType(intType);
        }

        JsonNode correctResponsesPatternNode = jsonNode.path("correctResponsesPattern");
        if (! correctResponsesPatternNode.isMissingNode()) {
            this.correctResponsesPattern = new ArrayList<String>();
            for (JsonNode element : correctResponsesPatternNode) {
                this.correctResponsesPattern.add(element.textValue());
            }
        }

        JsonNode choicesNode = jsonNode.path("choices");
        if (! choicesNode.isMissingNode()) {
            this.choices = new ArrayList<InteractionComponent>();
            for (JsonNode element : choicesNode) {
                this.choices.add(new InteractionComponent(element));
            }
        }

        JsonNode scaleNode = jsonNode.path("scale");
        if (! scaleNode.isMissingNode()) {
            this.scale = new ArrayList<InteractionComponent>();
            for (JsonNode element : scaleNode) {
                this.scale.add(new InteractionComponent(element));
            }
        }

        JsonNode sourceNode = jsonNode.path("source");
        if (! sourceNode.isMissingNode()) {
            this.source = new ArrayList<InteractionComponent>();
            for (JsonNode element : sourceNode) {
                this.source.add(new InteractionComponent(element));
            }
        }

        JsonNode targetNode = jsonNode.path("target");
        if (! targetNode.isMissingNode()) {
            this.target = new ArrayList<InteractionComponent>();
            for (JsonNode element : targetNode) {
                this.target.add(new InteractionComponent(element));
            }
        }

        JsonNode stepsNode = jsonNode.path("steps");
        if (! stepsNode.isMissingNode()) {
            this.steps = new ArrayList<InteractionComponent>();
            for (JsonNode element : stepsNode) {
                this.steps.add(new InteractionComponent(element));
            }
        }
    }

    public ActivityDefinition(String name, String description) {
        this();

        LanguageMap nameMap = new LanguageMap();
        nameMap.put("und", name);
        this.setName(nameMap);

        LanguageMap descriptionMap = new LanguageMap();
        descriptionMap.put("und", description);
        this.setDescription(descriptionMap);
    }

    @Override
    public ObjectNode toJSONNode(TCAPIVersion version) {
        ObjectMapper mapper = Mapper.getInstance();
        ObjectNode node = mapper.createObjectNode();
        if (this.name != null) {
            node.put("name", this.getName().toJSONNode(version));
        }
        if (this.description != null) {
            node.put("description", this.getDescription().toJSONNode(version));
        }
        if (this.type != null) {
            node.put("type", this.getType().toString());
        }
        if (this.moreInfo != null) {
            node.put("moreInfo", this.getMoreInfo().toString());
        }
        if (this.extensions != null) {
            node.put("extensions", this.getExtensions().toJSONNode(version));
        }
        if (this.interactionType != null) {
            node.put("interactionType", this.getInteractionType().toString());

            switch (this.interactionType) {
                case CHOICE:
                case SEQUENCING:
                    if (this.choices != null && this.choices.size() > 0) {
                        ArrayNode choices = mapper.createArrayNode();
                        node.put("choices", choices);

                        for(InteractionComponent ic : this.getChoices()) {
                            choices.add(ic.toJSONNode(version));
                        }
                    }
                    break;

                case LIKERT:
                    if (this.scale != null && this.scale.size() > 0) {
                        ArrayNode scale = mapper.createArrayNode();
                        node.put("scale", scale);

                        for(InteractionComponent ic : this.getScale()) {
                            scale.add(ic.toJSONNode(version));
                        }
                    }
                    break;

                case MATCHING:
                    if (this.source != null && this.source.size() > 0) {
                        ArrayNode source = mapper.createArrayNode();
                        node.put("source", source);

                        for(InteractionComponent ic : this.getSource()) {
                            source.add(ic.toJSONNode(version));
                        }
                    }
                    if (this.target != null && this.target.size() > 0) {
                        ArrayNode target = mapper.createArrayNode();
                        node.put("target", target);

                        for(InteractionComponent ic : this.getTarget()) {
                            target.add(ic.toJSONNode(version));
                        }
                    }
                    break;

                case PERFORMANCE:
                    if (this.steps != null && this.steps.size() > 0) {
                        ArrayNode steps = mapper.createArrayNode();
                        node.put("steps", steps);

                        for(InteractionComponent ic : this.getSteps()) {
                            steps.add(ic.toJSONNode(version));
                        }
                    }
                    break;

                case TRUE_FALSE:
                case FILL_IN:
                case LONG_FILL_IN:
                case NUMERIC:
                case OTHER:
                    break;
            }
        }
        if (this.correctResponsesPattern != null && this.correctResponsesPattern.size() > 0) {
            ArrayNode responses = mapper.createArrayNode();
            node.put("correctResponsesPattern", responses);

            for(String resp : this.getCorrectResponsesPattern()) {
                responses.add(resp);
            }
        }
        return node;
    }

    public void setType(URI type) {
        this.type = type;
    }

    public void setType(String type) throws URISyntaxException {
        this.setType(new URI(type));
    }

    public void setMoreInfo(URI moreInfo) {
        this.moreInfo = moreInfo;
    }

    public void setMoreInfo(String moreInfo) throws URISyntaxException {
        this.setMoreInfo(new URI(moreInfo));
    }
}
