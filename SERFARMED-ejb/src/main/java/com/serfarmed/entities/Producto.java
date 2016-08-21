/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.serfarmed.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Raul
 */
@Entity
@Table(name = "producto")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Producto.findAll", query = "SELECT p FROM Producto p"),
  @NamedQuery(name = "Producto.findByIdProducto", query = "SELECT p FROM Producto p WHERE p.idProducto = :idProducto"),
  @NamedQuery(name = "Producto.findByNombre", query = "SELECT p FROM Producto p WHERE p.nombre = :nombre"),
  @NamedQuery(name = "Producto.findByPrecio", query = "SELECT p FROM Producto p WHERE p.precio = :precio"),
  @NamedQuery(name = "Producto.findByDescripcion", query = "SELECT p FROM Producto p WHERE p.descripcion = :descripcion")})
public class Producto implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "idProducto")
  private Integer idProducto;
  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 200)
  @Column(name = "nombre")
  private String nombre;
  // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
  @Basic(optional = false)
  @NotNull
  @Column(name = "precio")
  private BigDecimal precio;
  @Size(max = 200)
  @Column(name = "descripcion")
  private String descripcion;
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "idProducto")
  private Collection<Detallecompra> detallecompraCollection;
  @JoinColumn(name = "idCategoria", referencedColumnName = "idCategoria")
  @ManyToOne(optional = false)
  private Categoria idCategoria;

  public Producto() {
  }

  public Producto(Integer idProducto) {
    this.idProducto = idProducto;
  }

  public Producto(Integer idProducto, String nombre, BigDecimal precio) {
    this.idProducto = idProducto;
    this.nombre = nombre;
    this.precio = precio;
  }

  public Integer getIdProducto() {
    return idProducto;
  }

  public void setIdProducto(Integer idProducto) {
    this.idProducto = idProducto;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public BigDecimal getPrecio() {
    return precio;
  }

  public void setPrecio(BigDecimal precio) {
    this.precio = precio;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  @XmlTransient
  public Collection<Detallecompra> getDetallecompraCollection() {
    return detallecompraCollection;
  }

  public void setDetallecompraCollection(Collection<Detallecompra> detallecompraCollection) {
    this.detallecompraCollection = detallecompraCollection;
  }

  public Categoria getIdCategoria() {
    return idCategoria;
  }

  public void setIdCategoria(Categoria idCategoria) {
    this.idCategoria = idCategoria;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (idProducto != null ? idProducto.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Producto)) {
      return false;
    }
    Producto other = (Producto) object;
    if ((this.idProducto == null && other.idProducto != null) || (this.idProducto != null && !this.idProducto.equals(other.idProducto))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return idProducto + "";
  }
  
}
