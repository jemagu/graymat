package se.gunta.graymat;

/**
 *
 * @author Magnus Gunnarsson
 */
public class CamelToSentence {
    
    public static String doit(String camel) {
        String fixed = CamelToSentence.fixAbbr(camel);
        StringBuilder result = new StringBuilder();
        String upper = fixed.toUpperCase();
        for (int i = 0; i < fixed.length(); i++) {
            if (upper.charAt(i) == fixed.charAt(i)) {
                result.append(' ');
                result.append(fixed.charAt(i));
            } else {
                result.append(fixed.charAt(i));
            }
        }
        return result.toString().toLowerCase();
    }
    
    public static String fixAbbr(String camel) {
        StringBuilder result = new StringBuilder();
        result.append(camel.charAt(0));
        String upper = camel.toUpperCase();
        String lower = camel.toLowerCase();
        for (int i = 1; i < camel.length()-1; i++) {
            if ((upper.charAt(i) == camel.charAt(i)) && (upper.charAt(i-1) == camel.charAt(i-1)) && (upper.charAt(i+1) == camel.charAt(i+1))) {
                result.append(lower.charAt(i));
            } else {
                result.append(camel.charAt(i));
            }
        }
        // last char
        int lastChar = camel.length()-1;
        if (upper.charAt(lastChar-1) == camel.charAt(lastChar-1)) {
            result.append(lower.charAt(lower.length()-1));
        } else {
            result.append(camel.charAt(camel.length()-1));
        }
        
        return result.toString();
    }
}
