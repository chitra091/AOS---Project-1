import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;


public class Worker implements Runnable {
	Socket clientsocket;
	String[] host;
	int[] port;
	int label;
	int tokens;
	String configfile;
	public Worker(Socket s, String[] hosts, int[] ports, int l, int t, String config) {
		// TODO Auto-generated constructor stub
		clientsocket = s;
		host = hosts;
		port = ports;
		label=l;
		tokens=t;
		configfile=config;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		synchronized(this){
			BufferedReader br = null;
			String[] temp = null;
			int index =0;
			int pathindex = 0;
			ArrayList<Integer> path = null;
			int sum = 0;
			Token token = new Token();
			int tokenval=0;
			try {
				br = new BufferedReader(new InputStreamReader(clientsocket.getInputStream()));
				String line = br.readLine();
				temp = line.split(" ");
				if(temp!=null){
					pathindex = Integer.parseInt(temp[0]);
					path = new ArrayList<Integer>(pathindex);
					for(int i=0; i<(pathindex);i++){
						path.add(Integer.parseInt(temp[i+1]));
					}
					sum = Integer.parseInt(temp[pathindex+1]);
					index = Integer.parseInt(temp[pathindex+2]);
					tokenval=Integer.parseInt(temp[pathindex+3]);
					token.path = path;
					token.sum = sum;

					if(index<(path.size()-1)){
						Socket client = new Socket(host[path.get(index)],port[path.get(index)]);
						BufferedWriter bw = null;
						try {
							bw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
							String data="";
							int length=0;
							for(int i=0; i<path.size();i++){
								data = data + path.get(i) + " ";
							}
							index++;
							length=path.size();
							token.sum = token.sum + label;
							bw.write(length + " " + data + token.sum + " " + index + " " + tokenval +"\n");
							bw.flush();
							bw.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						client.close();
					}

					else if(index==(path.size()-1)){
						Socket client = new Socket(host[path.get(index)],port[path.get(index)]);
						BufferedWriter bw = null;
						try {
							bw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
							String data="";
							int length=0;
							for(int i=0; i<path.size();i++){
								data = data + path.get(i) + " ";
							}
							index++;
							length=path.size();
							token.sum = token.sum + label;
							bw.write(length + " " + data + token.sum + " " + index + " " + tokenval + "\n");
							bw.flush();
							bw.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						client.close();
					}
					else{
						String filename = configfile+"-cxh141330-"+token.path.get(0)+".out";
						File file = new File(filename);
						if(file.exists()){
							int recvcount1=0;
							BufferedReader b = new BufferedReader(new FileReader(file));
							synchronized(file){
								while(b.ready()){
									String line1 = b.readLine();
									if(line1.startsWith("Received")){
										recvcount1++;
									}
								}
								if(recvcount1>0){
									BufferedWriter w= new BufferedWriter(new FileWriter(file,true));
									synchronized(w){
										w.write("Received token " + tokenval+ "\t Token sum = " + token.sum+"\n");
										if(recvcount1==(tokens-1)){
											w.write("All tokens received\n");
										}
									}
									w.flush();
									w.close();
								}
								else{
									BufferedWriter w= new BufferedWriter(new FileWriter(file,true));
									synchronized(w){
										w.write("Received token " + tokenval+ "\t Token sum = " + token.sum+"\n");
										if(tokens==1){
											w.write("All tokens received\n");
										}
									}
									w.flush();
									w.close();
								}
								b.close();
							}
						}

					}	
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
