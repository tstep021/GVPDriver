package com.mycompany.gvpdriver.callpark.sipbeans;

import java.util.HashMap;

import com.mycompany.gvpdriver.callpark.SipConstants;

public class SipHeaderFieldValue {

    private String value;
    
    private HashMap<SipHeaderParamName, String> params;
    
    public SipHeaderFieldValue(String val) {
        int startPos = value.indexOf(SipConstants.RIGHT_ANGLE_BRACKET);
        int pos;
        if (startPos > -1) {
            pos = val.indexOf(SipConstants.PARAM_SEPARATOR, startPos);
        } else {
            pos = val.indexOf(SipConstants.PARAM_SEPARATOR);
        }
        String paramsString;
        if (pos > -1) {
            this.value = val.substring(0,pos);
            paramsString = val.substring(pos);
        } else {
            this.value = val;
            paramsString = "";
        }
        params = new HashMap<SipHeaderParamName, String>();
        if (paramsString.contains(SipConstants.PARAM_SEPARATOR)) {
            String[] arr = paramsString.split(SipConstants.PARAM_SEPARATOR);
            if (arr.length > 1) {
                for (int i = 1; i < arr.length; ++i) {
                    String paramName = arr[i];
                    String paramValue = "";
                    pos = paramName.indexOf(SipConstants.PARAM_ASSIGNMENT);
                    if (pos > -1) {
                        paramName = arr[i].substring(0, pos);
                        paramValue = arr[i].substring(pos + 1);
                    }
                    params.put(new SipHeaderParamName(paramName), paramValue);
                }
            }
        }
    }

    public String getParam(SipHeaderParamName name) {
        return params.get(name);
    }
    
    public void addParam(SipHeaderParamName name, String val) {
        params.put(name, val);
    }
    
    public void removeParam(SipHeaderParamName name) {
        params.remove(name);
    }
    
    public String getValue() {
        return value;
    }

    public void setValue(String val) {
        this.value = val;
    }

    @Override
    public String toString() {
        if (params == null || params.isEmpty()) {
            return value;
        }
        StringBuffer buf = new StringBuffer(value);
        for (SipHeaderParamName name: params.keySet()) {
            buf.append(SipConstants.PARAM_SEPARATOR).append(name);
            String val = params.get(name);
            if (!"".equals(value.trim())) {
                buf.append(SipConstants.PARAM_ASSIGNMENT).append(val);
            }
        }
        return buf.toString();
    }

}