package tools;

public class NumberOfServiceDemandsBehaviour {

	String agent;
	float numberOfDemands;
	float numberOfBehaviours;
	long time;
	
	public NumberOfServiceDemandsBehaviour(String agent, float numberOfDemands,float numberOfBehaviours, long time) {
		this.agent = agent;
		this.numberOfDemands = numberOfDemands;
		this.numberOfBehaviours = numberOfBehaviours;
		this.time = time;
	}
	
	public String getAgent() {
		return agent;
	}

	public float getNumberOfDemands() {
		return numberOfDemands;
	}

	public float getNumberOfBehaviours() {
		return numberOfBehaviours;
	}

	public long getTime() {
		return time;
	}

	@Override
	public String toString() {
		return "DemandsVsBehaviours [agent=" + agent + ", numberOfDemands="
				+ numberOfDemands + ", numberOfBehaviours="
				+ numberOfBehaviours + ", time=" + time + "]";
	}
	
}
