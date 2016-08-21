/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.serfarmed.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Raul
 */
@Entity
@Table(name = "compra")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Compra.findAll", query = "SELECT c FROM Compra c"),
  @NamedQuery(name = "Compra.findByIdCompra", query = "SELECT c FROM Compra c WHERE c.idCompra = :idCompra"),
  @NamedQuery(name = "Compra.findByTotal", query = "SELECT c FROM Compra c WHERE c.total = :total"),
  @NamedQuery(name = "Compra.findByFechaHora", query = "SELECT c FROM Compra c WHERE c.fechaHora = :fechaHora"),
  @NamedQuery(name = "Compra.findByComprobante", query = "SELECT c FROM Compra c WHERE c.comprobante = :comprobante"),
  @NamedQuery(name = "Compra.findBySerie", query = "SELECT c FROM Compra c WHERE c.serie = :serie"),
  @NamedQuery(name = "Compra.findByNroComprobante", query = "SELECT c FROM Compra c WHERE c.nroComprobante = :nroComprobante"),
  @NamedQuery(name = "Compra.findByEstado", query = "SELECT c FROM Compra c WHERE c.estado = :estado")})
public class Compra implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "idCompra")
  private Integer idCompra;
  // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
  @Basic(optional = false)
  @NotNull
  @Column(name = "total")
  private BigDecimal total;
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
  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 20)
  @Column(name = "estado")
  private String estado;
  @JoinColumn(name = "idProveedor", referencedColumnName = "idProveedor")
  @ManyToOne(optional = false)
  private Proveedor idProveedor;
  @JoinColumn(name = "idUsuario", referencedColumnName = "idUsuario")
  @ManyToOne(optional = false)
  private Usuario idUsuario;
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "idCompra")
  private List<Detallecompra> detallecompraList;

  public Compra() {
  }

  public Compra(Integer idCompra) {
    this.idCompra = idCompra;
  }

  public Compra(Integer idCompra, BigDecimal total, Date fechaHora, String estado) {
    this.idCompra = idCompra;
    this.total = total;
    this.fechaHora = fechaHora;
    this.estado = estado;
  }

  public Integer getIdCompra() {
    return idCompra;
  }

  public void setIdCompra(Integer idCompra) {
    this.idCompra = idCompra;
  }

  public BigDecimal getTotal() {
    return total;
  }

  public void setTotal(BigDecimal total) {
    this.total = total;
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

  public String getEstado() {
    return estado;
  }

  public void setEstado(String estado) {
    this.estado = estado;
  }

  public Proveedor getIdProveedor() {
    return idProveedor;
  }

  public void setIdProveedor(Proveedor idProveedor) {
    this.idProveedor = idProveedor;
  }

  public Usuario getIdUsuario() {
    return idUsuario;
  }

  public void setIdUsuario(Usuario idUsuario) {
    this.idUsuario = idUsuario;
  }

  @XmlTransient
  public List<Detallecompra> getDetallecompraList() {
    return detallecompraList;
  }

  public void setDetallecompraList(List<Detallecompra> detallecompraList) {
    this.detallecompraList = detallecompraList;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (idCompra != null ? idCompra.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Compra)) {
      return false;
    }
    Compra other = (Compra) object;
    if ((this.idCompra == null && other.idCompra != null) || (this.idCompra != null && !this.idCompra.equals(other.idCompra))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return idCompra + "";
  }
  
}
