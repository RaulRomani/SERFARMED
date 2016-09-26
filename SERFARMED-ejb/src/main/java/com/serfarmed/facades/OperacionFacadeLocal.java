/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.serfarmed.facades;

import com.serfarmed.entities.Operacion;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Raul
 */
@Local
public interface OperacionFacadeLocal {

  void create(Operacion operacion);

  void edit(Operacion operacion);

  void remove(Operacion operacion);

  Operacion find(Object id);

  List<Operacion> findAll();

  List<Operacion> findRange(int[] range);

  int count();
  
  public List<Operacion> findEgresoByFecha(Date fecha);
  
  public List<Operacion> findIngresoByFecha(Date fecha);
  
  public List<Operacion> findIngresosMes(Date fecha);
  
  public List<Operacion> findEgresosMes(Date fecha);
  
}
