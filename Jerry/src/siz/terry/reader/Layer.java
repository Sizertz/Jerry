package siz.terry.reader;

import org.w3c.dom.Node;

public class Layer extends Entity {

	public Layer(Node node) {
		super(node);
	}

	@Override
	public String getType() {
		return "Layer";
	}
}
