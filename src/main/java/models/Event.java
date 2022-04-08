package models;

public class Event {
    private final String website;
    private final String title;
    private final String startDate;
    private final String endDate;
    private final String location;

    public Event(String website, String title, String startDate, String endDate, String location) {
        this.website = website;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.location = location;
    }

    public String getWebsite() {
        return website;
    }

    public String getTitle() {
        return title;
    }

    public String getLocation() {
        return location;
    }

    public String getStartDate() { return startDate; }

    public String getEndDate() { return endDate; }

    public Boolean isSingleDayEvent() {
        return this.endDate == null || this.endDate.equals(this.startDate);
    }
}
