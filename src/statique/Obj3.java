package statique;

//la classe Obj3 represente les objets

import java.util.LinkedList;
import java.util.StringTokenizer;


public class Obj3 {

 String nom;
 String code;
 
 String upperClass;
 int deep;
 
 public static LinkedList listeObjects;
 
 public Obj3(String code, String nom, String upperClass) {
         this.code = code;
         this.nom = nom;
         this.upperClass = upperClass;
         this.deep = this.InheritanceDeep();
 }
 private int eod(int j, LinkedList l) {
     // End Of Declaration : indique la fin d'une declaration
     int c = 0;
     boolean flag = false;
     for(int i=j;i<l.size();i++){
         if(l.get(i).equals(",")){ c++; }
         if(l.get(i).equals(";")){ c++; break; }
         if(l.get(i).equals("=")){ flag = true; }
         if(l.get(i).equals("(")){ if(!flag) return 0; }
         if(l.get(i).equals("class")){ return 0; }
     }
     return c;

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

 private int InheritanceDeep(){
     if(this.upperClass.length()==0){
         return 0;
     }
     else{
         int i =0;
         while(i<listeObjects.size()){
             Obj3 o = (Obj3) listeObjects.get(i);
             if(this.upperClass.equals(o.nom))
                 return 1+o.InheritanceDeep();
             else i++;
         }
     }
     return 1;
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