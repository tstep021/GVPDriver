package com.mycompany.gvpdriver.callpark.sipbeans;

public class SipHeaderParamName {

    private String name;
    
    public SipHeaderParamName(String s) {
        this.name = s;
    }
    
    
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        String objName = ((SipHeaderParamName)obj).getName();
        if (name.equalsIgnoreCase(objName)) {
            return true;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return name.toLowerCase().hashCode();
    }
    
    @Override
    public String toString() {
        return name;
    }
}