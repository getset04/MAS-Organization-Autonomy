package aspects;

import java.util.Iterator;

import tools.*;
import jade.core.Agent;
import jade.gui.GuiAgent;
import jade.domain.df;
import jade.domain.ams;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.tools.rma.rma;
import jade.tools.ToolAgent;
import jade.core.behaviours.Behaviour;

public aspect SearchAspect {
	
	pointcut MySearchPoint(Agent a,DFAgentDescription dfd) : call(public * jade.domain.DFService.search(Agent,DFAgentDescription)) && args(a,dfd) && this(Agent) && (!within(GuiAgent)&& (!within(df)) && (!within(ams)) && (!within(rma)) && (!within(ToolAgent)) && (!within(jade.tools.*)) && (!within(jade.tools.sniffer.*)) && (!within(jade.tools.sniffer.Sniffer.*)) && (!within(jade.tools.ToolNotifier.*)));
	
	before(Agent a,DFAgentDescription dfd) : MySearchPoint(a,dfd){
		
		String name = a.getAID().getLocalName();
		String agentClass = a.getClass().getName();
		String eventType = "search";
		String searchedService = "";
		Iterator it = dfd.getAllServices();
		if (it.hasNext()) {
			searchedService = ((ServiceDescription) it.next()).getType();
		}
		long time = (long) System.currentTimeMillis();
		MyData data = new MyData();
		data.setAgentName(name);
		data.setAgentClass(agentClass);
		data.setEventType(eventType);
		data.setSearchedService(searchedService);
		data.setTime(time);

		ClientUDP.send(data);
		
	}
	
	pointcut MySearchPoint2(Behaviour behaviour,DFAgentDescription dfd) : call(public * jade.domain.DFService.search(..,DFAgentDescription)) && args(..,dfd) && this(behaviour) && (!within(GuiAgent)&& (!within(df)) && (!within(ams)) && (!within(rma)) && (!within(ToolAgent)) && (!within(jade.tools.*)) && (!within(jade.tools.sniffer.*)) && (!within(jade.tools.sniffer.Sniffer.*)) && (!within(jade.tools.ToolNotifier.*)));

	before(Behaviour behaviour,DFAgentDescription dfd) : MySearchPoint2(behaviour,dfd){
		
		Agent a = behaviour.getAgent();
		String name = a.getAID().getLocalName();
		String agentClass = a.getClass().getName();
		String eventType = "search";
		String searchedService = "";
		Iterator it = dfd.getAllServices();
		if (it.hasNext()) {
			searchedService = ((ServiceDescription) it.next()).getType();
		}
		long time = (long) System.currentTimeMillis();
		MyData data = new MyData();
		data.setAgentName(name);
		data.setAgentClass(agentClass);
		data.setEventType(eventType);
		data.setSearchedService(searchedService);
		data.setTime(time);

		ClientUDP.send(data);
		
	}
}
