package statique;

import java.util.LinkedList;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import tools.BehaviouralWealth;
import tools.FrequencyOfServiceSearchBehaviour;
import tools.FrequencyOfServiceSearchTime;
import tools.NumberOfProvidedServicesDemand;
import tools.NumberOfServiceDemandsBehaviour;
import tools.NumberOfServiceSearch;
import tools.ServiceWealth;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Home
 */
public class ShowCharts extends javax.swing.JFrame {

    public ShowCharts() {
        initComponents();
    }
    
    public ShowCharts(
			LinkedList<BehaviouralWealth> listBehaviouralWealth,
			LinkedList<ServiceWealth> listServiceWealth,
			LinkedList<FrequencyOfServiceSearchTime> listFrequencyOfServiceSearchTime,
			LinkedList<FrequencyOfServiceSearchBehaviour> listFrequencyOfServiceSearchBehaviour,
			LinkedList<NumberOfServiceSearch> listNumberOfServiceSearch,
			LinkedList<NumberOfServiceDemandsBehaviour> listNumberOfServiceDemandsBehaviour,
			LinkedList<NumberOfProvidedServicesDemand> listNumberOfProvidedServicesDemand) {
    	
    	initComponents();
    	makeBWChart(listBehaviouralWealth);
    	makeSWChart(listServiceWealth);
    	makeFoSSTChart(listFrequencyOfServiceSearchTime);
    	makeFoSSBChart(listFrequencyOfServiceSearchBehaviour);
    	makeNoSSChart(listNumberOfServiceSearch);
    	makeNoSDBChart(listNumberOfServiceDemandsBehaviour);
    	makeNoPSDChart(listNumberOfProvidedServicesDemand);
	}

	void makeBWChart(LinkedList<BehaviouralWealth> listBehaviouralWealth){
    	// Behavioural Wealth
        DefaultCategoryDataset data = new DefaultCategoryDataset();
        
        for (int i = 0; i < listBehaviouralWealth.size(); i++) {
        	BehaviouralWealth element = listBehaviouralWealth.get(i);
        	data.addValue(element.getBehaviouralWealth(), element.getAgent(), element.getAgent());
        }
        
        JFreeChart chart = ChartFactory.createBarChart3D("Behavioural Wealth", "Agents", "Values", data, PlotOrientation.VERTICAL, true, true, true);
        ChartPanel panel = new ChartPanel(chart);
        panel.setSize(850, 506);
        panelBW.add(panel);
    }

	ServiceWealth getServiceWealth(ServiceWealth element,LinkedList<ServiceWealth> listServiceWealth){
    	String agent = element.getAgent();
    	int indexe = listServiceWealth.indexOf(element);
		for (int i = indexe - 1; i >= 0 ; i--) {
			ServiceWealth serviceWealth = listServiceWealth.get(i);
    		if(serviceWealth.getAgent().equals(agent)){
    			return serviceWealth;
    		}
		}
    	return null;
    }
	
	ServiceWealth getTurningPointServiceWealth(ServiceWealth element,LinkedList<ServiceWealth> listServiceWealth){
		ServiceWealth oldPoint = getServiceWealth(element, listServiceWealth);
		if(oldPoint==null)
			return null;
		ServiceWealth point = new ServiceWealth(element.getAgent(), oldPoint.getNumberOfServices(), oldPoint.getNumberOfAllServices(), element.getTime());
		return point;
	}
	
	LinkedList<String> getServiceWealthAgents(LinkedList<ServiceWealth> listServiceWealth){
		LinkedList<String> agents = new LinkedList<String>();
		
		for (int i = 0; i < listServiceWealth.size(); i++) {
			ServiceWealth element = listServiceWealth.get(i);
			String name = element.getAgent();
			if(!agents.contains(name))
				agents.add(name);
		}
		
		return agents;
	}
	
