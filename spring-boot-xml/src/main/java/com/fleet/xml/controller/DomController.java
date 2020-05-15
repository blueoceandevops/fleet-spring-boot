package com.fleet.xml.controller;

import com.fleet.xml.entity.Property;
import com.fleet.xml.entity.Protocol;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/dom")
public class DomController {

    @RequestMapping("/read")
    public List<Protocol> read() {
        List<Protocol> protocolList = new ArrayList<>();
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse("classpath:xml/protocol.xml");
            Element element = document.getDocumentElement();
            NodeList nodeList = element.getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node item = nodeList.item(i);
                if (item instanceof Element) {
                    Protocol protocol = new Protocol();
                    for (Node node = item.getFirstChild(); node != null; node = node.getNextSibling()) {
                        if (node.getNodeType() == Node.ELEMENT_NODE) {
                            String name = node.getNodeName();
                            String value = node.getFirstChild().getNodeValue();
                            if (name.equals("name")) {
                                protocol.setName(value);
                            }
                            if (name.equals("identifier")) {
                                protocol.setIdentifier(value);
                            }
                            if (name.equals("unit")) {
                                protocol.setUnit(value);
                            }
                            if (name.equals("type")) {
                                protocol.setType(Integer.parseInt(value));
                            }
                            if (name.equals("length")) {
                                protocol.setLength(Integer.parseInt(value));
                            }
                            if (name.equals("propertyList")) {
                                List<Property> propertyList = new ArrayList<>();
                                NodeList childNodeList = node.getChildNodes();
                                for (int j = 0; j < childNodeList.getLength(); j++) {
                                    Node childItem = childNodeList.item(j);
                                    if (childItem instanceof Element) {
                                        Property property = new Property();
                                        for (Node childNode = childItem.getFirstChild(); childNode != null; childNode = childNode.getNextSibling()) {
                                            if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                                                String childName = childNode.getNodeName();
                                                String childValue = childNode.getFirstChild().getNodeValue();
                                                if (childName.equals("value")) {
                                                    property.setValue(childValue);
                                                }
                                                if (childName.equals("desc")) {
                                                    property.setDesc(childValue);
                                                }
                                            }
                                        }
                                        propertyList.add(property);
                                    }
                                }
                                protocol.setPropertyList(propertyList);
                            }
                            if (name.equals("reservedWord")) {
                                protocol.setReservedWord(Integer.parseInt(value));
                            }
                            if (name.equals("remark")) {
                                protocol.setRemark(value);
                            }
                        }
                    }
                    protocolList.add(protocol);
                }
            }
        } catch (Exception e) {
        }

        return protocolList;
    }
}
