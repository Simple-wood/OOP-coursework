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
    private ArrayList<TypistSimulation> winners;

    // Accuracy thresholds for mistype and burnout events
    // (Ty tuned these values "by feel". They may need adjustment.)
    private int slide_back_amount = 2; 
    private boolean[] globalModes; // [autocorrect, caffeine, night]

    /**
     * Constructor for objects of class TypingRace.
     * Sets up the race with a passage of the given length.
     * Initially there are no typists seated.
     *
     * @param passageLength the number of characters in the passage to type
     */
    public TypingRaceSimulation(int passageLength, ArrayList<TypistSimulation> typists, boolean[] modes)
    {
        this.passageLength = passageLength;
        this.typists = typists;
        this.winners = new ArrayList<>();
        this.globalModes = modes;
    }

    public void configureGame()
    {
        if(globalModes[0])
        {
            slide_back_amount = slide_back_amount / 2;
        }

        if(globalModes[1])
        {
            updateTypistIncrements(3);
        }

        if(globalModes[2])
        {
            updateTypistAccuracy(0.05);
        }

        Iterator<TypistSimulation> typistsIterator = typists.iterator();
        TypistSimulation currentTypist = null;

        while(typistsIterator.hasNext())
        {
            currentTypist = typistsIterator.next();

            if(currentTypist.hasEnergyDrink())
            {
                currentTypist.setAccuracy(currentTypist.getAccuracy() * 2);
            }

            currentTypist.configureTypist();
        }
    }

    public void resetTypists()
    { 
        Iterator<TypistSimulation> typistsIterator = typists.iterator();
        TypistSimulation currentTypist = null;

        while(typistsIterator.hasNext())
        {
            currentTypist = typistsIterator.next();
            currentTypist.resetToStart();
        }
    }

    private void updateTypistIncrements(int amount)
    {
        Iterator<TypistSimulation> typistsIterator = typists.iterator();
        TypistSimulation currentTypist = null;

        while(typistsIterator.hasNext())
        {
            currentTypist = typistsIterator.next();
            currentTypist.setTypeIncrement(currentTypist.getTypeIncrement() + 1);
        }
    }

    private void updateTypistAccuracy(double decreaseAmount)
    {
        Iterator<TypistSimulation> typistsIterator = typists.iterator();
        TypistSimulation currentTypist = null;

        while(typistsIterator.hasNext())
        {
            currentTypist = typistsIterator.next();
            currentTypist.setAccuracy(currentTypist.getAccuracy() - decreaseAmount);
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
        if(winners.contains(theTypist))
        {
            return;
        }

        theTypist.incrementNumberOfTurns(1);
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
            theTypist.typeCharacter(passageLength);
            if(raceFinishedBy(theTypist)) // If we have finished the race now, there is no need to check for burnouts or mistypes!
            {
                if(! winners.contains(theTypist))
                {
                    winners.add(theTypist);
                }

                return;
            }

            theTypist.incrementCorrectCharactersTyped();

            // Burnout check — pushing too hard increases burnout risk
            // (probability scales with accuracy squared, capped at ~0.05)
            if (Math.random() < (theTypist.getBurnoutChanceCap() + (theTypist.getBurnoutChanceCap() * Math.pow(typistAccuracy, 3))))
            {
                theTypist.burnOut(theTypist.getBurnoutDuration());
                theTypist.incrementNumberOfBurnouts();
            }
        }
        else if (Math.random() < (1 - typistAccuracy) * theTypist.getMistypeBaseChance())
            {
                theTypist.incrementCharactersTyped();
                theTypist.slideBack(slide_back_amount);
            }

        if(globalModes[1])
        {
            if(theTypist.getTurns() == 10)
            {
                updateTypistIncrements(1);
                theTypist.setBurnoutDuration(theTypist.getBurnoutDuration() + 2); // Increased burnout risk now -> burn out duration lasts longer!
            }
        }

        if(theTypist.hasEnergyDrink())
        {
            if(theTypist.getTurns()== 15){
                theTypist.setAccuracy(theTypist.getAccuracy() * 0.25);
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
            
            if(currentTypist == winners.get(0))
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

    public boolean raceConcluded()
    {
        if(winners.size() == typists.size())
        {
            return true;
        }

        return false;
    }

    public void updateAndGetResults(ArrayList<PerformanceMetric> results, int numberOfWords, double timePerTurn)
    {
        int position = 1;
        Iterator<TypistSimulation> winnersIterator = winners.iterator();
        TypistSimulation currentTypist = winnersIterator.next();
        double oldAccuracy = currentTypist.getAccuracy();
        int numberOfBurnouts = currentTypist.getNumberOfBurnouts();
        double newAccuracy = calculateNewAccuracy(0.025, 1.0, oldAccuracy, numberOfBurnouts);
        double wpm = HelperUtilities.truncate(2, (numberOfWords / (timePerTurn * currentTypist.getTurns())) * 60.0);
        double trueAccuracy = currentTypist.calculateActualAccuracy();
        double accuracyChange = HelperUtilities.truncate(3, newAccuracy - oldAccuracy);
        PerformanceMetric result = new PerformanceMetric(wpm, trueAccuracy, numberOfBurnouts, position, accuracyChange, currentTypist);
        results.add(result);


        while(winnersIterator.hasNext())
        {
            position++;
            currentTypist = winnersIterator.next();
            oldAccuracy = currentTypist.getAccuracy();
            numberOfBurnouts = currentTypist.getNumberOfBurnouts();
            newAccuracy = calculateNewAccuracy(0.025, 1.0, oldAccuracy, numberOfBurnouts);
            wpm = HelperUtilities.truncate(2, (numberOfWords / (timePerTurn * currentTypist.getTurns())) * 60.0);
            trueAccuracy = currentTypist.calculateActualAccuracy();
            accuracyChange = HelperUtilities.truncate(3, newAccuracy - oldAccuracy);
            result = new PerformanceMetric(wpm, trueAccuracy, numberOfBurnouts, position, accuracyChange, currentTypist);
            results.add(result);
        }
    }

    public void restartRace()
    {
        updateTypistRatings();
        resetTypists();
        winners.clear();
    }
}