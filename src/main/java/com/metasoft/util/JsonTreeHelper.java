package com.metasoft.util;

import java.util.List;

public class JsonTreeHelper {

	public static class JsonNode {

		public String text;
		public JsonNode[] nodes;
		public String schema;
		public String tableModelId;
		
		public JsonNode(String text) {
			this.text = text;
		}
		
		public JsonNode(String text, String schema, String tableModelId) {
			this.text = text;
			this.schema = schema;
			this.tableModelId = tableModelId;
		}

		public void addChild(JsonNode child) {
			if (nodes == null) {
				nodes = new JsonNode[] { child };
			} else {
				JsonNode[] origin = nodes;
				nodes = new JsonNode[origin.length + 1];
				for (int i = 0; i < origin.length; i++) {
					nodes[i] = origin[i];
				}
				nodes[origin.length] = child;
			}
		}

		public int getChildSize() {
			if (nodes == null)
				return 0;
			else
				return nodes.length;
		}
	}

	public static JsonNode toJsonTree(List<String> categories) {
		final String separator = "/";

		JsonNode root = new JsonNode("root");

		for (String category : categories) {
			JsonNode current = root;
			String[] parts = category.split(separator);
			for (String part : parts) {
				JsonNode subNode = null;
				if (current.getChildSize() == 0) {
					subNode = new JsonNode(part);
					current.addChild(subNode);
				} else {
					for (JsonNode node : current.nodes) {
						if (node.text.equals(part))
							subNode = node;
					}
					if (subNode == null) {
						subNode = new JsonNode(part);
						current.addChild(subNode);
					}
				}
				current = subNode;
			}
		}

		return root;
	}
	
}
