import java.util.concurrent.TimeUnit;
import java.util.ArrayList;
import java.util.Iterator;
import java.lang.Math;

/**
 * A typing race simulation. Three typists race to complete a passage of text,
 * advancing character by character — or sliding backwards when they mistype.
 *
 * Originally written by Ty Posaurus, who left this project to "focus on his
 * two-finger technique". He assured us the code was "basically done".
 * We have found evidence to the contrary.
 *
 * @author TyPosaurus
 * @version 0.7 (the other 0.3 is left as an exercise for the reader)
 */
public class TypingRaceSimulation
{
    private int passageLength;   // Total characters in the passage to type
    private ArrayList<TypistSimulation> typists;
    private TypistSimulation winner = null;

    // Accuracy thresholds for mistype and burnout events
    // (Ty tuned these values "by feel". They may need adjustment.)
    private static final double MISTYPE_BASE_CHANCE = 0.3;
    private static final int SLIDE_BACK_AMOUNT = 2; // I wanna make this vary
    private static final int BURNOUT_DURATION  = 3; //Perhaps make this variable, the more you burnout the greater this value is!

    /**
     * Constructor for objects of class TypingRace.
     * Sets up the race with a passage of the given length.
     * Initially there are no typists seated.
     *
     * @param passageLength the number of characters in the passage to type
     */
    public TypingRaceSimulation(int passageLength)
    {
        this.passageLength = passageLength;
        typists = new ArrayList<>();
    }

    /**
     * Seats a typist at the given seat number (1, 2, or 3).
     *
     * @param theTypist  the typist to seat
     * @param seatNumber the seat to place them in (1–3)
     */
    public void addTypist(TypistSimulation theTypist)
    {
        typists.add(theTypist);
    }

    private void resetTypists(Iterator<TypistSimulation> typistsIterator)
    {
        TypistSimulation currentTypist = null;

        while(typistsIterator.hasNext())
        {
            currentTypist = typistsIterator.next();
            currentTypist.resetToStart();
        }
    }

    private void advanceTypists(Iterator<TypistSimulation> typistsIterator)
    {
        TypistSimulation currentTypist = null;

        while(typistsIterator.hasNext())
        {
            currentTypist = typistsIterator.next();
            advanceTypist(currentTypist);
        }       
    }

    private boolean checkWinners(Iterator<TypistSimulation> typistsIterator)
    {
        TypistSimulation currentTypist = null;

        while(typistsIterator.hasNext())
        {
            currentTypist = typistsIterator.next();
            boolean winnerAvaliable = raceFinishedBy(currentTypist);

            if(winnerAvaliable)
            {
                winner = currentTypist;
                return true;
            }
        } 

        return false;
    }
    
    private void printTypists(Iterator<TypistSimulation> typistsIterator)
    {
        TypistSimulation currentTypist = null;

        while(typistsIterator.hasNext())
        {
            currentTypist = typistsIterator.next();
            printSeat(currentTypist);
            System.out.println();
        } 
    }

