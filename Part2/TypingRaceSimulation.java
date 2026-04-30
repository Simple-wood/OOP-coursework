import java.util.ArrayList;
import java.util.Iterator;
import java.lang.Math;

/**
 * The TypingRaceSimulation class manages a full typing race between multiple typists.
 *
 * Each typist progresses through a passage over a series of turns, influenced by
 * their accuracy, typing configuration, and random events such as mistypes and burnout.
 *
 * The simulation tracks race progress, determines finishing order, and calculates
 * post-race performance metrics including words per minute and accuracy changes.
 *
 * Global modes can modify gameplay behaviour for all typists simultaneously.
 *
 * @author Kishal Chhetri
 * @version 1
 */
public class TypingRaceSimulation
{
    private int passageLength;   // Total number of characters required to finish the race
    private ArrayList<TypistSimulation> typists;  // All participants in the race
    private ArrayList<TypistSimulation> winners;  // Typists in the order they finished

    // Gameplay configuration
    private int slide_back_amount = 2; // Number of characters lost on a mistype

    // Global modifiers affecting all typists:
    // [0] = autocorrect mode (reduces penalty of mistypes)
    // [1] = caffeine mode (increases typing speed over time)
    // [2] = night mode (reduces accuracy)
    private boolean[] globalModes;

    /**
     * Constructs a TypingRaceSimulation with a given passage length and typists.
     *
     * @param passageLength total characters required to complete the race
     * @param typists list of participating typists
     * @param modes global gameplay modifiers
     */
    public TypingRaceSimulation(int passageLength, ArrayList<TypistSimulation> typists, boolean[] modes)
    {
        this.passageLength = passageLength;
        this.typists = typists;
        this.winners = new ArrayList<>();
        this.globalModes = modes;
    }

    /**
     * Applies global and individual configuration settings before the race begins.
     *
     * Effects include:
     * - Adjusting slide-back penalty
     * - Modifying typing speed and accuracy
     * - Applying typist-specific configurations
     * - Applying energy drink boosts (if present)
     */
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

            // Energy drink gives a large temporary accuracy boost
            if(currentTypist.hasEnergyDrink())
            {
                currentTypist.setAccuracy(currentTypist.getAccuracy() * 2);
            }

