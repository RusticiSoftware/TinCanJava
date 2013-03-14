package tincan;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import lombok.NoArgsConstructor;
import tincan.json.JSONBase;
import tincan.json.Mapper;

/**
 * Score model class
 */
@Data
@NoArgsConstructor
public class Score extends JSONBase {
    private Double scaled;
    private Double raw;
    private Double min;
    private Double max;

    public Score(JsonNode jsonNode) {
        this();

        JsonNode scaledNode = jsonNode.path("scaled");
        if (! scaledNode.isMissingNode()) {
            this.setScaled(scaledNode.doubleValue());
        }

        JsonNode rawNode = jsonNode.path("raw");
        if (! rawNode.isMissingNode()) {
            this.setRaw(rawNode.doubleValue());
        }

        JsonNode minNode = jsonNode.path("min");
        if (! minNode.isMissingNode()) {
            this.setMin(minNode.doubleValue());
        }

        JsonNode maxNode = jsonNode.path("max");
        if (! maxNode.isMissingNode()) {
            this.setMax(maxNode.doubleValue());
        }
    }

    @Override
    public ObjectNode toJSONNode(TCAPIVersion version) {
        ObjectNode node = Mapper.getInstance().createObjectNode();

        if (this.scaled != null) {
            node.put("scaled", this.getScaled());
        }
        if (this.raw != null) {
            node.put("raw", this.getRaw());
        }
        if (this.min != null) {
            node.put("min", this.getMin());
        }
        if (this.max != null) {
            node.put("max", this.getMax());
        }

        return node;
    }
}
