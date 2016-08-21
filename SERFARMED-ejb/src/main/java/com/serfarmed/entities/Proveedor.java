/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.serfarmed.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "proveedor")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Proveedor.findAll", query = "SELECT p FROM Proveedor p"),
  @NamedQuery(name = "Proveedor.findByIdProveedor", query = "SELECT p FROM Proveedor p WHERE p.idProveedor = :idProveedor"),
  @NamedQuery(name = "Proveedor.findByRazonSocial", query = "SELECT p FROM Proveedor p WHERE p.razonSocial = :razonSocial"),
  @NamedQuery(name = "Proveedor.findByRuc", query = "SELECT p FROM Proveedor p WHERE p.ruc = :ruc"),
  @NamedQuery(name = "Proveedor.findByDireccion", query = "SELECT p FROM Proveedor p WHERE p.direccion = :direccion"),
  @NamedQuery(name = "Proveedor.findByTelefono", query = "SELECT p FROM Proveedor p WHERE p.telefono = :telefono"),
  @NamedQuery(name = "Proveedor.findByCelular", query = "SELECT p FROM Proveedor p WHERE p.celular = :celular"),
  @NamedQuery(name = "Proveedor.findByCorreo", query = "SELECT p FROM Proveedor p WHERE p.correo = :correo"),
  @NamedQuery(name = "Proveedor.findByPaginaWeb", query = "SELECT p FROM Proveedor p WHERE p.paginaWeb = :paginaWeb"),
  @NamedQuery(name = "Proveedor.findByFechaCreacion", query = "SELECT p FROM Proveedor p WHERE p.fechaCreacion = :fechaCreacion"),
  @NamedQuery(name = "Proveedor.findByNota", query = "SELECT p FROM Proveedor p WHERE p.nota = :nota")})
public class Proveedor implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "idProveedor")
  private Integer idProveedor;
  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 60)
  @Column(name = "razonSocial")
  private String razonSocial;
  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 11)
  @Column(name = "RUC")
  private String ruc;
  @Size(max = 100)
  @Column(name = "direccion")
  private String direccion;
  @Size(max = 20)
  @Column(name = "telefono")
  private String telefono;
  @Size(max = 20)
  @Column(name = "celular")
  private String celular;
  @Size(max = 30)
  @Column(name = "correo")
  private String correo;
  @Size(max = 30)
  @Column(name = "paginaWeb")
  private String paginaWeb;
  @Basic(optional = false)
  @NotNull
  @Column(name = "fechaCreacion")
  @Temporal(TemporalType.TIMESTAMP)
  private Date fechaCreacion;
  @Size(max = 200)
  @Column(name = "Nota")
  private String nota;
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "idProveedor")
  private List<Compra> compraList;

  public Proveedor() {
  }

  public Proveedor(Integer idProveedor) {
    this.idProveedor = idProveedor;
  }

  public Proveedor(Integer idProveedor, String razonSocial, String ruc, Date fechaCreacion) {
    this.idProveedor = idProveedor;
    this.razonSocial = razonSocial;
    this.ruc = ruc;
    this.fechaCreacion = fechaCreacion;
  }

  public Integer getIdProveedor() {
    return idProveedor;
  }

  public void setIdProveedor(Integer idProveedor) {
    this.idProveedor = idProveedor;
  }

  public String getRazonSocial() {
    return razonSocial;
  }

  public void setRazonSocial(String razonSocial) {
    this.razonSocial = razonSocial;
  }

  public String getRuc() {
    return ruc;
  }

  public void setRuc(String ruc) {
    this.ruc = ruc;
  }

  public String getDireccion() {
    return direccion;
  }

  public void setDireccion(String direccion) {
    this.direccion = direccion;
  }

  public String getTelefono() {
    return telefono;
  }

  public void setTelefono(String telefono) {
    this.telefono = telefono;
  }

  public String getCelular() {
    return celular;
  }

  public void setCelular(String celular) {
    this.celular = celular;
  }

  public String getCorreo() {
    return correo;
  }

  public void setCorreo(String correo) {
    this.correo = correo;
  }

  public String getPaginaWeb() {
    return paginaWeb;
  }

  public void setPaginaWeb(String paginaWeb) {
    this.paginaWeb = paginaWeb;
  }

  public Date getFechaCreacion() {
    return fechaCreacion;
  }

  public void setFechaCreacion(Date fechaCreacion) {
    this.fechaCreacion = fechaCreacion;
  }

  public String getNota() {
    return nota;
  }

  public void setNota(String nota) {
    this.nota = nota;
  }

  @XmlTransient
  public List<Compra> getCompraList() {
    return compraList;
  }

  public void setCompraList(List<Compra> compraList) {
    this.compraList = compraList;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (idProveedor != null ? idProveedor.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Proveedor)) {
      return false;
    }
    Proveedor other = (Proveedor) object;
    if ((this.idProveedor == null && other.idProveedor != null) || (this.idProveedor != null && !this.idProveedor.equals(other.idProveedor))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return idProveedor + "";
  }
  
}
