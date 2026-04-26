import java.awt.*;

/**
 * The Typist class is used to represent individual competitors in the Typing race game. 
 *
 * Starter code generously abandoned by Ty Posaurus, your predecessor,
 * who typed with two fingers and considered that "good enough".
 * He left a sticky note: "the slide-back thing is optional probably".
 * It is not optional. Good luck.
 *
 * @author Kishal Chhetri
 * @version 29/03/2026
 */

public class TypistSimulation
{
    // Fields of class Typist
    // Hint: you will need six fields. Think carefully about their types.
    // One of them tracks how far along the passage the typist has reached.
    // Another tracks whether the typist is currently burnt out.
    // A third tracks HOW MANY turns of burnout remain (not just whether they are burnt out).
    // The remaining three should be fairly obvious.

    private String typistName;
    private char typistSymbol;
    private double typistAccuracy;
    private int typistProgress;
    private boolean burntOut;
    private boolean mistyped; // boolean flag to represent if a typist has mistyped or not
    private int burntOutTurnsRemaining;

    private int numberOfBurnouts;

    private final double upperAccuracyLimit = 1.0;
    private final double lowerAccuracyLimit = 0.0;
    private final int MINIMUM_PROGRESS = 0; // Progress cannot go below 0
    private int typeIncrement = 1;

    private String typingStyle = null;
    private String keyboard = null;
    private Color colour;
    private boolean[] accessories;

    private double mistypeBaseChance = 0.3;
    private double burnoutChanceCap = 0.05;
    private int burnoutDuration = 3;

    // Constructor of class Typist
    /**
     * Constructor for objects of class Typist.
     * Creates a new typist with a given symbol, name, and accuracy rating.
     *
     * @param typistSymbol  a single Unicode character representing this typist (e.g. '①', '②', '③')
     * @param typistName    the name of the typist (e.g. "TURBOFINGERS")
     * @param typistAccuracy the typist's accuracy rating, between 0.0 and 1.0
     */ 
    public TypistSimulation(char typistSymbol, String typistName, double typistAccuracy, String typingStyle, String keyboard, 
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

    // Methods of class Typist

    /**
     * Sets this typist into a burnout state for a given number of turns.
     * A burnt-out typist cannot type until their burnout has worn off.
     *
     * @param turns the number of turns the burnout will last
     */
    public void burnOut(int turns)
    {
        if(turns > 0) // turns must be greater than 0
        {
            burntOutTurnsRemaining = turns;
            burntOut = true;    
        }
    }

    /**
     * Reduces the remaining burnout counter by one turn.
     * When the counter reaches zero, the typist recovers automatically.
     * Has no effect if the typist is not currently burnt out.
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
     * Returns the typist's accuracy rating.
     *
     * @return accuracy as a double between 0.0 and 1.0
     */
    public double getAccuracy()
    {
        return typistAccuracy;
    }

    /**
     * Returns the typist's current progress through the passage.
     * Progress is measured in characters typed correctly so far.
     * Note: this value can decrease if the typist mistypes.
     *
     * @return progress as a non-negative integer
     */
    public int getProgress()
    {
        return typistProgress;
    }

    /**
     * Returns the name of the typist.
     *
     * @return the typist's name as a String
     */
    public String getName()
    {
        return typistName;
    }

    /**
     * Returns the character symbol used to represent this typist.
     *
     * @return the typist's symbol as a char
     */
    public char getSymbol()
    {
        return typistSymbol;
    }

    /**
     * Returns the number of turns of burnout remaining.
     * Returns 0 if the typist is not currently burnt out.
     *
     * @return burnout turns remaining as a non-negative integer
     */
    public int getBurnoutTurnsRemaining()
    {
        return burntOutTurnsRemaining;
    }

    public int getNumberOfBurnouts()
    {
        return numberOfBurnouts;
    }

    /**
     * Resets the typist to their initial state, ready for a new race.
     * Progress returns to zero, burnout is cleared entirely.
     */
    public void resetToStart()
    {
        typistProgress = 0;
        burntOut = false;
        burntOutTurnsRemaining = 0; // To entirely clear burnout, bruntOutTurnsRemaining must be set to 0
        mistyped = false;
        numberOfBurnouts = 0;
    }

    /**
     * Returns true if this typist is currently burnt out, false otherwise.
     *
     * @return true if burnt out
     */
    public boolean isBurntOut()
    {
        return burntOut;  
    }

    public boolean isMistyped()
    {
        return mistyped;
    }

    /**
     * Advances the typist forward by one character along the passage.
     * Should only be called when the typist is not burnt out.
     */
    public void typeCharacter(int passageLength)
    {
        typistProgress += typeIncrement;

        if(typistProgress > passageLength)
        {
            typistProgress = passageLength;
        }
    }

    /**
     * Moves the typist backwards by a given number of characters (a mistype).
     * Progress cannot go below zero — the typist cannot slide off the start.
     *
     * @param amount the number of characters to slide back (must be positive)
     */
    public void slideBack(int amount)
    {
        if(amount <= 0)
        {
            return;
        }
        
        typistProgress -= amount;

        if(typistProgress < MINIMUM_PROGRESS)
        {
            typistProgress = MINIMUM_PROGRESS; // Ensures progress cannot go below 0
        }

        mistyped = true;
    }

    public void leaveMistyped()
    {
        if(mistyped)
        {
            mistyped = false;
        }
    }

    /**
     * Sets the accuracy rating of the typist.
     * Values below 0.0 should be set to 0.0; values above 1.0 should be set to 1.0.
     *
     * @param newAccuracy the new accuracy rating
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
    }

    /**
     * Sets the symbol used to represent this typist.
     *
     * @param newSymbol the new symbol character
     */
    public void setSymbol(char newSymbol)
    {
        typistSymbol = newSymbol;
    }

    public void incrementNumberOfBurnouts()
    {
        numberOfBurnouts++;
    }

    public void setTypeIncrement(int increment)
    {
        typeIncrement = increment;
    }

    public void configureTypist()
    {
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

        if(accessories[0])
        {
            burnoutDuration = (int) (burnoutDuration / 2);
        }

        if(accessories[2])
        {
            mistypeBaseChance = mistypeBaseChance / 2;
        }
    }

    public double getMistypeBaseChance()
    {
        return mistypeBaseChance;
    }

    public double getBurnoutChanceCap()
    {
        return burnoutChanceCap;
    }

    public int getTypeIncrement()
    {
        return typeIncrement;
    }

    public int getBurnoutDuration()
    {
        return burnoutDuration;
    }

    public boolean hasEnergyDrink()
    {
        return accessories[1];
    }

    public Color getColour()
    {
        return colour;
    }
}