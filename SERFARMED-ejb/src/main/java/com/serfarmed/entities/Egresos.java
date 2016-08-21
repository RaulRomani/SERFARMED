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
@Table(name = "egresos")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Egresos.findAll", query = "SELECT e FROM Egresos e"),
  @NamedQuery(name = "Egresos.findByIdEgresos", query = "SELECT e FROM Egresos e WHERE e.idEgresos = :idEgresos"),
  @NamedQuery(name = "Egresos.findByFechaHora", query = "SELECT e FROM Egresos e WHERE e.fechaHora = :fechaHora"),
  @NamedQuery(name = "Egresos.findByComprobante", query = "SELECT e FROM Egresos e WHERE e.comprobante = :comprobante"),
  @NamedQuery(name = "Egresos.findBySerie", query = "SELECT e FROM Egresos e WHERE e.serie = :serie"),
  @NamedQuery(name = "Egresos.findByNroComprobante", query = "SELECT e FROM Egresos e WHERE e.nroComprobante = :nroComprobante"),
  @NamedQuery(name = "Egresos.findByDescripcion", query = "SELECT e FROM Egresos e WHERE e.descripcion = :descripcion"),
  @NamedQuery(name = "Egresos.findByMonto", query = "SELECT e FROM Egresos e WHERE e.monto = :monto"),
  @NamedQuery(name = "Egresos.findByFecha", query = "SELECT e FROM Egresos e WHERE e.fechaHora BETWEEN :startDate AND :endDate")})
public class Egresos implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "idEgresos")
  private Integer idEgresos;
  @Basic(optional = false)
  @Column(name = "fechaHora")
  @Temporal(TemporalType.TIMESTAMP)
  private Date fechaHora;
  @Size(max = 20)
  @Column(name = "comprobante")
  private String comprobante;
  @Size(max = 20)
  @Column(name = "serie")
  private String serie;
  @Size(max = 20)
  @Column(name = "nroComprobante")
  private String nroComprobante;
  @Size(max = 200)
  @Column(name = "descripcion")
  private String descripcion;
  // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
  @Basic(optional = false)
  @NotNull
  @Column(name = "monto")
  private BigDecimal monto;
  @JoinColumn(name = "idUsuario", referencedColumnName = "idUsuario")
  @ManyToOne(optional = false)
  private Usuario idUsuario;

  public Egresos() {
  }

  public Egresos(Integer idEgresos) {
    this.idEgresos = idEgresos;
  }

  public Egresos(Integer idEgresos, Date fechaHora, BigDecimal monto) {
    this.idEgresos = idEgresos;
    this.fechaHora = fechaHora;
    this.monto = monto;
  }

  public Integer getIdEgresos() {
    return idEgresos;
  }

  public void setIdEgresos(Integer idEgresos) {
    this.idEgresos = idEgresos;
  }

  public Date getFechaHora() {
    return fechaHora;
  }

  public void setFechaHora(Date fechaHora) {
    this.fechaHora = fechaHora;
  }

  public String getComprobante() {
    return comprobante;
  }

  public void setComprobante(String comprobante) {
    this.comprobante = comprobante;
  }

  public String getSerie() {
    return serie;
  }

  public void setSerie(String serie) {
    this.serie = serie;
  }

  public String getNroComprobante() {
    return nroComprobante;
  }

  public void setNroComprobante(String nroComprobante) {
    this.nroComprobante = nroComprobante;
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

  public Usuario getIdUsuario() {
    return idUsuario;
  }

  public void setIdUsuario(Usuario idUsuario) {
    this.idUsuario = idUsuario;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (idEgresos != null ? idEgresos.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Egresos)) {
      return false;
    }
    Egresos other = (Egresos) object;
    if ((this.idEgresos == null && other.idEgresos != null) || (this.idEgresos != null && !this.idEgresos.equals(other.idEgresos))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return idEgresos + "";
  }
  
}
