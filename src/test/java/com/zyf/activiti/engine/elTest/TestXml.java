package com.zyf.activiti.engine.elTest;

import java.io.InputStreamReader;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

public class TestXml {
	public @Test void testXmlFunctionFindAllNodes() throws Exception {
		XMLInputFactory xif = XMLInputFactory.newInstance();
		InputStreamReader in = new InputStreamReader(
				TestXml.class.getClassLoader().getResourceAsStream("function2.xml"), "UTF-8");
		XMLStreamReader xtr = xif.createXMLStreamReader(in);
		Boolean flagXmlEnd = true;
		while (flagXmlEnd) {
			if (xtr.isStartElement()) {
				System.out.println(xtr.getLocalName());
			}
			if (xtr.hasNext()) {
				xtr.next();
			} else {
				flagXmlEnd = false;
				break;
			}
		}
	}


	public @Test void testXmlFunction2() throws Exception {
		XMLInputFactory xif = XMLInputFactory.newInstance();
		// Class<? extends XMLInputFactory> xifClazz = xif.getClass();
		// // Field[] fs = xifClazz.getDeclaredFields();
		// Field mConfigField = xifClazz.getDeclaredField("mConfig");
		// mConfigField.setAccessible(true);
		// ReaderConfig mConfig = (ReaderConfig) mConfigField.get(xif);
		// mConfig.setInputBufferLength(10000);
		InputStreamReader in = new InputStreamReader(
				TestXml.class.getClassLoader().getResourceAsStream("function2.xml"), "UTF-8");
		XMLStreamReader xtr = xif.createXMLStreamReader(in);
		Boolean flagXmlEnd = true;
		while (flagXmlEnd) {
			if (xtr.isStartElement()) {
				if (xtr.getLocalName().equalsIgnoreCase("userTask")) {
					System.out.println(xtr.getAttributeValue(0));
					getElements(xtr); // 获取当前userTask下的所有元素
				}
			}
			if (xtr.hasNext()) {
				xtr.next();
			} else {
				flagXmlEnd = false;
				break;
			}
		}

	}

	private void getElements(XMLStreamReader xtr) throws XMLStreamException {
		boolean readyWithChildElements = false;
		boolean inExtensionElements = false;
		while (readyWithChildElements == false && xtr.hasNext()) {
			xtr.next();
			if (xtr.isStartElement()) {
				if ("extensionElements".equals(xtr.getLocalName())) {
					inExtensionElements = true; // 判断到达extensionElements节点
				} else if (inExtensionElements) {
					this.getTaskInfo(xtr); // 循环读取extensionElements节点下的节点
				}

			} else if (xtr.isEndElement()) {
				if ("extensionElements".equals(xtr.getLocalName())) {
					inExtensionElements = false;
				} else if ("userTask".equalsIgnoreCase(xtr.getLocalName())) {
					readyWithChildElements = true;
				}
			}

		}
	}
	
	private void getTaskInfo(XMLStreamReader xtr) throws XMLStreamException {
		boolean readyWithExtensionElement = false;
		String key = xtr.getLocalName();
		StringBuffer content = new StringBuffer();
		while (readyWithExtensionElement == false && xtr.hasNext()) {
			xtr.next();
			if (xtr.isCharacters() || XMLStreamReader.CDATA == xtr.getEventType()) {
				if (StringUtils.isNotEmpty(xtr.getText().trim())) {
					//content.append(xtr.getText());
					if(key.equals("usertaskfunction")){
						System.out.println(key + ":" +xtr.getText().trim());
					}
				}
			} else if (xtr.isStartElement()) {
				getTaskInfo(xtr); // 如果子节点中有子节点，则循环读取
			} else if (xtr.isEndElement() && key.equalsIgnoreCase(xtr.getLocalName())) {
				readyWithExtensionElement = true;
			}
		}
		//System.out.println(key + ":" + content.toString());
	}

	
}

