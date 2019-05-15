package siz.terry.reader;

import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import siz.terry.math.Transform3D;

public class Entity {
	protected Node node;
	protected LayerReader reader;

	protected Entity(Node node, LayerReader reader) {
		this.node = node;
		this.reader = reader;
	}

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public String getID() {
		return this.node.getAttributes().getNamedItem("id").getNodeValue();
	}

	public String toString() {
		return "[id= " + this.getID() + ", name= " + this.getName() + ", type= " + this.getType() + "]";
	}

	public String getType() {
		return "Entity";
	}

	public String getName() {
		Node nameNode = this.node.getAttributes().getNamedItem("name");

		if (nameNode != null)
			return nameNode.getNodeValue();
		else
			return "N-A";
	}

	/**
	 * 
	 * @return the group or layer that immediately contains this entity or null if
	 *         the entity is at the file layer root
	 */
	public Entity getContainer() {
		Entity container = null;
		try {
			Node from = (Node) reader.evaluate("//from[to/@id='" + this.getID() + "']",
					XPathConstants.NODE);
			if (from != null) {
				String containerID = from.getAttributes().getNamedItem("id").getNodeValue();
				container = reader.evaluateSingleEntity("//entity[@id='" + containerID + "']");

			}
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return container;
	}

	/**
	 * 
	 * @return a NodeList of the entity nodes that belong to the same container or
	 *         null if the entity doesn't have a container
	 */
	public List<Entity> findContainerSiblings() {
		Entity container = this.getContainer();
		if (container != null) {
			List<Entity> res = new ArrayList<>();
			try {
				NodeList toSiblings = (NodeList) reader.evaluate(
						"//from[@id='" + container.getID() + "']/to[@id!='" + this.getID() + "']",
						XPathConstants.NODESET);

				for (int i = 0; i < toSiblings.getLength(); i++) {
					String siblingID = toSiblings.item(i).getAttributes().getNamedItem("id").getNodeValue();
					res.add(reader.getEntityByID(siblingID));
				}

				return res;

			} catch (XPathExpressionException e) {
				e.printStackTrace();
			}

		}
		return null;
	}

	/**
	 * 
	 * @param futureParent
	 * @return the transformation for this object relative to the coordinate space
	 *         of a (future) parent object. Returns null if this is not a
	 *         TransformableEntity
	 */
	public Transform3D transformRelativeTo(TransformableEntity futureParent) {
		return null;
	}



}
