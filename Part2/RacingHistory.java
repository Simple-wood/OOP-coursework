import java.util.ArrayList;

public class RacingHistory {
    private ArrayList<PerformanceMetric> raceHistory;
    private double bestWPM;

    public RacingHistory(PerformanceMetric metric)
    {
        raceHistory = new ArrayList<>();
        raceHistory.add(metric);
        bestWPM = metric.getWPM();
    }

    public double getBestWPM()
    {
        return bestWPM;
    }

    public void setBestWPM(double amount)
    {
        if(amount > bestWPM)
        {
            bestWPM = amount;
        }
    }

    public void addMetric(PerformanceMetric metric)
    {
        double metricWPM = metric.getWPM();
        raceHistory.add(metric);
        setBestWPM(metricWPM);
    }

    public ArrayList<PerformanceMetric> getRaceHistory()
    {
        return raceHistory;
    }
}
