package com.acmetelecom.printer;

import org.joda.time.DateTime;

import com.acmetelecom.time.DateStringUtils;

/**
 * Outputs bill in HTML format to the console
 */
public class HtmlPrinter implements Printer {

    public void printHeading(String name, String phoneNumber, String pricePlan) {
        beginHtml();
        System.out.println(h2(name + "/" + phoneNumber + " - " + "Price Plan: " + pricePlan));
        beginTable();
    }
    
    public void printItem(DateTime time, String callee, String duration, String cost) {
    	System.out.println(tr(td(DateStringUtils.dateToBillingFormat(time)) + td(callee) + td(duration) + td(cost)));
    }
    
    public void printTotal(String total) {
    	endTable();
    	System.out.println(h2("Total: " + total));
    	endHtml();
    }

    private void beginTable() {
        System.out.println("<table border=\"1\">");
        System.out.println(tr(th("Time") + th("Number") + th("Duration") + th("Cost")));
    }

    private void endTable() {
        System.out.println("</table>");
    }

    private String h2(String text) {
        return "<h2>" + text + "</h2>";
    }

    private String tr(String text) {
        return "<tr>" + text + "</tr>";
    }

    private String th(String text) {
        return "<th width=\"160\">" + text + "</th>";
    }

    private String td(String text) {
        return "<td>" + text + "</td>";
    }

    private void beginHtml() {
        System.out.println("<html>");
        System.out.println("<head></head>");
        System.out.println("<body>");
        System.out.println("<h1>");
        System.out.println("Acme Telecom");
        System.out.println("</h1>");
    }

    private void endHtml() {
        System.out.println("</body>");
        System.out.println("</html>");
    }
}
