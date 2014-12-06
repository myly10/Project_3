import java.io.BufferedInputStream;
import java.io.File;
import java.util.ArrayDeque;
import java.util.Scanner;

public class Main{
	static final int MAX_THREADS=Runtime.getRuntime().availableProcessors();

	public static void main(String[] args){
		File in=new File(args[0]), out=new File(args[2]), qf=new File(args[1]);
	}

	public static Database[] dbReader(BufferedInputStream r){
		Scanner scn=new Scanner(r);
		ArrayDeque<Database> db=new ArrayDeque<Database>();
		String t;
		while (true){
			scn.nextByte();
			t=scn.nextLine();
			if (t.equals("EOF")) break;
			db.push(new Database(t, scn.nextLine()));
		}
		return null;
	}
}
