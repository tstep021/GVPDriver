package com.mycompany.gvpdriver.callpark.sipbeans;

public class SipHeader {

    private SipHeaderFieldName name;
    private SipHeaderFieldValue value;
    
    SipHeader(SipHeaderFieldName n, SipHeaderFieldValue v) {
        super();
        this.name = n;
        this.value = v;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SipHeader) {
            SipHeader objHdr = (SipHeader) obj;
            return name.equals(objHdr.name);
        }
        return false;
    }

    public SipHeaderFieldName getName() {
        return name;
    }

    public SipHeaderFieldValue getValue() {
        return value;
    }

    public void setValue(SipHeaderFieldValue v) {
        this.value = v;
    }
    
}