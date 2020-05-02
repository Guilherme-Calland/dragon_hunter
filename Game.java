 
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
    private Stack< Item > items;
    private boolean swordOfGoremack;
    private Stack< PlayerState > playerStates;
    
    /**
    * Create the game and initialise its internal map.
    */
    public Game() 
    {
        createGame();     
    }
    
    private boolean hasSwordOfGoremack()
    {
        return swordOfGoremack;
    }
    
    private void createGame()
    {
        parser = new Parser();
        items = new Stack< Item >();
        swordOfGoremack = false;
        playerStates = new Stack< PlayerState >();
        createRooms();
        play();
    }
    
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
        Item harryPotter = new Item("harry potter", "Book: Harry Potter and the Prisioner of Askaban");
        harryPotter.setInformation("You sit in the corner and read an entire Harry Potter book.\n"+
        "It takes 12 hours, the prince is fine but hurry.");
        library.placeItem(harryPotter);
        
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
        
        currentRoom = hall; //start in the main hall
        playerStates.push( new PlayerState(currentRoom, items) );
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
        
        while (!finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        
    }
    
    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to the Dragon Hunter!");
        System.out.println("You are a warrior princess and you have been sent into a castle\n" + 
        "to kill the dragon that's keeping the prince hostage. The dragon is hiding in one of\n" + 
        "the rooms of the castle and carrying the scared prince with him wrapped around his tail.\n" + 
        "Good Luck!");
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
        
        playerStates.push(new PlayerState(currentRoom, items));
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
        
        if (!command.hasThirdWord())
        {
            System.out.println("Invalid command.");
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
            if(currentRoom.hasDragon())
            {
                for(Item item : items)
                {
                    if (item.getName().equals("sword of goremack"))
                    {
                        this.swordOfGoremack = true;
                    }
                }
                if(!swordOfGoremack)
                {
                    showLosingText();
                    restart();
                } 
                else 
                {
                    showWinningText();
                    currentRoom.setDragon(false);
                }
                
                return;
            }
            
            System.out.println(currentRoom.getLongDescription());
        }
    }
    
    private void showLosingText()
    {
        System.out.println("You encounter a fire breathing dragon!\n"+
        "The beast shoots fire at you. You are dead. Game Over.\n");
    }
    
    private void showWinningText()
    {
        System.out.println("You encounter a fire breathing dragon!\n" +
        "The beast speaks.\n\n" + 
        "Silver Dragon: Why have you come here?\n\n" +
        "Warrior Princess: I've come to kill you dragon.\n\n" +
        "Silver Dragon: Hahahahahaha, many have tried, all met a fiery grave.\n\n" +
        "Prince: Help me!\n\n" +
        "You pull out the Sword of Goremack.\n\n" +
        "Silver Dragon: What? No? Impossible!\n\n" + 
        "Prince: I have no ideia what's going on.\n\n" +
        "Warrior Princess: Prepare to die!\n\n" +
        "An epic battle soon ensued, o man, if only you could see it,\n" +
        "such an awesome freaking battle, wow. You killed the dragon, saved the day.\n" +
        "Congratulations, the prince is saved, you are a true hero.\n\n" + 
        "Your quest is over, but feel free to walk around the castle");
        
    }
    
    private void feelDoor(Command command)
    {
        if (!command.hasSecondWord())
        {
            System.out.println("Feel what?");
            return;
        }
       
        if (!command.hasThirdWord())
        {
            System.out.println("Invalid command.");
            return;
        }
        
        String nextDoor = command.getSecondWord() + " " + command.getThirdWord();
        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(nextDoor);
    
        if (nextRoom == null) 
        {
            System.out.println("There is no door!");
        }
        else if ( nextRoom.hasDragon() )
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
            items.push( item );
            System.out.println("You have taken the " + item.getDescription());
        }  
    }
    
    private void putItem()
    {   
        if(items.size() == 0)
        {
            System.out.println("Nothing to put.");
        } else {
            Item item = items.pop();
            currentRoom.placeItem(item);
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
            if( books.containsKey("harry potter") )
            {
                output += "harry potter | "; 
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
        playerStates.pop();
        PlayerState previousState = playerStates.pop();
        playerStates.push(previousState);
        this.currentRoom = previousState.getCurrentRoom();
        this.items = previousState.getItems();
    }
    
    private void restart()
    {
        createGame();
        printWelcome();
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
