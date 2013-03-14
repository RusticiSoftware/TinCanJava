package tincan;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import lombok.NoArgsConstructor;
import tincan.json.JSONBase;
import tincan.json.Mapper;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;

import static tincan.InteractionType.*;

/**
 * Activity Definition model class
 */
@Data
@NoArgsConstructor
public class ActivityDefinition extends JSONBase {
    private LanguageMap name;
    private LanguageMap description;
    private String type;
    private Extensions extensions;
    private InteractionType interactionType;
    private ArrayList<String> correctResponsesPattern;
    private ArrayList<InteractionComponent> choices;
    private ArrayList<InteractionComponent> scale;
    private ArrayList<InteractionComponent> source;
    private ArrayList<InteractionComponent> target;
    private ArrayList<InteractionComponent> steps;

    public ActivityDefinition(JsonNode jsonNode) throws MalformedURLException {
        this();

        JsonNode typeNode = jsonNode.path("type");
        if (! typeNode.isMissingNode()) {
            this.setType(typeNode.textValue());
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

            Iterator it = correctResponsesPatternNode.elements();
            while(it.hasNext()) {
                this.correctResponsesPattern.add(((JsonNode) it.next()).textValue());
            }
        }

        JsonNode choicesNode = jsonNode.path("choices");
        if (! choicesNode.isMissingNode()) {
            this.choices = new ArrayList<InteractionComponent>();

            Iterator it = choicesNode.elements();
            while(it.hasNext()) {
                this.choices.add(new InteractionComponent((JsonNode) it.next()));
            }
        }

        JsonNode scaleNode = jsonNode.path("scale");
        if (! scaleNode.isMissingNode()) {
            this.scale = new ArrayList<InteractionComponent>();

            Iterator it = scaleNode.elements();
            while(it.hasNext()) {
                this.scale.add(new InteractionComponent((JsonNode) it.next()));
            }
        }

        JsonNode sourceNode = jsonNode.path("source");
        if (! sourceNode.isMissingNode()) {
            this.source = new ArrayList<InteractionComponent>();

            Iterator it = sourceNode.elements();
            while(it.hasNext()) {
                this.source.add(new InteractionComponent((JsonNode) it.next()));
            }
        }

        JsonNode targetNode = jsonNode.path("target");
        if (! targetNode.isMissingNode()) {
            this.target = new ArrayList<InteractionComponent>();

            Iterator it = targetNode.elements();
            while(it.hasNext()) {
                this.target.add(new InteractionComponent((JsonNode) it.next()));
            }
        }

        JsonNode stepsNode = jsonNode.path("steps");
        if (! stepsNode.isMissingNode()) {
            this.steps = new ArrayList<InteractionComponent>();

            Iterator it = stepsNode.elements();
            while(it.hasNext()) {
                this.steps.add(new InteractionComponent((JsonNode) it.next()));
            }
        }
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
            node.put("type", this.getType());
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
}
