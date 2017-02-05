

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Search
 */
@WebServlet("/Search")
public class Search extends HttpServlet {
	private static final long serialVersionUID = 1L;
	List<String> edit ;
    String spell_correction="" ;
    
    public static boolean ASC = true;
    public static boolean DESC = false;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Search() {
        super();
        // TODO Auto-generated constructor stub
    }

    Map<String, Double> cache = new HashMap<String, Double>();
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected  void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		long startTime = System.currentTimeMillis();
		

		List<Integer>doc_list = new ArrayList<Integer>();
	    edit = new ArrayList<String>();
		response.getWriter().append("Served at: ").append(request.getContextPath());
		Map<String, Double> tf_query = new HashMap<String, Double>();
		Map<String, Double> df = new HashMap<String, Double>();
		Map<String, Double> doc_rank  = new HashMap<String, Double>();
		List<String>doc_rank_list ;
		String query= request.getParameter("searchstr"); 
		System.out.println("Query is "+ query);
		String token;
		query = query.replaceAll("[^a-zA-Z]", " ");
		query = query.toLowerCase();
		System.out.println(" doc to string "+ query);
 //Check for spelling error
		query = query.trim();
		SpellingChecker.token = query;
		SpellingChecker.caller();
		String query_corrected = SpellingChecker.get_correct_spelling_token();
		query_corrected = query_corrected.replaceAll("\\[", "").replaceAll("\\]","");
		query_corrected = query_corrected.replaceAll("[',]", "");
		System.out.println("Correct query is "+query_corrected );
        query_corrected = query_corrected.trim();
        System.out.println(query.equals(query_corrected));
		if (!query.equals(query_corrected))
		{

			request.setAttribute("query_error", query_corrected); //categorylist is an arraylist      contains object of class category  
			getServletConfig().getServletContext().getRequestDispatcher("/Home.jsp").forward(request,response);
			return;
		}
		
		//Stem it 
	
	
		PythonCaller.setToken(query);
		PythonCaller.caller();
		query = PythonCaller.getStemmed_token();
		
       
		String exmpl=query;
		String[] parts = exmpl.split("\\s+");   
		Arrays.sort(parts);  
		StringBuilder sb = new StringBuilder();  
		for(String s:parts){  
		   sb.append(s);  
		   sb.append(" ");  
		}  

		String sorted = sb.toString().trim();
		sorted = sorted.replaceAll(" ","_");
		doc_rank_list = LRUCache.get(sorted);
		if (doc_rank_list==null)
	
