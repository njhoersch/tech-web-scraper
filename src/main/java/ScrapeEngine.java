import com.fasterxml.jackson.databind.ObjectMapper;
import models.Event;
import services.crawlers.ComputerWorldService;
import services.crawlers.TechMemeService;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static spark.Spark.*;

public class ScrapeEngine {
    private static final int NUMBER_OF_SITES = 2;
    private static final int PORT = 4567;

    public static void main(String[] args) {
        // Set port for server to start on
        port(PORT);

        // Enable CORS
        before((request, response) -> response.header("Access-Control-Allow-Origin", "*"));

        // Test Get Endpoint
        get("/hello", (req, res)->"hello world");

        // Events Endpoint
        get("/events", (req,res)->{
            System.out.println("Data Requested: '/events'");

            ArrayList<Event> events = new ArrayList<>();

            try {
                ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_OF_SITES);
                Future<ArrayList<Event>> techMemeEvents = executorService.submit(new TechMemeService());
                Future<ArrayList<Event>> computerWorldEvents = executorService.submit(new ComputerWorldService());

                events.addAll(techMemeEvents.get());
                events.addAll(computerWorldEvents.get());

            } catch (Exception e) {
                e.printStackTrace();
            }

            ObjectMapper objectMapper = new ObjectMapper();

            return objectMapper.writeValueAsString(events);
        });

        System.out.println("Server started on port: " + PORT);
    }
}

