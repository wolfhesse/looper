package com.jdkanani.looper.view.template;

import java.util.Map;

/**
 * @author jdkanani
 */
@FunctionalInterface
public interface TemplateEngine {
    public Object render(String templateName, Map<String, Object> params);
}
