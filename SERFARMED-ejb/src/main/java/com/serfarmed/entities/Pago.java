/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.serfarmed.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Raul
 */
@Entity
@Table(name = "pago")
@XmlRootElement
@Cacheable(false)
@NamedQueries({
  @NamedQuery(name = "Pago.findAll", query = "SELECT p FROM Pago p"),
  @NamedQuery(name = "Pago.findByIdPago", query = "SELECT p FROM Pago p WHERE p.idPago = :idPago"),
  @NamedQuery(name = "Pago.findByDescripcion", query = "SELECT p FROM Pago p WHERE p.descripcion = :descripcion"),
  @NamedQuery(name = "Pago.findByMonto", query = "SELECT p FROM Pago p WHERE p.monto = :monto"),
  @NamedQuery(name = "Pago.findByFechaHora", query = "SELECT p FROM Pago p WHERE p.fechaHora = :fechaHora"),
  @NamedQuery(name = "Pago.findByTipo", query = "SELECT p FROM Pago p WHERE p.tipo = :tipo"),
  @NamedQuery(name = "Pago.findByPersonal", query = "SELECT p FROM Pago p WHERE p.idPersonal = :idPersonal and p.fechaHora BETWEEN :startDate AND :endDate"),
  @NamedQuery(name = "Pago.findByFecha", query = "SELECT p FROM Pago p WHERE p.fechaHora BETWEEN :startDate AND :endDate")})
public class Pago implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "idPago")
  private Integer idPago;
  @Size(max = 200)
  @Column(name = "descripcion")
  private String descripcion;
  // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
  @Basic(optional = false)
  @NotNull
  @Column(name = "monto")
  private BigDecimal monto;
  @Basic(optional = false)
  @Column(name = "fechaHora")
  @Temporal(TemporalType.TIMESTAMP)
  private Date fechaHora;
  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 11)
  @Column(name = "tipo")
  private String tipo;
  @JoinColumn(name = "idUsuario", referencedColumnName = "idUsuario")
  @ManyToOne(optional = false)
  private Usuario idUsuario;
  @JoinColumn(name = "idPersonal", referencedColumnName = "idPersonal")
  @ManyToOne(optional = false)
  private Personal idPersonal;

  public Pago() {
  }

  public Pago(Integer idPago) {
    this.idPago = idPago;
  }

  public Pago(Integer idPago, BigDecimal monto, Date fechaHora, String tipo) {
    this.idPago = idPago;
    this.monto = monto;
    this.fechaHora = fechaHora;
    this.tipo = tipo;
  }

  public Integer getIdPago() {
    return idPago;
  }

  public void setIdPago(Integer idPago) {
    this.idPago = idPago;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public BigDecimal getMonto() {
    return monto;
  }

  public void setMonto(BigDecimal monto) {
    this.monto = monto;
  }

  public Date getFechaHora() {
    return fechaHora;
  }

  public void setFechaHora(Date fechaHora) {
    this.fechaHora = fechaHora;
  }

  public String getTipo() {
    return tipo;
  }

  public void setTipo(String tipo) {
    this.tipo = tipo;
  }

  public Usuario getIdUsuario() {
    return idUsuario;
  }

  public void setIdUsuario(Usuario idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Personal getIdPersonal() {
    return idPersonal;
  }

  public void setIdPersonal(Personal idPersonal) {
    this.idPersonal = idPersonal;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (idPago != null ? idPago.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Pago)) {
      return false;
    }
    Pago other = (Pago) object;
    if ((this.idPago == null && other.idPago != null) || (this.idPago != null && !this.idPago.equals(other.idPago))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return idPago + "";
  }
  
}
