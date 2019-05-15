package siz.terry.reader;

import org.w3c.dom.Node;

public class Group extends TransformableEntity {
	protected Group(Node groupAnchor, LayerReader reader) {
		super(groupAnchor, reader);
	}

	@Override
	public String getType() {
		return "Group";
	}
}
