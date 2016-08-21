/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.serfarmed.facades;

import com.serfarmed.entities.Compra;
import com.serfarmed.entities.Proveedor;
import com.serfarmed.entities.Usuario;
import com.serfarmed.entities.util.Carrito;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Raul
 */
@Local
public interface CompraFacadeLocal {

  void create(Compra compra);

  void edit(Compra compra);

  void remove(Compra compra);

  Compra find(Object id);

  List<Compra> findAll();

  List<Compra> findRange(int[] range);

  int count();
  
  public Integer grabarPedidoCompra(Carrito carrito, Proveedor proveedor, Usuario usuario);
  public List<Compra> findByEstado(String estado);
  public void grabarCompra(Compra compra);
  
}
