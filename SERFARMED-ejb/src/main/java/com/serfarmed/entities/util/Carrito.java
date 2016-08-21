/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.serfarmed.entities.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Raul
 */
public class Carrito {

  private BigDecimal total;
  private Date fecha;
  private String formaPago;
  private String comprobante;
  private List<CarritoItem> items;
  

//  final static Logger logger = Log4jConfig.getLogger(Carrito.class.getName());

  public Carrito() {
    items = new ArrayList<>();
    
  }

  public void add(CarritoItem item) {
    if (item == null) {
      return;
    }
    boolean encontro = false;
    for (CarritoItem i : items) {
      if (i.getIdProducto()== item.getIdProducto()) {
        i.setCantidad(i.getCantidad() + item.getCantidad());
        i.setImporte(i.getImporte().add(item.getImporte()));
        encontro = true;
//        logger.info("carrito no agregado ");
      }
    }
    if (!encontro) {
      items.add(item);
//      logger.info("carrito agregado : " + item.getNombreServicio());
    }
    int k = 0;
    //Eliminar si la cantidad es < 0   CHEKAR : 
//    while (k < items.size()) {
//      if (items.get(k).getCantidad()<= 0) {
//        items.remove(k);
//      } else {
//        k++;
//      }
//    }
  }

  public BigDecimal getTotal() {
    total= new BigDecimal(BigInteger.ZERO);
    for (CarritoItem i : items) {
      total= total.add(i.getImporte());
      System.out.println("Importe: "+i.getImporte() + " total :" + total);
//      logger.info("Importe: "+i.getImporte() + " total :" + total);
    }
    return total;
  }

  public void setTotal(BigDecimal total) {
    this.total = total;
  }

  public Date getFecha() {
    return fecha;
  }

  public void setFecha(Date fecha) {
    this.fecha = fecha;
  }

  public String getFormaPago() {
    return formaPago;
  }

  public void setFormaPago(String formaPago) {
    this.formaPago = formaPago;
  }

  public String getComprobante() {
    return comprobante;
  }

  public void setComprobante(String comprobante) {
    this.comprobante = comprobante;
  }

  public List<CarritoItem> getItems() {
    return items;
  }

  public void setItems(List<CarritoItem> items) {
    this.items = items;
  }

}
