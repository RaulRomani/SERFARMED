/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.serfarmed.facades;

import com.serfarmed.entities.Producto;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Raul
 */
@Local
public interface ProductoFacadeLocal {

  void create(Producto producto);

  void edit(Producto producto);

  void remove(Producto producto);

  Producto find(Object id);

  List<Producto> findAll();

  List<Producto> findRange(int[] range);

  int count();
  
}
