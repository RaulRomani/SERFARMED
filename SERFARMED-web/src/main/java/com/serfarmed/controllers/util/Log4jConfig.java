/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.serfarmed.controllers.util;

import java.net.URL;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.helpers.Loader;

/**
 *
 * @author Raul
 */
public class Log4jConfig {
  
  private static Logger logger ;
  private Log4jConfig() {
    
  }

  public static Logger getLogger(String clase) {
    logger = Logger.getLogger(clase);
    //String path = System.getProperty("java.class.path");
    //File log4jfile = new File( path + "/log4j.properties");
    //PropertyConfigurator.configure(log4jfile.getAbsolutePath());
    URL url = Loader.getResource("log4j.properties");  // Esto carga el fichero como recurso si está en el CLASSPATH
    PropertyConfigurator.configure(url);
    
    return logger;
  }
  
  
  
  
}
