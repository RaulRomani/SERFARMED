/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.serfarmed.entities;

import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Raul
 */
@Entity
@Table(name = "detallecompra")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Detallecompra.findAll", query = "SELECT d FROM Detallecompra d"),
  @NamedQuery(name = "Detallecompra.findByIdDetalleCompra", query = "SELECT d FROM Detallecompra d WHERE d.idDetalleCompra = :idDetalleCompra"),
  @NamedQuery(name = "Detallecompra.findByCantidad", query = "SELECT d FROM Detallecompra d WHERE d.cantidad = :cantidad"),
  @NamedQuery(name = "Detallecompra.findByImporte", query = "SELECT d FROM Detallecompra d WHERE d.importe = :importe"),
  @NamedQuery(name = "Detallecompra.findByRecibido", query = "SELECT d FROM Detallecompra d WHERE d.recibido = :recibido")})
public class Detallecompra implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "idDetalleCompra")
  private Integer idDetalleCompra;
  // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
  
  @Basic(optional = false)
  @NotNull
  @Column(name = "cantidad")
  private int cantidad;
  @Basic(optional = false)
  @NotNull
  @Column(name = "importe")
  private BigDecimal importe;
  @Basic(optional = false)
  @NotNull
  @Column(name = "recibido")
  private boolean recibido;
  @JoinColumn(name = "idProducto", referencedColumnName = "idProducto")
  @ManyToOne(optional = false)
  private Producto idProducto;
  @JoinColumn(name = "idCompra", referencedColumnName = "idCompra")
  @ManyToOne(optional = false)
  private Compra idCompra;

  public Detallecompra() {
  }

  public Detallecompra(Integer idDetalleCompra) {
    this.idDetalleCompra = idDetalleCompra;
  }

  public Detallecompra(Integer idDetalleCompra, int cantidad, BigDecimal importe, boolean recibido) {
    this.idDetalleCompra = idDetalleCompra;
    this.cantidad = cantidad;
    this.importe = importe;
    this.recibido = recibido;
  }

  public Integer getIdDetalleCompra() {
    return idDetalleCompra;
  }

  public void setIdDetalleCompra(Integer idDetalleCompra) {
    this.idDetalleCompra = idDetalleCompra;
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

  public boolean getRecibido() {
    return recibido;
  }

  public void setRecibido(boolean recibido) {
    this.recibido = recibido;
  }

  public Producto getIdProducto() {
    return idProducto;
  }

  public void setIdProducto(Producto idProducto) {
    this.idProducto = idProducto;
  }

  public Compra getIdCompra() {
    return idCompra;
  }

  public void setIdCompra(Compra idCompra) {
    this.idCompra = idCompra;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (idDetalleCompra != null ? idDetalleCompra.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Detallecompra)) {
      return false;
    }
    Detallecompra other = (Detallecompra) object;
    if ((this.idDetalleCompra == null && other.idDetalleCompra != null) || (this.idDetalleCompra != null && !this.idDetalleCompra.equals(other.idDetalleCompra))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return idDetalleCompra + "";
  }
  
}
