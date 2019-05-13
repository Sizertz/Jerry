package siz.terry.linker;

import java.util.List;
import java.util.Map;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import siz.terry.reader.Building;
import siz.terry.reader.Entity;
import siz.terry.reader.Group;
import siz.terry.reader.LayerReader;

public class ToBuildingLinker {
	private LayerReader reader;

	protected ToBuildingLinker() {
		reader = LayerReader.getInstance();
		reader.save("backup.layer");
	}

	/**
	 * Finds the future parents. Assumes that the user has named them 'parent'. Only
	 * buildings can be parents. This is a limitation of the game. Not sure why
	 * you'd want anything else to be a parent.
	 * 
	 * @return the parents in a NodeList
	 */
	public NodeList findFutureParents() {
		NodeList parents = null;
		try {
			parents = (NodeList) reader.getxPath().evaluate("//entity[@name='parent' and descendant::ECBuilding]",
					reader.getRoot(), XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return parents;
	}

	public Element fromElement(String id) {
		Element from = reader.getXmlDoc().createElement("from");
		from.setAttribute("id", id);
		return from;
	}

	public Element toElement(String id) {
		Element from = reader.getXmlDoc().createElement("to");
		from.setAttribute("id", id);
		return from;
	}

	public void writeLink(Map<Building, List<Entity>> futureFamilies) {
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
				System.out.println("parent "+ parent);
				System.out.println("container "+ parent.getContainer());
				if(parent.getContainer() instanceof Group) {
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
				System.out.println(containerID);
				Node containerFrom = reader.evaluateSingleNode("//from[@id='" + containerID + "']");
				containerFrom.getParentNode().removeChild(containerFrom);
				
			}
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
	}
}