            // Apply individual configuration (typing style, keyboard, accessories)
            currentTypist.configureTypist();
        }
    }

    /**
     * Resets all typists to their starting state for a new race.
     */
    public void resetTypists()
    { 
        Iterator<TypistSimulation> typistsIterator = typists.iterator();

        while(typistsIterator.hasNext())
        {
            typistsIterator.next().resetToStart();
        }
    }

    /**
     * Increases typing speed for all typists.
     *
     * Note: The parameter is not directly used; increment is always +1.
     * (Possibly unintended or placeholder logic.)
     *
     * @param amount increment amount
     */
    private void updateTypistIncrements(int amount)
    {
        Iterator<TypistSimulation> typistsIterator = typists.iterator();

        while(typistsIterator.hasNext())
        {
            TypistSimulation currentTypist = typistsIterator.next();
            currentTypist.setTypeIncrement(currentTypist.getTypeIncrement() + amount);
        }
    }

    /**
     * Reduces accuracy for all typists by a given amount.
     *
     * @param decreaseAmount amount to subtract from accuracy
     */
    private void updateTypistAccuracy(double decreaseAmount)
    {
        Iterator<TypistSimulation> typistsIterator = typists.iterator();

        while(typistsIterator.hasNext())
        {
            TypistSimulation currentTypist = typistsIterator.next();
            currentTypist.setAccuracy(currentTypist.getAccuracy() - decreaseAmount);
        }
    }

    /**
     * Simulates one turn for a given typist.
     *
     * Behaviour:
     * - If burnt out → recover instead of typing
     * - If mistyped previously → clear mistype flag
     * - Attempt to type based on accuracy
     * - Possible outcomes:
     *      • Successful typing
     *      • Mistype (slide back)
     *      • Burnout (temporary inability to type)
     *
     * Additional effects:
     * - Caffeine mode increases speed over time
     * - Energy drink causes late-race accuracy crash
     *
     * @param theTypist the typist taking a turn
     */
    public void advanceTypist(TypistSimulation theTypist)
    { 
        // Skip if already finished
        if(winners.contains(theTypist))
        {
            return;
        }

        theTypist.incrementNumberOfTurns(1);
        double typistAccuracy = theTypist.getAccuracy();

        // Handle burnout recovery
        if (theTypist.isBurntOut())
        {
            theTypist.recoverFromBurnout();
            return;
        }

        // Clear previous mistype state
        if(theTypist.isMistyped())
        {
            theTypist.leaveMistyped();
        }

        // Attempt successful typing
        if (Math.random() < typistAccuracy)
        {
            theTypist.typeCharacter(passageLength);

            // Check if race is completed
            if(raceFinishedBy(theTypist))
            {
                if(!winners.contains(theTypist))
                {
                    winners.add(theTypist);
                }
                return;
            }

            theTypist.incrementCorrectCharactersTyped();

            // Burnout probability increases with higher accuracy
            if (Math.random() < (theTypist.getBurnoutChanceCap() +
                (theTypist.getBurnoutChanceCap() * Math.pow(typistAccuracy, 3))))
            {
                theTypist.burnOut(theTypist.getBurnoutDuration());
                theTypist.incrementNumberOfBurnouts();
            }
        }
        // Mistype event
        else if (Math.random() < (1 - typistAccuracy) * theTypist.getMistypeBaseChance())
        {
            theTypist.incrementCharactersTyped();
            theTypist.slideBack(slide_back_amount);
        }

        // Caffeine mode scaling effect
        if(globalModes[1])
        {
            if(theTypist.getTurns() == 10)
            {
                updateTypistIncrements(1);
                theTypist.setBurnoutDuration(theTypist.getBurnoutDuration() + 2);
            }
        }

        // Energy drink crash effect
        if(theTypist.hasEnergyDrink())
        {
            if(theTypist.getTurns() == 15)
            {
                theTypist.setAccuracy(theTypist.getAccuracy() * 0.25);
            }
        }
    }

    /**
     * Updates typist accuracy ratings after a race.
     *
     * Winners gain accuracy, while burnout penalties reduce gains.
     */
    private void updateTypistRatings()
    {
        final double SWING_FACTOR = 0.025;
        final double WIN_OUTCOME = 1.0;
        final double LOSS_OUTCOME = 0.0;

        Iterator<TypistSimulation> typistsIterator = typists.iterator();

        while(typistsIterator.hasNext())
        {
            TypistSimulation currentTypist = typistsIterator.next();
            double currentAccuracy = currentTypist.getAccuracy();
            double outcome = LOSS_OUTCOME;

            if(currentTypist == winners.get(0))
            {
                outcome = WIN_OUTCOME;
            }

            double newAccuracy = calculateNewAccuracy(
                SWING_FACTOR,
                outcome,
                currentAccuracy,
                currentTypist.getNumberOfBurnouts()
            );

            currentTypist.setAccuracy(newAccuracy);
        }
    }

    /**
     * Calculates new accuracy based on performance and burnout penalties.
     *
     * @param swingFactor adjustment sensitivity
     * @param outcome win (1.0) or loss (0.0)
     * @param oldAccuracy previous accuracy
     * @param numberOfBurnouts burnout penalty factor
     * @return updated accuracy (rounded to 3dp)
     */
    private double calculateNewAccuracy(double swingFactor, double outcome,
                                        double oldAccuracy, int numberOfBurnouts)
    {
        double penalty = 0.075 * numberOfBurnouts;
        double newAccuracy = oldAccuracy + swingFactor * (outcome - penalty);

        return (int)(newAccuracy * 1000) / 1000.0;
    }

    /**
     * Checks if a typist has completed the passage.
     *
     * @param theTypist typist to check
     * @return true if finished
     */
    public boolean raceFinishedBy(TypistSimulation theTypist)
    {
        return theTypist.getProgress() >= passageLength;
    }

    /**
     * Checks if all typists have finished the race.
     *
     * @return true if race is fully completed
     */
    public boolean raceConcluded()
    {
        return winners.size() == typists.size();
    }

    /**
     * Generates performance metrics for all typists in finishing order.
     *
     * Metrics include:
     * - Words per minute (WPM)
     * - Actual accuracy
     * - Burnout count
     * - Finishing position
     * - Accuracy change
     *
     * @param results list to populate with results
     * @param numberOfWords total words in passage
     * @param timePerTurn time duration per turn
     */
    public void updateAndGetResults(ArrayList<PerformanceMetric> results, int numberOfWords, double timePerTurn)
    {
        if(!results.isEmpty())
        {
            results.clear();
        }

        int position = 1;
        Iterator<TypistSimulation> winnersIterator = winners.iterator();

        while(winnersIterator.hasNext())
        {
            TypistSimulation currentTypist = winnersIterator.next();

            double oldAccuracy = currentTypist.getAccuracy();
            double newAccuracy = calculateNewAccuracy(0.025, 1.0,
                    oldAccuracy, currentTypist.getNumberOfBurnouts());

            double wpm = HelperUtilities.truncate(2,
                (numberOfWords / (timePerTurn * currentTypist.getTurns())) * 60.0);

            double trueAccuracy = currentTypist.calculateActualAccuracy();
            double accuracyChange = HelperUtilities.truncate(3, newAccuracy - oldAccuracy);

            results.add(new PerformanceMetric(
                wpm,
                trueAccuracy,
                currentTypist.getNumberOfBurnouts(),
                position,
                accuracyChange,
                currentTypist
            ));

            position++;
        }
    }

    /**
     * Resets the race for a new run.
     *
     * - Updates typist ratings
     * - Resets typists
     * - Clears winner list
     */
    public void restartRace()
    {
        updateTypistRatings();
        resetTypists();
        winners.clear();
    }
}