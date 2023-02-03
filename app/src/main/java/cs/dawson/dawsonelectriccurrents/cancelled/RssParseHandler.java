package cs.dawson.dawsonelectriccurrents.cancelled;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;
import cs.dawson.dawsonelectriccurrents.beans.CancelledClass;

/**
 * Class which takes the items from the RSSFeed, gets the data from the feed and stores them
 * inside a CancelledClass object.
 * Created by: Alessandro Ciotola
 *
 */
public class RssParseHandler extends DefaultHandler
{
    private static final String ITEM = "item";
    private static final String TITLE = "title";
    private static final String COURSE = "course";
    private static final String TEACHER = "teacher";
    private static final String PUBDATE = "pubDate";
    private List<CancelledClass> items;
    private CancelledClass currentItem;
    private boolean parsingTitle;
    private boolean parsingCourse;
    private boolean parsingTeacher;
    private boolean parsingDateTimeCancelled;

    /**
     * No param constructor which initializes the ArrayList.
     */
    public RssParseHandler()
    {
        items = new ArrayList<CancelledClass>();
    }

    /**
     * Method which returns a List of items
     *
     * @return
     */
    public List<CancelledClass> getItems()
    {
        return items;
    }

    /**
     * Method which checks if the beginning of the current element is an element which will
     * be used for the CancelledClass object.
     *
     * @param uri
     * @param localName
     * @param qName
     * @param attributes
     * @throws SAXException
     */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
    {
        if (ITEM.equals(qName)) {
            currentItem = new CancelledClass();
        } else if (TITLE.equals(qName)) {
            parsingTitle = true;
        } else if (COURSE.equals(qName)) {
            parsingCourse = true;
        }else if (TEACHER.equals(qName)) {
            parsingTeacher = true;
        }else if (PUBDATE.equals(qName)) {
            parsingDateTimeCancelled = true;
        }
    }

    /**
     * Method which checks if the end of the current element is an element which will
     * be used for the CancelledClass object.
     *
     * @param uri
     * @param localName
     * @param qName
     * @throws SAXException
     */
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException
    {
        if (ITEM.equals(qName)) {
            items.add(currentItem);
            currentItem = null;
        } else if (TITLE.equals(qName)) {
            parsingTitle = false;
        } else if (COURSE.equals(qName)) {
            parsingCourse = false;
        }else if (TEACHER.equals(qName)) {
            parsingTeacher = false;
        }else if (PUBDATE.equals(qName)) {
            parsingDateTimeCancelled = false;
        }
    }

    /**
     * Method which sets the fields of the CancelledClass object.
     *
     * @param ch
     * @param start
     * @param length
     * @throws SAXException
     */
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException
    {
        if (parsingTitle)
        {
            if (currentItem != null)
            {
                currentItem.setTitle(new String(ch, start, length));
                parsingCourse = false;
            }
        }
        else if (parsingCourse)
        {
            if (currentItem != null)
            {
                currentItem.setCourse(new String(ch, start, length));
                parsingCourse = false;
            }
        }
        else if (parsingTeacher)
        {
            if (currentItem != null)
            {
                currentItem.setTeacher(new String(ch, start, length));
                parsingTeacher = false;
            }
        }
        else if (parsingDateTimeCancelled)
        {
            if (currentItem != null)
            {
                currentItem.setDateTimeCancelled(new String(ch, start, length));
                parsingDateTimeCancelled = false;
            }
        }
    }
}