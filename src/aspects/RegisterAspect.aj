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

public aspect RegisterAspect {
	
	pointcut MyRegisterPoint(Agent a,DFAgentDescription dfd) : call(public * jade.domain.DFService.register(Agent,DFAgentDescription)) && args(a,dfd) && this(Agent) && (!within(GuiAgent)&& (!within(df)) && (!within(ams)) && (!within(rma)) && (!within(ToolAgent)) && (!within(jade.tools.*)) && (!within(jade.tools.sniffer.*)) && (!within(jade.tools.sniffer.Sniffer.*)) && (!within(jade.tools.ToolNotifier.*)));
	
	before(Agent a,DFAgentDescription dfd) : MyRegisterPoint(a,dfd){
		
		String name = a.getAID().getLocalName();
		String agentClass = a.getClass().getName();
		String eventType = "register";
		long time = (long) System.currentTimeMillis();
		Iterator it = dfd.getAllServices();
		String s = "";
		if (it.hasNext()) {
			s = ((ServiceDescription) it.next()).getType();
		}
		while (it.hasNext()) {
			String tmp = ((ServiceDescription) it.next()).getType();
			s = s + " " + tmp;
		}
		String[] allServices = s.split(" ");
		
		MyData data = new MyData();
		data.setAgentName(name);
		data.setAgentClass(agentClass);
		data.setEventType(eventType);
		data.setTime(time);
		data.setAllServices(allServices);
		ClientUDP.send(data);
		
	}
	
	pointcut MyRegisterPoint2(Behaviour behaviour,DFAgentDescription dfd) : call(public * jade.domain.DFService.register(..,DFAgentDescription)) && args(..,dfd) && this(behaviour) && (!within(GuiAgent)&& (!within(df)) && (!within(ams)) && (!within(rma)) && (!within(ToolAgent)) && (!within(jade.tools.*)) && (!within(jade.tools.sniffer.*)) && (!within(jade.tools.sniffer.Sniffer.*)) && (!within(jade.tools.ToolNotifier.*)));
	
	before(Behaviour behaviour,DFAgentDescription dfd) : MyRegisterPoint2(behaviour,dfd){
		
		Agent a = behaviour.getAgent();
		String name = a.getAID().getLocalName();
		String agentClass = a.getClass().getName();
		String eventType = "register";
		long time = (long) System.currentTimeMillis();
		Iterator it = dfd.getAllServices();
		String s = "";
		if (it.hasNext()) {
			s = ((ServiceDescription) it.next()).getType();
		}
		while (it.hasNext()) {
			String tmp = ((ServiceDescription) it.next()).getType();
			s = s + " " + tmp;
		}
		String[] allServices = s.split(" ");
		
		MyData data = new MyData();
		data.setAgentName(name);
		data.setAgentClass(agentClass);
		data.setEventType(eventType);
		data.setTime(time);
		data.setAllServices(allServices);
		
		ClientUDP.send(data);
		
	}
}
