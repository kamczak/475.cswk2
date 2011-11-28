package com.acmetelecom;

import java.util.List;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import com.acmetelecom.bill.BillingSystem;

public class ListSizeMatcher extends TypeSafeMatcher<List> {
    
    @Factory
    public static Matcher<List> aListOfSize(int size) {
	return new ListSizeMatcher(size);
    }
    
    private int size;
    
    public ListSizeMatcher(int size) {
	this.size = size;
    }

    @Override
    public void describeTo(Description description) {
	description.appendText("a list of size ").appendValue(size);
    }

    @Override
    protected boolean matchesSafely(List list) {
	return list.size() == size;
    }

}
