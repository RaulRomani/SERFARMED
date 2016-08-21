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
import javax.persistence.Cacheable;
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
import com.serfarmed.entities.Servicio;
import com.serfarmed.entities.Servicioventa;
import java.math.BigInteger;
import javax.persistence.Transient;

/**
 *
 * @author Raul
 */
@Entity
@Table(name = "venta")
@XmlRootElement
@Cacheable(false)
@NamedQueries({
  @NamedQuery(name = "Venta.findAll", query = "SELECT v FROM Venta v"),
  @NamedQuery(name = "Venta.findByIdVenta", query = "SELECT v FROM Venta v WHERE v.idVenta = :idVenta"),
  @NamedQuery(name = "Venta.findBySubtotal", query = "SELECT v FROM Venta v WHERE v.subtotal = :subtotal"),
  @NamedQuery(name = "Venta.findByDescuento", query = "SELECT v FROM Venta v WHERE v.descuento = :descuento"),
  @NamedQuery(name = "Venta.findByTotal", query = "SELECT v FROM Venta v WHERE v.total = :total"),
  @NamedQuery(name = "Venta.findByFechaHora", query = "SELECT v FROM Venta v WHERE v.fechaHora = :fechaHora"),
  @NamedQuery(name = "Venta.findByFormapago", query = "SELECT v FROM Venta v WHERE v.formapago = :formapago"),
  @NamedQuery(name = "Venta.findByComprobante", query = "SELECT v FROM Venta v WHERE v.comprobante = :comprobante"),
  @NamedQuery(name = "Venta.findByEstado", query = "SELECT v FROM Venta v WHERE v.estado = :estado"),
  @NamedQuery(name = "Venta.findByformaPagoCliente", query = "SELECT v FROM Venta v WHERE v.formapago = :formapago AND v.idCliente = :idCliente"),
  @NamedQuery(name = "Venta.findByFecha", query = "SELECT v FROM Venta v WHERE v.fechaHora BETWEEN :startDate AND :endDate"),
  @NamedQuery(name = "Venta.findVentasMensualesByServicio", query = "SELECT s.nombre, SUM(sv.importe*sv.cantidad) FROM Venta v JOIN v.productoventaList sv JOIN sv.idServicio s WHERE v.estado = :estado AND v.fechaHora BETWEEN :startDate AND :endDate GROUP BY s.nombre"),
  @NamedQuery(name = "Venta.findVentasMensualesByServicioDoctor", query = "SELECT sv.idComision, SUM(sv.importe*sv.cantidad) FROM Venta v JOIN v.productoventaList sv JOIN sv.idServicio s WHERE v.estado = :estado AND s.nombre = :servicio AND v.fechaHora BETWEEN :startDate AND :endDate GROUP BY sv.idComision")})
public class Venta implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "idVenta")
  private Integer idVenta;
  // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
  @Basic(optional = false)
  @NotNull
  @Column(name = "subtotal")
  private BigDecimal subtotal;
  @Basic(optional = false)
  @NotNull
  @Column(name = "descuento")
  private BigDecimal descuento;
  @Basic(optional = false)
  @NotNull
  @Column(name = "total")
  private BigDecimal total;
  @Basic(optional = false)
  @Column(name = "fechaHora")
  @Temporal(TemporalType.TIMESTAMP)
  private Date fechaHora;
  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 20)
  @Column(name = "formapago")
  private String formapago;
  @Basic(optional = false)
  @NotNull
  @Size(min = 0, max = 20)
  @Column(name = "comprobante")
  private String comprobante;
  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 20)
  @Column(name = "estado")
  private String estado;
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "idVenta")
  private List<Servicioventa> productoventaList;
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "idVenta")
  private List<Credito> creditoList;
  @JoinColumn(name = "idCliente", referencedColumnName = "idCliente")
  @ManyToOne(optional = false)
  private Cliente idCliente;
  @JoinColumn(name = "idUsuario", referencedColumnName = "idUsuario")
  @ManyToOne(optional = false)
  private Usuario idUsuario;
  
  @Transient
  private BigDecimal comisionClinica;
  
  @Transient
  private BigDecimal comisionMedico;

  public Venta() {
  }

  public Venta(Integer idVenta) {
    this.idVenta = idVenta;
  }

  public Venta(Integer idVenta, BigDecimal subtotal, BigDecimal descuento, BigDecimal total, Date fechaHora, String formapago, String comprobante, String estado) {
    this.idVenta = idVenta;
    this.subtotal = subtotal;
    this.descuento = descuento;
    this.total = total;
    this.fechaHora = fechaHora;
    this.formapago = formapago;
    this.comprobante = comprobante;
    this.estado = estado;
  }

  public Integer getIdVenta() {
    return idVenta;
  }

  public void setIdVenta(Integer idVenta) {
    this.idVenta = idVenta;
  }

  public BigDecimal getSubtotal() {
    return subtotal;
  }

  public void setSubtotal(BigDecimal subtotal) {
    this.subtotal = subtotal;
  }

  public BigDecimal getComisionClinica() {

    comisionClinica = new BigDecimal(BigInteger.ZERO);

    for (Servicioventa productoventa : productoventaList) {
      comisionClinica = comisionClinica.add(productoventa.getComisionClinica());
    }

    return comisionClinica;
  }

  public void setComisionClinica(BigDecimal comisionClinica) {
    this.comisionClinica = comisionClinica;
  }

  public BigDecimal getComisionMedico() {
    comisionMedico = new BigDecimal(BigInteger.ZERO);

    for (Servicioventa productoventa : productoventaList) {
      comisionMedico = comisionMedico.add(productoventa.getComisionMedico());
    }

    return comisionMedico;
  }

  public void setComisionMedico(BigDecimal comisionMedico) {
    this.comisionMedico = comisionMedico;
  }

  public BigDecimal getDescuento() {
    return descuento;
  }

  public void setDescuento(BigDecimal descuento) {
    this.descuento = descuento;
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

  public String getFormapago() {
    return formapago;
  }

  public void setFormapago(String formapago) {
    this.formapago = formapago;
  }

  public String getComprobante() {
    return comprobante;
  }

  public void setComprobante(String comprobante) {
    this.comprobante = comprobante;
  }

  public String getEstado() {
    return estado;
  }

  public void setEstado(String estado) {
    this.estado = estado;
  }

  @XmlTransient
  public List<Servicioventa> getProductoventaList() {
    return productoventaList;
  }

  public void setProductoventaList(List<Servicioventa> productoventaList) {
    this.productoventaList = productoventaList;
  }

  @XmlTransient
  public List<Credito> getCreditoList() {
    return creditoList;
  }

  public void setCreditoList(List<Credito> creditoList) {
    this.creditoList = creditoList;
  }

  public Cliente getIdCliente() {
    return idCliente;
  }

  public void setIdCliente(Cliente idCliente) {
    this.idCliente = idCliente;
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
    hash += (idVenta != null ? idVenta.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Venta)) {
      return false;
    }
    Venta other = (Venta) object;
    if ((this.idVenta == null && other.idVenta != null) || (this.idVenta != null && !this.idVenta.equals(other.idVenta))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return idVenta + "";
  }

}
