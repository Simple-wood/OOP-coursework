import java.util.concurrent.TimeUnit;
import java.util.ArrayList;
import java.util.Iterator;
import java.lang.Math;
import java.util.Scanner;
import java.util.Random;

/**
 * A typing race simulation. Three typists race to complete a passage of text,
 * advancing character by character — or sliding backwards when they mistype.
 *
 * Originally written by Ty Posaurus.
 *
 * @author Kishal Chhetri
 * @version 1.0
 */
public class TypingRace
{
    private int passageLength;   // Total characters in the passage to type
    private ArrayList<Typist> typists;
    private Typist winner = null; // Winning typist, null represents that there is no winner as of yet

    // Accuracy thresholds for mistype and burnout events
    private static final double MISTYPE_BASE_CHANCE = 0.25;
    private static final int    SLIDE_BACK_AMOUNT = 3; 
    private static final int    BURNOUT_DURATION  = 3; 

    /**
     * Constructor for objects of class TypingRace.
     * Sets up the race with a passage of the given length.
     * Initially there are no typists seated.
     *
     * @param passageLength the number of characters in the passage to type
     */
    public TypingRace(int passageLength)
    {
        this.passageLength = passageLength;
        typists = new ArrayList<>();
    }

    /**
     * Adds a typist to this race.
     *
     * @param theTypist the typist to add
     */
    private void addTypist(Typist theTypist)
    {
        typists.add(theTypist);
    }

    /**
     * Resets all typists in the given iterator back to their starting state.
     *
     * @param typistsIterator iterator over typists currently in the race
     */
    private void resetTypists(Iterator<Typist> typistsIterator)
    {
        Typist currentTypist = null;

        while(typistsIterator.hasNext())
        {
            currentTypist = typistsIterator.next();
            currentTypist.resetToStart();
        }
    }

    /**
     * Advances each typist in the iterator by one simulated turn.
     *
     * @param typistsIterator iterator over typists currently in the race
     */
    private void advanceTypists(Iterator<Typist> typistsIterator)
    {
        Typist currentTypist = null;

        while(typistsIterator.hasNext())
        {
            currentTypist = typistsIterator.next();
            advanceTypist(currentTypist);
        }       
    }
    
    /**
     * Prints all typist lanes in their current state.
     *
     * @param typistsIterator iterator over typists to print
     */
    private void printTypists(Iterator<Typist> typistsIterator)
    {
        Typist currentTypist = null;

        while(typistsIterator.hasNext())
        {
            currentTypist = typistsIterator.next();
            printSeat(currentTypist);
            System.out.println();
        } 
    }

    /**
     * Interactively configures and adds typists for this race.
     * Ensures names and symbols are unique among all configured typists.
     *
     * @param scanner scanner used to read user input
     */
    public void configureTypists(Scanner scanner)
    {
        ArrayList<Character> seenTypistSymbols = new ArrayList<>(); // Keeps track of typist symbols
        ArrayList<String> seenTypistNames = new ArrayList<>(); // Keeps track of typist names
        String choice = ""; // Represents choice ( a yes or no ) for if we want to add another typist or not
        int numberOfTypists = 0;

        while(! (choice.equals("N") ||  choice.equals("n"))) // We guarentee at least 1 typist in the simulation
        {
            numberOfTypists++;
            System.out.println("Welcome Typist no. " + numberOfTypists);

            String name = configureName(seenTypistNames, scanner);
            char typistSymbol = configureSymbol(seenTypistSymbols, scanner);
            double typistAccuracy = configureAccuracy(scanner);
            Typist typist = new Typist(typistSymbol, name, typistAccuracy);

            seenTypistNames.add(name);
            seenTypistSymbols.add(typistSymbol);
            addTypist(typist);

            choice = Utilities.getChoice("Would you like to add another typist (y/n) - ", scanner);
            System.out.println();
        }
    }

    /**
     * Prompts the user for a typist symbol and validates uniqueness.
     *
     * @param seenSymbols symbols already assigned to existing typists
     * @param scanner scanner used to read user input
     * @return a unique symbol for the new typist
     */
    private char configureSymbol(ArrayList<Character> seenSymbols, Scanner scanner)
    {
        char symbol = Utilities.getCharacter("Please enter a character to represent your typist - ", scanner);

        while(seenSymbols.contains(symbol)) // We check if there is already a typist with that symbol
        {
            System.out.println("Sorry, that symbol is already taken!");
            symbol = Utilities.getCharacter("Please enter a character to represent your typist - ", scanner);
        }

        return symbol;
    }

    /**
     * Prompts the user for a typist accuracy and validates range [0.0, 1.0].
     *
     * @param scanner scanner used to read user input
     * @return a valid accuracy value between 0.0 and 1.0 inclusive
     */
    private double configureAccuracy(Scanner scanner)
    {
        double accuracy = Utilities.getDouble("Please enter an accuracy for your typist (0.0 - 1.0) - ", scanner);

        while(accuracy < 0.0 || accuracy > 1.0) // We need to check if the entered accuracy is within the valid range
        {
            System.out.println("You must enter an accuracy between 0.0 and 1.0 inclusive!");
            accuracy = Utilities.getDouble("Please enter an accuracy for your typist (0.0 - 1.0) - ", scanner);
        }

        return accuracy;
    }

