package tools;

public class FrequencyOfServiceSearchBehaviour {

	String agent;
	float numberOfSearch;
	float numberOfBehaviour;
	long time;
	
	public FrequencyOfServiceSearchBehaviour(String agent, float numberOfSearch,float numberOfBehaviour, long time) {
		this.agent = agent;
		this.numberOfSearch = numberOfSearch;
		this.numberOfBehaviour = numberOfBehaviour;
		this.time = time;
	}

	public String getAgent() {
		return agent;
	}

	public float getNumberOfSearch() {
		return numberOfSearch;
	}

	public float getNumberOfBehaviour() {
		return numberOfBehaviour;
	}

	public long getTime() {
		return time;
	}

	@Override
	public String toString() {
		return "BehaviouralSearchFrequency [agent=" + agent
				+ ", numberOfSearch=" + numberOfSearch + ", numberOfBehaviour="
				+ numberOfBehaviour + ", time=" + time + "]";
	}
	
}
