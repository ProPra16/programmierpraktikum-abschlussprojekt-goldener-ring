package xml;

import util.Exercise;

/** an example sink for content events. 
  It simply prints what it sees. */

public final class Sink 
        extends org.xml.sax.helpers.DefaultHandler
        implements org.xml.sax.ContentHandler
{ 
    
    private static Exercise exercise;
    
    final private static void print 
        ( final String context, final String text ) 
    {       
        System.out.println( context + ": \"" + text + "\"." ); 
    }

    @Override
    final public void startElement
        ( final String namespace, final String localname,
                final String type, final org.xml.sax.Attributes attributes )
                    throws org.xml.sax.SAXException 
    {       
        print( "startElement", type ); 
    }

    @Override
    final public void endElement
        ( final String namespace, final String localname, final String type ) 
            throws org.xml.sax.SAXException 
    { 
        print( "endElement  ", type ); 
    }

    @Override
    final public void characters 
        ( final char[] ch, final int start, final int len )
    { 
        final String text = new String( ch, start, len ); 
        final String text1 = text.trim(); 
        if( text1.length() > 0 )
            print( "characters  ", text1 ); 
    }
}