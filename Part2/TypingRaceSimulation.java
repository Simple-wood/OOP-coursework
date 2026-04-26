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
    private static  double mistype_base_chance = 0.3;
    private static  int slide_back_amount = 2; 
    private static int burnout_duration = 3; 

    /**
     * Constructor for objects of class TypingRace.
     * Sets up the race with a passage of the given length.
     * Initially there are no typists seated.
     *
     * @param passageLength the number of characters in the passage to type
     */
    public TypingRaceSimulation(int passageLength, ArrayList<TypistSimulation> typists)
    {
        this.passageLength = passageLength;
        this.typists = typists;
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

    public void advanceTypists()
    {
        TypistSimulation currentTypist = null;
        Iterator<TypistSimulation> typistsIterator = typists.iterator();

        while(typistsIterator.hasNext())
        {
            currentTypist = typistsIterator.next();
            advanceTypist(currentTypist);
        }       
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
    public void advanceTypist(TypistSimulation theTypist)
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
            
            if (Math.random() < (1 - typistAccuracy) * mistype_base_chance)
            {
                theTypist.slideBack(slide_back_amount);
            }

            // Burnout check — pushing too hard increases burnout risk
            // (probability scales with accuracy squared, capped at ~0.05)
            if (Math.random() < (0.05 + (0.05 * Math.pow(typistAccuracy, 2)) * Math.pow(typistAccuracy, 2)))
            {
                theTypist.burnOut(burnout_duration);
                theTypist.incrementNumberOfBurnouts();
            }
        }
    }

    private void updateTypistRatings()
    {
        final double SWING_FACTOR = 0.025;
        final double WIN_OUTCOME = 1.0;
        final double LOSS_OUTCOME = 0.0;

        TypistSimulation currentTypist = null;
        Iterator<TypistSimulation> typistsIterator = typists.iterator();
        
        while(typistsIterator.hasNext())
        {
            currentTypist = typistsIterator.next();
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
    public boolean raceFinishedBy(TypistSimulation theTypist)
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
}