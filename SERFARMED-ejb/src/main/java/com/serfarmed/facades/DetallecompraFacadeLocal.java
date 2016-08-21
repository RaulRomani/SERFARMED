/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.serfarmed.facades;

import com.serfarmed.entities.Detallecompra;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Raul
 */
@Local
public interface DetallecompraFacadeLocal {

  void create(Detallecompra productocompra);

  void edit(Detallecompra productocompra);

  void remove(Detallecompra productocompra);

  Detallecompra find(Object id);

  List<Detallecompra> findAll();

  List<Detallecompra> findRange(int[] range);

  int count();
  
}