    /**
     * Starts the typing race.
     * All typists are reset to the beginning, then the simulation runs
     * turn by turn until one typist completes the full passage.
     *
     * Note from Ty: "I didn't bother printing the winner at the end,
     * you can probably figure that out yourself."
     */
    public void startRace()
    {
        boolean finished = false;
        Iterator<TypistSimulation> typistsIterator = typists.iterator();
        // Reset all typists to the start of the passage 
        resetTypists(typistsIterator);

        while (!finished)
        {
            // Print the current state of the race
            printRace();

            // Advance each typist by one turn
            typistsIterator = typists.iterator();
            advanceTypists(typistsIterator);

            // Check if any typist has finished the passage
            typistsIterator = typists.iterator();
            finished = checkWinners(typistsIterator);

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

    private void endRace(Iterator<TypistSimulation> typistsIterator)
    {
        double oldWinnerAccuracy = winner.getAccuracy();
        updateTypistRatings(typistsIterator);
        printWinner(oldWinnerAccuracy); 
    }

    private void printWinner(double oldAccuracy)
    {
        System.out.println();
        System.out.println("And the winner is .... " + winner.getName());
        System.out.println("Final accuracy is: " + winner.getAccuracy() + " (improved from " + oldAccuracy + ")");
    }

    /**
     * Simulates one turn for a typist.
     *
     * If the typist is burnt out, they recover one turn's worth and skip typing.
     * Otherwise:
     *   - They may type a character (advancing progress) based on their accuracy.
     *   - They may mistype (sliding back) — the chance of a mistype should decrease
     *     for more accurate typists.
     *   - They may burn out — more likely for very high-accuracy typists
     *     who are pushing themselves too hard.
     *
     * @param theTypist the typist to advance
     */
    private void advanceTypist(TypistSimulation theTypist)
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
                return;
            }

            // Mistype check — the probability should reflect the typist's accuracy
            
            if (Math.random() < (1 - typistAccuracy) * MISTYPE_BASE_CHANCE)
            {
                theTypist.slideBack(SLIDE_BACK_AMOUNT);
                theTypist.incrementNumberOfMistypes();
            }

            // Burnout check — pushing too hard increases burnout risk
            // (probability scales with accuracy squared, capped at ~0.05)
            if (Math.random() < (0.05 + (0.05 * Math.pow(typistAccuracy, 2)) * Math.pow(typistAccuracy, 2)))
            {
                theTypist.burnOut(BURNOUT_DURATION);
                theTypist.incrementNumberOfBurnouts();
            }
        }
    }

    private void updateTypistRatings(Iterator<TypistSimulation> typistIterator)
    {
        final double SWING_FACTOR = 0.025;
        final double WIN_OUTCOME = 1.0;
        final double LOSS_OUTCOME = 0.0;

        TypistSimulation currentTypist = null;
        
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

    private double calculateNewAccuracy(double swingFactor, double outcome, double oldAccuracy, int numberOfBurnouts)
    {
        double penalty = 0.075 * numberOfBurnouts;
        double newAccuracy = oldAccuracy + swingFactor * (outcome - penalty);

        newAccuracy = (int)(newAccuracy * 1000) / 1000.0; // rounding to 3dp

        return newAccuracy; 
    }

    /**
     * Returns true if the given typist has completed the full passage.
     *
     * @param theTypist the typist to check
     * @return true if their progress has reached or passed the passage length
     */
    private boolean raceFinishedBy(TypistSimulation theTypist)
    {
        if (theTypist.getProgress() >= passageLength)
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
     * Shows each typist's position along the passage, burnout state,
     * and a WPM estimate based on current progress. (it does not do this)
     */
    private void printRace()
    {
        System.out.print('\u000C'); // Clear terminal

        System.out.println("  TYPING RACE — passage length: " + passageLength + " chars");
        multiplePrint('=', passageLength + 3);
        System.out.println();

        Iterator<TypistSimulation> typistsIterator = typists.iterator();
        printTypists(typistsIterator);

        multiplePrint('=', passageLength + 3);
        System.out.println();
        System.out.println("  [~] = burnt out    [<] = just mistyped"); // Should be ~ not zz
    }

    /**
     * Prints a single typist's lane.
     *
     * Examples:
     *   |          ⌨           | TURBOFINGERS (Accuracy: 0.85)
     *   |    [zz]              | HUNT_N_PECK  (Accuracy: 0.40) BURNT OUT (2 turns)
     *
     * Note: Ty forgot to show when a typist has just mistyped. That would
     * be a nice improvement — perhaps a [<] marker after their symbol.
     *
     * @param theTypist the typist whose lane to print
     */
    private void printSeat(TypistSimulation theTypist)
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
        // Append < when burnt out so the state is visible without hiding identity.
        else if (theTypist.isMistyped())
        {
            System.out.print('<');
            spacesAfter--;
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
}

class Simulation
{
    public static void main(String[] args) {
        TypingRaceSimulation race = new TypingRaceSimulation(40); 
        race.addTypist(new TypistSimulation('①', "TURBOFINGERS", 0.85));
        race.addTypist(new TypistSimulation('②', "QWERTY_QUEEN",  0.60));
        race.addTypist(new TypistSimulation('③', "HUNT_N_PECK",   0.30));
        race.startRace(); 
    }    
}