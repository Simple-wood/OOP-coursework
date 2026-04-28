import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.regex.*;

public class HelperUtilities
{
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

    public static boolean isChar(String text)
    {
        String checkText = text.trim();

        if(checkText.length() == 1)
        {
            return true;
        }

        return false;
    }

    public static int countWords(String passage)
    {
        int count = 0;
        Pattern pattern = Pattern.compile("[a-zA-Z0-9]+");
        Matcher matcher = pattern.matcher(passage);

        while(matcher.find())
        {
            count ++;
        }

        return count;
    }

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

    public static JLabel createError(String errorMessage)
    {
        JLabel error = new JLabel(errorMessage);
        centerText(error);
        addLabelPadding(error, 5);
        error.setForeground(Color.RED); 
        
        return error;
    }

    public static void refresh(JFrame window)
    {
        window.revalidate();
        window.repaint();
    }

    public static void refresh(JPanel panel)
    {
        panel.revalidate();
        panel.repaint();
    }

    public static void centerText(JLabel label)
    {
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
    }

    public static void addPanelPadding(JPanel panel, int size)
    {
        panel.setBorder(BorderFactory.createEmptyBorder(size, size, size, size));
    }

    public static void addLabelPadding(JLabel label, int size)
    {
        label.setBorder(BorderFactory.createEmptyBorder(size, size, size, size));
    }

    public static void addTextAreaPadding(JTextArea area, int size)
    {
        area.setBorder(BorderFactory.createEmptyBorder(size, size, size, size));
    }

    public static void addTextFieldPadding(JTextField area, int size)
    {
        area.setBorder(BorderFactory.createEmptyBorder(size, size, size, size));
    }

    public static void addTitleBorder(JPanel panel, String title)
    {
        TitledBorder titleBorder = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), title);
        titleBorder.setTitleJustification(TitledBorder.LEFT);
        titleBorder.setTitlePosition(TitledBorder.TOP);

        panel.setBorder(titleBorder);
    }
}