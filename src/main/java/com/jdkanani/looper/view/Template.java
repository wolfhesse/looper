package com.jdkanani.looper.view;

import org.eclipse.jetty.util.resource.Resource;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * @author jdkanani
 */
public class Template {
    private File templateRoot;

    public Template() {
        File tr = null;

        try {
            tr = Resource.newClassPathResource("templates").getFile();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (tr != null) {
            templateRoot = tr;
        }
    }

    public Template(File templateRoot) {
        this.templateRoot = templateRoot;
    }

    public File getTemplateRoot() {
        return templateRoot;
    }

    public void setTemplateRoot(File templateRoot) {
        this.templateRoot = templateRoot;
    }
}