    /**
     * Prompts the user for a typist name and validates uniqueness.
     *
     * @param seenNames names already assigned to existing typists
     * @param scanner scanner used to read user input
     * @return a unique name for the new typist
     */
    private String configureName(ArrayList<String> seenNames, Scanner scanner)
    {
        String name =  Utilities.getUserInput("Please enter the name of your typist - ", scanner);

        while(seenNames.contains(name)) // We check if there is already a typist with the specified name
        {
            System.out.println("Sorry, that name is already taken!");
            name = Utilities.getUserInput("Please enter the name of your typist - ", scanner);
        }

        return name;
    }

    /**
     * Starts the typing race.
     * All typists are reset to the beginning, then the simulation runs
     * turn by turn until one typist completes the full passage.
     * The winner and updated winner accuracy are printed at the end.
     */
    public void startRace()
    {
        Iterator<Typist> typistsIterator = typists.iterator();
        
        // Reset all typists to the start of the passage 
        resetTypists(typistsIterator);

        while (winner == null)
        {
            // Print the current state of the race
            printRace();

            // Advance each typist by one turn
            typistsIterator = typists.iterator();
            advanceTypists(typistsIterator);

            // Check if any typist has finished the passage
            typistsIterator = typists.iterator();

            // Wait 200ms between turns so the animation is visible
            try {
                TimeUnit.MILLISECONDS.sleep(200);
            } catch (Exception e) {}
        }

        printRace(); // Account for last frame of the race

        // Handles the printing of winner and updating accuracy for typists
        typistsIterator = typists.iterator();
        endRace(typistsIterator);
    }

    /**
     * Finalizes the race by updating ratings and announcing the winner.
     * Resets the winner field afterwards so a new race can begin cleanly.
     *
     * @param typistsIterator iterator over all typists in the race
     */
    private void endRace(Iterator<Typist> typistsIterator)
    {
        double oldWinnerAccuracy = winner.getAccuracy();
        updateTypistRatings(typistsIterator);
        printWinner(oldWinnerAccuracy);
        winner = null;
    }

    /**
     * Prints the winner's name and updated accuracy summary.
     *
     * @param oldAccuracy winner's accuracy value before post-race updates
     */
    private void printWinner(double oldAccuracy)
    {
        String accuracyMessage = "improved"; // Just to indicate if the accuracy has increased or decreased

        if(oldAccuracy > winner.getAccuracy())
        {
            accuracyMessage = "decreased";
        }

        System.out.println();
        System.out.println("And the winner is .... " + winner.getName() + "!");
        System.out.println("Final accuracy is: " + winner.getAccuracy() + " ( " + accuracyMessage + " from " + oldAccuracy + ")");
    }

    /**
     * Simulates one turn for a typist.
     *
     * If the typist is burnt out, they recover one turn and skip typing.
     * Otherwise, one of the following can happen:
     *   - On a successful typing attempt (based on accuracy), progress advances by one.
     *   - After a successful typing attempt, burnout may occur.
     *   - On a failed typing attempt, a mistype may occur and cause slide-back.
     *
     * @param theTypist the typist to advance
     */
    private void advanceTypist(Typist theTypist)
    { 
        double typistAccuracy = theTypist.getAccuracy();

        if (theTypist.isBurntOut())
        {
            // Recovering from burnout — skip this turn
            theTypist.recoverFromBurnout();
            return;
        }
        
        if(theTypist.isMistyped())
        {
            theTypist.leaveMistyped();
        }

        // Attempt to type a character
        if (Math.random() < typistAccuracy)
        {
            theTypist.typeCharacter();

            if(raceFinishedBy(theTypist)) // If we have finished the race now, there is no need to check for burnouts or mistypes!
            {
                winner = theTypist;
                return;
            }

            // Burnout check — pushing too hard increases burnout risk
            // (probability scales with accuracy cubed, capped at ~0.10)
            if (Math.random() < 0.10 * Math.pow(theTypist.getAccuracy(), 3))
            {
                theTypist.burnOut(BURNOUT_DURATION);
                theTypist.incrementNumberOfBurnouts();
            }
        }
        
        // Mistype check — the probability should reflect the typist's accuracy
        else if (Math.random() < (1 - typistAccuracy) * MISTYPE_BASE_CHANCE)
        {
            Random random = new Random();
            int slideBackAmount = random.nextInt(0, SLIDE_BACK_AMOUNT) + 1;

            theTypist.slideBack(slideBackAmount);
        }
    }

