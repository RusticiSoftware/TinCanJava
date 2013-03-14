package tincan;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.net.MalformedURLException;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;
import tincan.json.JSONBase;
import tincan.json.Mapper;

/**
 * SubStatement Class used when including a statement like object in another statement,
 * see the 'statement' context property
 */
@Data
@NoArgsConstructor
@Log
public class SubStatement extends JSONBase implements StatementTarget {
    private final String objectType = "SubStatement";

    private Agent actor;
    private Verb verb;
    private StatementTarget object;
    private Result result;
    private Context context;

    public SubStatement (JsonNode jsonNode) throws MalformedURLException {
        this();

        log.info("constructor (from JsonNode)");

        JsonNode actorNode = jsonNode.path("actor");
        if (! actorNode.isMissingNode()) {
            // TODO: check for Group (objectType)
            this.setActor(new Agent(actorNode));
        }

        JsonNode verbNode = jsonNode.path("verb");
        if (! verbNode.isMissingNode()) {
            this.setVerb(new Verb(verbNode));
        }

        JsonNode objectNode = jsonNode.path("object");
        if (! objectNode.isMissingNode()) {
            JsonNode objectTypeNode = objectNode.path("objectType");
            if (objectTypeNode.textValue().equals("Activity")) {
                this.setObject(new Activity(objectNode));
            }
        }
    }

    public SubStatement (String json) throws Exception {
        this(Mapper.getInstance().readValue(json, JsonNode.class));
    }

    @Override
    public ObjectNode toJSONNode(TCAPIVersion version) {
        log.info("toJSONNode - version: " + version.toString());
        ObjectNode node = Mapper.getInstance().createObjectNode();

        node.put("actor", this.getActor().toJSONNode(version));
        node.put("verb", this.getVerb().toJSONNode(version));
        node.put("object", this.getObject().toJSONNode(version));

        return node;
    }
}
