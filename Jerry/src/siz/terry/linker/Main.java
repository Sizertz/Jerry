package siz.terry.linker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.NodeList;

import siz.terry.math.Transform3D;
import siz.terry.reader.Building;
import siz.terry.reader.Entity;
import siz.terry.reader.EntityFactory;
import siz.terry.reader.LayerReader;
import siz.terry.reader.TransformableEntity;


public class Main {
	public static LayerReader reader;

	public static void main(String[] args) {
		// read file
		if(args.length>0)
			LayerReader.initialise(args[0]);
		else {
			return;
		}
		
		// backup file
		LayerReader.getInstance().save(args[0]+".backup");
				
		// instantiate model object
		ToBuildingLinker linker = new ToBuildingLinker();

		// find parent buildings
		NodeList parents = linker.findFutureParents();
		System.out.println("parents found: " + parents.getLength());

		Map<Building, List<Entity>> futureFamilies = new HashMap<>();
		// find all children entities
		for (int i = 0; i < parents.getLength(); i++) {
			Building futureParent = EntityFactory.newBuilding(parents.item(i));

			List<Entity> futureChildren = futureParent.findContainerSiblings();
			
			if(futureChildren == null) {
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
		linker.writeLink(futureFamilies);
		// save to file
		LayerReader.getInstance().save();

	}
}
