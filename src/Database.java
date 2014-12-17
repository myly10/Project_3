import java.util.Arrays;

public class Database{
	private static final int ALPHABET_SIZE=128;
	public String dbString;
	public String dbName;

	public Database(String dbName, String dbString){
		this.dbString=dbString;
		this.dbName=dbName;
	}

	public int indexOf(String q, int p){
		int[] charSkip=new int[ALPHABET_SIZE];
		Arrays.fill(charSkip,q.length());
		for (int i=0;i!=q.length()-1;++i)
			charSkip[q.charAt(i)]=q.length()-1-i;

		int[] suffixSkip=new int[q.length()];
		int lastPrefixPos=q.length();
		for (int i=q.length()-1;i>=0;--i){
			if (isPrefix(q, i+1))
				lastPrefixPos=i+1;
			suffixSkip[q.length()-1-i]=lastPrefixPos-i+q.length()-1;
		}
		for (int i=0;i<q.length()-1;++i){
			int slen=suffixLength(q, i);
			suffixSkip[slen]=q.length()-1-i+slen;
		}

		if (q.length()==0)
			return p;
		for (int i=p+q.length()-1, j;i<dbString.length();){
			for (j=q.length()-1;q.charAt(j)==dbString.charAt(i);--i, --j)
				if (j==0) return i;
			i+=Math.max(suffixSkip[q.length()-1-j], charSkip[dbString.charAt(i)]);
		}
		return -1;
	}

	private static boolean isPrefix(String qs, int p){
		for (int i=p, j=0;i<qs.length();++i, ++j)
			if (qs.charAt(i)!=qs.charAt(j))
				return false;
		return true;
	}

	private static int suffixLength(String qs, int p){
		int len=0;
		for (int i=p, j=qs.length()-1;i>=0 && qs.charAt(i)==qs.charAt(j);--i, --j)
			++len;
		return len;
	}
}