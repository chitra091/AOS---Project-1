import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class Parse {
	int[] nodeids;
	String[] hosts;
	int[] ports; 
	String[] line;
	ArrayList<ArrayList<Integer>> path = new ArrayList<ArrayList<Integer>>();
	public Parse(int num) {
		// TODO Auto-generated constructor stub
		nodeids = new int[num];
		hosts = new String[num];
		ports = new int[num];
		line = new String[50];
	}

	public void parsefile(File file){
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			int i=0;
			while(br.ready()){
				String temp = br.readLine();
				temp=temp.trim();
				if(!temp.isEmpty()){
					if((temp.charAt(0)!= '#') && (temp.charAt(0)!=' ') && (!(temp.startsWith("  ")) && (!(temp.startsWith("\t"))))){
						line[i]=temp;
						i++;
					}
				}
			}

			int nodeindex = 0;
			int noofnodes=0;

			for(int j=0; line[j]!=null; j++){
				if(j==0){
					String[] nodedata = line[j].split("\\s+");
					noofnodes = Integer.parseInt(nodedata[0]);
				}
				else if(j>0 && j<= noofnodes){
					String[] data = line[j].split("\\s+");
					for(int k=0; k < data.length; k++){
						if(k==0){
							nodeids[nodeindex] = Integer.parseInt(data[k]);
						}
						if(k==1){
							hosts[nodeindex] = data[k];
						}
						if(k==2){
							ports[nodeindex] = Integer.parseInt(data[k]);
						}
					}
					nodeindex++;
				}
				else{
					ArrayList<Integer> temppath = new ArrayList<Integer>();
					String[] line1=line[j].split("\\s+");
					for(int k=0; k<line1.length;k++){
						if(line1[k].charAt(0)=='#'){
							break;
						}
						else if((line1[k]!= " ") && (line1[k]!= "\t")){
							String hold = ""+ line1[k];
							temppath.add(Integer.parseInt(hold));
						}
					}
					String hold1 = ""+ line1[0];
					temppath.add(Integer.parseInt(hold1));
					path.add(temppath);
				}
			}
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
