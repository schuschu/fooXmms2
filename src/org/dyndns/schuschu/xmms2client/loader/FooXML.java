package org.dyndns.schuschu.xmms2client.loader;

import java.io.*;

import javax.xml.parsers.*;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class FooXML {

	private static DocumentBuilder builder = null;
	private static DocumentBuilderFactory builderFactory = null;
	private static String filename = null;
	private static Document document = null;

	public static void init(String filename) {

		FooXML.filename = filename;

		builderFactory = DocumentBuilderFactory.newInstance();

		try {
			builder = builderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}

	public static Document parse() {
		if (builder == null) {
			return null;
		}
		try {
			document = builder.parse(new FileInputStream(filename));
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return document;
	}

	public static String get(String elementName, String attrName) {

		String[] path = elementName.split("/");

		Element item = document.getDocumentElement();

		for (int j = 0; j < path.length; j++) {

			NodeList nodes = item.getChildNodes();
			item = null;

			for (int i = 0; i < nodes.getLength(); i++) {

				Node node = nodes.item(i);

				if (node instanceof Element) {

					Element child = (Element) node;
					if (child.getNodeName().equals(path[j])) {
						item = child;
						break;
					}
				}

			}

			if (item == null) {
				return null;
			}
		}

		return getTagValue(attrName, item);
	}

	private static String getTagValue(String sTag, Element eElement) {
		NodeList nlList = eElement.getElementsByTagName(sTag).item(0)
				.getChildNodes();
		Node nValue = (Node) nlList.item(0);

		return nValue.getNodeValue();

	}
}
