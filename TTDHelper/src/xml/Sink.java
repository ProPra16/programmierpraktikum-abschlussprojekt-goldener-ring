package xml;

import gui.WorkshopControl;
import java.util.ArrayList;
import java.util.List;
import util.Exercise;

/** an example sink for content events. 
  It simply prints what it sees. */

public final class Sink 
        extends org.xml.sax.helpers.DefaultHandler
        implements org.xml.sax.ContentHandler
{ 
    
    private Exercise exercise;
    
    private List<String> collectStrings;
    
    final private void process 
        ( final String context, final String text ) 
    {
        while(!text.equals("exercises") && !text.equals("exercise") && 
                this.exercise == null) { }
            
        if(context.equals("startElement"))
        {
            if(!text.equals("exercises") && !text.equals("classes") && 
                    !text.equals("exercise"))
                this.collectStrings = new ArrayList();
            else if(text.equals("exercise"))
                this.exercise = new Exercise();
        } else if(context.equals("endElement"))
        {
            if(text.equals("exercises") || text.equals("classes"))
                return;
            else if(text.equals("class"))
                this.exercise.addClass(this.collectStrings.get(0), 
                        this.collectStrings.subList(1, 
                                this.collectStrings.size()));
            else if(text.equals("test"))
                this.exercise.addTest(this.collectStrings.get(0), 
                        this.collectStrings.subList(1, 
                                this.collectStrings.size()));
            else if(text.equals("name"))
                this.exercise.setName(this.collectStrings.get(0));
            else if(text.equals("description"))
                this.exercise.setName(this.collectStrings.get(0));
            else if(text.equals("exercise"))
                WorkshopControl.addExercise(exercise);
        } else if(context.equals("characters"))
            this.collectStrings.add(text);
        
        
        
        //System.out.println( context + ": \"" + text + "\"." );
    }

    @Override
    final public void startElement
        ( final String namespace, final String localname,
                final String type, final org.xml.sax.Attributes attributes )
                    throws org.xml.sax.SAXException 
    {       
        process( "startElement", type );
    }

    @Override
    final public void endElement
        ( final String namespace, final String localname, final String type ) 
            throws org.xml.sax.SAXException 
    { 
        process( "endElement", type ); 
    }

    @Override
    final public void characters 
        ( final char[] ch, final int start, final int len )
    { 
        final String text = new String( ch, start, len ); 
        final String text1 = text.trim(); 
        if( text1.length() > 0 )
            process( "characters", text1 ); 
    }
}