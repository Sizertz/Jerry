package siz.terry.reader.test;

import java.io.File;

import javax.xml.xpath.XPathExpressionException;

import siz.terry.reader.Group;
import siz.terry.reader.GroupSignature;
import siz.terry.reader.LayerReader;

public class test {
	private static LayerReader reader;
	
	
	public static void main(String[] args) {
		// Read file
		testGroupSignature();
	}

	// testing the GroupSignature class and a Group's ability to find its signature
	private static void testGroupSignature() {
		reader = new LayerReader(new File("invisible_buildings_test.16a73f2db54d914.layer"));
		try {
			Group group = (Group) reader.getEntityByID("16a8839ad9a2740");
			System.out.println(group);
			GroupSignature signature = group.getSignature();
			System.out.println(signature);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
	}

}
