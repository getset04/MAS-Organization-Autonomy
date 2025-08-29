package ExampleWithOrganization;

import java.io.Serializable;

public class MyEvent implements Serializable{
    
    int line;
    int column;
    String group;
    String service;

    public MyEvent(int line, int column, String group, String service) {
        this.line = line;
        this.column = column;
        this.group = group;
        this.service = service;
    }
    
    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    public String getGroup() {
        return group;
    }

    public String getService() {
        return service;
    }

    @Override
    public String toString() {
        return "MyEvent{" + "line=" + line + ", column=" + column + ", group=" + group + ", service=" + service + '}';
    }
    
}
