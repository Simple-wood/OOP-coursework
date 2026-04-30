import java.awt.*;

/**
 * The TypistSimulation class models a competitor in a typing race.
 * 
 * Each typist progresses through a passage character by character,
 * influenced by their accuracy, typing style, keyboard, and accessories.
 * 
 * Typists may occasionally mistype (causing them to slide backwards)
 * or burn out (temporarily preventing them from typing).
 * 
 * The class also tracks statistics such as total characters typed,
 * correct characters typed, and number of burnouts.
 *
 * @author Kishal Chhetri
 * @version 1
 */

public class TypistSimulation
{
    private String typistName; // Name of the typist
    private char typistSymbol; // Symbol used to represent the typist visually

    // Performance attributes
    private double typistAccuracy;  // Base accuracy (0.0 to 1.0)
    private int typistProgress;    // Current progress in the passage

    // State tracking
    private boolean burntOut;  // Whether the typist is currently burnt out
    private boolean mistyped;   // Whether the last action was a mistype

    private int burntOutTurnsRemaining;    // Remaining turns of burnout
    private int numberOfBurnouts;  // Total number of burnouts experienced
    private int turns = 0; // Total turns taken

    // Statistics tracking
    private int totalTypedCharacters = 0; // Total characters attempted
    private int totalCorrectTypedCharacters = 0; // Total correctly typed characters

    // Constants
    private final double upperAccuracyLimit = 1.0;
    private final double lowerAccuracyLimit = 0.0;
    private final int MINIMUM_PROGRESS = 0;

    // Typing mechanics
    private int typeIncrement = 1; // Number of characters typed per successful turn

    // Customisation
    private String typingStyle;
    private String keyboard;
    private Color colour;
    private boolean[] accessories; // [wrist support, energy drink, headphones]      

    // Gameplay modifiers
    private double mistypeBaseChance = 0.3;
    private double burnoutChanceCap = 0.05;
    private int burnoutDuration = 3;

    /**
     * Constructs a TypistSimulation object with specified attributes.
     *
     * @param typistSymbol   character used to represent the typist
     * @param typistName     name of the typist
     * @param typistAccuracy base accuracy (0.0 to 1.0)
     * @param typingStyle    typing style (affects accuracy and burnout)
     * @param keyboard       keyboard type (affects speed and mistypes)
     * @param colour         display colour of the typist
     * @param accessories    array of accessories influencing behaviour
     */
    public TypistSimulation(char typistSymbol, String typistName, double typistAccuracy,
                            String typingStyle, String keyboard, 
                            Color colour, boolean[] accessories)
    {
        this.typistSymbol = typistSymbol;
        this.typistName = typistName;
        this.typingStyle = typingStyle;
        this.keyboard = keyboard;
        this.colour = colour;
        this.accessories = accessories;

        setAccuracy(typistAccuracy);
        resetToStart();
    }

    /**
     * Puts the typist into burnout for a given number of turns.
     * While burnt out, the typist cannot type.
     *
     * @param turns number of turns burnout should last (must be > 0)
     */
    public void burnOut(int turns)
    {
        if(turns > 0)
        {
            burntOutTurnsRemaining = turns;
            burntOut = true;
        }
    }

    /**
     * Reduces burnout duration by one turn.
     * Automatically clears burnout when duration reaches zero.
     */
    public void recoverFromBurnout()
    {
        if(burntOut)
        {
            burntOutTurnsRemaining--;
        }

        if(burntOutTurnsRemaining == 0 && burntOut)
        {
            burntOut = false;
        }
    }

    /**
     * Gets the typist's base accuracy.
     *
     * @return accuracy value between 0.0 and 1.0
     */
    public double getAccuracy()
    {
        return typistAccuracy;
    }

    /**
     * Gets the current progress through the passage.
     *
     * @return number of correctly typed characters so far
     */
    public int getProgress()
    {
        return typistProgress;
    }

