package contextaware.sensorapi.impl;

import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.SAXException;
import contextaware.GlobalVars;

class SensorValueXMLHandler extends DefaultHandler {

    public String ret = "";
    private boolean inDouble = false;

    @Override
    public void startElement(String uri, String localName, String qName, org.xml.sax.Attributes attributes) throws SAXException {
        if (localName.equals(GlobalVars.XML_ELEMENT)) {
            inDouble = true;
        }
    }

    public void endElement(String namespaceURI, String localName, String qualifiedName) throws SAXException {
        if (localName.equals(GlobalVars.XML_ELEMENT)) {
            inDouble = false;
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        if (inDouble) {
            for (int i = start; i < start + length; i++) {
                ret = ret + ch[i];
            }
        }
    }
}
