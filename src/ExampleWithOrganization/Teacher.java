package ExampleWithOrganization;

import java.io.IOException;
import java.util.LinkedList;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class Teacher extends Agent {

	AgentTag tag;
	boolean [][] emploi;
	int nombreMaxDesHeures;
	LinkedList<String> groupList;
	LinkedList<String> servicesList;
	public void setup(){
		Object[] obj = getArguments();
		if(obj!=null){
			groupList = (LinkedList<String>) obj[0];
			servicesList = (LinkedList<String>) obj[1];
		}
		
		emploi = new boolean[8][5];
		tag = new AgentTag(this);
		MainFrame.addTeacher(this);
		nombreMaxDesHeures = 9;
		
		addBehaviour(new TeacherSignWithDepartmentHead());
		addBehaviour(new TeacherWaitingMsg());
	}
	
	AgentTag getAgentTag(){
		return tag;
	}
	
	boolean [][] getAgentEmploi(){
		return emploi;
	}

	public LinkedList<String> getGroupList() {
		return groupList;
	}

	public LinkedList<String> getServicesList() {
		return servicesList;
	}
	
	boolean isCompatibleSession(MyEvent event){
		int i = event.getLine();
		int j = event.getColumn();
		return !emploi[i][j];
	}
	
	boolean isCompatibleService(MyEvent event){
		for (int i = 0; i < servicesList.size(); i++) {
			if(servicesList.get(i).equals(event.getService()))
				return true;
		}
		return false;
	}
	
	boolean isNotExhausted(){
		return numberOfFilledSessions() < nombreMaxDesHeures;
	}
	
	void affectNewSession(MyEvent event){
		emploi[event.getLine()][event.getColumn()] = true;
		tag.refreshTable();
	}
	
	int getDistance(MyEvent event){
		int line = event.getLine();
		int column = event.getColumn();
		if(!emploi[line][column])
			return 0;
		else{
			int after = getAfter(line, column);
			int before = getBefore(line,column);
			if(((Math.abs(before) <= after) && before!=0) || after==0)
				return before;
			if(((Math.abs(before) >= after) && after!=0) || before==0)
				return after;
		}
		return 0;
	}
	
	int getAfter(int line,int column){
		int after= 0;
		for (int i = line; i < 8; i++) {
			for (int j = 0; j < 5; j++) {
				if(j>column || i>line){
					after++;
					if(!emploi[i][j]) return after;
				}
			}
		}
		return 0;
	}
	
	int getBefore(int line,int column){
		int before = 0;
		for (int i = line; i >= 0; i--) {
			for (int j = 4; j >= 0; j--) {
				if(j<column || i<line){
					before--;
					if(!emploi[i][j]) return before;
				}
			}
		}
		return 0;
	}
	
	int numberOfFilledSessions(){
		int counter = 0;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 5; j++) {
				if(emploi[i][j]) counter++;
			}
		}
		return counter;
	}
	
	boolean isMyGroupHead(DepartmentHead head){
		String group = head.getGroup();
		return groupList.contains(group);
	}
	
	class TeacherSignWithDepartmentHead extends OneShotBehaviour{

		@Override
		public void action() {
			ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
			try {
				msg.setContentObject(servicesList);
			} catch (IOException e) { e.printStackTrace(); }
			for (int i = 0; i < MainFrame.headList.size(); i++) {
				DepartmentHead head = MainFrame.headList.get(i);
				if(isMyGroupHead(head)){
					msg.addReceiver(head.getAID());
				}
			}
			send(msg);
		}
		
	}
	
	class TeacherWaitingMsg extends CyclicBehaviour{

		@Override
		public void action() {
			ACLMessage msg = receive();
			if(msg!=null){
				try {
					if(msg.getPerformative()==ACLMessage.CFP){
						MyEvent event = (MyEvent) msg.getContentObject();
						if(isCompatibleService(event) && isNotExhausted()){
							MyProposition proposition = new MyProposition(numberOfFilledSessions(), getDistance(event), getLocalName());
							ACLMessage response = new ACLMessage(ACLMessage.PROPOSE);
							try {
								response.setContentObject(proposition);
							} catch (IOException e) {
								e.printStackTrace();
							}
							response.addReceiver(msg.getSender());
							send(response);
						}
						else{
							ACLMessage response = new ACLMessage(ACLMessage.REFUSE);
							response.addReceiver(msg.getSender());
							MyProposition proposition = new MyProposition(numberOfFilledSessions(), getDistance(event), getLocalName());
							try {
								response.setContentObject(proposition);
							} catch (IOException e) {
								e.printStackTrace();
							}
							send(response);
						} 
					}
					if(msg.getPerformative()==ACLMessage.REQUEST){
						MyEvent event = (MyEvent) msg.getContentObject();
						if(isCompatibleSession(event) && isCompatibleService(event)){
							affectNewSession(event);
							ACLMessage response = new ACLMessage(ACLMessage.INFORM);
							response.addReceiver(msg.getSender());
							send(response);
						}
						else{
							ACLMessage response = new ACLMessage(ACLMessage.FAILURE);
							response.addReceiver(msg.getSender());
							send(response);
						}
					}
					if(msg.getPerformative()==ACLMessage.ACCEPT_PROPOSAL){
						MyEvent event = (MyEvent) msg.getContentObject();
						if(isCompatibleSession(event) && isCompatibleService(event) && isNotExhausted()){
							affectNewSession(event);
							ACLMessage response = new ACLMessage(ACLMessage.INFORM);
							response.addReceiver(msg.getSender());
							send(response);
						}
						else{
							ACLMessage response = new ACLMessage(ACLMessage.FAILURE);
							response.addReceiver(msg.getSender());
							send(response);
						}
					}
				} catch (UnreadableException e) {
					e.printStackTrace();
				}
			}
			block();
		}
		
	}
	
}
