import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.regex.*;

/**
 * The HelperUtilities class provides a collection of static utility methods
 * used throughout the typing race application.
 *
 * These methods support:
 * - Input validation (numbers, doubles, characters)
 * - Text processing (word counting, truncation)
 * - UI styling and layout (padding, borders, alignment)
 * - Component refreshing
 *
 * This class is stateless and designed for reuse across different parts
 * of the application.
 * 
 * @author Kishal Chhetri
 * @version 1
 * 
 */
public class HelperUtilities
{
    /**
     * Checks if a given string can be parsed as an integer.
     *
     * @param text input string
     * @return true if the string represents a valid integer
     */
    public static boolean isNumber(String text)
    {
        try
        {
           Integer.parseInt(text);
        }
        catch(NumberFormatException e)
        {
            return false;
        }

        return true;
    }

    /**
     * Checks if a given string can be parsed as a double.
     *
     * @param text input string
     * @return true if the string represents a valid double
     */
    public static boolean isDouble(String text)
    {
        try
        {
            Double.parseDouble(text);
        }
        catch(NumberFormatException e)
        {
            return false;
        }

        return true;
    }

    /**
     * Checks if a given string represents a single character.
     *
     * Leading and trailing whitespace is ignored.
     *
     * @param text input string
     * @return true if the trimmed string has length 1
     */
    public static boolean isChar(String text)
    {
        String checkText = text.trim();

        if(checkText.length() == 1)
        {
            return true;
        }

        return false;
    }

    /**
     * Counts the number of words in a passage.
     *
     * Words are defined as sequences of alphanumeric characters.
     * (i.e. letters and digits only)
     *
     * @param passage text to analyse
     * @return number of words found
     */
    public static int countWords(String passage)
    {
        int count = 0;

        // Regex matches sequences of letters and digits
        Pattern pattern = Pattern.compile("[a-zA-Z0-9]+");
        Matcher matcher = pattern.matcher(passage);

        while(matcher.find())
        {
            count ++;
        }

        return count;
    }

    /**
     * Truncates a double to a specified number of decimal places.
     *
     * Note: This does NOT round — it simply cuts off extra precision.
     *
     * @param degrees number of decimal places
     * @param amount value to truncate
     * @return truncated value
     */
    public static double truncate(int degrees, double amount)
    {
        double rounded = (double)((int)(amount * (Math.pow(10.0, degrees))))
                         / (Math.pow(10.0, degrees));

        return rounded;
    }

    /**
     * Adds a styled title panel to the top of a container.
     *
     * The title is centered, padded, and wrapped in a bordered panel.
     *
     * @param container parent container (uses BorderLayout)
     */
    public static void addTitle(JPanel container)
    {
        JPanel titleWrapper = new JPanel();
        JLabel title = new JLabel("Kishal's Typing Race Simulator!");

        centerText(title);
        addLabelPadding(title, 5);

        titleWrapper.add(title);
        titleWrapper.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        container.add(titleWrapper, BorderLayout.NORTH);
    }

    /**
     * Creates a styled error label.
     *
     * The label is centered, padded, and coloured red.
     *
     * @param errorMessage message to display
     * @return configured JLabel
     */
    public static JLabel createError(String errorMessage)
    {
        JLabel error = new JLabel(errorMessage);

        centerText(error);
        addLabelPadding(error, 5);

        error.setForeground(Color.RED); 
        
        return error;
    }

    /**
     * Refreshes a JFrame by revalidating and repainting it.
     *
     * @param window frame to refresh
     */
    public static void refresh(JFrame window)
    {
        window.revalidate();
        window.repaint();
    }

    /**
     * Refreshes a JPanel by revalidating and repainting it.
     *
     * @param panel panel to refresh
     */
    public static void refresh(JPanel panel)
    {
        panel.revalidate();
        panel.repaint();
    }

    /**
     * Centers text horizontally and vertically within a JLabel.
     *
     * @param label label to align
     */
    public static void centerText(JLabel label)
    {
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
    }

    /**
     * Adds uniform padding around a JPanel.
     *
     * @param panel panel to modify
     * @param size padding size in pixels
     */
    public static void addPanelPadding(JPanel panel, int size)
    {
        panel.setBorder(BorderFactory.createEmptyBorder(size, size, size, size));
    }

    /**
     * Adds uniform padding around a JLabel.
     *
     * @param label label to modify
     * @param size padding size in pixels
     */
    public static void addLabelPadding(JLabel label, int size)
    {
        label.setBorder(BorderFactory.createEmptyBorder(size, size, size, size));
    }

    /**
     * Adds uniform padding around a JTextArea.
     *
     * @param area text area to modify
     * @param size padding size in pixels
     */
    public static void addTextAreaPadding(JTextArea area, int size)
    {
        area.setBorder(BorderFactory.createEmptyBorder(size, size, size, size));
    }

    /**
     * Adds uniform padding around a JTextField.
     *
     * @param area text field to modify
     * @param size padding size in pixels
     */
    public static void addTextFieldPadding(JTextField area, int size)
    {
        area.setBorder(BorderFactory.createEmptyBorder(size, size, size, size));
    }

    /**
     * Adds a titled border to a panel.
     *
     * The title appears at the top-left of the border.
     *
     * @param panel panel to decorate
     * @param title title text
     */
    public static void addTitleBorder(JPanel panel, String title)
    {
        TitledBorder titleBorder =
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK), title);

        titleBorder.setTitleJustification(TitledBorder.LEFT);
        titleBorder.setTitlePosition(TitledBorder.TOP);

        panel.setBorder(titleBorder);
    }
}