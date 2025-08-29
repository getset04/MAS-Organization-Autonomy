package tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

public class Utils {

	public static void openFileAppendMode(String string, String file){
		try{
			FileWriter fw = new FileWriter(new File(file),true);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter pw = new PrintWriter(bw);
			pw.println(string);
			pw.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void openFileWriteMode(String string, String file){
		try{
			FileWriter fw = new FileWriter(new File(file));
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter pw = new PrintWriter(bw);
			pw.println(string);
			pw.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static String fromMyDataToString(MyData data){
		String event = data.getEventType();
		String string = new String();	
		
		if(event.equals("main")){
			string = "Event type : "+event+" ; Time : "+data.getTime();
			return string;
		}
		if(event.equals("addBehaviour") || event.equals("action") || event.equals("done") || event.equals("onTick")){
			string = "Event type : "+event+" ; Time : "+data.getTime()+" ; Agent name : "+data.getAgentName()+" ; Agent class : "+data.getAgentClass()+" ; Behaviour name : "+data.getBehaviourName()+" ; Behaviour class : "+data.getBehaviourClass();
			return string;
		}
		if(event.equals("setup") || event.equals("deregister")){
			string = "Event type : "+event+" ; Time : "+data.getTime()+" ; Agent name : "+data.getAgentName()+" ; Agent class : "+data.getAgentClass();
			return string;
		}
		if(event.equals("search")){
			string = "Event type : "+event+" ; Time : "+data.getTime()+" ; Agent name : "+data.getAgentName()+" ; Agent class : "+data.getAgentClass()+" ; Searched service : "+data.getSearchedService();
			return string;
		}
		if(event.equals("register")){
			string = "Event type : "+event+" ; Time : "+data.getTime()+" ; Agent name : "+data.getAgentName()+" ; Agent class : "+data.getAgentClass()+" ; Registerd services : "+data.getAllServicesStrings();
			return string;
		}
		if(event.equals("receive") || event.equals("send")){
			string = "Event type : "+event+" ; Time : "+data.getTime()+" ; Agent name : "+data.getAgentName()+" ; Agent class : "+data.getAgentClass()+" ; Message type : "+data.getMessageType()+" ; Sender : "+data.getAgentSender()+" ; Receiver : "+data.getAgentReceiver();
			return string;
		}
		return string;
	}
	
}