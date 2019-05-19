package siz.terry.reader;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Node;

import siz.terry.math.Matrix3D;
import siz.terry.math.Transform3D;
import siz.terry.math.Utils;
import siz.terry.math.Vector3;

public class TransformableEntity extends Entity {
	private Transform3D transform;

	protected TransformableEntity(Node node, LayerReader reader) {
		super(node, reader);
		Node transformNode;
		try {
			transformNode = (Node) reader.evaluate("//entity[@id='" + this.getID() + "']/ECTransform",
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
	public Transform3D transformRelativeTo(Transform3D parentTransform) {
		Transform3D newTransform;
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
		Matrix3D newRotation = parentRotation.transpose().dot(oldRotation);
		Vector3 newAngles = newRotation.anglesFromRotationMatrix();

		// scale
		Vector3 newScale = oldScale;
		double cutOff = 1E-3;
		if (Utils.approxEquals(cutOff, parentScale.get(0), parentScale.get(1), parentScale.get(2))) {
			// parent is uniformly scaled
			// apply simplified formula for
			newScale = newScale.scalarDot(1 / parentScale.get(0));
		} else {
			// general formula
			// should work if axes align
			// and approximate if they don't
			Matrix3D mat = Matrix3D.diagonal(parentScale).dot(parentRotation.transpose().dot(oldRotation));
			for (int i = 0; i < 3; i++) {
				newScale.set(i, oldScale.get(i) / mat.getColumn(i).norm());
			}			
		}

		newTransform = new Transform3D(newAngles, newTranslations, newScale);
		return newTransform;
	}

	public void saveTransformToNode(Transform3D newTransform) {
		try {
			reader.evaluateSingleNode("//entity[@id='" + getID() + "']/ECTransform/@rotation")
					.setNodeValue(newTransform.getRotationAngles().toString());
			reader.evaluateSingleNode("//entity[@id='" + getID() + "']/ECTransform/@position")
					.setNodeValue(newTransform.getTranslations().toString());
			reader.evaluateSingleNode("//entity[@id='" + getID() + "']/ECTransform/@scale")
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
