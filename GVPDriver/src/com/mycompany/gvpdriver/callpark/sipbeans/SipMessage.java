package com.mycompany.gvpdriver.callpark.sipbeans;

import com.mycompany.gvpdriver.callpark.SipConstants;

public class SipMessage {
    
    protected String sipVersion;
    protected SipHeaders sipHeaders;
    protected byte[] body;
    
    protected String method;
    protected SipURI requestUri;

    public String getMethod() {
        return method;
    }

    public SipURI getRequestUri() {
        return requestUri;
    }
    

    public SipMessage(String meth, SipURI reqUri) {
        sipVersion = SipConstants.DEFAULT_SIP_VERSION;
        sipHeaders = new SipHeaders();
        this.method = meth;
        this.requestUri = reqUri;
    }
    
    public String getSipVersion() {
        return sipVersion;
    }

    public void setSipHeaders(SipHeaders sipHdrs) {
        this.sipHeaders = sipHdrs;
    }

    public SipHeaders getSipHeaders() {
        return sipHeaders;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] bb) {
        SipHeaderFieldName contentLengthName =
            new SipHeaderFieldName(SipConstants.HDR_CONTENT_LENGTH);
        SipHeaderFieldValue contentLengthValue =
            sipHeaders.get(contentLengthName);
        if (contentLengthValue == null) {
            contentLengthValue = new SipHeaderFieldValue(
                    String.valueOf(bb.length));
            sipHeaders.add(contentLengthName, contentLengthValue);
        } else {
            contentLengthValue.setValue(String.valueOf(bb.length));
        }
        this.body = bb;
    }

    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append(method).append(' ').append(requestUri).append(
        ' ').append(SipConstants.DEFAULT_SIP_VERSION).append(SipConstants.CRLF);
        
        buf.append(sipHeaders.toString());
        buf.append(SipConstants.CRLF);
        if (body != null) {
            buf.append(new String(body));
        }
        return buf.toString();
    }    
}