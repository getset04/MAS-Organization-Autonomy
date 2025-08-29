package tools;

public class NumberOfProvidedServicesDemand {

	String agent;
	float numberOfAnswers;
	float numberOfRequests;
	long time;
	
	public NumberOfProvidedServicesDemand(String agent, float numberOfAnswers,float numberOfRequests, long time) {
		this.agent = agent;
		this.numberOfAnswers = numberOfAnswers;
		this.numberOfRequests = numberOfRequests;
		this.time = time;
	}
	
	public String getAgent() {
		return agent;
	}
	
	public float getNumberOfAnswers() {
		return numberOfAnswers;
	}
	
	public float getNumberOfRequests() {
		return numberOfRequests;
	}
	
	public long getTime() {
		return time;
	}

	@Override
	public String toString() {
		return "AnsweredDemands [agent=" + agent + ", numberOfAnswers="
				+ numberOfAnswers + ", numberOfRequests=" + numberOfRequests
				+ ", time=" + time + "]";
	}
	
}