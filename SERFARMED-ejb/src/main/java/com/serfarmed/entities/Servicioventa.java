/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.serfarmed.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Raul
 */
@Entity
@Table(name = "servicioventa")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Servicioventa.findAll", query = "SELECT s FROM Servicioventa s"),
  @NamedQuery(name = "Servicioventa.findByIdServicioVenta", query = "SELECT s FROM Servicioventa s WHERE s.idServicioVenta = :idServicioVenta"),
  @NamedQuery(name = "Servicioventa.findByCantidad", query = "SELECT s FROM Servicioventa s WHERE s.cantidad = :cantidad"),
  @NamedQuery(name = "Servicioventa.findByImporte", query = "SELECT s FROM Servicioventa s WHERE s.importe = :importe")})
public class Servicioventa implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "idServicioVenta")
  private Integer idServicioVenta;
  @Basic(optional = false)
  @NotNull
  @Column(name = "cantidad")
  private int cantidad;
  // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
  @Basic(optional = false)
  @NotNull
  @Column(name = "importe")
  private BigDecimal importe;
  @JoinColumn(name = "idServicio", referencedColumnName = "idServicio")
  @ManyToOne(optional = false)
  private Servicio idServicio;
  @JoinColumn(name = "idComision", referencedColumnName = "idComision")
  @ManyToOne(optional = false)
  private Comision idComision;
  @JoinColumn(name = "idVenta", referencedColumnName = "idVenta")
  @ManyToOne(optional = false)
  private Venta idVenta;
  
  @Transient
  private BigDecimal comisionClinica ;
  @Transient
  private BigDecimal comisionMedico ;

  public Servicioventa() {
  }

  public Servicioventa(Integer idServicioVenta) {
    this.idServicioVenta = idServicioVenta;
  }

  public Servicioventa(Integer idServicioVenta, int cantidad, BigDecimal importe) {
    this.idServicioVenta = idServicioVenta;
    this.cantidad = cantidad;
    this.importe = importe;
  }

  public Integer getIdServicioVenta() {
    return idServicioVenta;
  }

  public void setIdServicioVenta(Integer idServicioVenta) {
    this.idServicioVenta = idServicioVenta;
  }

  public int getCantidad() {
    return cantidad;
  }

  public void setCantidad(int cantidad) {
    this.cantidad = cantidad;
  }

  public BigDecimal getImporte() {
    return importe;
  }

  public void setImporte(BigDecimal importe) {
    this.importe = importe;
  }

  public BigDecimal getComisionClinica() {
    
    if( idServicioVenta != null && idComision != null){
      
      String tipoComision = idComision.getTipo();
      BigDecimal comision = idComision.getComision();
      
      if (tipoComision.equals("PORCENTAJE")){
        comisionClinica = importe.multiply( new BigDecimal(100).subtract(comision).divide(new BigDecimal(100)) );
      } else if (tipoComision.equals("MONTO")){
        comisionClinica = importe.subtract(comision);
      } else if (tipoComision.equals("N/A")){
        comisionClinica = importe;
      }
    }
    return comisionClinica;
  }

  public void setComisionClinica(BigDecimal comisionClinica) {
    this.comisionClinica = comisionClinica;
  }

  public BigDecimal getComisionMedico() {
    
    if( idServicioVenta != null && idComision != null){
      
      String tipoComision = idComision.getTipo();
      BigDecimal comision = idComision.getComision();
      
      if (tipoComision.equals("PORCENTAJE")){
        comisionMedico = importe.multiply( comision).divide(new BigDecimal(100)) ;
      } else if (tipoComision.equals("MONTO")){
        comisionMedico = comision;
      } else if (tipoComision.equals("N/A")){
        comisionMedico = new BigDecimal(BigInteger.ZERO);
      }
    }
    return comisionMedico;
  }

  public void setComisionMedico(BigDecimal comisionMedico) {
    this.comisionMedico = comisionMedico;
  }
  
  public Servicio getIdServicio() {
    return idServicio;
  }

  public void setIdServicio(Servicio idServicio) {
    this.idServicio = idServicio;
  }

  public Comision getIdComision() {
    return idComision;
  }

  public void setIdComision(Comision idComision) {
    this.idComision = idComision;
  }
  
  

  public Venta getIdVenta() {
    return idVenta;
  }

  public void setIdVenta(Venta idVenta) {
    this.idVenta = idVenta;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (idServicioVenta != null ? idServicioVenta.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Servicioventa)) {
      return false;
    }
    Servicioventa other = (Servicioventa) object;
    if ((this.idServicioVenta == null && other.idServicioVenta != null) || (this.idServicioVenta != null && !this.idServicioVenta.equals(other.idServicioVenta))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return idServicioVenta + "";
  }
  
}
