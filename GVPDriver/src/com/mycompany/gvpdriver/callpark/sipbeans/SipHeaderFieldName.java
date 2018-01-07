package com.mycompany.gvpdriver.callpark.sipbeans;

public class SipHeaderFieldName {

    private final static SipHeadersTable SIP_HEADER_TABLE =
        new SipHeadersTable();

    private String name;

    public SipHeaderFieldName(String s) {
        super();
        if (s.length() == 1) {
            this.name = SIP_HEADER_TABLE.getLongForm(s.charAt(0));
        } else {
            this.name = s;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        String objName = ((SipHeaderFieldName)obj).getName();
        if (name.equalsIgnoreCase(objName)) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
    
    public String getName() {
        return name;
    }
    
    @Override
    public String toString() {
        return name;
    }
}