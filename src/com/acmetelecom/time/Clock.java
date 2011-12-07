package com.acmetelecom.time;

import org.joda.time.DateTime;

/**
 * Interface for getting current date and time
 */
public interface Clock {
    public DateTime getCurrentDateTime();
}