	XYSeries[] getAllServiceWealthSeries(LinkedList<String> agents,LinkedList<ServiceWealth> listServiceWealth){
		
		XYSeries[] series = new XYSeries[agents.size()];
		for (int i = 0; i < agents.size(); i++) {
			series[i] = new XYSeries(agents.get(i),false);
		}
		
		for (int i = 0; i < listServiceWealth.size(); i++) {
			ServiceWealth element = listServiceWealth.get(i);
			int indexe = agents.indexOf(element.getAgent());
			ServiceWealth extraPoint = getTurningPointServiceWealth(element, listServiceWealth);
			if(extraPoint!=null){
				if(extraPoint.getNumberOfAllServices()!=0){
					series[indexe].add(element.getTime(),(extraPoint.getNumberOfServices()/extraPoint.getNumberOfAllServices()));
				}
			}
			if(element.getNumberOfAllServices()!=0){
				series[indexe].add(element.getTime(),(element.getNumberOfServices()/element.getNumberOfAllServices()));
			}
		}
		
		return series;
	}
	
    void makeSWChart(LinkedList<ServiceWealth> listServiceWealth){
    	// Service Wealth
    	LinkedList<String> agents = getServiceWealthAgents(listServiceWealth);
    	XYSeries[] series = getAllServiceWealthSeries(agents, listServiceWealth);
    	XYSeriesCollection dataset = new XYSeriesCollection();
    	
    	for (int i = 0; i < series.length; i++) {
    		dataset.addSeries(series[i]);
		}
    	
    	JFreeChart chart = ChartFactory.createXYLineChart("Service Wealth", "Time", "Values", dataset, PlotOrientation.VERTICAL, true, true, true);
    	ValueAxis valueAxis = chart.getXYPlot().getDomainAxis();
    	valueAxis.setVerticalTickLabels(true);
    	ChartPanel panel = new ChartPanel(chart);
        panel.setSize(850, 506);
        panelSW.add(panel);
    }
    
	LinkedList<String> getFrequencyOfServiceSearchTimeAgents(LinkedList<FrequencyOfServiceSearchTime> listFrequencyOfServiceSearchTime){
		LinkedList<String> agents = new LinkedList<String>();
		
		for (int i = 0; i < listFrequencyOfServiceSearchTime.size(); i++) {
			FrequencyOfServiceSearchTime element = listFrequencyOfServiceSearchTime.get(i);
			String name = element.getAgent();
			if(!agents.contains(name))
				agents.add(name);
		}
		
		return agents;
	}
	
	XYSeries[] getAllFrequencyOfServiceSearchTimeSeries(LinkedList<String> agents,LinkedList<FrequencyOfServiceSearchTime> listFrequencyOfServiceSearchTime){
		
		XYSeries[] series = new XYSeries[agents.size()];
		for (int i = 0; i < agents.size(); i++) {
			series[i] = new XYSeries(agents.get(i),false);
		}
		
		for (int i = 0; i < listFrequencyOfServiceSearchTime.size(); i++) {
			FrequencyOfServiceSearchTime element = listFrequencyOfServiceSearchTime.get(i);
			int indexe = agents.indexOf(element.getAgent());
			if(element.getTime()!=0){
				series[indexe].add(element.getTime(),(element.getSearchNumber()/element.getTime()));
			}
		}
		
		return series;
	}
	
    void makeFoSSTChart(LinkedList<FrequencyOfServiceSearchTime> listFrequencyOfServiceSearchTime){
    	// Frequency of Service Search / Time
    	LinkedList<String> agents = getFrequencyOfServiceSearchTimeAgents(listFrequencyOfServiceSearchTime);
    	XYSeries[] series = getAllFrequencyOfServiceSearchTimeSeries(agents, listFrequencyOfServiceSearchTime);
    	XYSeriesCollection dataset = new XYSeriesCollection();
    	
    	for (int i = 0; i < series.length; i++) {
    		dataset.addSeries(series[i]);
		}
    	

    	JFreeChart chart = ChartFactory.createXYLineChart("Frequency of Service Search per Time", "Time", "Values", dataset, PlotOrientation.VERTICAL, true, true, true);
    	ValueAxis valueAxis = chart.getXYPlot().getDomainAxis();
    	valueAxis.setVerticalTickLabels(true);
    	ChartPanel panel = new ChartPanel(chart);
        panel.setSize(850, 506);
        panelFoSST.add(panel);
    }
    
