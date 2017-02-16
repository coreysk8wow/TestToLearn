package antTargetSequence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class AntTargetInvocationOrder {

    private ParseXMLUtils utils = new ParseXMLUtils();

    public List<String> getSequence(String targetName)
	    throws ParserConfigurationException, SAXException, IOException {

	String[] depends = utils.getAttributeValue(targetName, "depends");
	List<String> list = Arrays.asList(depends);
	List<String> list2 = new ArrayList<String>(list);
	list2.add(targetName);

	return list2;
    }

    public List<String> getCompleteTargetsInvocationSequence(String targetName) 
      throws ParserConfigurationException, SAXException, IOException {

	List<String> list = getSequence(targetName);
	List<String> subList = new ArrayList<String>();
	
	if(targetName.equals(list.get(0))) {
	    return list;
	} else {
	    for(int i = 0; i < list.size() - 1; i++) {
		subList.addAll(getCompleteTargetsInvocationSequence(list.get(i)));
	    }
	    Collections.reverse(list);
	    list.remove(list.size() - 1);
	    Collections.reverse(subList);
	    list.addAll(subList);
	    Collections.reverse(list);
	    
	    return list;
	}
	
    }

    public void printOneTargetInvocationSequence(String targetName) 
      throws ParserConfigurationException, SAXException, IOException {
	
	/*System.out.println(a.getSequence("jetty_dev") + " in AntTargetInvocationOrder");
	System.out.println(a.getSequence("envJDK") + " in AntTargetInvocationOrder");*/
	
	List list = getCompleteTargetsInvocationSequence(targetName);
	ParseXMLUtils.removeDuplicateWithOrder((ArrayList)list);
	
	for(Object s: list) {
	    System.out.print((String) s + ", ");
	}
    }
    
    public void printAllTargetInvocationSequence() 
      throws ParserConfigurationException, SAXException, IOException {
	
	File buildFile = new File(ParseXMLUtils.BUILD_FILE_PATH);
	DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	Document doc = builder.parse(buildFile);
	NodeList nodes = doc.getElementsByTagName("target");
	
	for(int i = 0; i < nodes.getLength(); i++) {
	    NamedNodeMap nnm = nodes.item(i).getAttributes();
	    printOneTargetInvocationSequence(nnm.getNamedItem("name").getNodeValue());
	    System.out.println();
	}
    }
    
    
    public static void main(String[] args) throws ParserConfigurationException,
	    SAXException, IOException {
	
	AntTargetInvocationOrder a = new AntTargetInvocationOrder();
//	a.printOneTargetInvocationSequence("ifaceviewer3MacOS");
	
	a.printAllTargetInvocationSequence();
	
    }
}
