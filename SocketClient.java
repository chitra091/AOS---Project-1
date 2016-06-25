import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;


public class SocketClient extends Thread {
	Socket client;
	String[] host;
	int[] port;
	int nextnodeindex;
	ArrayList<Integer> path;
	Token token;
	int label;
	int tokenval;

	public SocketClient(String[] hosts, int[] ports, int nextindex, Token t, int l, int tval) {
		// TODO Auto-generated constructor stub
		host = hosts;
		port = ports;
		nextnodeindex = nextindex;
		token = t;
		label = l;
		path = token.path;
		tokenval = tval;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		synchronized(this){
			try {
				client= new Socket(host[path.get(nextnodeindex)],port[path.get(nextnodeindex)]);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			BufferedWriter bw = null;
			String data = "";
			int length=0;
			try {
				bw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
				for(int i=0; i<path.size();i++){
					data = data + path.get(i) + " ";
				}
				length=path.size();
				nextnodeindex++;
				token.sum = token.sum + label;
				bw.write(length + " " + data + token.sum + " " + nextnodeindex + " " + tokenval + "\n");
				bw.flush();
				bw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				client.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
