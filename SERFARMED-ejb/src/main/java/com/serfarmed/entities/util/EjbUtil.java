/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.serfarmed.entities.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Raul
 */
public class EjbUtil {
  
  public static Map<String,String> getStartEndMonth(Date fecha){
    
    Map<String, String> parametro = new HashMap<>();
    
    SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy"); 
    SimpleDateFormat MonthFormat = new SimpleDateFormat("MM"); 
    
    String startDate = yearFormat.format(fecha) + "-" + MonthFormat.format(fecha) +  "-01 00:00:01";
    String endDate;
    int month = fecha.getMonth();  // return 0 if is january
    month = month + 2;
    if (month != 13){
      String endMes = String.format("%02d", month );
      endDate = yearFormat.format(fecha) + "-" + endMes + "-01 00:00:01";
    
    } else {
      endDate = yearFormat.format(fecha) + "-12-31 23:59:59";
    }
    
    parametro.put("startDate", startDate);
    parametro.put("endDate", endDate);
    
    return parametro;
  }
  
}
