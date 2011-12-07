package com.acmetelecom.time;

import org.joda.time.DateTime;

/**
 * This implementation of the Clock interface always returns the actual time
 * on the server.
 *
 */
public class SystemClock implements Clock {

    @Override
    public DateTime getCurrentDateTime() {
        return DateTime.now();
    }

}
