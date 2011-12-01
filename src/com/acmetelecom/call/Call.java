package com.acmetelecom.call;

import java.util.Date;

import com.acmetelecom.time.DateUtils;

public class Call {
    private CallEvent start;
    private CallEvent end;

    public Call(CallEvent start, CallEvent end) {
        this.start = start;
        this.end = end;
    }

    public String callee() {
        return start.getCallee();
    }

    public int durationSeconds() { 
        return (int) (((end.time() - start.time()) / 1000));
    }

    public String date() {
	return DateUtils.dateToBillingFormat(new Date(start.time()));
    }

    public Date startTime() {
        return new Date(start.time());
    }

    public Date endTime() {
        return new Date(end.time());
    }
}
