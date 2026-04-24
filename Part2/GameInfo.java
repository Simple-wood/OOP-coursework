import java.util.ArrayList;

public class GameInfo
{
    private String passage;
    private int numberOfTypists;
    private ArrayList<TypistSimulation> typists;
    private boolean autoCorrectMode = false;
    private boolean caffeineMode = false;
    private boolean nightMode = false;

    public String getPassage()
    {
        return passage;
    }

    public void setPassage(String newPassage)
    {
        passage = newPassage;
    }

    public int getNumberOfTypists()
    {
        return numberOfTypists;
    }

    public void setNumberOfTypists(int amount)
    {
        numberOfTypists = amount;
    }

    public ArrayList<TypistSimulation> getTypists()
    {
        return typists;
    }

    public void setTypists(ArrayList<TypistSimulation> typistsArray)
    {
        typists = typistsArray;
    }

    public boolean isAutoCorrect()
    {
        return autoCorrectMode;
    }

    public void activateAutoCorrect()
    {
        autoCorrectMode = true;
    }

    public boolean isCaffeine()
    {
        return caffeineMode;
    }

    public void activateCaffeine()
    {
        caffeineMode = true;
    }

    public boolean isNight()
    {
        return nightMode;
    }

    public void activateNight()
    {
        nightMode = true;
    }
}