/**
 * This class is part of the "World of Zuul" application. 
 * "World of Zuul" is a very simple, text based adventure game.
 * 
 * This class holds an enumeration of all command words known to the game.
 * It is used to recognise commands as they are typed in.
 *
 * @author  Michael Kölling and David J. Barnes
 * @version 2016.02.29
 */

public class CommandWords
{
    // a constant array that holds all valid command words
    private static final String[] validCommands = {
        "open", "feel", "take", "put", "read", "quit", "help", "back"
    };
    /*
    private static final String[] validSecondWords = {
        "north", "south", "east", "west"
    };

    private static final String[] validThirdWords = {
        "door"
    };
    */

    /**
     * Constructor - initialise the command words.
     */
    public CommandWords()
    {
        // nothing to do at the moment...
    }

    /**
     * Check whether a given String is a valid command word. 
     * @return true if it is, false if it isn't.
     */
    public boolean isCommand(String aString)
    {
        for(int i = 0; i < validCommands.length; i++) {
            if(validCommands[i].equals(aString))
                return true;
        }
        // if we get here, the string was not found in the commands
        return false;
    }
    /*
    public boolean isSecondWord(String aString)
    {
        for(int i = 0; i< validSecondWords.length; i++) {
            if(validSecondWords[i].equals(aString))
                return true;
        }
        return false;
    }
    
    public boolean isThirdWord(String aString)
    {
        for(int i = 0; i< validThirdWords.length; i++) {
            if(validThirdWords[i].equals(aString))
                return true;
        }
        return false;
    }
    */

    /**
     * Print all valid commands to System.out.
     */
    public void showAll() 
    {
        for(String command: validCommands) {
            System.out.print(command + "  ");
        }
        System.out.println();
    }
}
