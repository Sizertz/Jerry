package siz.terry.reader;

import org.w3c.dom.Node;

public class EntityFactory {
	public static Entity newEntity(Node node) {
		if (node == null) {
			return null;
		}
		Boolean transformable = false;
		for (Node iterator = node.getFirstChild(); iterator != null; iterator = iterator.getNextSibling()) {
			if (iterator.getNodeName().equals("ECLayer")) {
				return new Layer(node);
			}
			if (iterator.getNodeName().equals("ECBuilding")) {
				return new Building(node);
			}
			
			if (iterator.getNodeName().equals("ECTransform")) {
				transformable = true;
			}
			
			if (iterator.getNodeName().equals("ECSignature")) {
				return new Group(node);
			}
		}

		if (transformable)
			return new TransformableEntity(node);

		return new Entity(node);
	}

	public static Building newBuilding(Node node) {
		for (Node iterator = node.getFirstChild(); iterator != null; iterator = iterator.getNextSibling()) {
			if (iterator.getNodeName().equals("ECBuilding")) {
				return new Building(node);
			}
		}
		return null;
	}
}
