package siz.terry.reader;

import java.io.File;
import java.io.IOException;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class LayerReader {
	private File fileXML;
	private Document xmlDoc;
	private Element root;
	private XPath xPath;
	
	public LayerReader(File file) {
		initialise(file.getPath());
	}


	public void initialise(String inputPath) {
		// select file
		this.fileXML = new File(inputPath);

		// setup xml reading and manipulating
		boolean tryAgain = true;
		boolean fail;
		do {
			try {
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				this.xmlDoc = builder.parse(this.fileXML);
				this.root = this.xmlDoc.getDocumentElement();
				this.xPath = XPathFactory.newInstance().newXPath();
				fail = false;
			} catch (ParserConfigurationException | SAXException | IOException e) {
				fail = true;
				tryAgain = askTryAgain(e);
			}
		} while (fail && tryAgain);
	}

	public File getFileXML() {
		return fileXML;
	}

	public Document getXmlDoc() {
		return xmlDoc;
	}

	public Element getRoot() {
		return root;
	}

	public XPath getxPath() {
		return xPath;
	}

	private static boolean askTryAgain(Exception e) {
		e.printStackTrace();
		return false;
	}

	public static String selectFile() {
		return "C:\\Program Files (x86)\\Steam\\steamapps\\common\\Total War WARHAMMER II\\assembly_kit\\raw_data\\terrain\\tiles\\battle\\_assembly_kit\\5b289e09_3f17_4749_abf5_04c7cf315e63\\melons_test.16a9d91be657b74.layer";
	}

	public Object evaluate(String xPathExpression, QName returnType) throws XPathExpressionException {		
		return this.xPath.evaluate(xPathExpression, this.root, returnType);
	}
	
	public Entity evaluateSingleEntity(String xPathExpression) throws XPathExpressionException {
		return EntityFactory.newEntity((Node) this.xPath.evaluate(xPathExpression, this.root, XPathConstants.NODE), this);
	}
	
	public Node evaluateSingleNode(String xPathExpression) throws XPathExpressionException {
		return (Node) this.xPath.evaluate(xPathExpression, this.root, XPathConstants.NODE);
	}
	
	public Entity getEntityByID(String id) throws XPathExpressionException {
		return evaluateSingleEntity("//entity[@id='"+id+"']");
	}
	
	public void save(String filePath) {
		try {
        	//Inactive xml transfo
        	Transformer t = TransformerFactory.newInstance().newTransformer();
        	t.setOutputProperty(OutputKeys.INDENT, "yes");
        	// apply transformation  
        	StreamResult XML = new StreamResult(filePath);        	
			t.transform(new DOMSource(this.root), XML);
		} catch (TransformerException e) {
			e.printStackTrace();
		} 
	}
	
	public void save() {
		save(this.fileXML.getPath());
	}

	
}