    /**
     * Updates all typist accuracies after a race using outcome and burnout data.
     * The winner receives a positive outcome score; all others receive a loss score.
     *
     * @param typistIterator iterator over all typists whose ratings should be updated
     */
    private void updateTypistRatings(Iterator<Typist> typistIterator)
    {
        final double SWING_FACTOR = 0.025; // Handles how much the accuracy should vary by
        final double WIN_OUTCOME = 1.0;
        final double LOSS_OUTCOME = 0.0;

        Typist currentTypist = null;
        
        while(typistIterator.hasNext())
        {
            currentTypist = typistIterator.next();
            double currentAccuracy = currentTypist.getAccuracy();
            double outcome = LOSS_OUTCOME;
            int numberOfBurnouts = currentTypist.getNumberOfBurnouts();
            
            if(currentTypist == winner)
            {
                outcome = WIN_OUTCOME;
            }

            double newAccuracy = calculateNewAccuracy(SWING_FACTOR, outcome, currentAccuracy, numberOfBurnouts);
            currentTypist.setAccuracy(newAccuracy);
        }
    }

    /**
     * Calculates a typist's new accuracy after a race.
     * Applies a swing based on outcome and a burnout penalty, then truncates to 3 d.p.
     *
     * @param swingFactor multiplier controlling size of rating adjustments
     * @param outcome race outcome value (winner: 1.0, loser: 0.0)
     * @param oldAccuracy typist's previous accuracy
     * @param numberOfBurnouts number of burnouts incurred during the race
     * @return updated accuracy value
     */
    private double calculateNewAccuracy(double swingFactor, double outcome, double oldAccuracy, int numberOfBurnouts)
    {
        final double PENALTY_CONSTANT_FACTOR = 0.075;
        double penalty = PENALTY_CONSTANT_FACTOR * numberOfBurnouts;
        double newAccuracy = oldAccuracy + swingFactor * (outcome - penalty);

        newAccuracy = (int)(newAccuracy * 1000) / 1000.0; // Truncating to 3dp

        return newAccuracy; 
    }

    /**
     * Returns true if the given typist has completed the full passage.
     *
     * @param theTypist the typist to check
     * @return true if their progress has reached or passed the passage length
     */
    private boolean raceFinishedBy(Typist theTypist)
    {
        if (theTypist.getProgress() >= passageLength) // If an overshoot were to happen, it is correctly handled
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Prints the current state of the race to the terminal.
     * Shows each typist's position along the passage with burnout and mistype markers.
     */
    private void printRace()
    {
        // Clear terminal
        System.out.print("\033[H\033[2J"); 
        System.out.flush(); 

        System.out.println("\n  TYPING RACE — passage length: " + passageLength + " chars");
        multiplePrint('=', passageLength + 3);
        System.out.println();

        Iterator<Typist> typistsIterator = typists.iterator();
        printTypists(typistsIterator);

        multiplePrint('=', passageLength + 3);
        System.out.println();
        System.out.println("  [~] = burnt out    [<] = just mistyped"); 
    }

    /**
     * Prints a single typist's lane.
     * Uses '~' beside the symbol for burnout and '<' for a recent mistype.
     *
     * @param theTypist the typist whose lane to print
     */
    private void printSeat(Typist theTypist)
    {
        int spacesBefore = theTypist.getProgress();
        int spacesAfter  = passageLength - theTypist.getProgress();

        System.out.print('|');
        multiplePrint(' ', spacesBefore);

        // Always show the typist's symbol so they can be identified on screen.
        // Append ~ when burnt out so the state is visible without hiding identity.
        System.out.print(theTypist.getSymbol());

        if (theTypist.isBurntOut())
        {
            System.out.print('~');
            spacesAfter--; // symbol + ~ together take two characters
        }

        // Append < when mistyped so the state is visible without hiding identity.
        else if (theTypist.isMistyped())
        {
            System.out.print(" ");
            System.out.print('<');

            spacesAfter -= 2; // symbol + " " + < together takes three characters
        }

        multiplePrint(' ', spacesAfter);
        System.out.print('|');
        System.out.print(' ');
        System.out.print(theTypist.getName() + " (Accuracy: " + theTypist.getAccuracy() + ")");

        // Print name and accuracy
        if (theTypist.isBurntOut()) // Burnout takes priority
        {
            System.out.print(" BURNT OUT (" + theTypist.getBurnoutTurnsRemaining() + " turns)");
        }
        else if(theTypist.isMistyped())
        {
            System.out.print(" ← just mistyped ");      
        }
    }

    /**
     * Prints a character a given number of times.
     *
     * @param aChar the character to print
     * @param times how many times to print it
     */
    private void multiplePrint(char aChar, int times)
    {
        for(int i = 0; i < times; i++)
        {
            System.out.print(aChar);
        }
    }

    public static void main(String[] args) {
        System.out.println("Welcome to the Typing Race game! \n");
        
        Scanner scanner = new Scanner(System.in);
        int passage = Utilities.getPassageLength(scanner);
        String choice = "";
        TypingRace race = new TypingRace(passage); 
        
        race.configureTypists(scanner);

        while(! (choice.equals("N") || choice.equals("n")))
        {
            race.startRace(); 
            choice = Utilities.getChoice("Would you like to race again (y/n) - ", scanner);
        }

        System.out.println("\nThank you for playing!");
    }   
}