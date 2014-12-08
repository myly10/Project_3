import java.io.*;
import java.util.Scanner;
import java.util.Vector;

public class Main{
	static final int MAX_THREADS=Runtime.getRuntime().availableProcessors();

	public static void main(String[] args) throws IOException{
		long time=System.currentTimeMillis();
		File in=new File(args[0]), out=new File(args[2]), qf=new File(args[1]);
		SharedObjects.db=dbReader(new BufferedInputStream(new FileInputStream(in)));
		SharedObjects.qscn=new Scanner(new BufferedInputStream(new FileInputStream(qf)));
		SharedObjects.w=new BufferedWriter(new FileWriter(out));

		Thread[] threadPool=new Thread[MAX_THREADS];
		for (int i=0;i!=MAX_THREADS;++i)
			(threadPool[i]=new ProcessQuery()).start();
		for (Thread t:threadPool){
			try{
				t.join();
			}catch (InterruptedException e){
				e.printStackTrace();
			}
		}
		SharedObjects.w.close();
		System.out.println(System.currentTimeMillis()-time);
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

class ProcessQuery extends Thread{
	@Override
	public void run(){
		String qName, qString;
		Scanner qscn=SharedObjects.qscn;
		Database[] db=SharedObjects.db;
		while (true){
			synchronized (SharedObjects.qscn){
				if (!qscn.hasNextLine()) return;
				qName=qscn.nextLine();
				if (qName.equals(">EOF") || qName.equals("")) return;
				qName=qName.substring(1);
				qString=qscn.nextLine();
			}
			boolean found=false;
			String result=qName+"\n";
			for (Database i:db){
				int p=0;
				while ((p=i.dbString.indexOf(qString, p))!=-1){
					result+="    ["+i.dbName+"] at offset "+p+"\n";
					found=true;
					p+=qString.length();
				}
			}
			synchronized (SharedObjects.w){
				BufferedWriter w=SharedObjects.w;
				try{
					if (!found) result+="    NOT FOUND\n";
					w.write(result+"\n");
				}catch (IOException e){
					e.printStackTrace();
				}
			}
		}
	}
}