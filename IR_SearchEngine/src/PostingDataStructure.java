
public class PostingDataStructure {

	private int docid, term_frequency ;

	public PostingDataStructure(int docid,int term_frequency )
	{
		this.docid = docid;
		this.term_frequency = term_frequency;
	}
	
	public int getDocid() {
		return docid;
	}


	public int getTerm_frequency() {
		return term_frequency;
	}

	
	
}
