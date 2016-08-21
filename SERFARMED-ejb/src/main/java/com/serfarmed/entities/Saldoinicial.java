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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Raul
 */
@Entity
@Table(name = "saldoinicial")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Saldoinicial.findAll", query = "SELECT s FROM Saldoinicial s"),
  @NamedQuery(name = "Saldoinicial.findByIdSaldoinicial", query = "SELECT s FROM Saldoinicial s WHERE s.idSaldoinicial = :idSaldoinicial"),
  @NamedQuery(name = "Saldoinicial.findByFecha", query = "SELECT s FROM Saldoinicial s WHERE s.fecha = :fecha"),
  @NamedQuery(name = "Saldoinicial.findBySaldoinicial", query = "SELECT s FROM Saldoinicial s WHERE s.saldoinicial = :saldoinicial"),
  @NamedQuery(name = "Saldoinicial.findByFechaHoy", query = "SELECT s FROM Saldoinicial s WHERE  s.fecha = :fechaHoy")})
public class Saldoinicial implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "idSaldoinicial")
  private Integer idSaldoinicial;
  @Basic(optional = false)
  @NotNull
  @Column(name = "fecha")
  @Temporal(TemporalType.DATE)
  private Date fecha;
  // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
  @Basic(optional = false)
  @NotNull
  @Column(name = "saldoinicial")
  private BigDecimal saldoinicial;

  public Saldoinicial() {
  }

  public Saldoinicial(Integer idSaldoinicial) {
    this.idSaldoinicial = idSaldoinicial;
  }

  public Saldoinicial(Integer idSaldoinicial, Date fecha, BigDecimal saldoinicial) {
    this.idSaldoinicial = idSaldoinicial;
    this.fecha = fecha;
    this.saldoinicial = saldoinicial;
  }

  public Integer getIdSaldoinicial() {
    return idSaldoinicial;
  }

  public void setIdSaldoinicial(Integer idSaldoinicial) {
    this.idSaldoinicial = idSaldoinicial;
  }

  public Date getFecha() {
    return fecha;
  }

  public void setFecha(Date fecha) {
    this.fecha = fecha;
  }

  public BigDecimal getSaldoinicial() {
    return saldoinicial;
  }

  public void setSaldoinicial(BigDecimal saldoinicial) {
    this.saldoinicial = saldoinicial;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (idSaldoinicial != null ? idSaldoinicial.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Saldoinicial)) {
      return false;
    }
    Saldoinicial other = (Saldoinicial) object;
    if ((this.idSaldoinicial == null && other.idSaldoinicial != null) || (this.idSaldoinicial != null && !this.idSaldoinicial.equals(other.idSaldoinicial))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return idSaldoinicial + "";
  }
  
}
