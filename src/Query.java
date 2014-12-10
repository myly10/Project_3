import java.util.Arrays;

public class Query{
	public int[] position;
	private String qString;
	private boolean[] contain=new boolean[128];

	public Query(String q){
		qString=q;
		position=new int[q.length()];
		Arrays.fill(position, -1);
		Arrays.fill(contain, false);
		for (int i=0;i!=position.length;++i){
			contain[qString.charAt(i)]=true;
			for (int j=i-1;j>=0;--j)
				if (qString.charAt(j)==qString.charAt(i)){
					position[i]=j;
					break;
				}
		}
	}

	public String toString(){return qString;}
	public int length(){return qString.length();}
	public char charAt(int i){return qString.charAt(i);}
	public boolean contains(char t){return contain[(int)t];}
}
