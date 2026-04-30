import java.util.ArrayList;

/**
 * The GameInfo class stores all configuration data required to set up
 * and run a typing race simulation.
 *
 * It acts as a central data container, holding information such as:
 * - The passage to be typed
 * - Number of words in the passage
 * - Number of participating typists
 * - The list of typists
 * - Global gameplay modes (auto-correct, caffeine, night mode)
 *
 * This class does not contain any game logic — it simply provides
 * structured access to game settings and state.
 *
 * @author Kishal Chhetri
 * @version 1
 */
public class GameInfo
{
    private String passage;                        // Text passage to be typed in the race
    private int numberOfWords;                     // Total word count of the passage
    private int numberOfTypists;                   // Number of typists participating
    private ArrayList<TypistSimulation> typists;   // List of typist objects

    // Global game modes
    private boolean autoCorrectMode = false;       // Reduces mistype penalties
    private boolean caffeineMode = false;          // Increases typing speed over time
    private boolean nightMode = false;             // Reduces accuracy

    /**
     * Gets the passage text.
     *
     * @return passage string
     */
    public String getPassage()
    {
        return passage;
    }

    /**
     * Sets the passage text.
     *
     * @param newPassage the passage to use in the race
     */
    public void setPassage(String newPassage)
    {
        passage = newPassage;
    }

    /**
     * Gets the number of typists.
     *
     * @return number of participants
     */
    public int getNumberOfTypists()
    {
        return numberOfTypists;
    }

    /**
     * Sets the number of typists.
     *
     * @param amount number of participants
     */
    public void setNumberOfTypists(int amount)
    {
        numberOfTypists = amount;
    }

    /**
     * Gets the list of typists.
     *
     * @return ArrayList of TypistSimulation objects
     */
    public ArrayList<TypistSimulation> getTypists()
    {
        return typists;
    }

    /**
     * Sets the list of typists.
     *
     * @param typistsArray list of typists to assign
     */
    public void setTypists(ArrayList<TypistSimulation> typistsArray)
    {
        typists = typistsArray;
    }

    /**
     * Checks if auto-correct mode is enabled.
     *
     * @return true if enabled
     */
    public boolean isAutoCorrect()
    {
        return autoCorrectMode;
    }

    /**
     * Activates auto-correct mode.
     *
     * This reduces the penalty for mistypes during the race.
     */
    public void activateAutoCorrect()
    {
        autoCorrectMode = true;
    }

    /**
     * Checks if caffeine mode is enabled.
     *
     * @return true if enabled
     */
    public boolean isCaffeine()
    {
        return caffeineMode;
    }

    /**
     * Activates caffeine mode.
     *
     * This increases typing speed over time but may increase burnout risk.
     */
    public void activateCaffeine()
    {
        caffeineMode = true;
    }

    /**
     * Checks if night mode is enabled.
     *
     * @return true if enabled
     */
    public boolean isNight()
    {
        return nightMode;
    }

    /**
     * Activates night mode.
     *
     * This reduces typist accuracy, simulating low-visibility conditions.
     */
    public void activateNight()
    {
        nightMode = true;
    }

    /**
     * Gets the number of words in the passage.
     *
     * @return word count
     */
    public int getNumberOfWords()
    {
        return numberOfWords;
    }

    /**
     * Sets the number of words in the passage.
     *
     * @param amount word count
     */
    public void setNumberOfWords(int amount)
    {
        numberOfWords = amount;
    }
}