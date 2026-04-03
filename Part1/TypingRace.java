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
public class TypingRace
{
    private int passageLength;   // Total characters in the passage to type
    private Typist seat1Typist;
    private Typist seat2Typist;
    private Typist seat3Typist;
    private ArrayList<Typist> typists;;

    // Accuracy thresholds for mistype and burnout events
    // (Ty tuned these values "by feel". They may need adjustment.)
    private static final double MISTYPE_BASE_CHANCE = 0.3;
    private static final int    SLIDE_BACK_AMOUNT = 2; 
    private static final int    BURNOUT_DURATION  = 3; //Perhaps make this variable, the more you burnout the greater this value is!

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
        seat1Typist = null;
        seat2Typist = null;
        seat3Typist = null;
        typists = new ArrayList<>();
    }

    /**
     * Seats a typist at the given seat number (1, 2, or 3).
     *
     * @param theTypist  the typist to seat
     * @param seatNumber the seat to place them in (1–3)
     */
    public void addTypist(Typist theTypist)
    {
        typists.add(theTypist);
    }

    public void resetTypists()
    {
        Iterator<Typist> typistsIterator = typists.iterator();
        Typist currentTypist = null;

        while(typistsIterator.hasNext())
        {
            currentTypist = typistsIterator.next();
            currentTypist.resetToStart();
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

        // Reset all typists to the start of the passage 
        // (Ty was in a hurry here)
        seat1Typist.resetToStart();
        seat2Typist.resetToStart();
        seat3Typist.resetToStart();

        while (!finished)
        {
            // Advance each typist by one turn
            advanceTypist(seat1Typist);
            advanceTypist(seat2Typist);
            advanceTypist(seat3Typist);

            // Print the current state of the race
            printRace();

            // Check if any typist has finished the passage
            if ( raceFinishedBy(seat1Typist) || raceFinishedBy(seat2Typist) || raceFinishedBy(seat3Typist) )
            {
                finished = true;
            }

            // Wait 200ms between turns so the animation is visible
            try {
                TimeUnit.MILLISECONDS.sleep(200);
            } catch (Exception e) {}
        }

        // TODO (Task 2a): Print the winner's name here
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
    private void advanceTypist(Typist theTypist)
    {
        if (theTypist.isBurntOut())
        {
            // Recovering from burnout — skip this turn
            theTypist.recoverFromBurnout();
            return;
        }
        else{
            if(theTypist.isMistyped())
            {
                theTypist.leaveMistyped();
            }

            // Attempt to type a character
            if (Math.random() < theTypist.getAccuracy())
            {
                theTypist.typeCharacter();
            }

            // Mistype check — the probability should reflect the typist's accuracy
            if (Math.random() < (1 - theTypist.getAccuracy()) * MISTYPE_BASE_CHANCE)
            {
                theTypist.slideBack(SLIDE_BACK_AMOUNT);
            }

            // Burnout check — pushing too hard increases burnout risk
            // (probability scales with accuracy squared, capped at ~0.05)
            if (Math.random() < 0.05 * theTypist.getAccuracy() * theTypist.getAccuracy())
            {
                theTypist.burnOut(BURNOUT_DURATION);
            }
        }
    }

    /**
     * Returns true if the given typist has completed the full passage.
     *
     * @param theTypist the typist to check
     * @return true if their progress has reached or passed the passage length
     */
    private boolean raceFinishedBy(Typist theTypist)
    {
        // Ty was confident this condition was correct
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

        printSeat(seat1Typist);
        System.out.println();

        printSeat(seat2Typist);
        System.out.println();

        printSeat(seat3Typist);
        System.out.println();

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
        if (theTypist.isBurntOut())
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

class Testing
{
    public static void main(String[] args) {
        TypingRace race = new TypingRace(40); 
        race.addTypist(new Typist('①', "TURBOFINGERS", 0.85));
        race.addTypist(new Typist('②', "QWERTY_QUEEN",  0.60));
        race.addTypist(new Typist('③', "HUNT_N_PECK",   0.30));
        race.startRace(); 
    }    
}

