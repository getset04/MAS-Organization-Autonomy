package statique;

//la classe Obj1 represente l'agent

import java.util.LinkedList;
import java.util.StringTokenizer;


public class Obj1 {
 
 String nom;
 String code;
 
 String upperClass;
 int deep;
 
 String setup;

 public static LinkedList listeAgents;
     
 //nouvelle métrique
 float richesseComportementale;
 
 public float calculerRichesseComportementale(){
	 float nombreComportement = 0;
	 float nombreComportementTotal = Obj2.listeBehaviours.size();
	 LinkedList<String> tokenList = new LinkedList<String>();
	 StringTokenizer token = new StringTokenizer(code," \t");
	 
     while(token.hasMoreTokens()){
    	 String nextToken = token.nextToken();
         tokenList.add(nextToken);
     }
     
     for (int i = 0; i < tokenList.size(); i++) {
    	 String token1 = tokenList.get(i);
    	 if(token1.equals("class")){
    		 String token2 = tokenList.get(i+2);
    		 if(token2.equals("extends")){
    			 String token3 = tokenList.get(i+1);
    			 if(isInMyBehaviourList(token3)){
    				 nombreComportement++;
    			 }
    		 }
    	 }
	}
     
	 richesseComportementale = nombreComportement / nombreComportementTotal;
	 return richesseComportementale;
 }
 
 boolean isInMyBehaviourList(String behaviour){
	 for (int i = 0; i < Obj2.listeBehaviours.size(); i++) {
		 Obj2 obj2 = (Obj2) Obj2.listeBehaviours.get(i);
		 if(obj2.nom.equals(behaviour)) return true;
	}
	 return false;
 }
 
 public Obj1(String nom, String code, String upperClass) {
     this.nom = nom;
     this.code = ProjectUtils.refreshCode(code);
     this.upperClass = upperClass;
     this.deep = this.InheritanceDeep();
     findSetup();
 }
 
 private void eliminerInnerClass(LinkedList l){
     int acc = 0;
     boolean flag = false;
     for(int i=4;i<l.size();i++){
         if(l.get(i).equals("class")){
             flag = true;
             while(i<l.size() && flag){
                 if(l.get(i).equals("{")) acc++;
                 if(l.get(i).equals("}")){
                     acc--;
                     if(acc==0) flag = false;
                 }
                 l.set(i, "");
                 i++;
             }
         }
     }
 }
 
 private void findSetup(){
     LinkedList l = new LinkedList();
     StringTokenizer s = new StringTokenizer(this.code," ");
     while(s.hasMoreTokens()){
         l.add(s.nextToken());
     }
     eliminerInnerClass(l);
     String tmp = "";
     boolean flag = false;
     int acc = 0;
     for(int i=0;i<l.size()-3;i++){
         if(l.get(i).equals("void") && l.get(i+1).equals("setup") && l.get(i+2).equals("(") && l.get(i+3).equals(")")){
             int j = i;
             while(j<l.size() && !flag){
                 if(l.get(j).equals("{")){acc++;}
                 if(l.get(j).equals("}")){
                     acc--;
                     if(acc==0) flag=true;
                 }
                 tmp = tmp + " " + l.get(j);
                 j++;
             }
             if(flag){
                 break;
             }
         }
     }
     this.setup = tmp;
 }
 
 
 private int InheritanceDeep(){
     if(this.upperClass.equals("Agent")) return 1;
     else{
         int i =0;
         while(i<listeAgents.size()){
             Obj1 o = (Obj1) listeAgents.get(i);
             if(this.upperClass.equals(o.nom)) return 1+o.InheritanceDeep();
             else i++;
         }
     }
     return 0;
 }
 
 private boolean existe(String tmp,int j,LinkedList l){
     int i = 0;
     while(i<l.size() && i<j){
         if(l.get(i).equals(tmp)) return true;
         i++;
     }
     return false;
 }
 
     private void eliminerSymbole(LinkedList l){
     int i = 0;
     while(i<l.size()){
         if(i<l.size() && l.get(i).equals("(")){
             for(i=i+1;i<l.size();i++){
                 if(l.get(i).equals(")")) break;
                 else l.set(i, "");    
                     }                
         }
         if(i<l.size() && l.get(i).equals("[")){
             for(i=i+1;i<l.size();i++){
                 if(l.get(i).equals("]")) break;
                 else l.set(i, "");    
                     }
         }
         i++;
     }
 }
 
private void eliminerAccolade(LinkedList l){
     int i = 0;
     int c = 0;
     while(i<l.size()){
         if(l.get(i).equals("{")){
           if(c>2)
               l.set(i, "");
           c++;
         }else{
             if(l.get(i).equals("}")){
                 if(c>2) l.set(i, "");
                 c--;
             }
             else{
                 if(c>1) l.set(i, "");
             }
         }
         i++;
     }
 }
 
 String getName(){
     return nom;
 }
 
 String getSuper(){
     return upperClass;
 }
 
 String getCode(){
     return code;
 }
 
}