package model.asset;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

public class CheckInput {

    public static boolean isDouble( String input )
    {
        try
        {
            Double.parseDouble( input );
            return true;
        }
        catch( Exception e)
        {
            return false;
        }
    }


    public static boolean isInt( String input )
    {
        try
        {
            Integer.parseInt( input );
            return true;
        }
        catch( Exception e)
        {
            return false;
        }
    }

    // Check date value
    public static boolean isDate(String input)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(input.trim());
        } catch (ParseException pe) {
            return false;
        }
        return true;
    }
}
