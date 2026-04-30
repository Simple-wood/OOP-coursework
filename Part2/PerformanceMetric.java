public class PerformanceMetric
{
    private double wpm;
    private double trueAccuracy;
    private int burnoutCount;
    private int position;
    private TypistSimulation typist;
    private double accuracyChange;
    private double accuracy;

    public PerformanceMetric(double wpm, double trueAccuracy, int burnoutCount, int position, double accuracyChange, TypistSimulation typist)
    {
        this.wpm = wpm;
        this.trueAccuracy = trueAccuracy;
        this.burnoutCount = burnoutCount;
        this.position = position;
        this.accuracyChange = accuracyChange;
        this.typist = typist;
        this.accuracy = typist.getAccuracy();
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

    public double getAccuracy()
    {
        return accuracy;
    }

    public TypistSimulation getTypist()
    {
        return typist;
    }

}