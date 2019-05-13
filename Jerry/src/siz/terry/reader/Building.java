package siz.terry.reader;

import org.w3c.dom.Node;

public class Building extends TransformableEntity {

	protected Building(Node node) {
		super(node);
	}

	@Override
	public String getType() {
		return "Building";
	}

}
