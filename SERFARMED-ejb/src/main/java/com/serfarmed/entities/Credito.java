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
@Table(name = "credito")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Credito.findAll", query = "SELECT c FROM Credito c"),
  @NamedQuery(name = "Credito.findByIdCuota", query = "SELECT c FROM Credito c WHERE c.idCuota = :idCuota"),
  @NamedQuery(name = "Credito.findByTotalcuotas", query = "SELECT c FROM Credito c WHERE c.totalcuotas = :totalcuotas"),
  @NamedQuery(name = "Credito.findByCuotaspagado", query = "SELECT c FROM Credito c WHERE c.cuotaspagado = :cuotaspagado"),
  @NamedQuery(name = "Credito.findByFechaHora", query = "SELECT c FROM Credito c WHERE c.fechaHora = :fechaHora"),
  @NamedQuery(name = "Credito.findByPlazo", query = "SELECT c FROM Credito c WHERE c.plazo = :plazo"),
  @NamedQuery(name = "Credito.findByInicial", query = "SELECT c FROM Credito c WHERE c.inicial = :inicial"),
  @NamedQuery(name = "Credito.findByImporte", query = "SELECT c FROM Credito c WHERE c.importe = :importe")})
public class Credito implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "idCuota")
  private Integer idCuota;
  @Basic(optional = false)
  @NotNull
  @Column(name = "totalcuotas")
  private int totalcuotas;
  @Basic(optional = false)
  @NotNull
  @Column(name = "cuotaspagado")
  private int cuotaspagado;
  @Basic(optional = false)
  @Column(name = "fechaHora")
  @Temporal(TemporalType.TIMESTAMP)
  private Date fechaHora;
  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 100)
  @Column(name = "plazo")
  private String plazo;
  // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
  @Basic(optional = false)
  @NotNull
  @Column(name = "inicial")
  private BigDecimal inicial;
  @Basic(optional = false)
  @NotNull
  @Column(name = "importe")
  private BigDecimal importe;
  @JoinColumn(name = "IdVenta", referencedColumnName = "idVenta")
  @ManyToOne(optional = false)
  private Venta idVenta;

  public Credito() {
  }

  public Credito(Integer idCuota) {
    this.idCuota = idCuota;
  }

  public Credito(Integer idCuota, int totalcuotas, int cuotaspagado, Date fechaHora, String plazo, BigDecimal inicial, BigDecimal importe) {
    this.idCuota = idCuota;
    this.totalcuotas = totalcuotas;
    this.cuotaspagado = cuotaspagado;
    this.fechaHora = fechaHora;
    this.plazo = plazo;
    this.inicial = inicial;
    this.importe = importe;
  }

  public Integer getIdCuota() {
    return idCuota;
  }

  public void setIdCuota(Integer idCuota) {
    this.idCuota = idCuota;
  }

  public int getTotalcuotas() {
    return totalcuotas;
  }

  public void setTotalcuotas(int totalcuotas) {
    this.totalcuotas = totalcuotas;
  }

  public int getCuotaspagado() {
    return cuotaspagado;
  }

  public void setCuotaspagado(int cuotaspagado) {
    this.cuotaspagado = cuotaspagado;
  }

  public Date getFechaHora() {
    return fechaHora;
  }

  public void setFechaHora(Date fechaHora) {
    this.fechaHora = fechaHora;
  }

  public String getPlazo() {
    return plazo;
  }

  public void setPlazo(String plazo) {
    this.plazo = plazo;
  }

  public BigDecimal getInicial() {
    return inicial;
  }

  public void setInicial(BigDecimal inicial) {
    this.inicial = inicial;
  }

  public BigDecimal getImporte() {
    return importe;
  }

  public void setImporte(BigDecimal importe) {
    this.importe = importe;
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
    hash += (idCuota != null ? idCuota.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Credito)) {
      return false;
    }
    Credito other = (Credito) object;
    if ((this.idCuota == null && other.idCuota != null) || (this.idCuota != null && !this.idCuota.equals(other.idCuota))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return idCuota + "";
  }
  
}
