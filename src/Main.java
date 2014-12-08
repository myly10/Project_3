import java.io.*;
import java.util.Scanner;
import java.util.Vector;

public class Main{
	static final int MAX_THREADS=Runtime.getRuntime().availableProcessors();

	public static void main(String[] args) throws IOException{
		File in=new File(args[0]), out=new File(args[2]), qf=new File(args[1]);
		Database[] db=dbReader(new BufferedInputStream(new FileInputStream(in)));
		db.clone();
	}

	public static Database[] dbReader(BufferedInputStream r) throws IOException{
		Scanner scn=new Scanner(r);
		Vector<Database> db=new Vector<Database>();
		String t;
		while (true){
			t=scn.nextLine();
			if (t.equals(">EOF")) break;
			db.add(new Database(t.substring(1), scn.nextLine()));
		}
		r.close();
		Database[] dt=new Database[db.size()];
		for (int i=0;i!=db.size();++i) dt[i]=db.elementAt(i);
		return dt;
	}
}