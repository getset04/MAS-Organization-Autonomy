package ExampleWithoutOrganization;

import java.io.IOException;
import java.util.LinkedList;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class Teacher extends Agent{

	AgentTag tag;
	boolean[][] emploi;
	int nombreMaxDesHeures;
	LinkedList<String> servicesList;
	
	public void setup(){
		Object[] obj = getArguments();
		if(obj!=null)
			servicesList = (LinkedList<String>) obj[0];
		
		emploi = new boolean[8][5];
		tag = new AgentTag(this);
		MainFrame.addAgent(this);
		tag.setOfferedServices(servicesList);
		nombreMaxDesHeures = 9;
		
		addBehaviour(new RegisterServices());
		addBehaviour(new Perception(this,3000));
		addBehaviour(new WaitingMsg());
	}
	
	AgentTag getAgentTag(){
		return tag;
	}
	
	boolean [][] getAgentEmploi(){
		return emploi;
	}
	
	void sleepRandomTime(){
		/* sleep a random time, between 0 to 3 sec*/
		try{
			Thread.sleep((long)(Math.random()*3000));
		}catch(Exception e){
			
		}
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
	
	LinkedList<String> getAllAgentWithService(MyEvent event){
		String service = event.getService();
		LinkedList<String> agentList = new LinkedList<String>();
		
		DFAgentDescription dfd = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType(service);
		dfd.addServices(sd);
		try{
			DFAgentDescription[] searchResult = DFService.search(this, dfd);
			for (int i = 0; i < searchResult.length; i++) {
				String name = searchResult[i].getName().getLocalName();
				if(!name.equals(getLocalName()))
					agentList.add(name);
			}
		}catch(Exception e){
		}
		return agentList;
	}
		
	void sortPropositions(LinkedList<MyProposition> list){
		
		for (int i = 0; i < list.size(); i++) {
			int minIndex = i;
			int minVal = list.get(i).getNombreDesHeuresOccupees();
			int j = i + 1;
			while(j < list.size()){
				if(minVal > list.get(j).getNombreDesHeuresOccupees()){
					minIndex = j;
					minVal = list.get(j).getNombreDesHeuresOccupees();
				}
				j++;
			}
			MyProposition temp = list.get(i);
			list.set(i, list.get(minIndex));
			list.set(minIndex, temp);
		}
	}
	
	boolean resolutionStrategy(MyEvent event){
		LinkedList<String> possibleAgents = getAllAgentWithService(event);
		LinkedList<MyProposition> agentPropositions = new LinkedList<MyProposition>();
		LinkedList<MyProposition> freeAgentPropositions = new LinkedList<MyProposition>();
		for (int i = 0; i < possibleAgents.size(); i++) {
			String name = possibleAgents.get(i);
			ACLMessage msg = new ACLMessage(ACLMessage.CFP);
			try {
				msg.setContentObject(event);
			} catch (IOException e) {
				e.printStackTrace();
			}
			msg.addReceiver(new AID(name,AID.ISLOCALNAME));
			send(msg);
			ACLMessage reponse = blockingReceive();
			if(reponse.getPerformative()==ACLMessage.PROPOSE){
				try {
					MyProposition proposition = (MyProposition) reponse.getContentObject();
					agentPropositions.add(proposition);
					if(proposition.getDistance()==0){
						freeAgentPropositions.add(proposition);
					}
				} catch (UnreadableException e) {
					e.printStackTrace();
				}
			}
		}
		
		if(freeAgentPropositions.size() > 0){
			for (int i = 0; i < freeAgentPropositions.size(); i++) {
				MyProposition proposition = freeAgentPropositions.get(i);
				String name = proposition.getAgent();
				ACLMessage msg = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
				try {
					msg.setContentObject(event);
				} catch (IOException e) {
					e.printStackTrace();
				}
				msg.addReceiver(new AID(name,AID.ISLOCALNAME));
				send(msg);
				ACLMessage reponse = blockingReceive();
				if(reponse.getPerformative()==ACLMessage.INFORM){
					for (int j = 0; j < agentPropositions.size(); j++) {
						if(!isAlreadyAnsweredMe(freeAgentPropositions, i, agentPropositions, j)){
							String agent = agentPropositions.get(j).getAgent();
							ACLMessage extra = new ACLMessage(ACLMessage.REJECT_PROPOSAL);
							extra.addReceiver(new AID(agent,AID.ISLOCALNAME));
							send(extra);
						}
					}
					return true;
				}
			}
		}
		
		sortPropositions(agentPropositions);
		
		if(agentPropositions.size() > 0){
			for (int i = 0; i < agentPropositions.size(); i++) {
				MyProposition proposition = agentPropositions.get(i);
				String name = proposition.getAgent();
				ACLMessage msg = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
				MyEvent newEvent = transformEvent(event,proposition);
				try {
					msg.setContentObject(newEvent);
				} catch (IOException e) {
					e.printStackTrace();
				}
				msg.addReceiver(new AID(name,AID.ISLOCALNAME));
				send(msg);
				ACLMessage reponse = blockingReceive();
				if(reponse.getPerformative()==ACLMessage.INFORM){
					for (int j = i + 1; j < agentPropositions.size(); j++) {
						String agent = agentPropositions.get(j).getAgent();
						ACLMessage extra = new ACLMessage(ACLMessage.REJECT_PROPOSAL);
						extra.addReceiver(new AID(agent,AID.ISLOCALNAME));
						send(extra);
					}
					return true;
				}
			}
		}
		return false;
	}
	
	MyEvent transformEvent(MyEvent event,MyProposition proposition){
		String service = event.getService();
		int value = ((event.getLine()*5)+event.getColumn()) + proposition.getDistance();
		int line = value  / 5;
		int column = value % 5;
		MyEvent newEvent = new MyEvent(line, column, service);
		return newEvent;
	}
	
	boolean isAlreadyAnsweredMe(LinkedList<MyProposition> freeAgentPropositions,int i,LinkedList<MyProposition> agentPropositions,int j){
		MyProposition currentProp = agentPropositions.get(j);
		for (int indexe = 0; indexe <= i; indexe++) {
			MyProposition prop = freeAgentPropositions.get(indexe);
			if(prop.getAgent().equals(currentProp.getAgent()))
				return true;
		}
		return false;
	}
	
	class RegisterServices extends OneShotBehaviour{

		@Override
		public void action() {
			DFAgentDescription dfd = new DFAgentDescription();
			dfd.setName(myAgent.getAID());
			for (int i = 0; i < servicesList.size(); i++) {
				ServiceDescription sd = new ServiceDescription();
				sd.setType(servicesList.get(i));
				sd.setName(myAgent.getLocalName());
				dfd.addServices(sd);
			}
			try {
				DFService.register(myAgent, dfd);
			} catch (FIPAException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	class Perception extends TickerBehaviour{

		public Perception(Agent arg0, long arg1) {
			super(arg0, arg1);
		}

		@Override
		protected void onTick() {
			sleepRandomTime();
			if(MainFrame.eventList.size()>0){
				MyEvent event = MainFrame.eventList.getFirst();
				MainFrame.eventList.remove(event);
				if(isCompatibleSession(event) && isCompatibleService(event) && isNotExhausted()){
					// affectation simple
					addBehaviour(new Affectation(event));
				}
				else{
					// lance une strategie de resolution
					addBehaviour(new Strategy(event));
				}
			}
		}
		
	}
	
	class Affectation extends OneShotBehaviour{
		
		MyEvent event;
		
		public Affectation(MyEvent event){
			this.event = event;
		}
		
		@Override
		public void action() {
			affectNewSession(event);
		}
		
	}
	
	class Strategy extends OneShotBehaviour{
		
		MyEvent event;
		
		public Strategy(MyEvent event){
			this.event = event;
		}
		
		@Override
		public void action() {
			boolean b = resolutionStrategy(event);
			if(!b){
				MainFrame.eventList.add(event);
			}
		}
		
	}
	
	class WaitingMsg extends CyclicBehaviour{

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
