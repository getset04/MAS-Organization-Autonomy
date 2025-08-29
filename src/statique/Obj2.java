package statique;

import java.util.LinkedList;
import java.util.StringTokenizer;

// la classe Obj2 represente les behaviours

public class Obj2 {
	
    String nom;
    String code;
    String upperClass;
    
    public static LinkedList listeBehaviours;
    
    public Obj2(String nom, String code, String upperClass) {
        this.nom = nom;
        this.code = ProjectUtils.refreshCode(code);
        this.upperClass = upperClass;
    }

    Obj2(String nom, String code) {
        this.nom = nom;
        this.code = ProjectUtils.refreshCode(code);
    }

    public String getName(){
        return nom;
    }
    
    public String getSuper(){
        return upperClass;
    }
    
    public String getCode(){
        return code;
    }
    
}
