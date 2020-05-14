package model.asset;

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
}
