package org.landy.commons.web.conf;

import org.landy.commons.core.conf.BeanCopierConfiguration;
import org.landy.commons.core.conf.SettingsConfiguration;
import org.landy.commons.core.help.AbstractApplicationContextHelper;
import org.landy.commons.core.help.BeanInitializeCompletedListener;
import org.landy.commons.datacache.DataCacheFacade;
import org.landy.commons.datacache.conf.DataCacheConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.List;

/**
 * Application Context上下文基本配置
 */
@Configuration
@ComponentScan("org.landy.commons")
@Import({SettingsConfiguration.class,  // common settings configuration
        DataCacheConfiguration.class,  // common data cache component configuration
        BeanCopierConfiguration.class // Bean copier component configuration
        })
public class ApplicationContextConfiguration {

}