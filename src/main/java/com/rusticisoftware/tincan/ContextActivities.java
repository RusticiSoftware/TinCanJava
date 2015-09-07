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
import java.util.ArrayList;
import java.util.List;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.rusticisoftware.tincan.exceptions.IncompatibleTCAPIVersion;
import com.rusticisoftware.tincan.json.JSONBase;
import com.rusticisoftware.tincan.json.Mapper;

/**
 * ContextActivities Model class
 */
public class ContextActivities extends JSONBase {
    private List<Activity> parent;
    private List<Activity> grouping;
    private List<Activity> other;
    private List<Activity> category;

	public ContextActivities() {
	}
	

    public ContextActivities(JsonNode jsonNode) throws URISyntaxException {
        this();

        JsonNode parentNode = jsonNode.path("parent");
        if (! parentNode.isMissingNode()) {
            this.parent = new ArrayList<Activity>();

            if (parentNode.isArray()) {
                for (JsonNode element : parentNode) {
                    this.parent.add(new Activity(element));
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
                for (JsonNode element : groupingNode) {
                    this.grouping.add(new Activity(element));
                }
            }
            else {
                this.grouping.add(new Activity(groupingNode));
            }
        }

        JsonNode otherNode = jsonNode.path("other");
        if (! otherNode.isMissingNode()) {
            this.other = new ArrayList<Activity>();

            if (otherNode.isArray()) {
                for (JsonNode element : otherNode) {
                    this.other.add(new Activity(element));
                }
            }
            else {
                this.other.add(new Activity(otherNode));
            }
        }
        
        JsonNode categoryNode = jsonNode.path("category");
        if (! categoryNode.isMissingNode()) {
            this.category = new ArrayList<Activity>();

            if (categoryNode.isArray()) {
                for (JsonNode element : categoryNode) {
                    this.category.add(new Activity(element));
                }
            }
            else {
                this.category.add(new Activity(categoryNode));
            }
        }
    }

    @Override
    public ObjectNode toJSONNode(TCAPIVersion version) {
        ObjectMapper mapper = Mapper.getInstance();
        ObjectNode node = mapper.createObjectNode();

        if (this.getParent() != null && this.getParent().size() > 0) {
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
        if (this.getGrouping() != null && this.getGrouping().size() > 0) {
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
        if (this.getOther() != null && this.getOther().size() > 0) {
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
        if (this.getCategory() != null && this.getCategory().size() > 0) {
            if (version.ordinal() <= TCAPIVersion.V100.ordinal()) {
                ArrayNode category = mapper.createArrayNode();
                node.put("category", category);

                for (Activity element : this.getCategory()) {
                    category.add(element.toJSONNode(version));
                }
            } else {
                throw new IncompatibleTCAPIVersion("Version " + version.toString() + " doesn't support the category context activity");
            }
        }

        return node;
    }

	public List<Activity> getParent() {
		return parent;
	}

	public void setParent(List<Activity> parent) {
		this.parent = parent;
	}

	public List<Activity> getGrouping() {
		return grouping;
	}

	public void setGrouping(List<Activity> grouping) {
		this.grouping = grouping;
	}

	public List<Activity> getOther() {
		return other;
	}

	public void setOther(List<Activity> other) {
		this.other = other;
	}

	public List<Activity> getCategory() {
		return category;
	}

	public void setCategory(List<Activity> category) {
		this.category = category;
	}
}
