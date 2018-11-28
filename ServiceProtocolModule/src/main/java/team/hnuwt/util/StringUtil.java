package team.hnuwt.util;

public class StringUtil {

    public static int toInt(String s)
    {
        int len = s.length();
        StringBuilder result = new StringBuilder();
        for (int i = len; i > 0; i -= 2)
        {
            result.append(s.substring(i - 2, i));
        }
        return Integer.parseInt(result.toString(), 16);
    }
}
