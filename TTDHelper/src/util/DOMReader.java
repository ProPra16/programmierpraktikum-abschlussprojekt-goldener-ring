package util;

import gui.WorkshopControl;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * The Reader reads the Katalog file and parser for Exercises.
 */
public class DOMReader {
    
    
    /**
     * Reads XML Catalog and parses for Excersises.
     */
    public static void parseCatalog() 
    {
        try {
            //Iterating through the nodes and extracting the data.
            NodeList nodeList = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(
                    new FileInputStream("Katalog.xml"), "UTF-8").getDocumentElement().getChildNodes();
            
            Exercise exercise;
            List<String> list;
            
            for (int i = 0; i < nodeList.getLength(); i++) 
            {
                //System.out.println("found something");
                Node node = nodeList.item(i);
                Node ndCNode = node;
                if(ndCNode.getNodeName().equals(("exercise")))
                {
                    //System.out.println("exercise Found");
                    exercise = new Exercise();
                    NodeList ndChildNodes = node.getChildNodes();
                    for (int n = 0; n < ndChildNodes.getLength(); n++)
                    {
                        Node rdCNode = ndChildNodes.item(n);

                        String content = rdCNode.getTextContent().trim();
                        switch(rdCNode.getNodeName())
                        {
                            case "name":
                                exercise.setName(content);
                                //System.out.println("name Found");
                                break;
                            case "description":
                                exercise.setDescription(content);
                                //System.out.println("description Found");
                                break;
                            case "classes":
                                //System.out.println("classes Found");
                                NodeList clChildNodes = rdCNode.getChildNodes();
                                for (int m = 0; m < clChildNodes.getLength(); m++)
                                {
                                    Node clCNode = clChildNodes.item(m);
                                    if(clCNode.getNodeName().equals("class"))
                                    {
                                        //System.out.println("class Found");
                                        list = new ArrayList();
                                        list.add(clCNode.getAttributes().getNamedItem("name").getNodeValue());
                                        String[] ts = clCNode.getTextContent().split("\n");
                                        for(String s : ts)
                                        {
                                            if(s.length()>15)
                                            {
                                                list.add(s.substring(16));
                                            }
                                        }
                                        exercise.addClass(list.get(0), list.subList(1, list.size()));
                                    }
                                }
                                break;
                            case "tests":
                                //System.out.println("tests found");
                                NodeList tsChildNodes = rdCNode.getChildNodes();
                                for (int m = 0; m < tsChildNodes.getLength(); m++)
                                {
                                    Node tsCNode = tsChildNodes.item(m);
                                    if(tsCNode.getNodeName().equals("test"))
                                    {
                                        //System.out.println("test Found");
                                        list = new ArrayList();
                                        list.add(tsCNode.getAttributes().getNamedItem("name").getNodeValue());
                                        String[] ts = tsCNode.getTextContent().split("\n");
                                        for(String s : ts)
                                        {
                                            if(s.length()>15)
                                            {
                                                list.add(s.substring(16));
                                            }
                                        }
                                        exercise.addTest(list);
                                    }
                                }
                                break;
                            case "config":
                                //System.out.println("config found");
                                NodeList cfChildNodes = rdCNode.getChildNodes();
                                for (int m = 0; m < cfChildNodes.getLength(); m++)
                                {
                                    Node cfCNode = cfChildNodes.item(m);
                                    if(cfCNode.getNodeName().equals("babysteps"))
                                    {
                                        if("True".equals(cfCNode.getAttributes().getNamedItem("value").getNodeValue()))
                                            exercise.setBabysteps(
                                                    Integer.parseInt(cfCNode.getAttributes().getNamedItem("time").getNodeValue()));
                                    } else if(cfCNode.getNodeName().equals("timetracking"))
                                        exercise.setTimetrack(Boolean.parseBoolean(cfCNode.getAttributes().getNamedItem("value").getNodeValue()));
                                }
                                break;
                        }
                    }
                    WorkshopControl.addExercise(exercise);
                }
            }
        } catch (SAXException | IOException | ParserConfigurationException ex) {
            Logger.getLogger(DOMReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}