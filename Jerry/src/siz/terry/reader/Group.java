package siz.terry.reader;

import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Node;

public class Group extends TransformableEntity {
	GroupSignature signature = null;
	
	protected Group(Node groupAnchor, LayerReader reader) {
		super(groupAnchor, reader);
		try {
			this.signature = (GroupSignature) this.reader.evaluateSingleEntity("//entity[@id='" + this.getID() + "']/ECGroup/group/entities/entity/ECSignature/parent::*");
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getType() {
		return "Group";
	}
	
	public GroupSignature getSignature() {
		return signature;
	}
}
