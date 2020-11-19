
package com.fiable.service;

import com.fiable.hellorest.App;
import com.fiable.modal.EventItems;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

/**
 *
 * @author Thiyagaraja Kalidoss
 */
@Path("/events")
public class EventService {

    @Path("/{year}")
    @Produces(MediaType.APPLICATION_XML)
    @GET
    public String getEvents(@PathParam("year") String year) {
        Integer parsedInt = null;
        StringBuilder sb = new StringBuilder();
        try {
            parsedInt = Integer.parseInt(year);
        } catch (Exception e) {
           
        }
        if (parsedInt != null) {
            sb.append(getHeaderXML());
            sb.append("<channel>");
            sb.append("<title>" + parsedInt + "</title>");
            sb.append("<link>https://www.Asiahrm.com</link>");
            sb.append("<description> Events -" + parsedInt + "</description>");
            sb.append(getItems(parsedInt));
            sb.append("</channel>");
            sb.append("</rss>");
        } else {
            
        }

        return sb.toString();
    }
    private String getItems(Integer parsedInt){
        StringBuilder sb=new StringBuilder();
        List<EventItems> eventItems=getEventItems(parsedInt);
        for(EventItems items:eventItems){
            sb.append("<item>");
            sb.append("<title><![CDATA["+items.getTitle()+"]]></title>");
            sb.append("<link><![CDATA["+items.getLink()+"]]></link>");
            sb.append("<description><![CDATA["+items.getDescription()+"]]></description>");
            sb.append("</item>");
        }
        return sb.toString();
        
    }

    private String getHeaderXML() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<rss version=\"2.0\"\n"
                + "	xmlns:content=\"http://purl.org/rss/1.0/modules/content/\"\n"
                + "	xmlns:wfw=\"http://wellformedweb.org/CommentAPI/\"\n"
                + "	xmlns:dc=\"http://purl.org/dc/elements/1.1/\"\n"
                + "	xmlns:atom=\"http://www.w3.org/2005/Atom\"\n"
                + "	xmlns:sy=\"http://purl.org/rss/1.0/modules/syndication/\"\n"
                + "	xmlns:slash=\"http://purl.org/rss/1.0/modules/slash/\">";
    }

    private List<EventItems> getEventItems(Integer parsedInt) {
        List<EventItems> eventItems = new ArrayList<>();
        if (parsedInt != null) {
            try {
                String fName = parsedInt + "_Events_asiahrm.csv";
                Reader in = new FileReader(App.prop.getProperty("EventsDirPath") + fName);
                Iterable<CSVRecord> records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);

                for (CSVRecord rec : records) {
                    EventItems item = new EventItems(rec.get("Title"), rec.get("Link"), rec.get("Description"));
                    eventItems.add(item);
                }
            } catch (Exception e) {
               //e.printStackTrace();
            }

        }
        return eventItems;
    }

}
