/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.serfarmed.entities.util;

import java.math.BigDecimal;

/**
 *
 * @author Raul
 */
public class CarritoItem {
  
  //Datos de Producto y de ProductoVenta
  
  private Integer idProducto;
  private Integer idComision;
  private String nombreDoctor;
  private String nombreProducto;
  private BigDecimal precioProducto;
  private Integer cantidad;
  private BigDecimal importe;
  

  public Integer getIdProducto() {
    return idProducto;
  }

  public void setIdProducto(Integer idProducto) {
    this.idProducto = idProducto;
  }

  public String getNombreProducto() {
    return nombreProducto;
  }

  public Integer getIdComision() {
    return idComision;
  }

  public void setIdComision(Integer idComision) {
    this.idComision = idComision;
  }

  public void setNombreProducto(String nombreProducto) {
    this.nombreProducto = nombreProducto;
  }

  public BigDecimal getPrecioProducto() {
    return precioProducto;
  }

  public void setPrecioProducto(BigDecimal precioProducto) {
    this.precioProducto = precioProducto;
  }

  public Integer getCantidad() {
    return cantidad;
  }

  public void setCantidad(Integer cantidad) {
    this.cantidad = cantidad;
  }

  public BigDecimal getImporte() {
    return importe;
  }

  public void setImporte(BigDecimal importe) {
    this.importe = importe;
  }

  public String getNombreDoctor() {
    return nombreDoctor;
  }

  public void setNombreDoctor(String nombreDoctor) {
    this.nombreDoctor = nombreDoctor;
  }

  
  
}
