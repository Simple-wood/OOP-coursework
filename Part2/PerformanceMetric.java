/**
 * The PerformanceMetric class represents the post-race statistics for a typist.
 *
 * Each instance captures a snapshot of a typist's performance in a single race,
 * including speed, accuracy, burnout frequency, and finishing position.
 *
 * This acts as a lightweight "results card" that can be displayed, stored,
 * or analysed after the race concludes.
 *
 * @author Kishal Chhetri
 * @version 1
 */
public class PerformanceMetric
{
    private double wpm;              // Words per minute achieved during the race
    private double trueAccuracy;     // Actual accuracy based on correct vs total keystrokes
    private int burnoutCount;        // Number of burnouts experienced during the race
    private int position;            // Finishing position (1 = winner)
    private TypistSimulation typist; // Reference to the typist this metric belongs to

    private double accuracyChange;   // Change in accuracy rating after the race
    private double accuracy;         // Accuracy rating at the time results were recorded

    /**
     * Constructs a PerformanceMetric object for a typist after a race.
     *
     * @param wpm              words per minute achieved
     * @param trueAccuracy     actual measured accuracy during the race
     * @param burnoutCount     number of burnouts experienced
     * @param position         finishing position in the race
     * @param accuracyChange   change in accuracy rating after the race
     * @param typist           the typist this result belongs to
     */
    public PerformanceMetric(double wpm, double trueAccuracy, int burnoutCount,
                             int position, double accuracyChange, TypistSimulation typist)
    {
        this.wpm = wpm;
        this.trueAccuracy = trueAccuracy;
        this.burnoutCount = burnoutCount;
        this.position = position;
        this.accuracyChange = accuracyChange;
        this.typist = typist;

        // Store accuracy at this moment to preserve historical results
        this.accuracy = typist.getAccuracy();
    }

    /**
     * Gets the typist's typing speed.
     *
     * @return words per minute (WPM)
     */
    public double getWPM()
    {
        return wpm;
    }

    /**
     * Gets the typist's true accuracy based on actual performance.
     *
     * @return accuracy as a value between 0.0 and 1.0
     */
    public double getTrueAccuracy()
    {
        return trueAccuracy;
    }

    /**
     * Gets the change in accuracy rating after the race.
     *
     * @return accuracy difference (positive or negative)
     */
    public double getAccuracyChange()
    {
        return accuracyChange;
    }

    /**
     * Gets the finishing position of the typist.
     *
     * @return position (1 = first place)
     */
    public int getPosition()
    {
        return position;
    }

    /**
     * Gets the number of burnouts experienced during the race.
     *
     * @return burnout count
     */
    public int getBurnoutCount()
    {
        return burnoutCount;
    }

    /**
     * Gets the typist's stored accuracy at the time of result generation.
     *
     * Note: This may differ from the typist's current accuracy if it has
     * been updated after the race.
     *
     * @return stored accuracy value
     */
    public double getAccuracy()
    {
        return accuracy;
    }

    /**
     * Gets the typist associated with this performance metric.
     *
     * @return TypistSimulation object
     */
    public TypistSimulation getTypist()
    {
        return typist;
    }
}