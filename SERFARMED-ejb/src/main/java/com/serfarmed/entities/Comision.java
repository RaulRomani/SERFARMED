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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Raul
 */
@Entity
@Table(name = "comision")
@XmlRootElement
@Cacheable(false)
@NamedQueries({
  @NamedQuery(name = "Comision.findAll", query = "SELECT c FROM Comision c"),
  @NamedQuery(name = "Comision.findByIdComision", query = "SELECT c FROM Comision c WHERE c.idComision = :idComision"),
  @NamedQuery(name = "Comision.findByTipo", query = "SELECT c FROM Comision c WHERE c.tipo = :tipo"),
  @NamedQuery(name = "Comision.findByComision", query = "SELECT c FROM Comision c WHERE c.comision = :comision"),
  @NamedQuery(name = "Comision.findByNota", query = "SELECT c FROM Comision c WHERE c.nota = :nota")})
public class Comision implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "idComision")
  private Integer idComision;
  @Size(max = 50)
  @Column(name = "tipo")
  private String tipo;
  // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
  @Column(name = "comision")
  private BigDecimal comision;
  @Size(max = 200)
  @Column(name = "nota")
  private String nota;
  @JoinColumn(name = "idPersonal", referencedColumnName = "idPersonal")
  @ManyToOne(optional = false)
  private Personal idPersonal;
  
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "idComision")
  private List<Servicioventa> servicioVentaList;

  public Comision() {
  }

  public Comision(Integer idComision) {
    this.idComision = idComision;
  }

  public Integer getIdComision() {
    return idComision;
  }

  public void setIdComision(Integer idComision) {
    this.idComision = idComision;
  }

  public String getTipo() {
    return tipo;
  }

  public void setTipo(String tipo) {
    this.tipo = tipo;
  }

  public BigDecimal getComision() {
    return comision;
  }

  public void setComision(BigDecimal comision) {
    this.comision = comision;
  }

  public String getNota() {
    return nota;
  }

  public void setNota(String nota) {
    this.nota = nota;
  }

  public Personal getIdPersonal() {
    return idPersonal;
  }

  public void setIdPersonal(Personal idPersonal) {
    this.idPersonal = idPersonal;
  }
  
  @XmlTransient
  public List<Servicioventa> getServicioVentaList() {
    return servicioVentaList;
  }

  public void setServicioVentaList(List<Servicioventa> servicioVentaList) {
    this.servicioVentaList = servicioVentaList;
  }
  
  

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (idComision != null ? idComision.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Comision)) {
      return false;
    }
    Comision other = (Comision) object;
    if ((this.idComision == null && other.idComision != null) || (this.idComision != null && !this.idComision.equals(other.idComision))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return idComision + "";
  }
  
}
