package siz.terry.reader;

import org.w3c.dom.Node;

public class Group extends TransformableEntity {
	protected Group(Node groupAnchor) {
		super(groupAnchor);
	}

	@Override
	public String getType() {
		return "Group";
	}
}
