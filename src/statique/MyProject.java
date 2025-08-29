package statique;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.LinkedList;
import java.util.StringTokenizer;

import tools.BehaviouralWealth;

/**
 *
 * @author Home
 */
public class MyProject {
    private String mySourceCode;
    private boolean finish = true;
    private LinkedList listeObjects;
    private LinkedList checkBehaviours;
    private LinkedList listeBehaviours;
    private LinkedList checkAgents;
    private LinkedList listeAgents;
    private LinkedList listeMots;

    private LinkedList<BehaviouralWealth> listRichesseComportementale;
    
    public MyProject(String mySourceCode) {
        this.mySourceCode = mySourceCode;
        // l'initialisation des listes
            
        checkBehaviours = new LinkedList();
        listeBehaviours = new LinkedList();
        listeAgents = new LinkedList();
        checkAgents = new LinkedList();
        listeObjects = new LinkedList();
        listeMots = new LinkedList();
        listRichesseComportementale = new LinkedList<BehaviouralWealth>();
        // passage par reference
        Obj1.listeAgents = listeAgents;
        Obj2.listeBehaviours = listeBehaviours;
        Obj3.listeObjects = listeObjects;
        
        activateNow();
    }
    
    /* here it is a new method */
    public void calculerAllRichesseComportementale(){
    	System.out.println("calculerAllRichesseComportementale : ");
    	for (int i = 0; i < listeAgents.size(); i++) {
    		String agent = ((Obj1)listeAgents.get(i)).nom;
    		float behaviouralWealth = ((Obj1)listeAgents.get(i)).calculerRichesseComportementale();
    		listRichesseComportementale.add(new BehaviouralWealth(agent, behaviouralWealth));
		}
    }
    
    LinkedList<BehaviouralWealth> getBehaviouralWealthList(){
    	return listRichesseComportementale;
    }
    
    public void activateNow(){
        remplirMots();
        remplirAgents();
        remplirComportements();
        remplirObjects();
    }
    
    /******************** LES MOTS ********************/
    
    private void remplirMots(){
            
        String tmp = ProjectUtils.eclatement(mySourceCode+" ");
        StringTokenizer token = new StringTokenizer(tmp," \t");
        
        LinkedList l = new LinkedList();
        
        while(token.hasMoreTokens()){
            l.add(token.nextToken());
        }
        trick1(l);
        trick2(l);
        for(int i=0;i<l.size();i++)
            listeMots.add(l.get(i));
        trick3();
    }
        
    private void trick1(LinkedList l){
        // eliminer les chaines de caractères
        int i = 0;
        while(i<l.size()){
            if(i<l.size() && l.get(i).equals("\"")){
                for(i=i+1;i<l.size();i++){
                    if(l.get(i).equals("\"")){ i++; break;}
                    else l.set(i, "");    
                        }
            }
            i++;
        }
    }
    
    private void trick2(LinkedList l){
        // eliminer les caractères
        int i = 0;
        while(i<l.size()){
            if(i<l.size() && l.get(i).equals("'")){
                for(i=i+1;i<l.size();i++){
                    if(l.get(i).equals("'")){ i++; break;}
                    else l.set(i, "");    
                        }
            }
            i++;
        }
    }
    
    private void trick3(){
        // eliminer les commentaires
        int i = 0;
        while(i<listeMots.size()){
            if(listeMots.get(i).equals("//")) eliminerCom(i,"\n");
            if(listeMots.get(i).equals("/*")) eliminerCom(i,"*/");
            i++;
        }
    }
    
    private void eliminerCom(int i,String tmp){
        // eliminer un commentaire
        int j = i+1;
        boolean found = false;
        while(j<listeMots.size() && !found){
            if(listeMots.get(j).equals(tmp)) found = true;
            else listeMots.set(j, "");
            j++;
        }
    }
    
    /******************** LES AGENTS ********************/
    
    private void remplirAgents(){
        // remplir la liste des agents
        do{
            finish = false;
                String code;
                String upper;
                int acc = 0;
                int nom ;
                int j ;
                
                boolean flag = false;
        for(int i=0;i<listeMots.size();i++){
            if(listeMots.get(i).equals("extends") && isAgent(i+1)){
                nom = i-1;
                code = "class " + listeMots.get(nom) + " extends " + listeMots.get(i+1) + " ";
                  upper = listeMots.get(i+1).toString();
                  j=i+2; 
                  while(j<listeMots.size() && !flag){
                    if(listeMots.get(j).equals("{")){acc++;}
                    if(listeMots.get(j).equals("}")){
                        acc--;
                        if(acc==0) flag=true;
                    }
                    code = code + " " + listeMots.get(j);
                    j++;
                }
                if(flag){
                    String s = (String) listeMots.get(nom);
                    Obj1 o = new Obj1(s,code,upper);
                    if(!upper.equals("Agent"))
                        getInheritance(o,upper);
                    if(!redondant1(o))
                        listeAgents.add(o);
                    acc = 0;
                    code = "";
                    upper="";
                    flag = false;
                }
            }
        }
        }while(finish);
    }
    
