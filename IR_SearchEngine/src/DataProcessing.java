import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
public class DataProcessing {

final static String document_directory = "/Users/Admin/Documents/workspace/IR_SearchEngine/src/text/";
static int docid = 0;
static Map<Integer, String> doc_map = new TreeMap<Integer, String>();

public static String get_document_directory()
{
	return document_directory;
}

public static String get_filename(int d)
{
	return (doc_map.get(d));
}

static void processDirectory() throws FileNotFoundException
{

	File dir = new File(document_directory);
	  File[] directoryListing = dir.listFiles();
	  InvertedIndex.set_number_of_doc(directoryListing.length);
	  System.out.println("number of documents in "+ directoryListing.length);
	  if (directoryListing != null) {
	    for (File child : directoryListing) {
	      // Do something with child
	    	doc_map.put(docid, child.getName());
	    	System.out.println(child.getName());
	    	indexing_single_file(document_directory, child.getName());
	    docid++;
	    }
	  } else {
	    System.out.println("Kuch nhi mila");
	  }
	  InvertedIndex.print_inverted_index();
}

static void indexing_single_file(String directory_path, String FileName) throws FileNotFoundException
{
	
	String doc_string="";
	 File file = new File(directory_path+FileName);
	    StringBuilder fileContents = new StringBuilder((int)file.length());
	    Scanner scanner = new Scanner(file);
	    String lineSeparator = System.getProperty("line.separator");

	    try {
	        while(scanner.hasNextLine()) {
	            fileContents.append(scanner.nextLine() + lineSeparator);
	        }
	        doc_string = fileContents.toString();
	    } finally {
	        scanner.close();
	    }
	System.out.println(doc_string);
	tokenize(doc_string);
}

static void tokenize(String s)
{
	Map<String, Integer> tf = new HashMap<String, Integer>();
	s = s.replaceAll("[^a-zA-Z]", " ");
	s= s.toLowerCase();
	System.out.println(" doc to string "+ s);
	PythonCaller.setToken(s);
	PythonCaller.caller();
	s = PythonCaller.getStemmed_token();
	s = s.replaceAll("\\[", "").replaceAll("\\]","");
	s = s.replaceAll("[',]", "");
	System.out.println("Processed text -> "+s);
	String token ; 
	StringTokenizer multiTokenizer = new StringTokenizer(s, " ");
	while (multiTokenizer.hasMoreTokens())
	{
		token = multiTokenizer.nextToken();
		token = token.toLowerCase();
		
	
	    System.out.println(token);
	 
	    if (tf.containsKey(token) == true)
	    {
	    	tf.put(token, tf.get(token)+1);
	    }
	    else
	    	tf.put(token,1);
	}
	
	InvertedIndex.add(tf, docid);
		
}

	
}
