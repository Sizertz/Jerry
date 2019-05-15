package siz.terry.reader;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Node;

import siz.terry.math.Matrix3D;
import siz.terry.math.Transform3D;
import siz.terry.math.Vector3;

public class TransformableEntity extends Entity {
	private Transform3D transform;

	protected TransformableEntity(Node node, LayerReader reader) {
		super(node, reader);
		Node transformNode;
		try {
			transformNode = (Node) reader.evaluate("//ECTransform[parent::entity[@id='" + this.getID() + "']]",
					XPathConstants.NODE);
			this.transform = new Transform3D(transformNode.getAttributes().getNamedItem("rotation").getNodeValue(),
					transformNode.getAttributes().getNamedItem("position").getNodeValue(),
					transformNode.getAttributes().getNamedItem("scale").getNodeValue());
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
	}

	public Transform3D getTransform() {
		return transform;
	}

	public void setTransform(Transform3D transform) {
		this.transform = transform;
	}

	@Override
	public Transform3D transformRelativeTo(TransformableEntity futureParent) {
		Transform3D newTransform;
		Transform3D parentTransform = futureParent.getTransform();
		Transform3D oldTransform = this.getTransform();
		Vector3 oldTranslation = oldTransform.getTranslations();
		Vector3 parentTranslation = parentTransform.getTranslations();
		Matrix3D oldRotation = oldTransform.getRotation();
		Matrix3D parentRotation = parentTransform.getRotation();
		Vector3 oldScale = oldTransform.getScalingFactors();
		Vector3 parentScale = parentTransform.getScalingFactors();

		// translations
		Vector3 parentInverseScale = new Vector3(1 / parentScale.get(0), 1 / parentScale.get(1),
				1 / parentScale.get(2));
		Vector3 newTranslations = Vector3.cast((oldTranslation.minus(parentTranslation)).transpose().dot(parentRotation)
				.dot(Matrix3D.diagonal(parentInverseScale)));

		// rotations
		Matrix3D newRotation = oldRotation.dot(parentRotation.transpose());
		Vector3 newAngles = newRotation.anglesFromRotationMatrix();

		// scale
		Vector3 newScale = oldScale;
		if (parentScale.get(0) == parentScale.get(1) && parentScale.get(0) == parentScale.get(2)) {
			newScale = newScale.scalarDot(1 / parentScale.get(0));
		} else {
			boolean[] aligned = new boolean[3];
			for (int i = 0; i < 3; i++) {
				aligned[i] = false;
			}
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					if (oldRotation.getColumn(i).equals(parentRotation.getColumn(j))
							|| oldRotation.getColumn(i).equals(parentRotation.getColumn(j).scalarDot(-1))) {
						System.out.println(oldScale.get(i) + "=" + parentScale.get(j));
						newScale.set(i, oldScale.get(i) / parentScale.get(j));
						aligned[i] = true;
						if (parentScale.get((j + 1) % 3) == parentScale.get((j + 2) % 3)) {
							newScale.set((i + 1) % 3, oldScale.get((i + 1) % 3) / parentScale.get((j + 1) % 3));
							newScale.set((i + 2) % 3, oldScale.get((i + 2) % 3) / parentScale.get((j + 1) % 3));
							aligned[(i + 1) % 3] = true;
							aligned[(i + 2) % 3] = true;
						}
					}
				}
			}
			if (!aligned[0] || !aligned[1] || !aligned[2]) {
				flagScale();
			}
		}

		newTransform = new Transform3D(newAngles, newTranslations, newScale);
		return newTransform;
	}

	private void flagScale() {
		System.out.println("Proper scale cannot be computed for " + this);

	}

	public void saveTransformToNode(Transform3D newTransform) {
		try {
			reader.evaluateSingleNode("//@rotation[ancestor::entity/@id='" + getID() + "']")
					.setNodeValue(newTransform.getRotationAngles().toString());
			reader.evaluateSingleNode("//@position[ancestor::entity/@id='" + getID() + "']")
					.setNodeValue(newTransform.getTranslations().toString());
			reader.evaluateSingleNode("//@scale[ancestor::entity/@id='" + getID() + "']")
					.setNodeValue(newTransform.getScalingFactors().toString());

		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}

		for (Node n = this.node.getFirstChild(); n != null; n = n.getNextSibling()) {
			if (n.getNodeName().equals("ECTransform")) {

			}
		}
	}

}
