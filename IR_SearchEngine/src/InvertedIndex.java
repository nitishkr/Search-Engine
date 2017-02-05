import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class InvertedIndex {

	 
	static PostingDataStructure node;
	static List<PostingDataStructure> posting;
	static int total_number_of_documents; 
	static Map<String, List<PostingDataStructure>> inverted_index = new TreeMap<String, List<PostingDataStructure>>();
	
	static void set_number_of_doc(int docno)
	{
		total_number_of_documents = docno;
	}
	
	static int get_number_of_doc()
	{
		return total_number_of_documents;
	}
	
	static List<PostingDataStructure> getPosting(String word)
	{
		return (inverted_index.get(word));
	}
	
	static boolean is_term_present (String word)
	{
		return (inverted_index.containsKey(word));
		
	}
	
	static void print_inverted_index()
	{
		for (Map.Entry<String, List<PostingDataStructure>> entry : inverted_index.entrySet())
		{
			System.out.print(entry.getKey()+" --> ");
			posting = entry.getValue();
			
			for(PostingDataStructure object : posting) {
			    System.out.print(object.getDocid() +" | "+object.getTerm_frequency()+" -->");
			}
			System.out.println();
		}
		
	}
	
	public static   void add(Map<String, Integer> tf, int docid) {
		// TODO Auto-generated method stub
		String key;
		for (Map.Entry<String, Integer> entry : tf.entrySet())
		{
			
			node = new PostingDataStructure(docid, entry.getValue());
		    key = entry.getKey();
			if (inverted_index.containsKey(key)== true)
			{
				posting = inverted_index.get(key);
				posting.add(node);
			}
			else
			{
				posting = new  ArrayList();
				posting.add(node);
				inverted_index.put(key, posting);
			}
		   
		}
		
		//node = 
	}

}