    FrequencyOfServiceSearchBehaviour getFrequencyOfServiceSearchBehaviour(FrequencyOfServiceSearchBehaviour element,LinkedList<FrequencyOfServiceSearchBehaviour> listFrequencyOfServiceSearchBehaviour){
    	String agent = element.getAgent();
    	int indexe = listFrequencyOfServiceSearchBehaviour.indexOf(element);
		for (int i = indexe - 1; i >= 0 ; i--) {
			FrequencyOfServiceSearchBehaviour searchFrequency = listFrequencyOfServiceSearchBehaviour.get(i);
    		if(searchFrequency.getAgent().equals(agent)){
    			return searchFrequency;
    		}
		}
    	return null;
    }
	
    FrequencyOfServiceSearchBehaviour getTurningPointFrequencyOfServiceSearchBehaviour(FrequencyOfServiceSearchBehaviour element,LinkedList<FrequencyOfServiceSearchBehaviour> listFrequencyOfServiceSearchBehaviour){
    	FrequencyOfServiceSearchBehaviour oldPoint = getFrequencyOfServiceSearchBehaviour(element, listFrequencyOfServiceSearchBehaviour);
		if(oldPoint==null)
			return null;
		FrequencyOfServiceSearchBehaviour point = new FrequencyOfServiceSearchBehaviour(element.getAgent(), oldPoint.getNumberOfSearch(), oldPoint.getNumberOfBehaviour(), element.getTime());
		return point;
	}
    
	LinkedList<String> getFrequencyOfServiceSearchBehaviourAgents(LinkedList<FrequencyOfServiceSearchBehaviour> listFrequencyOfServiceSearchBehaviour){
		LinkedList<String> agents = new LinkedList<String>();
		
		for (int i = 0; i < listFrequencyOfServiceSearchBehaviour.size(); i++) {
			FrequencyOfServiceSearchBehaviour element = listFrequencyOfServiceSearchBehaviour.get(i);
			String name = element.getAgent();
			if(!agents.contains(name))
				agents.add(name);
		}
		
		return agents;
	}
	
	XYSeries[] getAllFrequencyOfServiceSearchBehaviourSeries(LinkedList<String> agents,LinkedList<FrequencyOfServiceSearchBehaviour> listFrequencyOfServiceSearchBehaviour){
		
		XYSeries[] series = new XYSeries[agents.size()];
		for (int i = 0; i < agents.size(); i++) {
			series[i] = new XYSeries(agents.get(i),false);
		}
		
		for (int i = 0; i < listFrequencyOfServiceSearchBehaviour.size(); i++) {
			FrequencyOfServiceSearchBehaviour element = listFrequencyOfServiceSearchBehaviour.get(i);
			int indexe = agents.indexOf(element.getAgent());
			FrequencyOfServiceSearchBehaviour extraPoint = getTurningPointFrequencyOfServiceSearchBehaviour(element, listFrequencyOfServiceSearchBehaviour);
			if(extraPoint!=null){
				if(extraPoint.getNumberOfBehaviour()!=0){
					series[indexe].add(element.getTime(),(extraPoint.getNumberOfSearch()/extraPoint.getNumberOfBehaviour()));
				}
			}
			if(element.getNumberOfBehaviour()!=0){
				series[indexe].add(element.getTime(),(element.getNumberOfSearch()/element.getNumberOfBehaviour()));
			}
		}
		
		return series;
	}
	
    
    void makeFoSSBChart(LinkedList<FrequencyOfServiceSearchBehaviour> listFrequencyOfServiceSearchBehaviour){
    	// Frequency of Service Search / Behaviour
    	LinkedList<String> agents = getFrequencyOfServiceSearchBehaviourAgents(listFrequencyOfServiceSearchBehaviour);
    	XYSeries[] series = getAllFrequencyOfServiceSearchBehaviourSeries(agents, listFrequencyOfServiceSearchBehaviour);
    	XYSeriesCollection dataset = new XYSeriesCollection();
    	
    	for (int i = 0; i < series.length; i++) {
    		dataset.addSeries(series[i]);
		}
    	
        JFreeChart chart = ChartFactory.createXYLineChart("Frequency of Service Search per Behaviour", "Time", "Values", dataset, PlotOrientation.VERTICAL, true, true, true);
        ValueAxis valueAxis = chart.getXYPlot().getDomainAxis();
    	valueAxis.setVerticalTickLabels(true);
        ChartPanel panel = new ChartPanel(chart);
        panel.setSize(850, 506);
        panelFoSSB.add(panel);
    }
    
