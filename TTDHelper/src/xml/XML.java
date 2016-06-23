package xml;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.XMLReader;

public final class XML
{ 
    final public static org.xml.sax.XMLReader makeXMLReader() throws Exception 
    { 
        final SAXParserFactory saxParserFactory   =  
                SAXParserFactory.newInstance(); 
        final SAXParser saxParser = 
                saxParserFactory.newSAXParser(); 
        final XMLReader parser = 
                saxParser.getXMLReader(); 
        return parser; 
    }
}