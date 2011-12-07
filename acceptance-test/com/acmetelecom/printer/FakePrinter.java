package com.acmetelecom.printer;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import com.acmetelecom.time.DateStringUtils;

/**
 * Allows for storing all the input in convenient lists, so that they can be returned later on.
 * Stores customers and their calls separately and flattens all calls to one list. Requires all
 * data on one customer to be printed in a sequence, that is items for customer A must not
 * interleave items of customer B. 
 * 
 */
public class FakePrinter implements Printer {

	private List<Heading> headings;
	private List<Call> calls;

	private String currentNumber;
	private Heading currentHeading;

	public FakePrinter()
	{
		headings = new ArrayList<Heading>();
		calls = new ArrayList<Call>();
	}

	/**
	 * Stores the data describing details of one customer.
	 */
	@Override
	public void printHeading(String name, String phoneNumber, String pricePlan) {
		currentHeading = new Heading(name, phoneNumber, pricePlan);
		headings.add(currentHeading);
		currentNumber = phoneNumber;

	}

	/**
	 * Stores one call item. Requires that the most recently printed heading described
	 * the callee passed as parameter to this method.
	 */
	@Override
	public void printItem(DateTime time, String callee, String duration, String cost) {
		calls.add(new Call(time, currentNumber, callee, duration, cost));
	}

	/**
	 * Stores the total cost of all calls by the customer whose heading was most recently printed. 
	 */
	@Override
	public void printTotal(String total) {
		currentHeading.setTotal(total);
	}

	/**
	 * Brings the FakePrinter to its initial state, clearing all the entries that have been
	 * printed so far.
	 */
	public void clear() {
		headings.clear();
		calls.clear();

	}

	/**
	 * 
	 * @return All headings that have been printed since last initialisation.
	 */
	public List<Heading> getCustomers() {
		return headings;
	}

	/**
	 * 
	 * @return All items that have been printed since last initialisation.
	 */
	public List<Call> getCalls() {
		return calls;
	}

	/**
	 * Simple container class for one heading printed with printHeading method
	 */
	public static class Heading {
		final private String name, phoneNumber, pricePlan;
		private String total;

		public Heading(String name, String phoneNumber, String pricePlan) {
			this.name = name;
			this.phoneNumber = phoneNumber;
			this.pricePlan = pricePlan;
		}

		public void setTotal(String total) {
			this.total = total;
		}

		public String getName() {
			return name;
		}

		public String getPhoneNumber() {
			return phoneNumber;
		}

		public String getPricePlan() {
			return pricePlan;
		}

		public String getTotal() {
			return total;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			result = prime * result + ((phoneNumber == null) ? 0 : phoneNumber.hashCode());
			result = prime * result + ((pricePlan == null) ? 0 : pricePlan.hashCode());
			result = prime * result + ((total == null) ? 0 : total.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Heading other = (Heading) obj;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			if (phoneNumber == null) {
				if (other.phoneNumber != null)
					return false;
			} else if (!phoneNumber.equals(other.phoneNumber))
				return false;
			if (pricePlan == null) {
				if (other.pricePlan != null)
					return false;
			} else if (!pricePlan.equals(other.pricePlan))
				return false;
			if (total == null) {
				if (other.total != null)
					return false;
			} else if (!total.equals(other.total))
				return false;
			return true;
		}

	}

	/**
	 * Simple container class for one item printed with printItem method
	 */
	public static class Call {
		final private String time, caller, callee, duration, cost;

		public Call(DateTime time, String caller, String callee, String duration, String cost) {
			this.time = DateStringUtils.dateToString(time);
			this.caller = caller;
			this.callee = callee;
			this.duration = duration;
			this.cost = cost;
		}

		public String getTime() {
			return time;
		}

		public String getCaller() {
			return caller;
		}

		public String getCallee() {
			return callee;
		}

		public String getDuration() {
			return duration;
		}

		public String getCost() {
			return cost;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((callee == null) ? 0 : callee.hashCode());
			result = prime * result + ((caller == null) ? 0 : caller.hashCode());
			result = prime * result + ((cost == null) ? 0 : cost.hashCode());
			result = prime * result + ((duration == null) ? 0 : duration.hashCode());
			result = prime * result + ((time == null) ? 0 : time.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Call other = (Call) obj;
			if (callee == null) {
				if (other.callee != null)
					return false;
			} else if (!callee.equals(other.callee))
				return false;
			if (caller == null) {
				if (other.caller != null)
					return false;
			} else if (!caller.equals(other.caller))
				return false;
			if (cost == null) {
				if (other.cost != null)
					return false;
			} else if (!cost.equals(other.cost))
				return false;
			if (duration == null) {
				if (other.duration != null)
					return false;
			} else if (!duration.equals(other.duration))
				return false;
			if (time == null) {
				if (other.time != null)
					return false;
			} else if (!time.equals(other.time))
				return false;
			return true;
		}
	}
}
