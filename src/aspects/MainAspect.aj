package aspects;

import statique.Saisie;
import tools.*;

public aspect MainAspect {
	
	pointcut MyMainPoint() : execution(* main(..));
	
	before() : MyMainPoint(){
		MyData data = new MyData();
		long time = (long) System.currentTimeMillis();
		data.setTime(time);
		data.setEventType("main");
		Saisie saisie = new Saisie();
		saisie.setVisible(true);
		ClientUDP.send(data);
	}
}
