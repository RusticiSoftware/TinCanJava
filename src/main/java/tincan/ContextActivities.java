package tincan;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import lombok.NoArgsConstructor;
import tincan.exceptions.IncompatibleTCAPIVersion;
import tincan.json.JSONBase;
import tincan.json.Mapper;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * ContextActivities Model class
 */
@Data
@NoArgsConstructor
public class ContextActivities extends JSONBase {
    private List<Activity> parent;
    private List<Activity> grouping;
    private List<Activity> other;

    public ContextActivities(JsonNode jsonNode) throws MalformedURLException {
        this();

        JsonNode parentNode = jsonNode.path("parent");
        if (! parentNode.isMissingNode()) {
            this.parent = new ArrayList<Activity>();

            if (parentNode.isArray()) {
                Iterator it = parentNode.elements();
                while(it.hasNext()) {
                    this.parent.add(new Activity((JsonNode) it.next()));
                }
            }
            else {
                this.parent.add(new Activity(parentNode));
            }
        }

        JsonNode groupingNode = jsonNode.path("grouping");
        if (! groupingNode.isMissingNode()) {
            this.grouping = new ArrayList<Activity>();

            if (groupingNode.isArray()) {
                Iterator it = groupingNode.elements();
                while(it.hasNext()) {
                    this.grouping.add(new Activity((JsonNode) it.next()));
                }
            }
            else {
                this.grouping.add(new Activity(groupingNode));
            }
        }

        JsonNode otherNode = jsonNode.path("other");
        if (! otherNode.isMissingNode()) {
            this.other = new ArrayList<Activity>();

            if (parentNode.isArray()) {
                Iterator it = otherNode.elements();
                while(it.hasNext()) {
                    this.other.add(new Activity((JsonNode) it.next()));
                }
            }
            else {
                this.other.add(new Activity(otherNode));
            }
        }
    }

    @Override
    public ObjectNode toJSONNode(TCAPIVersion version) {
        ObjectMapper mapper = Mapper.getInstance();
        ObjectNode node = mapper.createObjectNode();

        if (this.parent != null && this.parent.size() > 0) {
            if (version.equals(TCAPIVersion.V095) && this.getParent().size() > 1) {
                throw new IncompatibleTCAPIVersion("Version " + TCAPIVersion.V095.toString() + " doesn't support lists of activities (parent)");
            }

            if (version.equals(TCAPIVersion.V095)) {
                node.put("parent", this.getParent().get(0).toJSONNode(version));
            }
            else {
                ArrayNode parent = mapper.createArrayNode();
                node.put("parent", parent);

                for (Activity element : this.getParent()) {
                    parent.add(element.toJSONNode(version));
                }
            }
        }
        if (this.grouping != null && this.grouping.size() > 0) {
            if (version.equals(TCAPIVersion.V095) && this.getGrouping().size() > 1) {
                throw new IncompatibleTCAPIVersion("Version " + TCAPIVersion.V095.toString() + " doesn't support lists of activities (grouping)");
            }

            if (version.equals(TCAPIVersion.V095)) {
                node.put("grouping", this.getGrouping().get(0).toJSONNode(version));
            }
            else {
                ArrayNode grouping = mapper.createArrayNode();
                node.put("grouping", grouping);

                for (Activity element : this.getGrouping()) {
                    grouping.add(element.toJSONNode(version));
                }
            }
        }
        if (this.other != null && this.other.size() > 0) {
            if (version.equals(TCAPIVersion.V095) && this.getOther().size() > 1) {
                throw new IncompatibleTCAPIVersion("Version " + TCAPIVersion.V095.toString() + " doesn't support lists of activities (other)");
            }

            if (version.equals(TCAPIVersion.V095)) {
                node.put("other", this.getGrouping().get(0).toJSONNode(version));
            }
            else {
                ArrayNode other = mapper.createArrayNode();
                node.put("other", other);

                for (Activity element : this.getOther()) {
                    other.add(element.toJSONNode(version));
                }
            }
        }

        return node;
    }
}
