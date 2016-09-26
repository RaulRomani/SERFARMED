/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.serfarmed.facades;

import com.serfarmed.entities.Pago;
import com.serfarmed.entities.Personal;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Raul
 */
@Local
public interface PagoFacadeLocal {

  void create(Pago pago);

  void edit(Pago pago);

  void remove(Pago pago);

  Pago find(Object id);

  List<Pago> findAll();

  List<Pago> findRange(int[] range);

  int count();
  
  public List<Pago> findAdelantosByPersonal(Personal personal, String fechaHora);
  
  public List<Pago> findByFecha(Date fecha);
  
  public List<Pago> findAdelantosByFecha(Date fecha);
  
  public List<Pago> findPagoDoctorHoy(Date fecha);
  
  public List<Pago> findAdelantosMes(Date fecha);
  
  public List<Pago> findPagoDoctorMes(Date fecha);
  
  public List<Pago> findPagoByDoctorMes(Personal doctor, Date fecha);
  
}
