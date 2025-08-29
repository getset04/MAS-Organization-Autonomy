package aspects;

import tools.ClientUDP;
import tools.MyData;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;

public aspect ActionAspect {

	pointcut MyActionPoint(Behaviour behaviour) : execution(* Behaviour.action()) && this(behaviour) && (!within(jade.proto.*)) && (!within(jade.proto.*.*)) && (!within(jade.core.*)) && (!within(jade.core.*.*)) && (!within(jade.domain.*)) && (!within(jade.domain.*.*)) && (!within(jade.gui.*) && (!within(jade.tools.*)) && (!within(jade.tools.sniffer.*)) && (!within(jade.tools.sniffer.Sniffer.*)) && (!within(jade.tools.ToolNotifier.*)) );
	
    before(Behaviour behaviour) : MyActionPoint(behaviour){
    	
    	Agent a = behaviour.getAgent();
    	String name = a.getAID().getLocalName();
		String agentClass = a.getClass().getName();
		String eventType = "action";
		long time = (long) System.currentTimeMillis();
		String behaviourName = behaviour.getBehaviourName();
		String behaviourClass = behaviour.getClass().getName();
		
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