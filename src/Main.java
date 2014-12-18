import java.io.*;
import java.util.Scanner;
import java.util.Vector;

public class Main{
	static final boolean isMultiThreaded=true;

	static final int MAX_THREADS=isMultiThreaded?Runtime.getRuntime().availableProcessors():1;
	private static final byte A=0, T=1, G=2, C=3;

	public static void main(String[] args) throws IOException, InterruptedException{
		long time=System.currentTimeMillis();
		File in=new File(args[0]), out=new File(args[2]), qf=new File(args[1]);
		SharedObjects.db=dbReader(new BufferedInputStream(new FileInputStream(in)));
		SharedObjects.qscn=new Scanner(new BufferedInputStream(new FileInputStream(qf)));
		SharedObjects.w=new BufferedWriter(new FileWriter(out));
		System.out.println("Read database finished in "+(System.currentTimeMillis()-time)+"ms");

		Thread[] threadPool=new Thread[MAX_THREADS];
		for (int i=0;i!=MAX_THREADS;++i)
			(threadPool[i]=new ProcessQuery()).start();
		for (Thread t:threadPool){
			t.join();
		}
		SharedObjects.w.close();
		System.out.println("Total time: "+(System.currentTimeMillis()-time)+"ms. Exiting.");
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
		long time=System.currentTimeMillis(), t=time;
		synchronized (System.out){
			System.out.println("Thread "+currentThread().getId()+" started...");
		}
		String qName;
		String q;
		Scanner qscn=SharedObjects.qscn;
		Database[] db=SharedObjects.db;
		String result;
		while (true){
			synchronized (SharedObjects.qscn){
				if (!qscn.hasNextLine()) break;
				qName=qscn.nextLine();
				if (qName.equals(">EOF") || qName.equals("")) break;
				qName=qName.substring(1);
				q=qscn.nextLine();
			}
			boolean found=false;
			result=qName+"\n";
			for (Database i:db){
				int p=i.indexOf(q,0);
				if (p!=-1){
					result+="    ["+i.dbName+"] at offset "+p+"\n";
					found=true;
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
			if (System.currentTimeMillis()-t>1000){
				t=System.currentTimeMillis();
				synchronized (System.out){
					System.out.print("\rThread "+currentThread().getId()+" Time: "+(System.currentTimeMillis()-time)+"ms Processing: "+qName+"   ");
				}
			}
		}
		synchronized (System.out){
			System.out.println("Thread "+currentThread().getId()+" stopped... in "+(System.currentTimeMillis()-time)+"ms");
		}
	}
}