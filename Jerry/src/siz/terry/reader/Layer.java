package siz.terry.reader;

import org.w3c.dom.Node;

public class Layer extends Entity {

	public Layer(Node node, LayerReader reader) {
		super(node,reader);
	}

	@Override
	public String getType() {
		return "Layer";
	}
}