    /**
     * Gets the typist's name.
     *
     * @return typist name
     */
    public String getName()
    {
        return typistName;
    }

    /**
     * Gets the typist's display symbol.
     *
     * @return typist symbol
     */
    public char getSymbol()
    {
        return typistSymbol;
    }

    /**
     * Gets remaining burnout duration.
     *
     * @return number of turns left in burnout (0 if not burnt out)
     */
    public int getBurnoutTurnsRemaining()
    {
        return burntOutTurnsRemaining;
    }

    /**
     * Gets total number of burnouts experienced.
     *
     * @return burnout count
     */
    public int getNumberOfBurnouts()
    {
        return numberOfBurnouts;
    }

    /**
     * Resets the typist to starting conditions.
     * Clears progress, burnout state, and all tracked statistics.
     */
    public void resetToStart()
    {
        typistProgress = 0;
        burntOut = false;
        burntOutTurnsRemaining = 0;
        mistyped = false;
        numberOfBurnouts = 0;
        totalTypedCharacters = 0;
        totalCorrectTypedCharacters = 0;
        turns = 0;
    }

    /**
     * Checks if the typist is currently burnt out.
     *
     * @return true if burnt out, false otherwise
     */
    public boolean isBurntOut()
    {
        return burntOut;
    }

    /**
     * Checks if the typist mistyped in the last action.
     *
     * @return true if a mistype occurred
     */
    public boolean isMistyped()
    {
        return mistyped;
    }

    /**
     * Advances the typist forward based on typing speed.
     * Should only be called when not burnt out.
     *
     * @param passageLength maximum length of the passage
     */
    public void typeCharacter(int passageLength)
    {
        typistProgress += typeIncrement;

        // Prevent overshooting the passage
        if(typistProgress > passageLength)
        {
            typistProgress = passageLength;
        }
    }

    /**
     * Moves the typist backwards due to a mistype.
     *
     * @param amount number of characters to move back (must be positive)
     */
    public void slideBack(int amount)
    {
        if(amount <= 0)
        {
            return;
        }

        typistProgress -= amount;

        // Prevent progress from dropping below zero
        if(typistProgress < MINIMUM_PROGRESS)
        {
            typistProgress = MINIMUM_PROGRESS;
        }

        mistyped = true;
    }

    /**
     * Clears the mistyped flag.
     * Should be called after handling a mistype event.
     */
    public void leaveMistyped()
    {
        if(mistyped)
        {
            mistyped = false;
        }
    }

    /**
     * Sets the typist's accuracy.
     * Ensures the value stays within valid bounds.
     *
     * @param newAccuracy new accuracy value
     */
    public void setAccuracy(double newAccuracy)
    {
        if(newAccuracy < lowerAccuracyLimit)
        {
            typistAccuracy = lowerAccuracyLimit;
        }
        else if(newAccuracy > upperAccuracyLimit)
        {
            typistAccuracy = upperAccuracyLimit;
        }
        else
        {
            typistAccuracy = newAccuracy;
        }

        // Truncate to 3 decimal places
        typistAccuracy = ((int)(typistAccuracy * 1000)) / 1000.0;
    }

    /**
     * Sets a new display symbol for the typist.
     *
     * @param newSymbol replacement symbol
     */
    public void setSymbol(char newSymbol)
    {
        typistSymbol = newSymbol;
    }

    /**
     * Increments the burnout counter.
     */
    public void incrementNumberOfBurnouts()
    {
        numberOfBurnouts++;
    }

    /**
     * Sets how many characters are typed per successful action.
     *
     * @param increment typing speed
     */
    public void setTypeIncrement(int increment)
    {
        typeIncrement = increment;
    }

