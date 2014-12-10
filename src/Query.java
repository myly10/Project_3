public class Query{
	private int[] position;
	private String qString;

	public Query(String q){
		qString=q;
		position=new int[q.length()];
		for (int i=0;i!=position.length;++i)
			for (int j=i-1;j>=0;--j)
				if (qString.charAt(j)==qString.charAt(i))
					position[i]=j;
		//TODO check correctness
	}
}
