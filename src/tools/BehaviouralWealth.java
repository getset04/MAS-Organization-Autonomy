package tools;

public class BehaviouralWealth {

	String agent;
	float behaviouralWealth;
	
	public BehaviouralWealth(String agent, float behaviouralWealth) {
		this.agent = agent;
		this.behaviouralWealth = behaviouralWealth;
	}

	public String getAgent() {
		return agent;
	}

	public float getBehaviouralWealth() {
		return behaviouralWealth;
	}

	@Override
	public String toString() {
		return "BehaviouralWealth [agent=" + agent + ", behaviouralWealth="
				+ behaviouralWealth + "]";
	}
	
}
