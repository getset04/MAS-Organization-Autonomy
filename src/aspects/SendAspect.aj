package aspects;

import java.util.Iterator;

import tools.*;
import jade.core.Agent;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.gui.GuiAgent;
import jade.domain.df;
import jade.domain.ams;
import jade.tools.rma.rma;
import jade.tools.ToolAgent;
import jade.core.behaviours.Behaviour;

public aspect SendAspect {

	pointcut MySendPoint(Agent a,ACLMessage msg) : call(public * jade.core.Agent.send(ACLMessage)) && args(msg) && this(a) && (!within(jade.gui.*)&& (!within(jade.domain.*)) && (!within(jade.domain.*.*)) && (!within(jade.tools.*)) && (!within(jade.tools.*.*))) && (!within(jade.proto.*)) && (!within(jade.proto.*.*)) && (!within(jade.core.*)) && (!within(jade.core.*.*)) && (!within(jade.tools.*)) && (!within(jade.tools.sniffer.*)) && (!within(jade.tools.sniffer.Sniffer.*)) && (!within(jade.tools.ToolNotifier.*));
	
	before(Agent a,ACLMessage msg) : MySendPoint(a,msg){
		
		String name = a.getAID().getLocalName();
		String agentClass = a.getClass().getName();
		long time = (long) System.currentTimeMillis();
		String eventType = "send";
		String messageType = msg.getPerformative(msg.getPerformative());
		String agentSender = name;
		Iterator iter = msg.getAllReceiver();
		String agentReceiver = "";
		if(iter.hasNext()){
			agentReceiver = ((AID) iter.next()).getLocalName();
		}

		MyData data = new MyData();
		data.setAgentName(name);
		data.setAgentClass(agentClass);
		data.setTime(time);
		data.setEventType(eventType);
		data.setMessageType(messageType);
		data.setAgentSender(agentSender);
		data.setAgentReceiver(agentReceiver);

		ClientUDP.send(data);
		
	}
	pointcut MySendPoint2(Behaviour behaviour,ACLMessage msg) : call(public * jade.core.Agent.send(ACLMessage)) && args(msg) && this(behaviour) && (!within(jade.gui.*)&& (!within(jade.domain.*)) && (!within(jade.domain.*.*)) && (!within(jade.tools.*)) && (!within(jade.tools.*.*))) && (!within(jade.proto.*)) && (!within(jade.proto.*.*)) && (!within(jade.core.*)) && (!within(jade.core.*.*)) && (!within(jade.tools.*)) && (!within(jade.tools.sniffer.*)) && (!within(jade.tools.sniffer.Sniffer.*)) && (!within(jade.tools.ToolNotifier.*));
	
	before(Behaviour behaviour,ACLMessage msg) : MySendPoint2(behaviour,msg){
		
		Agent a = behaviour.getAgent();
		String name = a.getAID().getLocalName();
		String agentClass = a.getClass().getName();
		long time = (long) System.currentTimeMillis();
		String eventType = "send";
		String messageType = msg.getPerformative(msg.getPerformative());
		String agentSender = name;
		Iterator iter = msg.getAllReceiver();
		String agentReceiver = "";
		if(iter.hasNext()){
			agentReceiver = ((AID) iter.next()).getLocalName();
		}

		MyData data = new MyData();
		data.setAgentName(name);
		data.setAgentClass(agentClass);
		data.setTime(time);
		data.setEventType(eventType);
		data.setMessageType(messageType);
		data.setAgentSender(agentSender);
		data.setAgentReceiver(agentReceiver);

		ClientUDP.send(data);
		
	}
		
}