    NumberOfServiceSearch getNumberOfServiceSearch(NumberOfServiceSearch element,LinkedList<NumberOfServiceSearch> listNumberOfServiceSearch){
    	String service = element.getService();
    	int indexe = listNumberOfServiceSearch.indexOf(element);
		for (int i = indexe - 1; i >= 0 ; i--) {
			NumberOfServiceSearch searchService = listNumberOfServiceSearch.get(i);
    		if(searchService.getService().equals(service)){
    			return searchService;
    		}
		}
    	return null;
    }
	
    NumberOfServiceSearch getTurningPointNumberOfServiceSearch(NumberOfServiceSearch element,LinkedList<NumberOfServiceSearch> listNumberOfServiceSearch){
    	NumberOfServiceSearch oldPoint = getNumberOfServiceSearch(element, listNumberOfServiceSearch);
		if(oldPoint==null)
			return null;
		NumberOfServiceSearch point = new NumberOfServiceSearch(element.getService(), oldPoint.getNumberOfSearch(), element.getTime());
		return point;
	}
    
	LinkedList<String> getNumberOfServiceSearchServices(LinkedList<NumberOfServiceSearch> listNumberOfServiceSearch){
		LinkedList<String> services = new LinkedList<String>();
		
		for (int i = 0; i < listNumberOfServiceSearch.size(); i++) {
			NumberOfServiceSearch element = listNumberOfServiceSearch.get(i);
			String name = element.getService();
			if(!services.contains(name))
				services.add(name);
		}
		
		return services;
	}
	
	XYSeries[] getAllNumberOfServiceSearchSeries(LinkedList<String> services,LinkedList<NumberOfServiceSearch> listNumberOfServiceSearch){
		
		XYSeries[] series = new XYSeries[services.size()];
		for (int i = 0; i < services.size(); i++) {
			series[i] = new XYSeries(services.get(i),false);
		}
		
		for (int i = 0; i < listNumberOfServiceSearch.size(); i++) {
			NumberOfServiceSearch element = listNumberOfServiceSearch.get(i);
			int indexe = services.indexOf(element.getService());
			NumberOfServiceSearch oldPextraPointoint = getTurningPointNumberOfServiceSearch(element, listNumberOfServiceSearch);
			if(oldPextraPointoint!=null){
				series[indexe].add(element.getTime(),oldPextraPointoint.getNumberOfSearch());
			}
			series[indexe].add(element.getTime(),element.getNumberOfSearch());
		}
		
		return series;
	}
	
    void makeNoSSChart(LinkedList<NumberOfServiceSearch> listNumberOfServiceSearch){
    	// Number of Service Search
    	LinkedList<String> services = getNumberOfServiceSearchServices(listNumberOfServiceSearch);
    	XYSeries[] series = getAllNumberOfServiceSearchSeries(services, listNumberOfServiceSearch);
    	XYSeriesCollection dataset = new XYSeriesCollection();
    	
    	for (int i = 0; i < series.length; i++) {
    		dataset.addSeries(series[i]);
		}
    	
    	JFreeChart chart = ChartFactory.createXYLineChart("Number of Service Search", "Time", "Values", dataset, PlotOrientation.VERTICAL, true, true, true);
    	ValueAxis valueAxis = chart.getXYPlot().getDomainAxis();
    	valueAxis.setVerticalTickLabels(true);
        ChartPanel panel = new ChartPanel(chart);
        panel.setSize(850, 506);
        panelNoSS.add(panel);
    }
    
