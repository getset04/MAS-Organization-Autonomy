package aspects;

import tools.*;
import jade.core.Agent;
import jade.gui.GuiAgent;
import jade.domain.df;
import jade.domain.ams;
import jade.tools.rma.rma;
import jade.tools.ToolAgent;
import jade.core.behaviours.Behaviour;

public aspect DeregisterAspect {
	
	pointcut MyDiregisterPoint(Agent a) : call(public * jade.domain.DFService.deregister(..))  && this(a) && (!within(GuiAgent) && (!within(df)) && (!within(ams)) && (!within(rma)) && (!within(ToolAgent)) && (!within(jade.tools.*)) && (!within(jade.tools.sniffer.*)) && (!within(jade.tools.sniffer.Sniffer.*)) && (!within(jade.tools.ToolNotifier.*)));
	
	before(Agent a) : MyDiregisterPoint(a){
		
		String name = a.getAID().getLocalName();
		String agentClass = a.getClass().getName();
		String eventType = "deregister";
		long time = (long) System.currentTimeMillis();
		
		MyData data = new MyData();
		data.setAgentName(name);
		data.setAgentClass(agentClass);
		data.setEventType(eventType);
		data.setTime(time);

		ClientUDP.send(data);

	}
	
	pointcut MyDiregisterPoint2(Behaviour behaviour) : call(public * jade.domain.DFService.deregister(..))  && this(behaviour) && (!within(GuiAgent) && (!within(df)) && (!within(ams)) && (!within(rma)) && (!within(ToolAgent)) && (!within(jade.tools.*)) && (!within(jade.tools.sniffer.*)) && (!within(jade.tools.sniffer.Sniffer.*)) && (!within(jade.tools.ToolNotifier.*)));
	
	before(Behaviour behaviour) : MyDiregisterPoint2(behaviour){
		
		Agent a = behaviour.getAgent();
		String name = a.getAID().getLocalName();
		String agentClass = a.getClass().getName();
		String eventType = "deregister";
		long time = (long) System.currentTimeMillis();
		
		MyData data = new MyData();
		data.setAgentName(name);
		data.setAgentClass(agentClass);
		data.setEventType(eventType);
		data.setTime(time);

		ClientUDP.send(data);

	}
}
