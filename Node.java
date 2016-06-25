import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

public class Node implements Runnable {
	int nodeid;
	String[] hosts;
	int[] ports;
	ServerSocket serverSocket;
	static int label;
	int tokens;
	String configfile;
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			serverSocket = new ServerSocket(ports[nodeid]);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		while(true){
			try {
				Socket clientsocket = serverSocket.accept();
				new Thread(new Worker(clientsocket, hosts, ports, label,tokens,configfile)).start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public Node(int nodeindex, String[] host, int[] port, int t,String config){
		nodeid = nodeindex;
		hosts = host;
		ports = port;
		Random random = new Random();
		label = random.nextInt(50);
		tokens=t;
		configfile = config;
		if(tokens==0){
			String filename = configfile+"-cxh141330-"+nodeid+".out";
			File file = new File(filename);
			BufferedWriter w;
			try {
				w = new BufferedWriter(new FileWriter(file));
				w.write("Net ID: cxh141330\n");
				w.write("Node ID: " + nodeid+"\n");
				w.write("Listening on " + hosts[nodeid] + ":" + ports[nodeid] + "\n");
				w.write("Random number: " + label + "\n");
				w.write("All tokens received\n");
				w.flush();
				w.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String filename = args[1];
		File file = new File(filename);
		String gfile = file.getName();
		String[] config1 = gfile.split("\\.(?=[^\\.]+$)");
		String config = config1[0];
		BufferedReader br = null;
		String line = null;
		int no_of_tokens = 0;
		int tokenvalue=0;
		try {
			br = new BufferedReader(new FileReader(file));
			while(br.ready()){
				String temp = br.readLine();
				temp=temp.trim();
				if(temp.charAt(0)!= '#'){
					line=temp;
					break;
				}
			}
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String[] temp = line.split(" ");
		int num = Integer.parseInt(temp[0]);

		Parse parse = new Parse(num);
		parse.parsefile(file); 

		String[] hosts=parse.hosts;
		int[] ports = parse.ports;
		ArrayList<ArrayList<Integer>> path = new ArrayList<ArrayList<Integer>>();
		for(int i=0; i<parse.path.size();i++){
			ArrayList<Integer> temppath = new ArrayList<Integer>();
			for(int j=0; j<parse.path.get(i).size();j++){
				temppath.add(parse.path.get(i).get(j));
			}
			path.add(temppath);
		}

		int nodeindex=Integer.parseInt(args[0]);
		
		for(int i=0; i<path.size();i++){
			if(path.get(i).get(0)==nodeindex){
				no_of_tokens++;
			}
		}
		new Thread(new Node(nodeindex, hosts,ports,no_of_tokens,config)).start();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for(int i=0; i<path.size();i++){
			if(path.get(i).get(0)==nodeindex){
				int nextnodeindex=1;
				tokenvalue++;
				Token token = new Token(path.get(i), 0);
				String filename1 = config+"-cxh141330-"+token.path.get(0)+".out";
				File file1 = new File(filename1);
				BufferedWriter w;
				ArrayList<String> data = new ArrayList<String>();
				String tempdata="";
				for(int j=0;j<(token.path.size());j++){
					if(j < (token.path.size()-1)){
						tempdata = tempdata + token.path.get(j) + " -> ";
					}
					else{
						tempdata = tempdata + token.path.get(j) + "";
					}
				}
				data.add(tempdata);
				if(tokenvalue==1){
					try {
						w = new BufferedWriter(new FileWriter(file1));
						w.write("Net ID: cxh141330\n");
						w.write("Node ID: " + token.path.get(0)+"\n");
						w.write("Listening on " + hosts[token.path.get(0)] + ":" + ports[token.path.get(0)] + "\n");
						w.write("Random number: " + label + "\n");
						w.write("Emitting token " + tokenvalue+ " with path " + data.get(0) + "\n");
						w.flush();
						w.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else{
					if(file1.exists()){
						try {
							w = new BufferedWriter(new FileWriter(file1,true));
							w.write("Emitting token " + tokenvalue+ " with path " + data.get(0) + "\n");
							w.flush();
							w.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				SocketClient s = new SocketClient(hosts,ports, nextnodeindex, token, label, tokenvalue);
				s.start();
			}
		}
	}
}
