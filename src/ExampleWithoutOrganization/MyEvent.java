package ExampleWithoutOrganization;

import java.io.Serializable;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Home
 */
public class MyEvent implements Serializable{
    
    int line;
    int column;
    String service;

    public MyEvent(int line, int column, String service) {
        this.line = line;
        this.column = column;
        this.service = service;
    }
    
    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    public String getService() {
        return service;
    }

    @Override
    public String toString() {
        return "MyEvent{" + "line=" + line + ", column=" + column + ", service=" + service + '}';
    }
    
}
