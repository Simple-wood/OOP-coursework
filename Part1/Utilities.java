import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Utility helper methods for robust user input handling in the typing race.
 *
 * Contains shared validation routines for reading strings, integers, doubles,
 * characters, yes/no choices, and constrained passage lengths from a Scanner.
 *
 * @author Kishal Chhetri
 * @version 1.0
 */
public class Utilities
{
    /**
     * Prompts the user and returns their raw line of input.
     *
     * @param message prompt text shown before reading input
     * @param scanner scanner used to read from standard input
     * @return the user's input line as entered
     */
    public static String getUserInput(String message, Scanner scanner)
    {
        String response;

        System.out.println(message);
        response = scanner.nextLine();

        return response;
    }

    /**
     * Prompts for an integer and repeats until a valid integer is entered.
     *
     * @param message prompt text shown before reading input
     * @param scanner scanner used to read from standard input
     * @return a valid integer value
     */
    public static int getInteger(String message, Scanner scanner) 
    {
        int integerResponse = -1;
        boolean isInt = false;
        String response = getUserInput(message, scanner).trim();

        while(! isInt)
        {
            try{
                integerResponse = Integer.parseInt(response); // We try to ensure that we have recieved an integer
            }
            catch(NumberFormatException e){ // If we haven't, an exception is thrown
                System.out.println("Sorry, you must enter an integer!");
                response = getUserInput(message, scanner).trim(); 
                continue;
            }

            isInt = true;
        }

        return integerResponse;
    }

    /**
     * Prompts for a decimal number and repeats until a valid double is entered.
     *
     * @param message prompt text shown before reading input
     * @param scanner scanner used to read from standard input
     * @return a valid double value
     */
    public static double getDouble(String message, Scanner scanner) 
    {
        double doubleResponse = -1.0;
        boolean isDouble = false;
        String response = getUserInput(message, scanner).trim();

        while(! isDouble)
        {
            try{
                doubleResponse = Double.parseDouble(response); // We try to ensure that we have recieved a decimal number
            }
            catch(NumberFormatException e){ // If we haven't, an exception is thrown
                System.out.println("Sorry, you must enter a decimal number!");
                response = getUserInput(message, scanner).trim(); 
                continue;
            }

            isDouble = true;
        }

        return doubleResponse;
    }

    /**
     * Prompts for a single character and repeats until exactly one character is entered.
     *
     * @param message prompt text shown before reading input
     * @param scanner scanner used to read from standard input
     * @return the validated single character input
     */
    public static char getCharacter(String message, Scanner scanner)
    {
        char charResponse = '0';
        boolean isChar = false;
        String response = getUserInput(message, scanner).trim(); // We call trim() to remove any leading or trailing spaces!

        while(! isChar)
        {
            if(response.length() == 1) // Indicates entered input is a character
            {
                charResponse = response.charAt(0);
                isChar = true;

            }
            else{
                System.out.println("Please enter a valid character!");
                response = getUserInput(message, scanner).trim();
            }
        }

        return charResponse;
    }

    /**
     * Prompts for a yes/no choice and repeats until one of Y, y, N, or n is entered.
     *
     * @param message prompt text shown before reading input
     * @param scanner scanner used to read from standard input
     * @return the validated choice value
     */
    public static String getChoice(String message, Scanner scanner)
    {
        String response = getUserInput(message, scanner);
        ArrayList<String> validResponses = new ArrayList<>(List.of("Y", "y", "N", "n")); // List of valid responses

        while(! validResponses.contains(response))
        {
            System.out.println("Please respond with either \"Y\" / \"y\" or \"N\" / \"n\"");     
            response = getUserInput(message, scanner);
        }

        return response;
    }

    /**
     * Prompts for passage length and validates it is within the allowed bounds.
     *
     * @param scanner scanner used to read from standard input
     * @return validated passage length between 1 and 75 inclusive
     */
    public static int getPassageLength(Scanner scanner)
    {
        final int LOWER_BOUND = 1;
        final int UPPER_BOUND = 75;
        int response = getInteger("Please enter the length of the passage (" + LOWER_BOUND + "-" + UPPER_BOUND + ") - ", scanner); 
        
        while(response < LOWER_BOUND || response > UPPER_BOUND)
        {
            System.out.println("Please enter a passage length that is within the specified range!");
            response = getInteger("Please enter the length of the passage (" + LOWER_BOUND + "-" + UPPER_BOUND + ") - ", scanner);   
        }

        System.out.println();

        return response;
    }
}