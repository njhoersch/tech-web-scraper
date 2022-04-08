package services.crawlers;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import models.Event;
import services.WebClientService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class ComputerWorldService implements Callable<ArrayList<Event>> {
    private static final String baseUrl = "https://www.computerworld.com/article/3313417/tech-event-calendar-shows-conferences-and-it-expos-updated.html";
    private static final String websiteName = "Computer World";

    @Override
    public ArrayList<Event> call() throws Exception {
        Thread.sleep(1000);
        return getEventData();
    }

    private static ArrayList<Event> getEventData() {
        WebClient webClient = new WebClientService().getWebClient();

        ArrayList<Event> events = new ArrayList<>();

        try {
            HtmlPage page = webClient.getPage(baseUrl);

            List<DomNode> nodes = (List<DomNode>) page.getByXPath("//table[@id='cwsearchabletable']//tbody//tr//th");

            for (DomNode node : nodes) {
                events.add(getEventsFromNodes(node));
            }

        } catch (NullPointerException e) {
            System.out.println("Error occurred while traversing nodes: " + e);
        } catch (IOException e) {
            System.out.println("An error occurred: " + e);
        }

        return events;
    }

    private static Event getEventsFromNodes(DomNode topNode) throws NullPointerException {
        String title;
        String startDate;
        String endDate;
        String location;

        title = topNode.getFirstChild().getFirstChild().asText();

        topNode = topNode.getNextSibling().getNextSibling().getNextSibling().getNextSibling();
        startDate = topNode.getFirstChild().asText();

        topNode = topNode.getNextSibling().getNextSibling();
        endDate = topNode.getFirstChild().asText();

        topNode = topNode.getNextSibling().getNextSibling();
        location = topNode.getFirstChild().asText();

        return new Event(websiteName, title, startDate, endDate, location);
    }

    // For Testing Purposes
    public static void printEvents(ArrayList<Event> events) {
        for (Event e : events) {
            System.out.println("Event Name: " + e.getTitle()
                    + "\nEvent Location: " + e.getLocation()
                    + "\nEvent Start Date: " + e.getStartDate()
                    + "\nEvent End Date: " + e.getEndDate()
                    + "\nSingle Day Event: " + e.isSingleDayEvent()
                    + "\n-------------------------------------"
            );
        }
        System.out.println("Total Events: " + events.toArray().length);
    }

}
