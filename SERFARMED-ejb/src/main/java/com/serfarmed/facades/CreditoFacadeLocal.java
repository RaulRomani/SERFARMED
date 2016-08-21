/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.serfarmed.facades;

import com.serfarmed.entities.Credito;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Raul
 */
@Local
public interface CreditoFacadeLocal {

  void create(Credito credito);

  void edit(Credito credito);

  void remove(Credito credito);

  Credito find(Object id);

  List<Credito> findAll();

  List<Credito> findRange(int[] range);

  int count();
  
}
