public class Database{
	public String dbString;
	public String dbName;

	public Database(String dbName, String dbString){
		this.dbString=dbString;
		this.dbName=dbName;
	}

	public int indexOf(Query q, int p){
			/*for (int i=p;i+q.length()<dbString.length();){
				for (int j=q.length()-1;;--j){
					if (q.charAt(j)!=dbString.charAt(i+j)){
						if (!q.contains(dbString.charAt(i+j))) i+=j+1;
						else i+=j-q.position[j];
						break;
					}else if (j==0) return i;
				}
			}
		return -1;*/
		return indexOf(dbString, q.toString(), p);
	}

	public static int indexOf(String haystack, String needle, int p) {
		if (needle.length() == 0) {
			return p;
		}
		int charTable[] = makeCharTable(needle);
		int offsetTable[] = makeOffsetTable(needle);
		for (int i = p+needle.length() - 1, j; i < haystack.length();) {
			for (j = needle.length() - 1; needle.charAt(j) == haystack.charAt(i); --i, --j) {
				if (j == 0) {
					return i;
				}
			}
			// i += needle.length - j; // For naive method
			i += Math.max(offsetTable[needle.length() - 1 - j], charTable[haystack.charAt(i)]);
		}
		return -1;
	}

	/**
	 * Makes the jump table based on the mismatched character information.
	 */
	private static int[] makeCharTable(String needle) {
		final int ALPHABET_SIZE = 256;
		int[] table = new int[ALPHABET_SIZE];
		for (int i = 0; i < table.length; ++i) {
			table[i] = needle.length();
		}
		for (int i = 0; i < needle.length() - 1; ++i) {
			table[needle.charAt(i)] = needle.length() - 1 - i;
		}
		return table;
	}

	/**
	 * Makes the jump table based on the scan offset which mismatch occurs.
	 */
	private static int[] makeOffsetTable(String needle) {
		int[] table = new int[needle.length()];
		int lastPrefixPosition = needle.length();
		for (int i = needle.length() - 1; i >= 0; --i) {
			if (isPrefix(needle, i + 1)) {
				lastPrefixPosition = i + 1;
			}
			table[needle.length() - 1 - i] = lastPrefixPosition - i + needle.length() - 1;
		}
		for (int i = 0; i < needle.length() - 1; ++i) {
			int slen = suffixLength(needle, i);
			table[slen] = needle.length() - 1 - i + slen;
		}
		return table;
	}

	/**
	 * Is needle[p:end] a prefix of needle?
	 */
	private static boolean isPrefix(String needle, int p) {
		for (int i = p, j = 0; i < needle.length(); ++i, ++j) {
			if (needle.charAt(i) != needle.charAt(j)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns the maximum length of the substring ends at p and is a suffix.
	 */
	private static int suffixLength(String needle, int p) {
		int len = 0;
		for (int i = p, j = needle.length() - 1;
			 i >= 0 && needle.charAt(i) == needle.charAt(j); --i, --j) {
			len += 1;
		}
		return len;
	}
}