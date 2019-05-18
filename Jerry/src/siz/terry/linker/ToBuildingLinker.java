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
import siz.terry.reader.GroupSignature;
import siz.terry.reader.LayerReader;
import siz.terry.reader.TransformableEntity;

public class ToBuildingLinker {
	private static LayerReader reader;

	/**
	 * Finds all the buildings named 'parent' in the provided .layer file and writes Logical and Transform links with all the other entities in the same layer.<br>
	 * Creates a backup of the input file.<br>
	 * 
	 * @param file - the layer file you want to process
	 */
	public static void process(File file) {
		// Read file
		reader = new LayerReader(file);

		// backup file
		reader.save(file.getPath() + ".backup");

		// find parent buildings
		NodeList parents = findFutureParents();
		System.out.println("parents found: " + parents.getLength());

		Map<Building, List<Entity>> futureFamilies = new HashMap<Building, List<Entity>>();
		// find all children entities
		for (int i = 0; i < parents.getLength(); i++) {
			System.out.println("====================");
			Building futureParent = EntityFactory.newBuilding(parents.item(i), reader);
			System.out.println("parent "+i+" : "+futureParent);
			
			// ignore parents that are in groups
			if(futureParent.getContainer() instanceof Group) {
				continue;
			}

			List<Entity> futureChildren = futureParent.findContainerSiblings();

			// ignore parents that have no future children
			if (futureChildren == null) {
				continue;
			}

			// compute new transforms for all children
			for (Entity futureChild : futureChildren) {
				Transform3D newTransform = futureChild.transformRelativeTo(futureParent.getTransform());
				System.out.println("New transform for "+ futureChild+"\n" + newTransform);

				// overwrite nodes
				if (futureChild instanceof TransformableEntity) {
					System.out.println("Overwriting ");
					((TransformableEntity) futureChild).saveTransformToNode(newTransform);
				}
				
				// All (nested) signatures need to be transformed
				if(futureChild instanceof Group) {
					try {
						NodeList signatures = (NodeList) reader.evaluate("//entity[@id='"+futureChild.getID()+"']//ECSignature/parent::entity", XPathConstants.NODESET);
						for(int j=0; j<signatures.getLength(); j++) {
							GroupSignature signature = (GroupSignature) EntityFactory.newEntity(signatures.item(j), reader);
							Transform3D signatureTransform = signature.transformRelativeTo(futureParent.getTransform());
							System.out.println("New transform for signature "+signature.getID()+"\n" + signatureTransform);
							System.out.println("Overwriting");
							signature.saveTransformToNode(signatureTransform);
							
						}
					} catch (XPathExpressionException e) {
						e.printStackTrace();
					}
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
				
				// Remove links with containing layer for children
				String containerID = parent.getContainer().getID();
				System.out.println("containerID "+containerID);
				for (Entity child : futureFamilies.get(parent)) {
					Node containerTo = reader.evaluateSingleNode("//from[@id='" + containerID + "']/to[@id='" + child.getID() + "']");
					containerTo.getParentNode().removeChild(containerTo);
				}

				// Remove "parent" names
				parent.getNode().getAttributes().removeNamedItem("name");

			}
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
	}
}
