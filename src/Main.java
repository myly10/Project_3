import java.io.*;
import java.util.ArrayDeque;
import java.util.Scanner;

public class Main{
	static final int MAX_THREADS=Runtime.getRuntime().availableProcessors();

	public static void main(String[] args) throws IOException{
		File in=new File(args[0]), out=new File(args[2]), qf=new File(args[1]);
		Database[] db=dbReader(new BufferedInputStream(new FileInputStream(in)));
	}

	public static Database[] dbReader(BufferedInputStream r) throws IOException{
		Scanner scn=new Scanner(r);
		ArrayDeque<Database> db=new ArrayDeque<Database>();
		String t;
		while (true){
			scn.nextByte();
			t=scn.nextLine();
			if (t.equals("EOF")) break;
			db.push(new Database(t, scn.nextLine()));
		}
		r.close();
		return (Database[])(db.toArray());
	}
}