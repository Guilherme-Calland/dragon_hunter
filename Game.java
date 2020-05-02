 
import java.util.*;
/**
*  This class is the main class of the "World of Zuul" application. 
*  "World of Zuul" is a very simple, text based adventure game.  Users 
*  can walk around some scenery. That's all. It should really be extended 
*  to make it more interesting!
* 
*  To play this game, create an instance of this class and call the "play"
*  method.
* 
*  This main class creates and initialises all the others: it creates all
*  rooms, creates the parser and starts the game.  It also evaluates and
*  executes the commands that the parser returns.
* 
* @author  Michael Kölling and David J. Barnes
* @version 2016.02.29
*/

public class Game 
{
    private Parser parser;
    private Room currentRoom;
    private ArrayList< Item > items;
    private boolean swordOfGoremack;
    
    /**
    * Create the game and initialise its internal map.
    */
    public Game() 
    {
        createRooms();
        parser = new Parser();
        items = new ArrayList< Item >();
        swordOfGoremack = false;
    }
    
    private boolean hasSwordOfGoremack()
    {
        return swordOfGoremack;
    }
    
    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        Room hall, livingRoom, queenBedroom, kingBedroom, diningRoom, ballroom, kitchen, library, princeBedroom;
      
        // create the rooms
        hall = new Room("the hall room");
        livingRoom = new Room("the living room");
        queenBedroom = new Room("the queen's bedroom");
        kingBedroom = new Room("the king's bedroom");
        diningRoom = new Room("the dining room");
        ballroom = new Room("the ballroom");
        kitchen = new Room("the kitchen");
        library = new Room("the library");
        princeBedroom = new Room("the prince's bedroom");
       
        // initialise room exits
        hall.setExit("north door", livingRoom);
        
        //living room
        livingRoom.setExit("west door", library);
        livingRoom.setExit("east door", princeBedroom);
        livingRoom.setExit("north door", queenBedroom);
        livingRoom.setExit("south door", hall);
        
        //library
        library.setExit("east door", livingRoom);
        library.setExit("north door", kingBedroom);
        Item bookOfDragon = new Item("silver dragon", "Book: The Silver Dragon");
        bookOfDragon.setInformation("The Silver Dragon, this beast has been an enemy of the kingdom for many years.\n" + 
        "It hates the royal family out of jealousy, it too wishes to be wealty and important.\n" + 
        "This monster has notoriously thick scales which can only be pierced by \nThe Sword of Goremack.");
        library.placeItem(bookOfDragon);
        
        //king's bedroom
        kingBedroom.setExit("west door", ballroom);
        kingBedroom.setExit("east door", queenBedroom);
        kingBedroom.setExit("south door", library);
        kingBedroom.setDragon(true);
        
        //prince's bedroom
        princeBedroom.setExit("west door", livingRoom);
        princeBedroom.setExit("north door", diningRoom);
        Item princeSword = new Item("sword of prince", "Prince's Sword");
        princeBedroom.placeItem( princeSword );
        
        //queen's bedroom
        queenBedroom.setExit("west door", kingBedroom);
        queenBedroom.setExit("east door", diningRoom);
        queenBedroom.setExit("south door", livingRoom);
        
        //ballroom
        ballroom.setExit("east door", kingBedroom);
        
        //dining room
        diningRoom.setExit("west door", queenBedroom);
        diningRoom.setExit("east door", kitchen);
        diningRoom.setExit("south door", princeBedroom);
        
        //kitchen
        kitchen.setExit("west door", diningRoom);
        Item goremackSword = new Item("sword of goremack","Sword of Goremack");
        kitchen.placeItem( goremackSword );
        Item bookOfGoremack = new Item("goremack's sword", "Book: The Sword of Goremack");
        bookOfGoremack.setInformation("The Sword of Goremack, it has existed for ten thousand years." +
        " \nForged by the legendary warrior Goremack, it has been \nused to slay all sorts of diferent monsters." +
        " \nLegend says only the worthy can wield it.");
        kitchen.placeItem( bookOfGoremack );
       