    private boolean isAgent(int i){
        String tmp = listeMots.get(i).toString();
        if(tmp.equals("Agent")){
            if(!checkAgents.contains(listeMots.get(i-2))){
                checkAgents.add(listeMots.get(i-2));
                finish = true;
            }
            return true;
        }
        else{
            if(checkAgents.contains(tmp)){
                if(!checkAgents.contains(listeMots.get(i-2))){
                    checkAgents.add(listeMots.get(i-2));
                    finish = true;
                }
                return true;
            }
            else return false;
        }
    }
    
    private void getInheritance(Obj1 o,String tmp){
        // mise a jour des valeurs grace a l'heritage
        for(int i=0;i<listeAgents.size();i++){
            Obj1 e = (Obj1) listeAgents.get(i);
            if(e.nom.equals(tmp)){
                break;
            }
        } 
    }
    
    private boolean redondant1(Obj1 o){
        // verifier si un Agent existe deja dans la liste des Agents
        boolean b=false;
        for(int i=0;i<listeAgents.size();i++){
            Obj1 e = (Obj1) listeAgents.get(i);
            if(e.nom.equals(o.nom)) b = true;
        }
        return b;
    }
    
        /******************** LES COMPORTEMENTS ********************/
    
        private void remplirComportements(){
        // remplir la liste des comportements
        do{
        finish = false;
        String nom;
        String code;
        String upperClass;
        for(int i=0;i<listeMots.size();i++){
            if(listeMots.get(i).equals("extends") && isBehaviour(i+1)){
                nom = listeMots.get(i-1).toString() ;
                code = "class " + nom + " extends " + listeMots.get(i+1) + " ";
                upperClass = listeMots.get(i+1).toString();
                int j = i+2;
                int acc = 0;
                boolean flag = false;
                while(j<listeMots.size() && !flag){
                    if(listeMots.get(j).equals("{")){ acc++;}
                    if(listeMots.get(j).equals("}")){
                        acc--;
                        if(acc==0) flag = true;
                    }
                    code = code + " " + listeMots.get(j);
                    j++;
                }
                if(flag){
                    Obj2 o = new Obj2(nom,code,upperClass);
                    if(!redondant2(o))
                        listeBehaviours.add(o);
                    nom = "";
                    code = "";
                    upperClass = "";
                }
            }
        }
        }while(finish);
    }
    
    private boolean isBehaviour(int i){
        // verifier si une class est heritée a partir d'une class Behaviour
        String tmp = listeMots.get(i).toString();
        if(tmp.equals("Behaviour") || tmp.equals("OneShotBehaviour") || tmp.equals("CyclicBehaviour") || tmp.equals("FSMBehaviour")){
            if(!checkBehaviours.contains(listeMots.get(i-2))){
                checkBehaviours.add(listeMots.get(i-2));
                finish = true;
            }
            return true;
        }
        else{
            if(checkBehaviours.contains(tmp)){
                if(!checkBehaviours.contains(listeMots.get(i-2))){
                    checkBehaviours.add(listeMots.get(i-2));
                    finish = true;
                }
                return true;
            }
            else return false;
        }
    }
        
    private boolean redondant2(Obj2 o){
        // verifier si un comportement existe deja dans la liste des comportements
        boolean b=false;
        for(int i=0;i<listeBehaviours.size();i++){
            Obj2 e = (Obj2) listeBehaviours.get(i);
            if(e.nom.equals(o.nom)) b = true;
        }
        return b;
    }
        
    /******************** LES OBJETS ********************/
    
    private void remplirObjects(){
        // remplir la liste des objets
        String upperClass = "";
        String code = "";
        String nom = "";
        
        for(int i=0;i<listeMots.size();i++){
            if(listeMots.get(i).equals("class") && !(isSymbol(listeMots.get(i+1).toString()) || isSymbol(listeMots.get(i+2).toString())) &&!isAgent(i+3) && !isBehaviour(i+3)){
                if(listeMots.get(i+2).equals("extends"))
                    upperClass = listeMots.get(i+3).toString();
                code = "class " + listeMots.get(i+1) + " ";
                nom = listeMots.get(i+1).toString();
                int j = i+2;
                int acc = 0;
                boolean flag = false;
                  while(j<listeMots.size() && !flag){
                    if(listeMots.get(j).equals("{")){acc++;}
                    if(listeMots.get(j).equals("}")){
                        acc--;
                        if(acc==0) flag=true;
                    }
                    code = code + " " + listeMots.get(j);
                    j++;
                  }
                  if(flag){
                      Obj3 o = new Obj3(code,nom,upperClass);
                      if(!redondant3(o))
                          listeObjects.add(o);
                      nom = "";
                      code = "";
                      upperClass = "";
                  }
            }
        }
    }
    
    private boolean redondant3(Obj3 o){
        // verifier si un Objet existe deja dans la liste des Objets
        boolean b=false;
        for(int i=0;i<listeObjects.size();i++){
            Obj3 e = (Obj3) listeObjects.get(i);
            if(e.nom.equals(o.nom)) b = true;
        }
        return b;
    }
        
    private boolean isSymbol(String tmp){
        if(tmp.equals("=") || tmp.equals(";") || tmp.equals(",") || tmp.equals("(") || tmp.equals(")") || tmp.equals(".") || tmp.equals("!") || tmp.equals("[") || tmp.equals("]") || tmp.equals("\"") || tmp.equals("\'"))
            return true;
        return false;
    }
}
