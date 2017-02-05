import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GetPostings {
	
	public static List<String> retrieve_documents(Map<String, Double> tf_query)
	{
		List<String> doc_list = new ArrayList<String>();
		for (Map.Entry<String, Double> entry : tf_query.entrySet())
		{
			String query_term = entry.getKey();
	
		}
		
		return null;
	}
		
	
}