        currentRoom = hall;  // start in the main hall
    }
    
    
    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();
        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
                
        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Congratulations, the quest is over, you are a true hero. Goodbye");
    }
    
    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to the Dragon Hunter!");
        System.out.println("Dragon Hunter is a new, incredibly boring adventure game.");
        System.out.println("Type 'help' if you need help.");
        System.out.println();
        System.out.println(currentRoom.getLongDescription());
    }
    
    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;
    
        if(command.isUnknown()) {
            System.out.println("I don't know what you mean...");
            return false;
        }
        
        String commandWord = command.getCommandWord();
        if (commandWord.equals("help")) {
            printHelp();
        }
        else if (commandWord.equals("open")) {
            goRoom(command);
        }
        else if (commandWord.equals("quit")) {
            wantToQuit = quit(command);
        }
        else if (commandWord.equals("feel")) {
            feelDoor(command);
        }
        else if(commandWord.equals("take")) {
            takeItem();
        }
        else if(commandWord.equals("put")) {
            putItem();
        }else if (commandWord.equals("read")) {
            readBook(command);
        }else if (commandWord.equals("back")) {
            back();
        }
        // else command not recognised.
        return wantToQuit;
    }
    
    // implementations of user commands:
    
    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        //System.out.println("You are in " + currentRoom.getShortDescription() );
        System.out.println();
        System.out.println("Your command words are:");
        parser.showCommands();
    }
    
    /** 
    * Try to in to one direction. If there is an exit, enter the new
    * room, otherwise print an error message.
     */
    private void goRoom(Command command) 
    {
        
        if (!command.hasSecondWord())
        {
            System.out.println("Open what?");
            return;
        }
        
        String secondWord = command.getSecondWord();
        
        if(
            !(
                secondWord.equals("north") ||
                secondWord.equals("south") ||
                secondWord.equals("east")  ||
                secondWord.equals("west") 
            ) 
        )
        {
            System.out.println("Invalid command.");
            return;
        } 
        
        if (!command.hasThirdWord())
        {
            System.out.println("Open " + secondWord + " what?");
            return;
        }
        
        String thirdWord = command.getThirdWord();
        
        if( !thirdWord.equals("door") )
        {
            System.out.println("Only doors can be opened.");
            return;
        }
        
        String nextDoor = command.getSecondWord() + " " + command.getThirdWord();
        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(nextDoor);
    
        if (nextRoom == null) {
            System.out.println("There is no door!");
        }
        else {
            currentRoom = nextRoom;
            System.out.println(currentRoom.getLongDescription());
        }
    }
    
    private void feelDoor(Command command)
    {
        if (!command.hasSecondWord())
        {
            System.out.println("Feel what?");
            return;
        }
        
        String secondWord = command.getSecondWord();
        
        if(
            !(
                secondWord.equals("north") ||
                secondWord.equals("south") ||
                secondWord.equals("east")  ||
                secondWord.equals("west") 
            )
        )
        {
            System.out.println("Invalid command.");
            return;
        } 
        
        if (!command.hasThirdWord())
        {
            System.out.println("Feel " + secondWord + " what?");
            return;
        }
        
        String thirdWord = command.getThirdWord();
        
        if( !thirdWord.equals("door") )
        {
            System.out.println("Invalid command.\nYou may feel doors if you like.");
            return;
        }
        
        String nextDoor = command.getSecondWord() + " " + command.getThirdWord();
        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(nextDoor);
    
        if (nextRoom == null) 
        {
            System.out.println("There is no door!");
        }
        else if ( nextRoom.getDragon() )
        {
            System.out.println("The door feels warm.");
        } 
        else
        {
            System.out.println("The door feels normal.");
        }
    }
            
    private void takeItem()
    {
        Item item = currentRoom.takeItem();
        if(item == null)
        {
            System.out.println("Nothing to take.");
        } else {
            items.add( item );
            System.out.println("You have taken the " + item.getDescription());
        }  
    }
    
    private void putItem()
    {   
        if(items.size() == 0)
        {
            System.out.println("Nothing to put.");
        } else {
            Item item = items.get(0);
            currentRoom.placeItem(item);
            items.remove(0);
            System.out.println("You have put the " + item.getDescription()
            + " in " + currentRoom.getShortDescription() );
        }  
    }
    
    private void readBook(Command command)
    {
        HashMap< String, Item > books = new HashMap< String, Item >();
        
        for(Item item : items) // create a hashmap of items
        {
            if( item.getInformation() != "not a book" )
            books.put( item.getName() , item );
        }
        
        if(books.size() == 0)
        {
            System.out.println("Nothing to read.");
            return;
        }
            
        if(!command.hasSecondWord())
        {
            System.out.println("Read what?");
            
            String output = "\nThese are your books: | ";
            if( books.containsKey("goremack's sword") )
            {
                output += "goremack's sword | ";
            }
            if( books.containsKey("silver dragon") )
            {
                output += "silver dragon | "; 
            }
            System.out.println( output );
            return;
        } 
            
        String book = command.getSecondWord() + " " + command.getThirdWord();
        
        if (!books.containsKey(book)) 
        {
            System.out.println("There is no such book!");
        }
        else 
        {
            System.out.println(books.get(book).getInformation());
        }
    }
    
    
    /** 
    * "Quit" was entered. Check the rest of the command to see
    * whether we really quit the game.
    * @return true, if this command quits the game, false otherwise.
    */
    private void back()
    {
        
    }
   
    private boolean quit(Command command) 
    {
            
        
        
            
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }
}
