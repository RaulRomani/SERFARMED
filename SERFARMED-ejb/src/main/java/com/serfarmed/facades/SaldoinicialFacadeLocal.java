/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.serfarmed.facades;

import com.serfarmed.entities.Saldoinicial;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Raul
 */
@Local
public interface SaldoinicialFacadeLocal {

  void create(Saldoinicial saldoinicial);

  void edit(Saldoinicial saldoinicial);

  void remove(Saldoinicial saldoinicial);

  Saldoinicial find(Object id);

  List<Saldoinicial> findAll();

  List<Saldoinicial> findRange(int[] range);

  int count();
  
  public Saldoinicial findByFechaHoy();
  
}
