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
public class DoctorItem {

  private Integer idDoctor;
  private String nombre;
  private String apellido;
  private String DNI;
  private String tipoComision;
  private BigDecimal comision;
  private String nota;

  public Integer getIdDoctor() {
    return idDoctor;
  }

  public void setIdDoctor(Integer idDoctor) {
    this.idDoctor = idDoctor;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getTipoComision() {
    return tipoComision;
  }

  public void setTipoComision(String tipoComision) {
    this.tipoComision = tipoComision;
  }
  
  public String getApellido() {
    return apellido;
  }
  
  public String getNombreCompleto(){
    return nombre + " " + apellido;
  }
  

  public void setApellido(String apellido) {
    this.apellido = apellido;
  }

  public String getDNI() {
    return DNI;
  }

  public void setDNI(String DNI) {
    this.DNI = DNI;
  }

  public BigDecimal getComision() {
    return comision;
  }

  public void setComision(BigDecimal comision) {
    this.comision = comision;
  }

  public String getNota() {
    return nota;
  }

  public void setNota(String nota) {
    this.nota = nota;
  }

}
