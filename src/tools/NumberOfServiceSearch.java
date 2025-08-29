package tools;

public class NumberOfServiceSearch {
	
	String service;
	float numberOfSearch;
	long time;
	
	public NumberOfServiceSearch(String service, float numberOfSearch, long time) {
		this.service = service;
		this.numberOfSearch = numberOfSearch;
		this.time = time;
	}

	public String getService() {
		return service;
	}

	public float getNumberOfSearch() {
		return numberOfSearch;
	}

	public long getTime() {
		return time;
	}

	@Override
	public String toString() {
		return "ServiceSearch [service=" + service + ", numberOfSearch="
				+ numberOfSearch + ", time=" + time + "]";
	}
	
}
