package com.jdkanani.looper.view;

import org.eclipse.jetty.util.resource.Resource;
import org.rythmengine.Rythm;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author jdkanani
 */
public class RythmEngine implements TemplateEngine {

    private File templateRoot;
    private org.rythmengine.RythmEngine rythmEngine;

    public RythmEngine(File templateRootPath) {
        templateRoot = templateRootPath;
        if (templateRoot != null) {
            Map<String, Object> conf = new HashMap<String, Object>();
            conf.put("home.template", templateRoot);
            rythmEngine = new org.rythmengine.RythmEngine(conf);
        }
    }

    @Override
    public Object render(String templateName, Map<String, Object> params) {
        return rythmEngine.render(templateName);
    }
}
