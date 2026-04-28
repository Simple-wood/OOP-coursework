public class PerformanceMetric
{
    private double wpm;
    private double trueAccuracy;
    private int burnoutCount;
    private int position;
    private double accuracyChange;

    public PerformanceMetric(double wpm, double trueAccuracy, int burnoutCount, int position, double accuracyChange)
    {
        this.wpm = wpm;
        this.trueAccuracy = trueAccuracy;
        this.burnoutCount = burnoutCount;
        this.position = position;
        this.accuracyChange = accuracyChange;
    }

    public double getWPM()
    {
        return wpm;
    }

    public double getTrueAccuracy()
    {
        return trueAccuracy;
    }

    public double getAccuracyChange()
    {
        return accuracyChange;
    }

    public int getPosition()
    {
        return position;
    }

    public int getBurnoutCount()
    {
        return burnoutCount;
    }

}