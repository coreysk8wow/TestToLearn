package antTargetSequence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ParseXMLUtils {
    
    public static final String BUILD_FILE_PATH = "./src/antTargetSequence/dsview.xml";
    
    public Map getTargetDefinition(String targetName)
      throws ParserConfigurationException, SAXException, IOException {

	File buildFile = new File(BUILD_FILE_PATH);
	DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	Document doc = builder.parse(buildFile);
	NodeList nodes = doc.getElementsByTagName("target");

	for (int i = 0; i < nodes.getLength(); i++) {
	    NamedNodeMap nnm = nodes.item(i).getAttributes();
	    if (targetName.equals(nnm.getNamedItem("name").getNodeValue())) {
		return getAttributes(nnm);
	    }
	}
	return null;
    }

    public List getAllTargetDefinitions()
      throws ParserConfigurationException, SAXException, IOException {

	File buildFile = new File(BUILD_FILE_PATH);
	DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	Document doc = builder.parse(buildFile);
	NodeList nodes = doc.getElementsByTagName("target");

	List list = new ArrayList();
	for (int i = 0; i < nodes.getLength(); i++) {
	    NamedNodeMap nnm = nodes.item(i).getAttributes();
	    list.add(getAttributes(nnm));
	}

	return list;
    }

    private Map getAttributes(NamedNodeMap nnm) {

	int attrNum = nnm.getLength();

	Map<String, String[]> map = new HashMap<String, String[]>();
	for (int i = attrNum - 1; i >= 0; i--) {
	    map.put(nnm.item(i).getNodeName(), genList(nnm.item(i).getNodeValue()));
	}

	return map;
    }

    private String[] genList(String s) {

	List list = new ArrayList();
	/*Pattern p = Pattern.compile("\\s*,\\s*");
	String[] arr = p.split(s);*/
	String[] arr = s.split("\\s*,\\s*");

	return arr;
    }

    public String[] getAttributeValue(String targetName, String attributeName)
      throws ParserConfigurationException, SAXException, IOException {

	Map map = getTargetDefinition(targetName);

	return map.get(attributeName) == null ? new String[]{} : (String[]) map.get(attributeName);
    }

    
    public static void removeDuplicateWithOrder(ArrayList arlList) {
	Set set = new HashSet();
	List newList = new ArrayList();
	for (Iterator iter = arlList.iterator(); iter.hasNext();) {
	    Object element = iter.next();
	    if (set.add(element))
		newList.add(element);
	}
	arlList.clear();
	arlList.addAll(newList);
    }

    public static void main(String[] args)
      throws ParserConfigurationException, SAXException, IOException {

	ParseXMLUtils p = new ParseXMLUtils();
	
	Map map = new ParseXMLUtils().getTargetDefinition("tweaks");
	Set keySet = map.keySet();
	for (Object key : keySet) {
	    String[] s = (String[]) map.get(key);
	    System.out.print((String) key + ":");
	    for (int i = 0; i < s.length; i++) {
		System.out.print(s[i] + ", ");
	    }
	}
	 

//	 System.out.println(new ParseXMLUtils().getAllTargetDefinitions());

//	 System.out.println(new ParseXMLUtils().genList("a,b,c")[0]);

	/*String[] s = p.getAttributeValue("tweaks", "depends");
	for (int i = 0; i < s.length; i++) {
	    System.out.print(s[i] + ", ");
	}*/
    }
}
