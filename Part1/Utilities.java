import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Utilities
{
    public static String getUserInput(String message, Scanner scanner)
    {
        String response;

        System.out.println(message);
        response = scanner.nextLine();

        return response;
    }

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

    public static char getCharacter(String message, Scanner scanner)
    {
        char charResponse = '0';
        boolean isChar = false;
        String response = getUserInput(message, scanner).trim();

        while(! isChar)
        {
            if(response.length() == 1)
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

    public static String getChoice(String message, Scanner scanner)
    {
        String response = getUserInput(message, scanner);
        ArrayList<String> validResponses = new ArrayList<>(List.of("Y", "y", "N", "n"));

        while(! validResponses.contains(response))
        {
            System.out.println("Please respond with either \"y\" or \"n\"");     
            response = getUserInput(message, scanner);
        }

        return response;
    }
}