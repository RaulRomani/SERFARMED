/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.serfarmed.controllers.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 *
 * @author Raul
 */
public class CalcularEdad {

  public static String Calcular(Date fechaNacimiento){
    
    SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat añoFormat = new SimpleDateFormat("yyyy");
    SimpleDateFormat mesFormat = new SimpleDateFormat("MM");
    SimpleDateFormat diaFormat = new SimpleDateFormat("dd");

//    Date fechaNacimiento = new Date();
//    try {
//      fechaNacimiento = dtFormat.parse("1991-03-05");
//    } catch (ParseException ex) {
//    }
    Date fechaActual = new Date();

    Integer diaA = fechaNacimiento.getDay();
    Integer mesA = fechaNacimiento.getMonth();
    Integer añoA = fechaNacimiento.getYear();

    Integer diaB = fechaActual.getDay();
    Integer mesB = fechaActual.getMonth();
    Integer añoB = fechaActual.getYear();
    String edadActual = cDias(diaA,mesA,añoA,diaB,mesB,añoB)/365  + "";
    
    
    return edadActual;
  
  }

  public static long cDias(int diainicial, int mesinicial, int añoinicial, int diafinal,
          int mesfinal, int añofinal) {

    final long msDia = 24 * 60 * 60 * 1000;

    Calendar calendarini = new GregorianCalendar(añoinicial, mesinicial - 1, diainicial);
    Calendar calendarfin = new GregorianCalendar(añofinal, mesfinal - 1, diafinal);
    java.sql.Date fechaini = new java.sql.Date(calendarini.getTimeInMillis());
    java.sql.Date fechafin = new java.sql.Date(calendarfin.getTimeInMillis());
    long dias = (fechafin.getTime() - fechaini.getTime()) / msDia;

    return dias;
  }
}
