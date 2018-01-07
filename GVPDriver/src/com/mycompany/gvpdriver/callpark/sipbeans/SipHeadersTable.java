package com.mycompany.gvpdriver.callpark.sipbeans;

import java.util.HashMap;

import com.mycompany.gvpdriver.callpark.SipConstants;

public class SipHeadersTable {
    
    private HashMap<Character, String> headers;
    
    /**
     * should be instanciated only once, it was a singleton.
     */
    public SipHeadersTable() {
        headers = new HashMap<Character, String>();
        //RFC 3261 Section 10
        headers.put(SipConstants.COMPACT_HDR_CALLID,           SipConstants.HDR_CALLID);
        headers.put(SipConstants.COMPACT_HDR_CONTACT,          SipConstants.HDR_CONTACT);
        headers.put(SipConstants.COMPACT_HDR_CONTENT_ENCODING, SipConstants.HDR_CONTENT_ENCODING);
        headers.put(SipConstants.COMPACT_HDR_CONTENT_LENGTH,   SipConstants.HDR_CONTENT_LENGTH);
        headers.put(SipConstants.COMPACT_HDR_CONTENT_TYPE,     SipConstants.HDR_CONTENT_TYPE);
        headers.put(SipConstants.COMPACT_HDR_FROM,             SipConstants.HDR_FROM);
        headers.put(SipConstants.COMPACT_HDR_SUBJECT,          SipConstants.HDR_SUBJECT);
        headers.put(SipConstants.COMPACT_HDR_SUPPORTED,        SipConstants.HDR_SUPPORTED);
        headers.put(SipConstants.COMPACT_HDR_TO,               SipConstants.HDR_TO);
        headers.put(SipConstants.COMPACT_HDR_VIA,              SipConstants.HDR_VIA);
    }
    
    public String getLongForm(char compactForm) {
        return headers.get(compactForm);
    }
    
}