		{
		doc_rank_list	= new ArrayList<String>();
	
	   StringTokenizer multiTokenizer = new StringTokenizer(query, " \t\n\r\f,.:;?![]'");
	   //Creating TF
	   while (multiTokenizer.hasMoreTokens())
		{
			token = multiTokenizer.nextToken();
			
			
			  if (tf_query.containsKey(token) == true)
			    {
			    	tf_query.put(token, tf_query.get(token)+1);
			    }
			    else
			    	{
			    	tf_query.put(token , (double) 1);
			    	}
		}
		
        printmap(tf_query);
		for (Map.Entry<String, Double> entry : tf_query.entrySet())
		{
			String query_term = entry.getKey();
			Double val = entry.getValue();
			val = 1 + Math.log10(val);
			tf_query.put(query_term, val); //Creating tf wt
			List<PostingDataStructure> pds;
			
			System.out.println(query_term +" "+InvertedIndex.is_term_present(query_term));
			
			if (InvertedIndex.is_term_present(query_term) == true)
			{
				 pds = InvertedIndex.getPosting(query_term);
				 for(PostingDataStructure pd: pds )
				 {
					 if(doc_list.contains(pd.getDocid()) == false)
						 	doc_list.add(pd.getDocid());
				 }
				 
				
				 double idf = Math.log10( InvertedIndex.get_number_of_doc()/pds.size());
				 df.put(query_term,  idf);		//Computing idf 
				 
			}
			else
			{
			    	edit.add(query_term);
			    	
			}
		}
		System.out.println("creating tf wt ");
		printmap(tf_query);
		System.out.println("df is ");
		printmap(df);
		
		for(String x :edit)
		{
			tf_query.remove(x);
		}
		System.out.println("size of tf_query and df"+ tf_query.size()+" "+ df.size());
		double s = 0;
		//Calculating tf.idf of query terms
		for (Map.Entry<String, Double> entry : df.entrySet())
		{
			df.put(entry.getKey(), entry.getValue()*tf_query.get(entry.getKey()));
			s+= Math.pow(df.get(entry.getKey()),2.0);
			
		}
		
		s = Math.sqrt(s);	
		
		for (Map.Entry<String, Double> entry : df.entrySet())
		{
			df.put(entry.getKey(), entry.getValue()/(double) s);
		}
		
		for (Map.Entry<String, Double> entry : df.entrySet())
		{
			
			df.put(entry.getKey(), entry.getValue() * tf_query.get(entry.getKey()));
		}
		
		System.out.println(doc_list.size() +" length");
for(int x :doc_list)
{
String directory_path= DataProcessing.get_document_directory();
String FileName = DataProcessing.get_filename(x); 


try
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
	String st  = doc_string;
	Map<String, Double> tf = new HashMap<String, Double>();
	st = st.replaceAll("[^a-zA-Z]", " ");
	st = st.toLowerCase();
	//System.out.println(" doc to string "+ s);
	PythonCaller.setToken(st);
	PythonCaller.caller();
	st = PythonCaller.getStemmed_token();
	st = st.replaceAll("\\[", "").replaceAll("\\]","");
	st = st.replaceAll("[',]", "");
	System.out.println("Processed text -> "+st);
	//st.has
	multiTokenizer = new StringTokenizer(st, " ");
	while (multiTokenizer.hasMoreTokens())
	{
		token = multiTokenizer.nextToken();
		
	    if (tf.containsKey(token) == true)
	    {
	    	tf.put(token, tf.get(token)+1);
	    }
	    else
	    	tf.put(token,1.0);
	}
	s = 0;
	System.out.println("--- TF -- ");
	printmap(tf);
	for (Map.Entry<String, Double> entry : tf.entrySet())
	{
		String query_term = entry.getKey();
		double val = entry.getValue();
		val = 1 + Math.log10(val);
		tf.put(query_term, val); //Creating tf wt
		s+=(val*val);
	}
	System.out.println("--- TF  Wt-- ");
	printmap(tf);
	s = Math.sqrt(s);
	System.out.println("Normalized deno "+s);
	double doc_score = 0;
	System.out.println("Multiplication ");
	for (Map.Entry<String, Double> entry : tf.entrySet())
	{
		String term = entry.getKey();
		double val = entry.getValue();
		val = val/(double)s;
		tf.put(term, val); //Creating tf wt
		if (df.containsKey(term)==true)
			doc_score += (val * df.get(term));
		
	}
	System.out.println("Document score is "+doc_score );
	doc_rank.put(FileName, doc_score);
	
} catch(Exception e)
{}
}	

doc_rank = sortByComparator(doc_rank, DESC);
System.out.println("doc rank size "+doc_rank.size());
printmap(doc_rank);

for (Map.Entry<String, Double> entry : doc_rank.entrySet())
{
doc_rank_list .add(entry.getKey());
}
LRUCache.set(sorted,doc_rank_list);
}
		
request.setAttribute("list", doc_rank_list); //categorylist is an arraylist      contains object of class category  

long endTime   = System.currentTimeMillis();
long totalTime = (endTime - startTime);
System.out.println(totalTime);
request.setAttribute("exect", String.valueOf(totalTime/(double)1000.0));
getServletConfig().getServletContext().getRequestDispatcher("/Home.jsp").forward(request,response);


}

	 private void printmap(Map<String, Double> tf_query) {
		// TODO Auto-generated method stub
		 for (Map.Entry<String, Double> entry : tf_query.entrySet())
			{
			 System.out.println(entry.getKey()+" "+entry.getValue());
			}
	}


	private static Map<String, Double> sortByComparator(Map<String, Double> doc_rank, final boolean order)
	    {

	        List<Entry<String, Double>> list = new LinkedList<Entry<String, Double>>(doc_rank.entrySet());

	        // Sorting the list based on values
	        Collections.sort(list, new Comparator<Entry<String, Double>>()
	        {
	            public int compare(Entry<String, Double> o1,
	                    Entry<String, Double> o2)
	            {
	                if (order)
	                {
	                    return o1.getValue().compareTo(o2.getValue());
	                }
	                else
	                {
	                    return o2.getValue().compareTo(o1.getValue());

	                }
	            }
	        });

	        // Maintaining insertion order with the help of LinkedList
	        Map<String, Double> sortedMap = new LinkedHashMap<String, Double>();
	        for (Entry<String, Double> entry : list)
	        {
	            sortedMap.put(entry.getKey(), entry.getValue());
	        }

	        return sortedMap;
	    }
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
