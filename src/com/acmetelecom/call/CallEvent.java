package com.acmetelecom.call;

import org.joda.time.DateTime;

/**
 * This class encapsulates all information needed to describe a call event:
 * 		1. Initiation
 * 		2. Completion
 */
public class CallEvent {
	// Represents telephone number of the caller
    private String caller;
    // Represents telephone number of the call recipient (callee)
    private String callee;
    // Records date+time of the call initialisation/completion
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
