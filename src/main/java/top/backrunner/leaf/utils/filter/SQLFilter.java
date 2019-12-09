package top.backrunner.leaf.utils.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class SQLFilter {

    private static final String[] sqlRegEx = {
            "[:/*+'<>=%,()-]+",
            "(?i)(\\b)+SELECT(\\s+)",
            "(?i)(\\b)+UPDATE(\\s+)",
            "(?i)(\\b)+INSERT(\\s+)",
            "(?i)(\\b)+FROM(\\s+)",
            "(?i)(\\b)+TRUNCATE(\\s+)",
            "(?i)(\\b)+DELETE(\\s+)",
            "(?i)(\\b)+DROP(\\s+)",
            "(?i)(\\b)+KILL(\\s+)",
            "(?i)(\\b)+CREATE(\\s+)",
            "(?i)(\\b)+ALTER(\\s+)",
            "(.*)(-){2,}(.*)",
    };
    private static List<Pattern> patterns = getPatterns();

    private static List<Pattern> getPatterns() {
        List<Pattern> patterns = new ArrayList<Pattern>();
        for (String s : sqlRegEx){
            patterns.add(getPattern(s));
        }
        return patterns;
    }
    private static Pattern getPattern(String reg){
        return Pattern.compile(reg, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
    }

    public static String filter(String code){
        if (code == null || code.length() < 0){
            return code;
        }
        for (Pattern p : patterns){
            code = p.matcher(code).replaceAll("");
        }
        return code;
    }
}
