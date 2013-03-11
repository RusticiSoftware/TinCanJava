package tincan;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import java.io.IOException;
import java.net.MalformedURLException;

import static org.codehaus.jackson.map.SerializationConfig.Feature.INDENT_OUTPUT;

/**
 * SubStatement Class used when including a statement like object in another statement,
 * see the 'statement' context property
 */
@Data
@NoArgsConstructor
@Log
public class SubStatement implements StatementTarget {
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
            if (objectTypeNode.getTextValue().equals("Activity")) {
                this.setObject(new Activity(objectNode));
            }
        }
    }

    public SubStatement (String json) throws Exception {
        this(JSONMapper.getInstance().readValue(json, JsonNode.class));
    }

    public ObjectNode toJSONNode(TCAPIVersion version) {
        log.info("toJSONNode - version: " + version.toString());
        ObjectNode node = JSONMapper.getInstance().createObjectNode();

        node.put("actor", this.getActor().toJSONNode(version));
        node.put("verb", this.getVerb().toJSONNode(version));
        node.put("object", this.getObject().toJSONNode(version));

        return node;
    }

    public ObjectNode toJSONNode() {
        TCAPIVersion version = TCAPIVersion.latest();
        return this.toJSONNode(version);
    }

    public String toJSON() throws IOException {
        return JSONMapper.getInstance().writeValueAsString(this.toJSONNode());
    }

    public String toJSONPretty() throws IOException {
        ObjectMapper jsonMapper = JSONMapper.getInstance();
        jsonMapper.configure(INDENT_OUTPUT, true);

        String result = jsonMapper.writeValueAsString(this.toJSONNode());

        jsonMapper.configure(INDENT_OUTPUT, false);

        return result;
    }
}
