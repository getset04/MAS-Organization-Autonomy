package aspects;

import tools.ClientUDP;
import tools.MyData;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;

public aspect DoneAspect {
	
	pointcut MyDonePoint(Behaviour behaviour) : execution(* Behaviour.done()) && this(behaviour) && (!within(jade.proto.*)) && (!within(jade.proto.*.*)) && (!within(jade.core.*)) && (!within(jade.core.*.*)) && (!within(jade.domain.*)) && (!within(jade.domain.*.*)) && (!within(jade.gui.*)) && (!within(jade.tools.*)) && (!within(jade.tools.sniffer.*)) && (!within(jade.tools.sniffer.Sniffer.*)) && (!within(jade.tools.ToolNotifier.*));
	
    after(Behaviour behaviour)  returning(boolean b) : MyDonePoint(behaviour){
    	if(b==true){
    		
        	Agent a = behaviour.getAgent();
        	String name = a.getAID().getLocalName();
    		String agentClass = a.getClass().getName();
    		String eventType = "done";
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
}
