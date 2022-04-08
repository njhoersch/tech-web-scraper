package services.crawlers;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import models.Event;
import services.DateService;
import services.WebClientService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class TechMemeService implements Callable<ArrayList<Event>> {
    private static final String baseUrl = "https://www.techmeme.com/events";
    private static final String websiteName = "Tech Meme";

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

            List<DomNode> nodes = (List<DomNode>) page.getByXPath("//div[@class='rhov']");

            for (DomNode node : nodes) {
                events.add(getEventsFromNodes(node));
            }

        } catch (NullPointerException e) {
            System.out.println("Error occurred while traversing nodes: " + e);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return events;
    }

    private static Event getEventsFromNodes(DomNode topNode) throws NullPointerException {
        String title;
        String date;
        String location;

        topNode = topNode.getFirstChild().getFirstChild();
        date = topNode.getFirstChild().asText();

        topNode = topNode.getNextSibling();
        title = topNode.getFirstChild().asText();

        if (topNode.getFirstChild().getLocalName() != null) {
            title = title + " " + topNode.getFirstChild().getNextSibling().asText();
        }

        topNode = topNode.getNextSibling();
        location = (topNode.hasChildNodes()) ? topNode.getFirstChild().asText() : "Location Unknown";

        return  DateService.convertTechMemeDatesToStandardDates(new Event(websiteName, title, null, null, location), date);
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
