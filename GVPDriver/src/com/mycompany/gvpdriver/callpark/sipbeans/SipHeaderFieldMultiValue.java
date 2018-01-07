package com.mycompany.gvpdriver.callpark.sipbeans;

import java.util.List;

public class SipHeaderFieldMultiValue extends SipHeaderFieldValue {

    private List<SipHeaderFieldValue> values;
    
    private static String toString(List<SipHeaderFieldValue> list) {
        if (list == null) {
            return null;
        }
        String arrToString = list.toString();
        return arrToString.substring(1, arrToString.length() - 1);
    }
    
    public SipHeaderFieldMultiValue(List<SipHeaderFieldValue> vv) {
        super(toString(vv));
        this.values = vv;
    }

    public List<SipHeaderFieldValue> getValues() {
        return values;
    }
    
    @Override
    public String toString() {
        return toString(values);
    }
}