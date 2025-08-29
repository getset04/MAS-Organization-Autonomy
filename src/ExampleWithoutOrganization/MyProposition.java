package ExampleWithoutOrganization;

import java.io.Serializable;

public class MyProposition implements Serializable{

	int nombreDesHeuresOccupees;
	int distance;
	String agent;
	
	public MyProposition(int nombreDesHeuresOccupees, int distance, String agent) {
		this.nombreDesHeuresOccupees = nombreDesHeuresOccupees;
		this.distance = distance;
		this.agent = agent;
	}

	public int getNombreDesHeuresOccupees() {
		return nombreDesHeuresOccupees;
	}

	public int getDistance() {
		return distance;
	}

	public String getAgent() {
		return agent;
	}

	@Override
	public String toString() {
		return "MyProposition [nombreDesHeuresOccupees="
				+ nombreDesHeuresOccupees + ", distance=" + distance
				+ ", agent=" + agent + "]";
	}
	
}
