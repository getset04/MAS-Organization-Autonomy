package aspects;

import tools.*;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.gui.GuiAgent;
import jade.domain.df;
import jade.domain.ams;
import jade.tools.rma.rma;
import jade.tools.ToolAgent;

public aspect BehaviourAspect {
	
	pointcut MyBehaviourPoint(Agent a,Behaviour b) : call(public * jade.core.Agent.addBehaviour(Behaviour)) && args(b) && this(a) && (!within(GuiAgent) && (!within(df)) && (!within(ams)) && (!within(rma)) && (!within(ToolAgent))&& (!within(jade.tools.*)) && (!within(jade.tools.sniffer.*)) && (!within(jade.tools.sniffer.Sniffer.*)) && (!within(jade.tools.ToolNotifier.*)));
	
	before(Agent a,Behaviour b) : MyBehaviourPoint(a,b){
		
		String name = a.getAID().getLocalName();
		String agentClass = a.getClass().getName();
		String eventType = "addBehaviour";
		long time = (long) System.currentTimeMillis();
		String behaviourName = b.getBehaviourName();
		String behaviourClass = b.getClass().getName();
		
		MyData data = new MyData();
		data.setAgentName(name);
		data.setAgentClass(agentClass);
		data.setEventType(eventType);
		data.setTime(time);
		data.setBehaviourName(behaviourName);
		data.setBehaviourClass(behaviourClass);
		
		ClientUDP.send(data);
		
	}
	
}