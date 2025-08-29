package aspects;

import tools.*;
import jade.core.Agent;
import jade.gui.GuiAgent;
import jade.domain.df;
import jade.domain.ams;
import jade.tools.rma.rma;
import jade.tools.ToolAgent;

public aspect SetupAspect {

	pointcut MySetupPoint(Agent a) : execution(* setup()) && this(a) && (!within(GuiAgent)&& (!within(df)) && (!within(ams)) && (!within(rma)) && (!within(ToolAgent)) && (!within(jade.tools.*)) && (!within(jade.tools.sniffer.*)) && (!within(jade.tools.sniffer.Sniffer.*)) && (!within(jade.tools.ToolNotifier.*)));
	
	before(Agent a) : MySetupPoint(a){

		String name = a.getAID().getLocalName();
		String agentClass = a.getClass().getName();
		String eventType = "setup";
		long time = (long) System.currentTimeMillis();
		
		MyData data = new MyData();
		data.setAgentName(name);
		data.setAgentClass(agentClass);
		data.setEventType(eventType);
		data.setTime(time);

		ClientUDP.send(data);
		
	}
	
}
