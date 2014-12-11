public class Database{
	public String dbString;
	public String dbName;

	public Database(String dbName, String dbString){
		this.dbString=dbString;
		this.dbName=dbName;
	}

	public int indexOf(Query q, int p){
		if (q.length()==0)
			return p;
		for (int i=p+q.length()-1, j;i<dbString.length();){
			for (j=q.length()-1;q.charAt(j)==dbString.charAt(i);--i, --j)
				if (j==0) return i;
			i+=Math.max(q.suffixSkip[q.length()-1-j], q.charSkip[dbString.charAt(i)]);
		}
		return -1;
	}
}