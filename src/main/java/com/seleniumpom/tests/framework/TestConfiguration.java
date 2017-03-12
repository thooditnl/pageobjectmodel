package com.seleniumpom.tests.framework;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.testng.Assert.assertNotNull;

public class TestConfiguration {
  protected Logger logger = Logger.getLogger(getClass());
  private Properties properties;

  private static TestConfiguration instance;
  private TestConfiguration()
  {
    properties = System.getProperties();
    //First load developer-specific settings, if any
    merge(properties, propertiesFrom("local.properties"));
    //Then load the global settings (used by Jenkins)
    merge(properties, propertiesFrom("default.properties"));
  }

  public static TestConfiguration getInstance()
  {
    if (instance == null){
      instance = new TestConfiguration();
    }
    return instance;
  }

  private Properties propertiesFrom(String propertyFileName) {
    final Properties developerSpecific = new Properties();
    loadPropertiesFile(developerSpecific, propertyFileName);
    return developerSpecific;
  }

  private void loadPropertiesFile(Properties target, String propertyFileName) {
    final InputStream stream = getClass().getClassLoader().getResourceAsStream(propertyFileName);
    if (stream != null) {
      try {
        target.load(stream);
      } catch (IOException e) {
        logger.warn("Properties file ["+propertyFileName+"] not found - ignoring");
      }
    }
  }

  /**
   * Merge properties according to Ant's precedence rules.  That is, properties should be immutable, and once set, the values cannot be set again.
   *
   * @param original  The original (immutable) set of properties
   * @param additions Properties to be added to original.  Any properties in this set that share a key with original will be discarded.
   */
  private void merge(Properties original, Properties additions) {
    for (Object keyToAdd : additions.keySet()) {
      if (!original.containsKey(keyToAdd)) {
        original.put(keyToAdd, additions.get(keyToAdd));
      }
    }
  }

  private String get(String key) {
    String property = properties.getProperty(key);
    assertNotNull(property, "Required property [" + key + "] not found");
    return property;
  }

  // Public methods
  public BrowserUtil.BrowserType getBrowser(){
    String property = get("webdriver.browser");
    return BrowserUtil.BrowserType.valueOf(property.toUpperCase());
  }

  public BrowserUtil.DriverType getDriverType(){
    String property = get("webdriver.type");
    return BrowserUtil.DriverType.valueOf(property.toUpperCase());
  }

  public String getSiteUrl(){
    return get("site.url");
  }

  public String getSeleniumGridURL(){
    return get("webdriver.seleniumgrid.url");
  }

}