    NumberOfServiceDemandsBehaviour getNumberOfServiceDemandsBehaviour(NumberOfServiceDemandsBehaviour element,LinkedList<NumberOfServiceDemandsBehaviour> listNumberOfServiceDemandsBehaviour){
    	String agent = element.getAgent();
    	int indexe = listNumberOfServiceDemandsBehaviour.indexOf(element);
		for (int i = indexe - 1; i >= 0 ; i--) {
			NumberOfServiceDemandsBehaviour numberOfService = listNumberOfServiceDemandsBehaviour.get(i);
    		if(numberOfService.getAgent().equals(agent)){
    			return numberOfService;
    		}
		}
    	return null;
    }
	
    NumberOfServiceDemandsBehaviour getTurningPointNumberOfServiceDemandsBehaviour(NumberOfServiceDemandsBehaviour element,LinkedList<NumberOfServiceDemandsBehaviour> listNumberOfServiceDemandsBehaviour){
    	NumberOfServiceDemandsBehaviour oldPoint = getNumberOfServiceDemandsBehaviour(element, listNumberOfServiceDemandsBehaviour);
		if(oldPoint==null)
			return null;
		NumberOfServiceDemandsBehaviour point = new NumberOfServiceDemandsBehaviour(element.getAgent(), oldPoint.getNumberOfDemands(), oldPoint.getNumberOfBehaviours(), element.getTime());
		return point;
	}
    
	LinkedList<String> getNumberOfServiceDemandsBehaviourAgents(LinkedList<NumberOfServiceDemandsBehaviour> listNumberOfServiceDemandsBehaviour){
		LinkedList<String> agents = new LinkedList<String>();
		
		for (int i = 0; i < listNumberOfServiceDemandsBehaviour.size(); i++) {
			NumberOfServiceDemandsBehaviour element = listNumberOfServiceDemandsBehaviour.get(i);
			String name = element.getAgent();
			if(!agents.contains(name))
				agents.add(name);
		}
		
		return agents;
	}
	
	XYSeries[] getAllNumberOfServiceDemandsBehaviourSeries(LinkedList<String> agents,LinkedList<NumberOfServiceDemandsBehaviour> listNumberOfServiceDemandsBehaviour){
		
		XYSeries[] series = new XYSeries[agents.size()];
		for (int i = 0; i < agents.size(); i++) {
			series[i] = new XYSeries(agents.get(i),false);
		}
		
		for (int i = 0; i < listNumberOfServiceDemandsBehaviour.size(); i++) {
			NumberOfServiceDemandsBehaviour element = listNumberOfServiceDemandsBehaviour.get(i);
			int indexe = agents.indexOf(element.getAgent());
			NumberOfServiceDemandsBehaviour extraPoint = getTurningPointNumberOfServiceDemandsBehaviour(element, listNumberOfServiceDemandsBehaviour);
			if(extraPoint!=null){
				if(extraPoint.getNumberOfBehaviours()!=0){
					series[indexe].add(element.getTime(),(extraPoint.getNumberOfDemands()/extraPoint.getNumberOfBehaviours()));
				}
			}
			if(element.getNumberOfBehaviours()!=0){
				series[indexe].add(element.getTime(),(element.getNumberOfDemands()/element.getNumberOfBehaviours()));
			}
		}
		
		return series;
	}
    
    void makeNoSDBChart(LinkedList<NumberOfServiceDemandsBehaviour> listNumberOfServiceDemandsBehaviour){
    	// Number of Service Demands / Behaviour
    	LinkedList<String> agents = getNumberOfServiceDemandsBehaviourAgents(listNumberOfServiceDemandsBehaviour);
    	XYSeries[] series = getAllNumberOfServiceDemandsBehaviourSeries(agents, listNumberOfServiceDemandsBehaviour);
    	XYSeriesCollection dataset = new XYSeriesCollection();
    	
    	for (int i = 0; i < series.length; i++) {
    		dataset.addSeries(series[i]);
		}
    	
    	JFreeChart chart = ChartFactory.createXYLineChart("Number of Service Demands per Behaviour", "Time", "Values", dataset, PlotOrientation.VERTICAL, true, true, true);
    	ValueAxis valueAxis = chart.getXYPlot().getDomainAxis();
    	valueAxis.setVerticalTickLabels(true);
        ChartPanel panel = new ChartPanel(chart);
        panel.setSize(850, 506);
        panelNoSDB.add(panel);
    }
    
