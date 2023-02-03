package cs.dawson.dawsonelectriccurrents.cancelled;

import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import cs.dawson.dawsonelectriccurrents.beans.CancelledClass;

/**
 * Class which takes in a URL and parses it in order to get the data from the RSSFeed.
 * Created by: Alessandro Ciotola
 */
public class RssFeeder
{
    private String url;

    /**
     * Constructor which initializes the URL
     *
     * @param url
     */
    public RssFeeder(String url)
    {
        this.url = url;
    }

    /**
     * Method which parses the URL and returns the items.
     *
     * @return
     * @throws Exception
     */
    public List<CancelledClass> getItems() throws Exception
    {

        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();

        RssParseHandler handler = new RssParseHandler();
        saxParser.parse(url, handler);

        return handler.getItems();
    }
}
