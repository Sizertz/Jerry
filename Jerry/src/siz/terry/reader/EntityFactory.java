package siz.terry.reader;

import org.w3c.dom.Node;

public class EntityFactory {
	public static Entity newEntity(Node node, LayerReader reader) {
		
		if (node == null) {
			return null;
		}
		Boolean transformable = false;
		for (Node iterator = node.getFirstChild(); iterator != null; iterator = iterator.getNextSibling()) {
			if (iterator.getNodeName().equals("ECLayer")) {
				return new Layer(node, reader);
			}
			if (iterator.getNodeName().equals("ECBuilding")) {
				return new Building(node, reader);
			}

			if (iterator.getNodeName().equals("ECTransform")) {
				transformable = true;
			}

			if (iterator.getNodeName().equals("ECSignature")) {
				return new GroupSignature(node, reader);
			}
			
			if (iterator.getNodeName().equals("ECGroup")) {
				return new Group(node, reader);
			}
		}

		if (transformable)
			return new TransformableEntity(node, reader);

		return new Entity(node, reader);
	}

	public static Building newBuilding(Node node, LayerReader reader) {
		for (Node iterator = node.getFirstChild(); iterator != null; iterator = iterator.getNextSibling()) {
			if (iterator.getNodeName().equals("ECBuilding")) {
				return new Building(node, reader);
			}
		}
		return null;
	}
}
