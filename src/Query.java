import java.util.Arrays;

public class Query{
	private static final int ALPHABET_SIZE=128;

	public int[] charSkip;
	public int[] suffixSkip;
	private String qString;

	public Query(String q){
		qString=q;

		charSkip=new int[ALPHABET_SIZE];
		Arrays.fill(charSkip, q.length());
		for (int i=0;i!=q.length()-1;++i)
			charSkip[q.charAt(i)]=q.length()-1-i;

		suffixSkip=new int[q.length()];
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

	public String toString(){return qString;}
	public int length(){return qString.length();}
	public char charAt(int i){return qString.charAt(i);}
}
