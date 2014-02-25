package com.jdkanani.looper.route;

/**
 * Represents each filter entry
 *
 * @author jdkanani
 */
public class FilterEntry extends BaseRoute {
    private Filter filter;

    public FilterEntry(HttpMethod method, String path, Filter filter) {
        this.method = method;
        this.path = path;
        this.filter = filter;

        this.pathMatcher = new UriMatcher(path);
    }

    public Filter getFilter() {
        return filter;
    }

}
