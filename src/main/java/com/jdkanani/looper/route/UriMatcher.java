package com.jdkanani.looper.route;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * It validates template URI and generates {@code regexp} pattern which will
 * eventually match incoming request' URI and will parse variables.
 *
 * @author jdkanani
 */
public class UriMatcher {
    private final static String LINE_START = "^";
    private final static String SLASH = "/";
    private final static String LINE_END = "$";
    /**
     * Template validation pattern
     */
    //private final static Pattern TEMPLATE_PATTERN = Pattern.compile("^((/(\\*/?)?)|(((/[0-9a-z +@~!;:|%_.]+)|(/<[a-z]+>))+)(/\\*)?/?)$", Pattern.CASE_INSENSITIVE);
    private final static Pattern TEMPLATE_PATTERN = Pattern.compile("^(/[^/\\*<]*(<[a-zA-Z0-9_]+>)?)+(\\*/?)?$", Pattern.CASE_INSENSITIVE);
    private final static Pattern VARIABLE_PATTERN = Pattern.compile("<([a-zA-Z0-9_]+)>");
    private final static String VARIABLE_TEMPLATE1 = "(?<$1>[^/]+)";
    private final static String VARIABLE_TEMPLATE2 = "\\\\k<$1>";
    private final static String SPILT_PATTERN = "(?=<)|(?<=>)";

    private String template;
    private Pattern pattern;
    private List<String> variables;

    /**
     * Validates template and generates regexp pattern
     *
     * @param template
     */
    public UriMatcher(String template) {
        this.template = Objects.requireNonNull(template, "URI template must not be null!");
        if (!TEMPLATE_PATTERN.matcher(this.template).matches()) {
            throw new IllegalArgumentException("URI template is not valid!");
        }
        // Mutable list (For internal use only)
        List<String> _variables = new ArrayList<String>();
        // Immutable list (For outside world)
        variables = Collections.unmodifiableList(_variables);


        // TODO write better version
        StringBuffer result = new StringBuffer(LINE_START);
        String[] tokens = this.template.split(SPILT_PATTERN);
        for (int i = 0; i < tokens.length; i++) {
            boolean isVariable = false;
            String token = tokens[i];
            Matcher matcher = VARIABLE_PATTERN.matcher(token);
            while (matcher.find()) {
                isVariable = true;
                String var = matcher.group(1);
                if (!_variables.contains(var)) {
                    _variables.add(var);
                    tokens[i] = matcher.replaceFirst(VARIABLE_TEMPLATE1);
                } else {
                    tokens[i] = matcher.replaceFirst(VARIABLE_TEMPLATE2);
                }
            }
            if (!isVariable) {
                tokens[i] = escape(tokens[i]).replaceAll("/\\*", "/.\\*");
            }
        }
        Arrays.asList(tokens).forEach(result::append);
        result.append((result.charAt(result.length() - 1) == '/') ? "?" : (SLASH + "?"));
        result.append(LINE_END);
        this.pattern = Pattern.compile(result.toString());
    }

    /**
     * Matches URI with template/pattern and parses URI for variables.
     *
     * @param uri
     * @return It returns 1) null if URI doen't match 2) empty map if URI matches
     * but no variable found 3) map of variable to values if URI
     */
    public Map<String, String> matches(String uri) {
        if (this.pattern == null) return null;

        Matcher matcher = this.pattern.matcher(uri);
        if (!matcher.matches()) return null;

        final Map<String, String> values = new HashMap<String, String>();
        variables.forEach(var -> {
            values.put(var, matcher.group(var));
        });
        return Collections.unmodifiableMap(values);
    }

    public String getTemplate() {
        return template;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public List<String> getVariables() {
        return variables;
    }

    /**
     * Escape string token
     *
     * @param token
     * @return escaped token
     */
    private String escape(String token) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < token.length(); i++) {
            char c = token.charAt(i);
            if (".+".indexOf(c) != -1) {
                sb.append("\\");
            }
            sb.append(c);
        }
        return sb.toString();
    }
}
