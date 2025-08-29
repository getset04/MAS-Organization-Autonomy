package tools;
import java.io.Serializable;


public class MyData implements Serializable{
	
	String agentName;
	String agentClass;
	String agentSender;
	String agentReceiver;
	String messageType;
	String behaviourName ;
	String behaviourClass ;
	String eventType;
	long time;
	String[] allServices;
	String searchedService;
	public MyData(){
		agentName = new String();
		agentClass = new String();
		agentSender = new String();
		agentReceiver = new String();
		messageType = new String();
		behaviourName = new String();
		behaviourClass = new String();
		eventType = new String();
		searchedService = new String();
		time = 0;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public String getAgentClass() {
		return agentClass;
	}

	public void setAgentClass(String agentClass) {
		this.agentClass = agentClass;
	}

	public String getAgentSender() {
		return agentSender;
	}

	public void setAgentSender(String agentSender) {
		this.agentSender = agentSender;
	}

	public String getAgentReceiver() {
		return agentReceiver;
	}

	public void setAgentReceiver(String agentReceiver) {
		this.agentReceiver = agentReceiver;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public String getBehaviourName() {
		return behaviourName;
	}

	public void setBehaviourName(String behaviourName) {
		this.behaviourName = behaviourName;
	}

	public String getBehaviourClass() {
		return behaviourClass;
	}

	public void setBehaviourClass(String behaviourClass) {
		this.behaviourClass = behaviourClass;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	
	
	public String[] getAllServices() {
		return allServices;
	}

	public void setAllServices(String[] allServices) {
		this.allServices = allServices;
	}

	public String getAllServicesStrings(){
		String tmp = "";
		for (int i = 0; i < allServices.length; i++) {
			tmp = tmp + allServices[i] + " , "; 
		}
		return tmp;
	}
	
	public String getSearchedService() {
		return searchedService;
	}

	public void setSearchedService(String searchedService) {
		this.searchedService = searchedService;
	}

	@Override
	public String toString() {
		return "MyData [agentName=" + agentName + ", agentClass=" + agentClass
				+ ", agentSender=" + agentSender + ", agentReceiver="
				+ agentReceiver + ", messageType=" + messageType
				+ ", behaviourName="
				+ behaviourName + ", behaviourClass=" + behaviourClass
				+ ", eventType=" + eventType + ", time=" + time + "]";
	}
	
}
