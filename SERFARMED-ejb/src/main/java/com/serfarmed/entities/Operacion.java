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
@Table(name = "operacion")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Operacion.findAll", query = "SELECT o FROM Operacion o"),
  @NamedQuery(name = "Operacion.findByIdOperacion", query = "SELECT o FROM Operacion o WHERE o.idOperacion = :idOperacion"),
  @NamedQuery(name = "Operacion.findByTipo", query = "SELECT o FROM Operacion o WHERE o.tipo = :tipo"),
  @NamedQuery(name = "Operacion.findByFechaHora", query = "SELECT o FROM Operacion o WHERE o.fechaHora = :fechaHora"),
  @NamedQuery(name = "Operacion.findByComprobante", query = "SELECT o FROM Operacion o WHERE o.comprobante = :comprobante"),
  @NamedQuery(name = "Operacion.findBySerie", query = "SELECT o FROM Operacion o WHERE o.serie = :serie"),
  @NamedQuery(name = "Operacion.findByNroComprobante", query = "SELECT o FROM Operacion o WHERE o.nroComprobante = :nroComprobante"),
  @NamedQuery(name = "Operacion.findByDescripcion", query = "SELECT o FROM Operacion o WHERE o.descripcion = :descripcion"),
  @NamedQuery(name = "Operacion.findByMonto", query = "SELECT o FROM Operacion o WHERE o.monto = :monto"),
  @NamedQuery(name = "Operacion.findEgresoByFecha", query = "SELECT o FROM Operacion o WHERE o.tipo != 'INGRESO' AND o.fechaHora BETWEEN :startDate AND :endDate"),
  @NamedQuery(name = "Operacion.findIngresoByFecha", query = "SELECT o FROM Operacion o WHERE o.tipo = 'INGRESO' AND o.fechaHora BETWEEN :startDate AND :endDate")})
public class Operacion implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "idOperacion")
  private Integer idOperacion;
  @Size(max = 20)
  @Column(name = "tipo")
  private String tipo;
  @Basic(optional = false)
  @NotNull
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

  public Operacion() {
  }

  public Operacion(Integer idOperacion) {
    this.idOperacion = idOperacion;
  }

  public Operacion(Integer idOperacion, Date fechaHora, BigDecimal monto) {
    this.idOperacion = idOperacion;
    this.fechaHora = fechaHora;
    this.monto = monto;
  }

  public Integer getIdOperacion() {
    return idOperacion;
  }

  public void setIdOperacion(Integer idOperacion) {
    this.idOperacion = idOperacion;
  }

  public String getTipo() {
    return tipo;
  }

  public void setTipo(String tipo) {
    this.tipo = tipo;
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
    hash += (idOperacion != null ? idOperacion.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Operacion)) {
      return false;
    }
    Operacion other = (Operacion) object;
    if ((this.idOperacion == null && other.idOperacion != null) || (this.idOperacion != null && !this.idOperacion.equals(other.idOperacion))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "com.serfarmed.entities.Operacion[ idOperacion=" + idOperacion + " ]";
  }
  
}
