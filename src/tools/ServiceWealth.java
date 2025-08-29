package tools;

public class ServiceWealth {
	
	String agent;
	float numberOfServices;
	float numberOfAllServices;
	long time;
	
	public ServiceWealth(String agent, float numberOfServices,float numberOfAllServices,long time) {
		this.agent = agent;
		this.numberOfServices = numberOfServices;
		this.numberOfAllServices = numberOfAllServices;
		this.time = time;
	}

	public String getAgent() {
		return agent;
	}

	public float getNumberOfServices() {
		return numberOfServices;
	}

	public float getNumberOfAllServices() {
		return numberOfAllServices;
	}

	public long getTime(){
		return this.time;
	}
	
	@Override
	public String toString() {
		return "WealthService [agent=" + agent + ", numberOfServices="
				+ numberOfServices + ", numberOfAllServices="
				+ numberOfAllServices + ", time=" + time + "]";
	}
}
