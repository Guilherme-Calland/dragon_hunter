

/**
 * Write a description of class Item here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Item
{
    // instance variables - replace the example below with your own
    private String name;
    private String description;
    private String information;
    
    /**
     * Constructor for objects of class Item
     */
    public Item(String name, String description)
    {
        // initialise instance variables
        this.name = name; 
        this.description = description;
        this.information = "not a book";
    }
    
    public String getName()
    {
       return this.name;
    }
    
    public String getDescription()
    {
       return this.description;
    }
    
    public String getInformation()
    {
        return this.information;
    }
    
    public void setInformation(String information)
    {
       this.information = information;
    }

    /**
     * An example of a method - replace this comment with your own
     *
     * @param  y  a sample parameter for a method
     * @return    the sum of x and y
     */
    
}
