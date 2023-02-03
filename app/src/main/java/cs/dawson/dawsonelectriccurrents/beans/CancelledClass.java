package cs.dawson.dawsonelectriccurrents.beans;

import java.io.Serializable;

/**
 * CancelledClass holds all the information that a class contains, including
 * the title, course, teacher and the date it was cancelled.
 * Created by: Alessandro Ciotola
 *
 */
public class CancelledClass implements Serializable
{
    private String title;
    private String course;
    private String teacher;
    private String dateTimeCancelled;

    /**
     * No parameter constructor which sets the initial values to be empty strings.
     */
    public CancelledClass()
    {
        this("", "", "", "");
    }

    /**
     * Constructor which initializes all the values.
     *
     * @param title
     * @param course
     * @param teacher
     * @param dateTimeCancelled
     */
    public CancelledClass(String title, String course, String teacher, String dateTimeCancelled)
    {
        this.title = title;
        this.course = course;
        this.teacher = teacher;
        this.dateTimeCancelled = dateTimeCancelled;
    }

    /**
     * Getter which returns the title of a course.
     *
     * @return
     */
    public String getTitle()
    {
        return title;
    }

    /**
     * Setter which returns the title of a course.
     *
     * @param title
     */
    public void setTitle(String title)
    {
        this.title = title;
    }

    /**
     * Getter which returns the course name.
     *
     * @return
     */
    public String getCourse()
    {
        return course;
    }

    /**
     * Setter which sets the course.
     * @param course
     */
    public void setCourse(String course)
    {
        this.course = course;
    }

    /**
     * Getter which returns the teacher name.
     *
     * @return
     */
    public String getTeacher()
    {
        return teacher;
    }

    /**
     * Setter which sets the teachers name.
     * @param teacher
     */
    public void setTeacher(String teacher)
    {
        this.teacher = teacher;
    }

    /**
     * Getter which returns the date and time the course was cancelled.
     *
     * @return
     */
    public String getDateTimeCancelled()
    {
        return dateTimeCancelled;
    }

    /**
     * Set the time the course was cancelled.
     *
     * @param dateTimeCancelled
     */
    public void setDateTimeCancelled(String dateTimeCancelled)
    {
        this.dateTimeCancelled = dateTimeCancelled;
    }
}