    NumberOfProvidedServicesDemand getNumberOfProvidedServicesDemand(NumberOfProvidedServicesDemand element,LinkedList<NumberOfProvidedServicesDemand> listNumberOfProvidedServicesDemand){
    	String agent = element.getAgent();
    	int indexe = listNumberOfProvidedServicesDemand.indexOf(element);
		for (int i = indexe - 1; i >= 0 ; i--) {
			NumberOfProvidedServicesDemand numberOfProvidedServices = listNumberOfProvidedServicesDemand.get(i);
    		if(numberOfProvidedServices.getAgent().equals(agent)){
    			return numberOfProvidedServices;
    		}
		}
    	return null;
    }
	
    NumberOfProvidedServicesDemand getTurningPointNumberOfProvidedServicesDemand(NumberOfProvidedServicesDemand element,LinkedList<NumberOfProvidedServicesDemand> listNumberOfProvidedServicesDemand){
    	NumberOfProvidedServicesDemand oldPoint = getNumberOfProvidedServicesDemand(element, listNumberOfProvidedServicesDemand);
		if(oldPoint==null)
			return null;
		NumberOfProvidedServicesDemand point = new NumberOfProvidedServicesDemand(element.getAgent(), oldPoint.getNumberOfAnswers(), oldPoint.getNumberOfRequests(), element.getTime());
		return point;
	}
    
	LinkedList<String> getNumberOfProvidedServicesDemandAgents(LinkedList<NumberOfProvidedServicesDemand> listNumberOfProvidedServicesDemand){
		LinkedList<String> agents = new LinkedList<String>();
		
		for (int i = 0; i < listNumberOfProvidedServicesDemand.size(); i++) {
			NumberOfProvidedServicesDemand element = listNumberOfProvidedServicesDemand.get(i);
			String name = element.getAgent();
			if(!agents.contains(name))
				agents.add(name);
		}
		
		return agents;
	}
	
	XYSeries[] getAllNumberOfProvidedServicesDemandSeries(LinkedList<String> agents,LinkedList<NumberOfProvidedServicesDemand> listNumberOfProvidedServicesDemand){
		
		XYSeries[] series = new XYSeries[agents.size()];
		for (int i = 0; i < agents.size(); i++) {
			series[i] = new XYSeries(agents.get(i),false);
		}
		
		for (int i = 0; i < listNumberOfProvidedServicesDemand.size(); i++) {
			NumberOfProvidedServicesDemand element = listNumberOfProvidedServicesDemand.get(i);
			int indexe = agents.indexOf(element.getAgent());
			NumberOfProvidedServicesDemand extraPoint = getTurningPointNumberOfProvidedServicesDemand(element, listNumberOfProvidedServicesDemand);
			if(extraPoint!=null){
				if(extraPoint.getNumberOfRequests()!=0){
					series[indexe].add(element.getTime(),(extraPoint.getNumberOfAnswers()/extraPoint.getNumberOfRequests()));
				}
			}
			if(element.getNumberOfRequests()!=0){
				series[indexe].add(element.getTime(),(element.getNumberOfAnswers()/element.getNumberOfRequests()));
			}
		}
		
		return series;
	}
    
