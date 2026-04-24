public class HelperUtilities
{
    public static boolean isNumber(String text)
    {
        try
        {
            int intResponse = Integer.parseInt(text);
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
            double doubleResponse = Double.parseDouble(text);
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
}