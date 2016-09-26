/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.serfarmed.entities.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

/**
 *
 * @author Raul
 */
public class CarritoItem {
  
  //Datos de Producto y de ProductoVenta
  
  private Integer idProducto;
  private Integer idPersonal;
  private String nombreDoctor;
  private String nombreProducto;
  private BigDecimal precioProducto;
  private Integer cantidad;
  private BigDecimal importe;
  private String tipoComision;
  private BigDecimal comision;
  private BigDecimal comisionClinica;
  private BigDecimal comisionMedico;
  private String sePago;
  

  public Integer getIdProducto() {
    return idProducto;
  }

  public void setIdProducto(Integer idProducto) {
    this.idProducto = idProducto;
  }

  public String getNombreProducto() {
    return nombreProducto;
  }

  public Integer getIdPersonal() {
    return idPersonal;
  }

  public void setIdPersonal(Integer idPersonal) {
    this.idPersonal = idPersonal;
  }

  public String getSePago() {
    return sePago;
  }

  public void setSePago(String sePago) {
    this.sePago = sePago;
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

  public String getTipoComision() {
    return tipoComision;
  }

  public void setTipoComision(String tipoComision) {
    this.tipoComision = tipoComision;
  }

  public BigDecimal getComision() {
    if (tipoComision.equals("N/A"))
      comision = new BigDecimal(BigInteger.ZERO);
    return comision;
  }

  public void setComision(BigDecimal comision) {
    this.comision = comision;
  }

  public BigDecimal getComisionClinica() {
    
    if( comision != null){
      
      if (tipoComision.equals("PORCENTAJE")){
        comisionClinica = importe.multiply( new BigDecimal(100).subtract(comision).divide(new BigDecimal(100)) );
      } else if (tipoComision.equals("MONTO")){
        comisionClinica = importe.subtract(comision);
      } else if (tipoComision.equals("N/A")){
        comisionClinica = importe;
      } else {
        comisionClinica = new BigDecimal(1000000);
      }
    }
    return comisionClinica.setScale(2, RoundingMode.CEILING);
  }

  public void setComisionClinica(BigDecimal comisionClinica) {
    this.comisionClinica = comisionClinica;
  }

  public BigDecimal getComisionMedico() {
    
    if( comision != null){
      
      if (tipoComision.equals("PORCENTAJE")){
        comisionMedico = importe.multiply( comision).divide(new BigDecimal(100)) ;
      } else if (tipoComision.equals("MONTO")){
        comisionMedico = comision;
      } else if (tipoComision.equals("N/A")){
        comisionMedico = new BigDecimal(BigInteger.ZERO);
      }
    }
    return comisionMedico.setScale(2, RoundingMode.CEILING);
  }
  
  

  
  
  
}
