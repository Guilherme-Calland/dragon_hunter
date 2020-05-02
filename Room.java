import java.util.Set;
import java.util.HashMap;
import java.util.*;
/**
 * Class Room - a room in an adventure game.
 *
 * This class is part of the "World of Zuul" application. 
 * "World of Zuul" is a very simple, text based adventure game.  
 *
 * A "Room" represents one location in the scenery of the game.  It is 
 * connected to other rooms via exits.  For each existing exit, the room 
 * stores a reference to the neighboring room.
 * 
 * @author  Michael Kölling and David J. Barnes
 * @version 2016.02.29
 */


public class Room 
{
    private String description;
    private HashMap<String, Room> exits;
    private ArrayList<Item> items;
    private boolean dragon;
    

    /**
     * Create a room described "description". Initially, it has
     * no exits. "description" is something like "a kitchen" or
     * "an open court yard".
     * @param description The room's description.
     */
    public Room(String description) 
    {
        this.description = description;
        exits = new HashMap<>();
        items = new ArrayList<Item>();
        dragon = false;
    }
    
    public void setDragon(boolean hasDragon)
    {
        this.dragon = hasDragon;
    }
    
    public boolean getDragon()
    {
        return this.dragon;
    }

    /**
     * Define an exit from this room.
     * @param direction The direction of the exit.
     * @param neighbor  The room to which the exit leads.
     */
    public void setExit(String direction, Room neighbor) 
    {
        exits.put(direction, neighbor);
    }

    /**
     * @return The short description of the room
     * (the one that was defined in the constructor).
     */
    public String getShortDescription()
    {
        return description;
    }

    /**
     * Return a description of the room in the form:
     *     You are in the kitchen.
     *     Exits: north west
     * @return A long description of this room
     */
    public String getLongDescription()
    {
        String output = "";
        output += "You are in " + description + ".\n" + getExitString();
        output += "\n" + getItemListString();
        return output;
    }

    /**
     * Return a string describing the room's exits, for example
     * "Exits: north west".
     * @return Details of the room's exits.
     */
    private String getExitString()
    {
        String returnString = "Exits:";
        Set<String> keys = exits.keySet();
        for(String exit : keys) {
            returnString += " | " + exit;
        }
        return  returnString + " |";
    }

    /**
     * Return the room that is reached if we go from this room in direction
     * "direction". If there is no room in that direction, return null.
     * @param direction The exit's direction.
     * @return The room in the given direction.
     */
    public Room getExit(String direction) 
    {
        return exits.get(direction);
    }
    
    public void placeItem(Item item)
    {
        items.add(item);
    }
    
    public Item takeItem()
    {
        if(items.size() > 0)
        {
            Item tempItem = items.get(0);
            items.remove(0);
            return tempItem;
        }
        return null;
    }
    
    public String getItemListString()
    {
        String output = "";
        
        if(items.size() == 0)
        {
            output = "This room is empty.";
            return output;
        }else{   
            output = "Room Items: | ";
            for(Item item :items)
            {
                output += item.getDescription() + " | ";
            }
        }
        
        return output;
    }
}