    void makeNoPSDChart(LinkedList<NumberOfProvidedServicesDemand> listNumberOfProvidedServicesDemand){
    	// Number of Provided Services / Demand
    	LinkedList<String> agents = getNumberOfProvidedServicesDemandAgents(listNumberOfProvidedServicesDemand);
    	XYSeries[] series = getAllNumberOfProvidedServicesDemandSeries(agents, listNumberOfProvidedServicesDemand);
    	XYSeriesCollection dataset = new XYSeriesCollection();
    	
    	for (int i = 0; i < series.length; i++) {
    		dataset.addSeries(series[i]);
		}
    	
    	JFreeChart chart = ChartFactory.createXYLineChart("Number of Provided Services per Demand", "Time", "Values", dataset, PlotOrientation.VERTICAL, true, true, true);
    	ValueAxis valueAxis = chart.getXYPlot().getDomainAxis();
    	valueAxis.setVerticalTickLabels(true);
        ChartPanel panel = new ChartPanel(chart);
        panel.setSize(850, 506);
    	panelNoPSD.add(panel);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        panelBW = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        panelSW = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        panelFoSST = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        panelFoSSB = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        panelNoSS = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        panelNoSDB = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        panelNoPSD = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Charts");

        javax.swing.GroupLayout panelBWLayout = new javax.swing.GroupLayout(panelBW);
        panelBW.setLayout(panelBWLayout);
        panelBWLayout.setHorizontalGroup(
            panelBWLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 850, Short.MAX_VALUE)
        );
        panelBWLayout.setVerticalGroup(
            panelBWLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 506, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelBW, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelBW, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("BW", jPanel1);

        javax.swing.GroupLayout panelSWLayout = new javax.swing.GroupLayout(panelSW);
        panelSW.setLayout(panelSWLayout);
        panelSWLayout.setHorizontalGroup(
            panelSWLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 850, Short.MAX_VALUE)
        );
        panelSWLayout.setVerticalGroup(
            panelSWLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 506, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelSW, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelSW, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("SW", jPanel2);

        javax.swing.GroupLayout panelFoSSTLayout = new javax.swing.GroupLayout(panelFoSST);
        panelFoSST.setLayout(panelFoSSTLayout);
        panelFoSSTLayout.setHorizontalGroup(
            panelFoSSTLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 850, Short.MAX_VALUE)
        );
        panelFoSSTLayout.setVerticalGroup(
            panelFoSSTLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 506, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelFoSST, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelFoSST, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("FoSS/T", jPanel3);

        javax.swing.GroupLayout panelFoSSBLayout = new javax.swing.GroupLayout(panelFoSSB);
        panelFoSSB.setLayout(panelFoSSBLayout);
        panelFoSSBLayout.setHorizontalGroup(
            panelFoSSBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 850, Short.MAX_VALUE)
        );
        panelFoSSBLayout.setVerticalGroup(
            panelFoSSBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 506, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelFoSSB, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelFoSSB, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("FoSS/B", jPanel4);

        javax.swing.GroupLayout panelNoSSLayout = new javax.swing.GroupLayout(panelNoSS);
        panelNoSS.setLayout(panelNoSSLayout);
        panelNoSSLayout.setHorizontalGroup(
            panelNoSSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 850, Short.MAX_VALUE)
        );
        panelNoSSLayout.setVerticalGroup(
            panelNoSSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 506, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelNoSS, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelNoSS, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("NoSS", jPanel5);

        javax.swing.GroupLayout panelNoSDBLayout = new javax.swing.GroupLayout(panelNoSDB);
        panelNoSDB.setLayout(panelNoSDBLayout);
        panelNoSDBLayout.setHorizontalGroup(
            panelNoSDBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 850, Short.MAX_VALUE)
        );
        panelNoSDBLayout.setVerticalGroup(
            panelNoSDBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 506, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelNoSDB, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelNoSDB, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("NoSD/B", jPanel6);

        javax.swing.GroupLayout panelNoPSDLayout = new javax.swing.GroupLayout(panelNoPSD);
        panelNoPSD.setLayout(panelNoPSDLayout);
        panelNoPSDLayout.setHorizontalGroup(
            panelNoPSDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 850, Short.MAX_VALUE)
        );
        panelNoPSDLayout.setVerticalGroup(
            panelNoPSDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 506, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelNoPSD, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelNoPSD, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("NoPS/D", jPanel7);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );

        pack();
    }// </editor-fold>                        

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ShowCharts.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ShowCharts.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ShowCharts.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ShowCharts.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ShowCharts().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify                     
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JPanel panelBW;
    private javax.swing.JPanel panelFoSSB;
    private javax.swing.JPanel panelFoSST;
    private javax.swing.JPanel panelNoPSD;
    private javax.swing.JPanel panelNoSDB;
    private javax.swing.JPanel panelNoSS;
    private javax.swing.JPanel panelSW;
    // End of variables declaration                   
}