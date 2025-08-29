package ExampleWithOrganization;

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

public class DepartmentHead extends Agent {
	AgentTag tag;
	boolean [][] emploi;
	String group;
	LinkedList<MyEvent> eventList;
	LinkedList<String> teacherList; 
	LinkedList<String> servicesList;
	LinkedList<LinkedList<String>> allAgentsWithServices;
	
	public void setup(){
		Object[] obj = getArguments();
		if(obj!=null){
			group = (String) obj[0];
			eventList = (LinkedList<MyEvent>) obj[1];
		}
		emploi = new boolean[8][5];
		tag = new AgentTag(this);
		MainFrame.addDepartmentHead(this);
		teacherList = new LinkedList<String>();
		servicesList = new LinkedList<String>();
		allAgentsWithServices = new LinkedList<LinkedList<String>>();
		
		addBehaviour(new HeadWaitingMsg());
		addBehaviour(new Perception(this, 3000));
	}
	
	AgentTag getAgentTag(){
		return tag;
	}
	
	boolean [][] getAgentEmploi(){
		return emploi;
	}
	public String getGroup() {
		return group;
	}
	
	void sleepRandomTime(){
		/* sleep a random time, between 0 to 3 sec*/
		try{
			Thread.sleep((long)(Math.random()*3000));
		}catch(Exception e){
			
		}
	}
	
	void refreshServicesInDF(LinkedList<String> list){
		try{
			DFService.deregister(this);
		}catch(Exception e){
			
		}
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(this.getAID());
		for (int i = 0; i < list.size(); i++) {
			ServiceDescription sd = new ServiceDescription();
			sd.setType(list.get(i));
			sd.setName(this.getLocalName());
			dfd.addServices(sd);
		}
		try {
			DFService.register(this, dfd);
		} catch (FIPAException e) {
			e.printStackTrace();
		}
	}
	
	void refreshServicesList(LinkedList<String> list){
		boolean flag = false;
		for (int i = 0; i < list.size(); i++) {
			String service = list.get(i);
			if(!servicesList.contains(service)){
				servicesList.add(service);
				flag = true;
				allAgentsWithServices.add(new LinkedList<String>());
			}
		}
		if(flag){
			refreshServicesInDF(servicesList);
			tag.setHeadOfferedServices(servicesList);
		}
	}
	
