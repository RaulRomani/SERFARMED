/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.serfarmed.entities;

import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Raul
 */
@Entity
@Table(name = "servicio")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Servicio.findAll", query = "SELECT s FROM Servicio s"),
  @NamedQuery(name = "Servicio.findByIdServicio", query = "SELECT s FROM Servicio s WHERE s.idServicio = :idServicio"),
  @NamedQuery(name = "Servicio.findByNombre", query = "SELECT s FROM Servicio s WHERE s.nombre = :nombre"),
  @NamedQuery(name = "Servicio.findByPrecio", query = "SELECT s FROM Servicio s WHERE s.precio = :precio"),
  @NamedQuery(name = "Servicio.findByDescripcion", query = "SELECT s FROM Servicio s WHERE s.descripcion = :descripcion")})
public class Servicio implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "idServicio")
  private Integer idServicio;
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
  @JoinColumn(name = "idCategoria", referencedColumnName = "idCategoria")
  @ManyToOne(optional = false)
  private Categoria idCategoria;
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "idServicio")
  private List<Servicioventa> servicioventaList;

  public Servicio() {
  }

  public Servicio(Integer idServicio) {
    this.idServicio = idServicio;
  }

  public Servicio(Integer idServicio, String nombre, BigDecimal precio) {
    this.idServicio = idServicio;
    this.nombre = nombre;
    this.precio = precio;
  }

  public Integer getIdServicio() {
    return idServicio;
  }

  public void setIdServicio(Integer idServicio) {
    this.idServicio = idServicio;
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

  public Categoria getIdCategoria() {
    return idCategoria;
  }

  public void setIdCategoria(Categoria idCategoria) {
    this.idCategoria = idCategoria;
  }

  @XmlTransient
  public List<Servicioventa> getServicioventaList() {
    return servicioventaList;
  }

  public void setServicioventaList(List<Servicioventa> servicioventaList) {
    this.servicioventaList = servicioventaList;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (idServicio != null ? idServicio.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Servicio)) {
      return false;
    }
    Servicio other = (Servicio) object;
    if ((this.idServicio == null && other.idServicio != null) || (this.idServicio != null && !this.idServicio.equals(other.idServicio))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return idServicio + "";
  }
  
}
