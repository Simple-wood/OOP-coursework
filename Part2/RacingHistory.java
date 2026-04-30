import java.util.ArrayList;

/**
 * The RacingHistory class maintains a record of a typist's past race performances.
 *
 * It stores a list of PerformanceMetric objects, each representing the outcome
 * of a completed race, and tracks the highest WPM achieved across all races.
 *
 * This acts as a long-term memory for a typist, allowing analysis of improvement
 * (or decline…) over time.
 *
 * Note: The best WPM is updated incrementally as new results are added,
 * rather than recalculated from the full history each time.
 * 
 * @author Kishal Chhetri
 * @version 1
 * 
 */
public class RacingHistory
{
    private ArrayList<PerformanceMetric> raceHistory; // List of all recorded race results
    private double bestWPM;                           // Highest WPM achieved across all races

    /**
     * Constructs a RacingHistory with an initial performance metric.
     *
     * This ensures that the history always contains at least one entry
     * and initializes the best WPM accordingly.
     *
     * @param metric the first recorded race result
     */
    public RacingHistory(PerformanceMetric metric)
    {
        raceHistory = new ArrayList<>();
        raceHistory.add(metric);

        // Initialise best WPM from the first race result
        bestWPM = metric.getWPM();
    }

    /**
     * Gets the highest WPM achieved in the recorded history.
     *
     * @return best WPM value
     */
    public double getBestWPM()
    {
        return bestWPM;
    }

    /**
     * Updates the best WPM if the provided value exceeds the current best.
     *
     * Note: This method will not lower the best WPM, even if a smaller value is passed.
     *
     * @param amount candidate WPM value
     */
    public void setBestWPM(double amount)
    {
        if(amount > bestWPM)
        {
            bestWPM = amount;
        }
    }

    /**
     * Adds a new performance metric to the race history.
     *
     * Also checks whether this new result sets a new best WPM.
     *
     * @param metric the performance result to add
     */
    public void addMetric(PerformanceMetric metric)
    {
        double metricWPM = metric.getWPM();

        raceHistory.add(metric);

        // Update best WPM if applicable
        setBestWPM(metricWPM);
    }

    /**
     * Returns the full race history.
     *
     * Note: This returns the internal list directly, meaning external code
     * can modify it. If immutability is desired, consider returning a copy instead.
     *
     * @return list of PerformanceMetric objects
     */
    public ArrayList<PerformanceMetric> getRaceHistory()
    {
        return raceHistory;
    }
}