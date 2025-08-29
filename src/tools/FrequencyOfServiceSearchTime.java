package tools;

public class FrequencyOfServiceSearchTime {

	String agent;
	long searchNumber;
	long time;
	
	public FrequencyOfServiceSearchTime(String agent,long searchNumber, long time) {
		this.agent = agent;
		this.searchNumber = searchNumber;
		this.time = time;
	}

	public String getAgent() {
		return agent;
	}

	public long getTime() {
		return time;
	}

	public long getSearchNumber() {
		return searchNumber;
	}

	@Override
	public String toString() {
		return "SearchFrequency [agent=" + agent + ", searchNumber="
				+ searchNumber + ", time=" + time + "]";
	}
	
}
