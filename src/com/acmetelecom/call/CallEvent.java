package com.acmetelecom.call;

import com.acmetelecom.customer.Customer;

public class CallEvent {
    private String caller;
    private String callee;
    private long time;

    public CallEvent(Customer caller, Customer callee, long timeStamp) {
        this.caller = caller.getPhoneNumber();
        this.callee = callee.getPhoneNumber();
        this.time = timeStamp;
    }

    public String getCaller() {
        return caller;
    }

    public String getCallee() {
        return callee;
    }

    public long time() {
        return time;
    }
}
