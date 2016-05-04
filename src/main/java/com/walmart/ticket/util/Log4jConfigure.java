package com.walmart.ticket.util;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.xml.DOMConfigurator;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
public class Log4jConfigure implements ResourceLoaderAware, InitializingBean {
    private static final Log LOG = LogFactory.getLog(Log4jConfigure.class);

    private ResourceLoader resourceLoader;

    private String file;

    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public String getFile() {
        return file;
    }

    @Required
    public void setFile(String file) {
        this.file = file;
    }

    public void afterPropertiesSet() throws Exception {
        DOMConfigurator.configure(this.resourceLoader.getResource(getFile()).getURL());
    }

}