    /**
     * Configures typist attributes based on typing style,
     * keyboard type, and accessories.
     *
     * This method modifies accuracy, typing speed, mistype chance,
     * and burnout behaviour.
     *
     * Assumes valid string values are provided for style and keyboard.
     */
    public void configureTypist()
    {
        // Typing style adjustments
        if(typingStyle.equals("Touch Typist"))
        {
            setAccuracy(getAccuracy() * 1.1);
            burnoutChanceCap = 0.12;
        }
        else if(typingStyle.equals("Hunt & Peck"))
        {
            setAccuracy(getAccuracy() * 1.2);
            burnoutDuration = 2;
        }
        else if(typingStyle.equals("Phone Thumbs"))
        {
            setAccuracy(getAccuracy() * 0.9);
            burnoutChanceCap = 0.02;
        }
        else if(typingStyle.equals("Voice-to-Text"))
        {
            setAccuracy(getAccuracy() * 0.7);
            burnoutDuration = 5;
        }

        // Keyboard adjustments
        if(keyboard.equals("Mechanical"))
        {
            typeIncrement = 3;
            mistypeBaseChance = 0.4;
        }
        else if(keyboard.equals("Membrane"))
        {
            typeIncrement = 2;
            mistypeBaseChance = 0.35;
        }
        else if(keyboard.equals("Touchscreen"))
        {
            mistypeBaseChance = 0.2;
        }
        else if(keyboard.equals("Stenography"))
        {
            typeIncrement = 5;
            mistypeBaseChance = 0.6;
        }

        // Accessories effects
        if(accessories[0]) // Wrist support
        {
            burnoutDuration = (int)(burnoutDuration / 2);
        }

        if(accessories[2]) // Headphones
        {
            mistypeBaseChance = mistypeBaseChance / 2;
        }
    }

    /**
     * Gets base mistype probability.
     *
     * @return mistype chance
     */
    public double getMistypeBaseChance()
    {
        return mistypeBaseChance;
    }

    /**
     * Gets maximum burnout probability.
     *
     * @return burnout chance cap
     */
    public double getBurnoutChanceCap()
    {
        return burnoutChanceCap;
    }

    /**
     * Gets typing speed increment.
     *
     * @return characters typed per turn
     */
    public int getTypeIncrement()
    {
        return typeIncrement;
    }

    /**
     * Gets burnout duration.
     *
     * @return number of turns burnout lasts
     */
    public int getBurnoutDuration()
    {
        return burnoutDuration;
    }

    /**
     * Sets burnout duration manually.
     *
     * @param amount new burnout duration
     */
    public void setBurnoutDuration(int amount)
    {
        burnoutDuration = amount;
    }

    /**
     * Checks if the typist has an energy drink.
     *
     * @return true if energy drink is equipped
     */
    public boolean hasEnergyDrink()
    {
        return accessories[1];
    }

    /**
     * Gets display colour of the typist.
     *
     * @return colour object
     */
    public Color getColour()
    {
        return colour;
    }

    /**
     * Gets total number of turns taken.
     *
     * @return turn count
     */
    public int getTurns()
    {
        return turns;
    }

    /**
     * Increments turn counter.
     *
     * @param amount number of turns to add
     */
    public void incrementNumberOfTurns(int amount)
    {
        turns += amount;
    }

    /**
     * Increments total typed characters (correct or incorrect).
     */
    public void incrementCharactersTyped()
    {
        totalTypedCharacters++;
    }

    /**
     * Increments correctly typed characters.
     * Also counts toward total typed characters.
     */
    public void incrementCorrectCharactersTyped()
    {
        totalCorrectTypedCharacters++;
        incrementCharactersTyped();
    }

    /**
     * Calculates actual accuracy based on performance.
     *
     * @return accuracy rounded to 3 decimal places
     */
    public double calculateActualAccuracy()
    {
        double accuracy = (double)totalCorrectTypedCharacters / (double)totalTypedCharacters;
        double roundedAccuracy = HelperUtilities.truncate(3, accuracy);

        return roundedAccuracy;
    }
}