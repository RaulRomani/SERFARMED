/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.serfarmed.facades;

import com.serfarmed.entities.Egresos;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Raul
 */
@Local
public interface EgresosFacadeLocal {

  void create(Egresos egresos);

  void edit(Egresos egresos);

  void remove(Egresos egresos);

  Egresos find(Object id);

  List<Egresos> findAll();

  List<Egresos> findRange(int[] range);

  int count();
  
  public List<Egresos> findByFecha(Date fecha);
  
}
