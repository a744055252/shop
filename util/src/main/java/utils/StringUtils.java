package utils;

/**
 * @author guanhuan_li
 */
public abstract class StringUtils extends org.apache.commons.lang3.StringUtils {
    public static String toUpperCaseFirstOne(String s){
        if(Character.isUpperCase(s.charAt(0))) {
            return s;
        } else {
            return Character.toUpperCase(s.charAt(0))+s.substring(1);
        }
    }
}
