package tools;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.LinkedList;

import org.apache.commons.lang.SerializationUtils;

import statique.Obj2;

/**
 *
 * @author Home
 */
public class ServerUDP {
	LinkedList<MyData> list;
    DatagramSocket datamSocket;
    String file;
    Thread t;
    public ServerUDP() {
    	list = new LinkedList<MyData>();
    	file = "rapport.txt";
        try{
            InetSocketAddress sa = new InetSocketAddress("localhost",54321);
            datamSocket = new DatagramSocket(sa);
        }catch(Exception e){
            e.printStackTrace();
        }
        waitForClients();
    }
    
    public void waitForClients(){
            t = new Thread(){
            	public void run(){
            		byte[] buffer = new byte[1000];
                    try{
                        while(true){
                            DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                            datamSocket.receive(request);
                            byte[] dataBytes = request.getData();
                            MyData data = (MyData) SerializationUtils.deserialize(dataBytes);
                            list.add(data);
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                    }
            	}
            };
            t.start();
    }
    
    public void stopWaitingForClients(){
    	t.stop();
    }
    
    public void UpdatTimeList(){
    	// remember to call it before generating the report
    	long startTime = 0;
    	if(list.size()>0){
    		startTime = list.get(0).getTime();
    	}
    	for (int i = 0; i < list.size(); i++) {
    		list.get(i).setTime(list.get(i).getTime() - startTime);
		}
    }
    
    public void generateTextualReport(){
    	if(list.size()>0){
    		MyData data = list.get(0);
    		String string = Utils.fromMyDataToString(data);
    		Utils.openFileWriteMode(string, file);
    	}
    	for (int i = 1; i < list.size(); i++) {
    		MyData data = list.get(i);
    		String string = Utils.fromMyDataToString(data);
    		Utils.openFileAppendMode(string, file);
		}
    }
    
    /***************************** from here metrics start ********************************/
    // richesse de service , OK
    
    LinkedList<ServiceWealth> richesseDeService;
    float nombreTotalDeService;
    
    MyData getLastData(String agent,int indexe){
    	for (int i = indexe; i >= 0; i--) {
    		MyData data = list.get(i);
    		if(data.getAgentName().equals(agent) && data.getEventType().equals("register"))
    			return data;
		}
    	return null;
    }
    
    public void generateRichesseDeService(){
    	richesseDeService = new LinkedList<ServiceWealth>();
    	LinkedList<String> listOfAgents = new LinkedList<String>(); 
    	nombreTotalDeService = 0;
    	for (int i = 0; i < list.size(); i++) {
    		MyData data = list.get(i);
    		String event = data.getEventType();
    		String agent = data.getAgentName();
    		long time = data.getTime();
    		if(event.equals("register")){
    			if(!listOfAgents.contains(agent)){
    				listOfAgents.add(agent);
    				richesseDeService.add(new ServiceWealth(agent, 0, 1, time-15));
    				float val = 1;
    				if(nombreTotalDeService>0)
    					val = nombreTotalDeService;
    				updateServiceWealth(agent,listOfAgents,val,time-15);
    			}
    			float number = data.getAllServices().length;
    			nombreTotalDeService = nombreTotalDeService + number; 
    			richesseDeService.add(new ServiceWealth(agent, number, nombreTotalDeService, time));
    			updateServiceWealth(agent,listOfAgents,nombreTotalDeService,time);
    		}
    		if(event.equals("deregister")){
    			if(!listOfAgents.contains(agent)){
    				listOfAgents.add(agent);
    				richesseDeService.add(new ServiceWealth(agent, 0, 1, time-15));
    				float val = 1;
    				if(nombreTotalDeService>0)
    					val = nombreTotalDeService;
    				updateServiceWealth(agent,listOfAgents,val,time-15);
    			}
    			MyData lastData = getLastData(agent, i-1);
    			float number = 0;
    			if(lastData!=null)
    				number = lastData.getAllServices().length;
    			nombreTotalDeService = nombreTotalDeService - number; 
    			richesseDeService.add(new ServiceWealth(agent, 0, nombreTotalDeService, time));
    			updateServiceWealth(agent,listOfAgents,nombreTotalDeService,time);
    		}
		}
    	displayWealthService();
    }

    ServiceWealth getLastServiceWealth(String agent){
    	for (int i = richesseDeService.size() - 1; i >= 0; i--) {
			ServiceWealth element = richesseDeService.get(i);
			if(element.getAgent().equals(agent)) return element;
		}
    	return null;
    }
    
	private void updateServiceWealth(String caller,LinkedList<String> listOfAgents, float nombreTotalDeService2,long time) {
		for (int i = 0; i < listOfAgents.size(); i++) {
			String agent = listOfAgents.get(i);
			if(!caller.equals(agent)){
				ServiceWealth lastServiceWealth = getLastServiceWealth(agent);
				if(lastServiceWealth!=null){
					ServiceWealth update = new ServiceWealth(agent, lastServiceWealth.getNumberOfServices(), nombreTotalDeService2, time);
					richesseDeService.add(update);
				}
			}
		}
	}

	void displayWealthService(){
		System.out.println("richesse de service");
		for (int i = 0; i < richesseDeService.size(); i++) {
			System.out.println(richesseDeService.get(i));
		}
	}
	
	// frequence de recherche par duree , OK
	
    LinkedList<FrequencyOfServiceSearchTime> frequenceDeRechercheDuree;
    
    FrequencyOfServiceSearchTime getLastSearchFrequency(String agent){
    	for (int i = frequenceDeRechercheDuree.size() - 1; i >= 0 ; i--) {
    		FrequencyOfServiceSearchTime searchFrequency = frequenceDeRechercheDuree.get(i);
    		if(searchFrequency.getAgent().equals(agent)){
    			return searchFrequency;
    		}
		}
    	return null;
    }
    
    public void generateFrequenceDeRechercheDuree(){
    	frequenceDeRechercheDuree = new LinkedList<FrequencyOfServiceSearchTime>();
    	
    	for (int i = 0; i < list.size(); i++) {
    		MyData data = list.get(i);
    		String event = data.getEventType();
    		if(event.equals("search")){
    			String agent = data.getAgentName();
    			long time = data.getTime();
    			FrequencyOfServiceSearchTime searchFrequency = getLastSearchFrequency(agent);
    			if(searchFrequency!=null){
    				FrequencyOfServiceSearchTime element = new FrequencyOfServiceSearchTime(agent, 1 + searchFrequency.getSearchNumber(), time);
    				frequenceDeRechercheDuree.add(element);
    			}
    			else{
    				FrequencyOfServiceSearchTime element = new FrequencyOfServiceSearchTime(agent, 1, time);
    				frequenceDeRechercheDuree.add(element);
    			}
    		}
		}
    	displaySearchFrequencey();
    }
    
    void displaySearchFrequencey(){
    	System.out.println("frequence de recherche par duree");
		for (int i = 0; i < frequenceDeRechercheDuree.size(); i++) {
			System.out.println(frequenceDeRechercheDuree.get(i));
		}
    }
    
    // frequence de recherche par comportement, OK
    
    LinkedList<FrequencyOfServiceSearchBehaviour> frequenceDeRechercheComportement;
    
    FrequencyOfServiceSearchBehaviour getLastBehaviouralSearchFrequency(String agent){
    	for(int i = frequenceDeRechercheComportement.size() - 1; i >= 0 ; i--){
    		FrequencyOfServiceSearchBehaviour element = frequenceDeRechercheComportement.get(i);
    		if(element.getAgent().equals(agent))
    			return element;
    	}
    	return null;
    }
    
    public void generateFrequenceDeRechercheComportement(){
    	frequenceDeRechercheComportement = new LinkedList<FrequencyOfServiceSearchBehaviour>();
    	
    	for(int i = 0; i < list.size(); i++) {
    		MyData data = list.get(i);
			String event = data.getEventType();
			String agent = data.getAgentName();
			long time = data.getTime();
			if(event.equals("action") || event.equals("onTick")){
				FrequencyOfServiceSearchBehaviour element = getLastBehaviouralSearchFrequency(agent);
				if(element!=null){
					float numberOfSearch = element.getNumberOfSearch();
					float numberOfBehaviour = 1 + element.getNumberOfBehaviour();
					FrequencyOfServiceSearchBehaviour newElement = new FrequencyOfServiceSearchBehaviour(agent,numberOfSearch,numberOfBehaviour,time);
					frequenceDeRechercheComportement.add(newElement);
				}
				else{
					float numberOfSearch = 0;
					float numberOfBehaviour = 1;
					FrequencyOfServiceSearchBehaviour newElement = new FrequencyOfServiceSearchBehaviour(agent,numberOfSearch,numberOfBehaviour,time);
					frequenceDeRechercheComportement.add(newElement);
				}
			}
			if(event.equals("search")){
				FrequencyOfServiceSearchBehaviour element = getLastBehaviouralSearchFrequency(agent);
				if(element!=null){
					float numberOfSearch = 1 + element.getNumberOfSearch();
					float numberOfBehaviour = element.getNumberOfBehaviour();
					FrequencyOfServiceSearchBehaviour newElement = new FrequencyOfServiceSearchBehaviour(agent,numberOfSearch,numberOfBehaviour,time);
					frequenceDeRechercheComportement.add(newElement);
				}
				else{
					float numberOfSearch = 1;
					float numberOfBehaviour = 0;
					FrequencyOfServiceSearchBehaviour newElement = new FrequencyOfServiceSearchBehaviour(agent,numberOfSearch,numberOfBehaviour,time);
					frequenceDeRechercheComportement.add(newElement);
				}
			}
    	}
    	displayBehaviouralSearchFrequencey();
    }
    void displayBehaviouralSearchFrequencey(){
    	System.out.println("frequence de recherche par comportement");
		for (int i = 0; i < frequenceDeRechercheComportement.size(); i++) {
			System.out.println(frequenceDeRechercheComportement.get(i));
		}
    }
    
    // nombre de recherche d'un service(x) par tous les agents, OK
    LinkedList<NumberOfServiceSearch> rechercheDesServices;
    
    NumberOfServiceSearch getLastServiceSearch(String service){
    	for(int i = rechercheDesServices.size() - 1; i >= 0 ; i--){
    		NumberOfServiceSearch element = rechercheDesServices.get(i);
    		if(element.getService().equals(service))
    			return element;
    	}
    	return null;
    }
    
    public void generateRechercheDesServices(){
    	rechercheDesServices = new LinkedList<NumberOfServiceSearch>();
    	
    	for (int i = 0; i < list.size(); i++) {
    		MyData data = list.get(i);
			String event = data.getEventType();
			long time = data.getTime();
			if(event.equals("search")){
				String service = data.getSearchedService();
				NumberOfServiceSearch element = getLastServiceSearch(service);
				if(element!=null){
					float numberOfSearch = 1 + element.getNumberOfSearch();
					NumberOfServiceSearch newElement = new NumberOfServiceSearch(service, numberOfSearch , time);
					rechercheDesServices.add(newElement);
				}
				else{
					int numberOfSearch = 1 ;
					NumberOfServiceSearch newElement = new NumberOfServiceSearch(service, numberOfSearch , time);
					rechercheDesServices.add(newElement);
				}
			}
    	}
    	displayServiceSearch();
    }
    
    void displayServiceSearch(){
    	System.out.println("nombre de recherche d'un service(x) par tous les agents");
		for (int i = 0; i < rechercheDesServices.size(); i++) {
			System.out.println(rechercheDesServices.get(i));
		}
    }

    // nombre de demandes de services par comportement reussi, OK
    LinkedList<NumberOfServiceDemandsBehaviour> nombreDesDemandesEtComportements;
    
    boolean isOneShotBehaviour(String behaviour){
    	LinkedList listeBehaviours = Obj2.listeBehaviours;
    	
    	for (int i = 0; i < listeBehaviours.size(); i++) {
			Obj2 o = (Obj2) listeBehaviours.get(i);
			if(o.getName().equals(behaviour)){
				if(o.getSuper().equals("OneShotBehaviour")){
					return true;
				}
				else{
					return isOneShotBehaviour(o.getSuper());
				}
			}
		}
    	return false;
    }
    
    NumberOfServiceDemandsBehaviour getLastDemandsVsBehaviours(String agent){
    	for(int i = nombreDesDemandesEtComportements.size() - 1; i >= 0 ; i--){
    		NumberOfServiceDemandsBehaviour element = nombreDesDemandesEtComportements.get(i);
    		if(element.getAgent().equals(agent))
    			return element;
    	}
    	return null;
    }
    
    public void generateNombreDesDemandesEtComportements(){
    	nombreDesDemandesEtComportements = new LinkedList<NumberOfServiceDemandsBehaviour>();
    	
    	for (int i = 0; i < list.size(); i++) {
    		MyData data = list.get(i);
    		String agent = data.getAgentName();
    		String behaviour = data.getBehaviourName();
    		String event = data.getEventType();
    		long time = data.getTime();
    		if(event.equals("send")){
    			String msgType = data.getMessageType();
    			if(msgType.equals("REQUEST") || msgType.equals("CFP")){
    				NumberOfServiceDemandsBehaviour element = getLastDemandsVsBehaviours(agent);
    				if(element!=null){
    					float numberOfDemands = 1 + element.getNumberOfDemands();
    					float numberOfBehaviours = element.getNumberOfBehaviours();
						NumberOfServiceDemandsBehaviour newElement = new NumberOfServiceDemandsBehaviour(agent, numberOfDemands, numberOfBehaviours, time);
						nombreDesDemandesEtComportements.add(newElement);
    				}
    				else{
    					float numberOfDemands = 1 ;
    					float numberOfBehaviours = 0 ; 
						NumberOfServiceDemandsBehaviour newElement = new NumberOfServiceDemandsBehaviour(agent, numberOfDemands, numberOfBehaviours, time);
						nombreDesDemandesEtComportements.add(newElement);
    				}
    			}
    		}
    		if(event.equals("done") || (event.equals("action") && isOneShotBehaviour(behaviour))){
    			NumberOfServiceDemandsBehaviour element = getLastDemandsVsBehaviours(agent);
    			if(element!=null){
    				float numberOfDemands = element.getNumberOfDemands();
					float numberOfBehaviours = 1 + element.getNumberOfBehaviours();
    				NumberOfServiceDemandsBehaviour newElement = new NumberOfServiceDemandsBehaviour(agent, numberOfDemands, numberOfBehaviours, time);
    				nombreDesDemandesEtComportements.add(newElement);
    			}
    			else{
    				float numberOfDemands = 0 ;
					float numberOfBehaviours = 1 ; 
					NumberOfServiceDemandsBehaviour newElement = new NumberOfServiceDemandsBehaviour(agent, numberOfDemands, numberOfBehaviours, time);
					nombreDesDemandesEtComportements.add(newElement);
    			}
    		}
    	}
    	displayNombreDesDemandesEtComportements();
    }
    
    void displayNombreDesDemandesEtComportements(){
    	System.out.println("nombreDesDemandesEtComportements");
    	for (int i = 0; i < nombreDesDemandesEtComportements.size(); i++) {
			System.out.println(nombreDesDemandesEtComportements.get(i));
		}
    }
    
    // nombre de servcice rendues par demandes, OK
    LinkedList<NumberOfProvidedServicesDemand> servicesRenduesParDemandes;
    
    NumberOfProvidedServicesDemand getLastAnsweredDemands(String agent){
    	for(int i = servicesRenduesParDemandes.size() - 1; i >= 0 ; i--){
    		NumberOfProvidedServicesDemand element = servicesRenduesParDemandes.get(i);
    		if(element.getAgent().equals(agent))
    			return element;
    	}
    	return null;
    }
    
    public void generateServicesRenduesParDemandes(){
    	servicesRenduesParDemandes = new LinkedList<NumberOfProvidedServicesDemand>();
    	
    	for (int i = 0; i < list.size(); i++) {
    		MyData data = list.get(i);
    		String agent = data.getAgentName();
    		String event = data.getEventType();
    		String msgType = data.getMessageType();
    		long time = data.getTime();
    		if(event.equals("send")){
    			if(msgType.equals("INFORM")){
    				NumberOfProvidedServicesDemand element = getLastAnsweredDemands(agent);
    				if(element!=null){
    					float numberOfAnswers = 1 + element.getNumberOfAnswers();
    					float numberOfRequests = element.getNumberOfRequests();
						NumberOfProvidedServicesDemand newElement = new NumberOfProvidedServicesDemand(agent, numberOfAnswers, numberOfRequests, time);
						servicesRenduesParDemandes.add(newElement);
    				}
    				else{
    					float numberOfAnswers = 1 ;
    					float numberOfRequests = 0;
						NumberOfProvidedServicesDemand newElement = new NumberOfProvidedServicesDemand(agent, numberOfAnswers, numberOfRequests, time);
						servicesRenduesParDemandes.add(newElement);
    				}
    			}
    		}
			if(event.equals("receive")){
				if(msgType.equals("REQUEST") || msgType.equals("CFP")){
					NumberOfProvidedServicesDemand element = getLastAnsweredDemands(agent);
					if(element!=null){
						float numberOfAnswers = element.getNumberOfAnswers();
    					float numberOfRequests = 1 + element.getNumberOfRequests();
						NumberOfProvidedServicesDemand newElement = new NumberOfProvidedServicesDemand(agent, numberOfAnswers, numberOfRequests, time);
						servicesRenduesParDemandes.add(newElement);
					}
					else{
						float numberOfAnswers = 0 ;
    					float numberOfRequests = 1;
						NumberOfProvidedServicesDemand newElement = new NumberOfProvidedServicesDemand(agent, numberOfAnswers, numberOfRequests, time);
						servicesRenduesParDemandes.add(newElement);
					}
				}
			}
    	}
    	displayServicesRenduesParDemandes();
    }
    void displayServicesRenduesParDemandes(){
    	System.out.println("servicesRenduesParDemandes");
    	for (int i = 0; i < servicesRenduesParDemandes.size(); i++) {
			System.out.println(servicesRenduesParDemandes.get(i));
		}
    }

	public LinkedList<ServiceWealth> getRichesseDeService() {
		return richesseDeService;
	}

	public LinkedList<FrequencyOfServiceSearchTime> getFrequenceDeRechercheDuree() {
		return frequenceDeRechercheDuree;
	}

	public LinkedList<FrequencyOfServiceSearchBehaviour> getFrequenceDeRechercheComportement() {
		return frequenceDeRechercheComportement;
	}

	public LinkedList<NumberOfServiceSearch> getRechercheDesServices() {
		return rechercheDesServices;
	}

	public LinkedList<NumberOfServiceDemandsBehaviour> getNombreDesDemandesEtComportements() {
		return nombreDesDemandesEtComportements;
	}

	public LinkedList<NumberOfProvidedServicesDemand> getServicesRenduesParDemandes() {
		return servicesRenduesParDemandes;
	}
    
}