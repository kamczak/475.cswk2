package com.acmetelecom.call;

import org.joda.time.DateTime;

public class CallEvent {
    private String caller;
    private String callee;
    private DateTime dateTime;

    public CallEvent(String caller, String callee, DateTime timeStamp) {
        this.caller = caller;
        this.callee = callee;
        this.dateTime = timeStamp;
    }

    public String getCaller() {
        return caller;
    }

    public String getCallee() {
        return callee;
    }

    public DateTime getDateTime() {
        return dateTime;
    }
}