	void refreshTeacherList(String teacher){
		if(!teacherList.contains(teacher)){
			teacherList.add(teacher);
		}
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
	
	LinkedList<String> getCorrespondingAgentList(int indexe){
		return allAgentsWithServices.get(indexe);
	}
	
	boolean isAvailableService(String service){
		return servicesList.contains(service);
	}
	
	void setInAllAgentsWithServices(String agent,LinkedList<String> agentServicesList){
		for (int i = 0; i < agentServicesList.size(); i++) {
			String agentService = agentServicesList.get(i);
			if(isAvailableService(agentService)){
				int indexe = servicesList.indexOf(agentService);
				getCorrespondingAgentList(indexe).add(agent);
			}
		}
	}
	
	boolean resolutionWithInnerStrategy(MyEvent event, boolean force){
		LinkedList<MyProposition> allAgentPropositions = new LinkedList<MyProposition>();
		LinkedList<MyProposition> agentPropositions = new LinkedList<MyProposition>();
		LinkedList<MyProposition> freeAgentPropositions = new LinkedList<MyProposition>();
		int indexe = servicesList.indexOf(event.getService());
		LinkedList<String> list = getCorrespondingAgentList(indexe);
		for (int i = 0; i < list.size(); i++) {
			String teacher = list.get(i);
			ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
			cfp.addReceiver(new AID(teacher,AID.ISLOCALNAME));
			try {
				cfp.setContentObject(event);
			} catch (IOException e) {e.printStackTrace();}
			send(cfp);
			ACLMessage reponse = blockingReceive();
			try{
			if(reponse.getPerformative()==ACLMessage.PROPOSE){
				MyProposition proposition = (MyProposition) reponse.getContentObject();
				agentPropositions.add(proposition);
				if(proposition.getDistance()==0){
					freeAgentPropositions.add(proposition);
				}
				allAgentPropositions.add(proposition);
			}
			if(reponse.getPerformative()==ACLMessage.REFUSE){
				MyProposition proposition = (MyProposition) reponse.getContentObject();
				allAgentPropositions.add(proposition);
			}
			} catch (UnreadableException e) { e.printStackTrace(); }
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
		
		if(force){
			
			sortPropositions(allAgentPropositions);
			
			for (int i = 0; i < allAgentPropositions.size(); i++) {
				MyProposition proposition = allAgentPropositions.get(i);
				MyEvent newEvent = transformEvent(event, proposition);
				ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
				request.addReceiver(new AID(proposition.getAgent(),AID.ISLOCALNAME));
				try {
					request.setContentObject(newEvent);
				} catch (IOException e) {e.printStackTrace();}
				send(request);
				ACLMessage reponse = blockingReceive();
				if(reponse.getPerformative()==ACLMessage.INFORM){
					return true;
				}
			}
		}
		
		return false;
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
	
	boolean resolutionWithOuterStrategy(MyEvent event){
		LinkedList<String> otherHeadAgents = getAllAgentWithService(event);
		for (int i = 0; i < otherHeadAgents.size(); i++) {
			ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
			try {
				request.setContentObject(event);
			} catch (IOException e) { e.printStackTrace(); }
			String name = otherHeadAgents.get(i);
			request.addReceiver(new AID(name,AID.ISLOCALNAME));
			send(request);
			ACLMessage answer = blockingReceive();
			if(answer.getPerformative()==ACLMessage.INFORM)
				return true;
		}
		return false;
	}
	
	MyEvent transformEvent(MyEvent event,MyProposition proposition){
		String group = event.getGroup();
		String service = event.getService();
		int value = ((event.getLine()*5)+event.getColumn()) + proposition.getDistance();
		int line = value  / 5;
		int column = value % 5;
		MyEvent newEvent = new MyEvent(line, column, group, service);
		return newEvent;
	}
	
	class Perception extends TickerBehaviour{
		public Perception(Agent arg0, long arg1) {
			super(arg0, arg1);
		}
		@Override
		protected void onTick() {
			sleepRandomTime();
			if(eventList.size() > 0){
				MyEvent event = eventList.getFirst();
				eventList.remove(event);
				if(servicesList.contains(event.getService())){
					addBehaviour(new HeadInnerStrategy(event));
				}
				else{
					addBehaviour(new HeadOuterStrategy(event));
				}
			}
		}
		
	}
	
	class HeadInnerStrategy extends OneShotBehaviour{
		MyEvent event;
		
		public HeadInnerStrategy(MyEvent event){
			this.event = event;
		}
		@Override
		public void action() {
			boolean flag = resolutionWithInnerStrategy(event,true);
			if(!flag){
				eventList.add(event);
			}
		}
		
	}
	
	
	class HeadOuterStrategy extends OneShotBehaviour{
		MyEvent event;
		public HeadOuterStrategy(MyEvent event){
			this.event = event;
		}
		
		@Override
		public void action() {
			boolean flag = resolutionWithOuterStrategy(event);
			if(!flag){
				eventList.add(event);
			}
		}
		
	}
	class HeadWaitingMsg extends CyclicBehaviour{
		@Override
		public void action() {
			ACLMessage msg = receive();
			if(msg!=null){
				if(msg.getPerformative()==ACLMessage.INFORM){
					try {
						LinkedList<String> list = (LinkedList<String>) msg.getContentObject();
						if(list!=null){
							refreshTeacherList(msg.getSender().getLocalName());
							refreshServicesList(list);
							setInAllAgentsWithServices(msg.getSender().getLocalName(), list);
						}
					} catch (UnreadableException e) { e.printStackTrace(); }
				}
				if(msg.getPerformative()==ACLMessage.REQUEST){
					MyEvent event;
					try {
						event = (MyEvent) msg.getContentObject();
						boolean flag = resolutionWithInnerStrategy(event, false);
						if(flag){
							ACLMessage answer = new ACLMessage(ACLMessage.INFORM);
							answer.addReceiver(msg.getSender());
							send(answer);
						}
						else{
							ACLMessage answer = new ACLMessage(ACLMessage.FAILURE);
							answer.addReceiver(msg.getSender());
							send(answer);
						}
					} catch (UnreadableException e) {e.printStackTrace();}
				}
			}
			block();
		}
		
	}
}