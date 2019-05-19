package siz.terry.reader;

import org.w3c.dom.Node;

public class GroupSignature extends TransformableEntity {

	protected GroupSignature(Node node, LayerReader reader) {
		super(node, reader);
	}
	
	@Override
	public String getType() {
		return "Signature";
	}

}
