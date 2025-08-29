package statique;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.LinkedList;
import java.util.StringTokenizer;

/**
 *
 * @author Home
 */
public class ProjectUtils {
    // contient toutes les fonctionalitites statiques
    
    public static String eclatement(String s){
        // pour séparer les caractères speciaux
        String t = "";
        for(int i=0;i<s.length();i++){
            if(s.charAt(i)=='/' && s.charAt(i+1)=='/'){ t = t + " // "; i=i+2;}
            if(s.charAt(i)=='/' && s.charAt(i+1)=='*'){ t = t + " /* "; i=i+2;}
            if(s.charAt(i)=='*' && s.charAt(i+1)=='/'){ t = t + " */ "; i=i+2;}
            if(s.charAt(i)=='!' && s.charAt(i+1)=='='){ t = t + " != "; i=i+2;}
            if(s.charAt(i)=='=' && s.charAt(i+1)=='='){ t = t + " == "; i=i+2;}
            if(s.charAt(i)==';' || s.charAt(i)=='{' || s.charAt(i)=='}' || s.charAt(i)=='(' || s.charAt(i)==')' || s.charAt(i)=='[' || s.charAt(i)==']' || s.charAt(i)==':' || s.charAt(i)==',' || s.charAt(i)=='=' || s.charAt(i)=='\"')
                t = t + " " + s.charAt(i) + " ";
            else{
                if(s.charAt(i)=='\n') t = t + " " + s.charAt(i) + " ";
                else t = t + s.charAt(i);
            }
                
        }
        return t;
    }
        
    public static String refreshCode(String s){
        // pour separer les point
        String t = "";
        
        for(int i=0;i<s.length();i++){
            if(s.charAt(i)=='.')
                t = t + " " + s.charAt(i) + " ";
            else{
                if(s.charAt(i)=='\n') t = t + " " + s.charAt(i) + " ";
                else t = t + s.charAt(i);
            }
                
        }
        
        return t;
    }
            
}
