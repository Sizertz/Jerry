package siz.terry.linker;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import siz.terry.math.Transform3D;
import siz.terry.reader.Building;
import siz.terry.reader.Entity;
import siz.terry.reader.EntityFactory;
import siz.terry.reader.Group;
import siz.terry.reader.LayerReader;
import siz.terry.reader.TransformableEntity;

public class ToBuildingLinker {
	private static LayerReader reader;

	public static void process(File file) {
		// Read file
		reader = new LayerReader(file);

		// backup file
		reader.save(file.getPath() + ".backup");

		// find parent buildings
		NodeList parents = findFutureParents();
		System.out.println("parents found: " + parents.getLength());

		Map<Building, List<Entity>> futureFamilies = new HashMap<>();
		// find all children entities
		for (int i = 0; i < parents.getLength(); i++) {
			Building futureParent = EntityFactory.newBuilding(parents.item(i), reader);

			List<Entity> futureChildren = futureParent.findContainerSiblings();

			if (futureChildren == null) {
				continue;
			}

			// compute new transforms for all children
			for (Entity futureChild : futureChildren) {
				Transform3D newTransform = futureChild.transformRelativeTo(futureParent);
				System.out.println("New transform\n" + newTransform);

				// overwrite nodes
				if (futureChild instanceof TransformableEntity) {
					System.out.println("Overwriting");
					((TransformableEntity) futureChild).saveTransformToNode(newTransform);
				}
			}

			futureFamilies.put(futureParent, futureChildren);
		}

		// link things in the XML
		writeLink(futureFamilies);
		// save to file
		reader.save();
	}

	/**
	 * Finds the future parents. Assumes that the user has named them 'parent'. Only
	 * buildings can be parents. This is a limitation of the game. Not sure why
	 * you'd want anything else to be a parent.
	 * 
	 * @return the parents in a NodeList
	 */
	public static NodeList findFutureParents() {
		NodeList parents = null;
		try {
			parents = (NodeList) reader.getxPath().evaluate("//entity[@name='parent' and descendant::ECBuilding]",
					reader.getRoot(), XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return parents;
	}

	public static Element fromElement(String id) {
		Element from = reader.getXmlDoc().createElement("from");
		from.setAttribute("id", id);
		return from;
	}

	public static Element toElement(String id) {
		Element from = reader.getXmlDoc().createElement("to");
		from.setAttribute("id", id);
		return from;
	}

	public static void writeLink(Map<Building, List<Entity>> futureFamilies) {
		try {
			for (Building parent : futureFamilies.keySet()) {
				// Logical links
				Node logical = reader.evaluateSingleNode("/layer/associations/Logical");
				// From
				Element from = fromElement(parent.getID());
				logical.appendChild(from);
				// To
				for (Entity child : futureFamilies.get(parent)) {
					from.appendChild(toElement(child.getID()));
				}

				// Transform links
				Node transform = reader.evaluateSingleNode("/layer/associations/Transform");
				// From
				Element from2 = fromElement(parent.getID());
				transform.appendChild(from2);
				// To
				for (Entity child : futureFamilies.get(parent)) {
					from2.appendChild(toElement(child.getID()));
				}

				// Take things out of groups and get them back into main <entities>
				System.out.println("parent " + parent);
				System.out.println("container " + parent.getContainer());
				if (parent.getContainer() instanceof Group) {
					Node mainEntities = reader.evaluateSingleNode("/layer/entities");
					parent.getNode().getParentNode().removeChild(parent.getNode());
					mainEntities.appendChild(parent.getNode());
					for (Entity child : futureFamilies.get(parent)) {
						child.getNode().getParentNode().removeChild(child.getNode());
						mainEntities.appendChild(child.getNode());
					}
				}

				// Remove links with containers
				String containerID = parent.getContainer().getID();
				System.out.println("containerID "+containerID);
				Node containerFrom = reader.evaluateSingleNode("//from[@id='" + containerID + "']");
				containerFrom.getParentNode().removeChild(containerFrom);

				// Remove "parent" names
				parent.getNode().getAttributes().removeNamedItem("name");

			}
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
	}
}
