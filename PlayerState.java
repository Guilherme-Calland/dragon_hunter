import java.util.*;
/**
 * Write a description of class PlayerState here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class PlayerState
{
    // instance variables - replace the example below with your own
    private Room currentRoom;
    private Stack< Item > items;
    
    /**
     * Constructor for objects of class PlayerState
     */
    public PlayerState()
    {}
    
    public Room getCurrentRoom()
    {
        return this.currentRoom;
    }
    
    public Stack< Item > getItems()
    {
        return this.items;
    }
    

}
