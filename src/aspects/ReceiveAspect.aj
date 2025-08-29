package aspects;

import tools.*;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.gui.GuiAgent;
import jade.domain.df;
import jade.domain.ams;
import jade.tools.rma.rma;
import jade.tools.ToolAgent;
import jade.core.behaviours.Behaviour;

public aspect ReceiveAspect {
	
	pointcut MyReceivePoint(Agent a) : this(a) && (call(public * jade.core.Agent.blockingReceive()) || call(public * jade.core.Agent.receive()))  && (!within(GuiAgent) && (!within(df)) && (!within(ams)) && (!within(rma)) && (!within(ToolAgent)) && (!within(jade.tools.*)) && (!within(jade.tools.sniffer.*)) && (!within(jade.tools.sniffer.Sniffer.*)) && (!within(jade.tools.ToolNotifier.*)));
	
	after(Agent a) returning(ACLMessage msg) : MyReceivePoint(a){
		if(msg!=null){
			
			String name = a.getAID().getLocalName();
			String agentClass = a.getClass().getName();
			long time = (long) System.currentTimeMillis();
			String eventType = "receive";
			String messageType = msg.getPerformative(msg.getPerformative());
			String agentSender = msg.getSender().getLocalName();
			String agentReceiver = name;
			
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

	pointcut MyReceivePoint2(Behaviour behaviour) : this(behaviour) && (call(public * jade.core.Agent.blockingReceive()) || call(public * jade.core.Agent.receive()))  && (!within(GuiAgent) && (!within(df)) && (!within(ams)) && (!within(rma)) && (!within(ToolAgent)) && (!within(jade.tools.*)) && (!within(jade.tools.sniffer.*)) && (!within(jade.tools.sniffer.Sniffer.*)) && (!within(jade.tools.ToolNotifier.*)));
	
	after(Behaviour behaviour) returning(ACLMessage msg) : MyReceivePoint2(behaviour){
		if(msg!=null){
			
			Agent a = behaviour.getAgent();
			String name = a.getAID().getLocalName();
			String agentClass = a.getClass().getName();
			long time = (long) System.currentTimeMillis();
			String eventType = "receive";
			String messageType = msg.getPerformative(msg.getPerformative());
			String agentSender = msg.getSender().getLocalName();
			String agentReceiver = name;
			
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
}
