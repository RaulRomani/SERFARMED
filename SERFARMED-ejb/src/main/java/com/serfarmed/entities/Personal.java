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
@Table(name = "personal")
@XmlRootElement
@Cacheable(false)
@NamedQueries({
  @NamedQuery(name = "Personal.findAll", query = "SELECT p FROM Personal p"),
  @NamedQuery(name = "Personal.findByIdPersonal", query = "SELECT p FROM Personal p WHERE p.idPersonal = :idPersonal"),
  @NamedQuery(name = "Personal.findByNombre", query = "SELECT p FROM Personal p WHERE p.nombre = :nombre"),
  @NamedQuery(name = "Personal.findByApellido", query = "SELECT p FROM Personal p WHERE p.apellido = :apellido"),
  @NamedQuery(name = "Personal.findByCargo", query = "SELECT p FROM Personal p WHERE p.cargo = :cargo"),
  @NamedQuery(name = "Personal.findByDni", query = "SELECT p FROM Personal p WHERE p.dni = :dni"),
  @NamedQuery(name = "Personal.findBySueldo", query = "SELECT p FROM Personal p WHERE p.sueldo = :sueldo"),
  @NamedQuery(name = "Personal.findByEspecialidad", query = "SELECT p FROM Personal p WHERE p.especialidad = :especialidad"),
  @NamedQuery(name = "Personal.findByCorreo", query = "SELECT p FROM Personal p WHERE p.correo = :correo"),
  @NamedQuery(name = "Personal.findByDireccion", query = "SELECT p FROM Personal p WHERE p.direccion = :direccion"),
  @NamedQuery(name = "Personal.findByLugarNacimiento", query = "SELECT p FROM Personal p WHERE p.lugarNacimiento = :lugarNacimiento"),
  @NamedQuery(name = "Personal.findByFechaNacimiento", query = "SELECT p FROM Personal p WHERE p.fechaNacimiento = :fechaNacimiento"),
  @NamedQuery(name = "Personal.findByCelular", query = "SELECT p FROM Personal p WHERE p.celular = :celular"),
  @NamedQuery(name = "Personal.findBySexo", query = "SELECT p FROM Personal p WHERE p.sexo = :sexo"),
  @NamedQuery(name = "Personal.findByEdad", query = "SELECT p FROM Personal p WHERE p.edad = :edad"),
  @NamedQuery(name = "Personal.findByFechaCreacion", query = "SELECT p FROM Personal p WHERE p.fechaCreacion = :fechaCreacion")})
public class Personal implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "idPersonal")
  private Integer idPersonal;
  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 50)
  @Column(name = "nombre")
  private String nombre;
  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 100)
  @Column(name = "apellido")
  private String apellido;
  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 40)
  @Column(name = "cargo")
  private String cargo;
  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 8)
  @Column(name = "DNI")
  private String dni;
  // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
  @Basic(optional = false)
  @NotNull
  @Column(name = "sueldo")
  private BigDecimal sueldo;
  @Size(max = 100)
  @Column(name = "especialidad")
  private String especialidad;
  @Size(max = 50)
  @Column(name = "correo")
  private String correo;
  @Size(max = 100)
  @Column(name = "direccion")
  private String direccion;
  @Size(max = 200)
  @Column(name = "lugarNacimiento")
  private String lugarNacimiento;
  @Column(name = "fechaNacimiento")
  @Temporal(TemporalType.DATE)
  private Date fechaNacimiento;
  @Size(max = 20)
  @Column(name = "celular")
  private String celular;
  @Size(max = 10)
  @Column(name = "sexo")
  private String sexo;
  @Size(max = 3)
  @Column(name = "edad")
  private String edad;
  @Basic(optional = false)
  @Column(name = "fechaCreacion")
  @Temporal(TemporalType.TIMESTAMP)
  private Date fechaCreacion;
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "idPersonal")
  private List<Pago> pagoList;
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "idPersonal")
  private List<Usuario> usuarioList;
  
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "idPersonal")
  private List<Comision> comisionList;
  

  public Personal() {
  }

  public Personal(Integer idPersonal) {
    this.idPersonal = idPersonal;
  }

  public Personal(Integer idPersonal, String nombre, String apellido, String cargo, String dni, BigDecimal sueldo, Date fechaCreacion) {
    this.idPersonal = idPersonal;
    this.nombre = nombre;
    this.apellido = apellido;
    this.cargo = cargo;
    this.dni = dni;
    this.sueldo = sueldo;
    this.fechaCreacion = fechaCreacion;
  }

  public Integer getIdPersonal() {
    return idPersonal;
  }

  public void setIdPersonal(Integer idPersonal) {
    this.idPersonal = idPersonal;
  }

  public String getNombre() {
    return nombre;
  }
  
  public String getNombreCompleto() {
    return nombre+ " " +apellido;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getApellido() {
    return apellido;
  }

  public void setApellido(String apellido) {
    this.apellido = apellido;
  }

  public String getCargo() {
    return cargo;
  }

  public void setCargo(String cargo) {
    this.cargo = cargo;
  }

  public String getDni() {
    return dni;
  }

  public void setDni(String dni) {
    this.dni = dni;
  }

  public BigDecimal getSueldo() {
    return sueldo;
  }

  public void setSueldo(BigDecimal sueldo) {
    this.sueldo = sueldo;
  }

  public String getEspecialidad() {
    return especialidad;
  }

  public void setEspecialidad(String especialidad) {
    this.especialidad = especialidad;
  }

  public String getCorreo() {
    return correo;
  }

  public void setCorreo(String correo) {
    this.correo = correo;
  }

  public String getDireccion() {
    return direccion;
  }

  public void setDireccion(String direccion) {
    this.direccion = direccion;
  }

  public String getLugarNacimiento() {
    return lugarNacimiento;
  }

  public void setLugarNacimiento(String lugarNacimiento) {
    this.lugarNacimiento = lugarNacimiento;
  }

  public Date getFechaNacimiento() {
    return fechaNacimiento;
  }

  public void setFechaNacimiento(Date fechaNacimiento) {
    this.fechaNacimiento = fechaNacimiento;
  }

  public String getCelular() {
    return celular;
  }

  public void setCelular(String celular) {
    this.celular = celular;
  }

  public String getSexo() {
    return sexo;
  }

  public void setSexo(String sexo) {
    this.sexo = sexo;
  }

  public String getEdad() {
    return edad;
  }

  public void setEdad(String edad) {
    this.edad = edad;
  }

  public Date getFechaCreacion() {
    return fechaCreacion;
  }

  public void setFechaCreacion(Date fechaCreacion) {
    this.fechaCreacion = fechaCreacion;
  }

  @XmlTransient
  public List<Pago> getPagoList() {
    return pagoList;
  }

  public void setPagoList(List<Pago> pagoList) {
    this.pagoList = pagoList;
  }

  @XmlTransient
  public List<Usuario> getUsuarioList() {
    return usuarioList;
  }

  public void setUsuarioList(List<Usuario> usuarioList) {
    this.usuarioList = usuarioList;
  }

  @XmlTransient
  public List<Comision> getComisionList() {
    return comisionList;
  }

  public void setComisionList(List<Comision> comisionList) {
    this.comisionList = comisionList;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (idPersonal != null ? idPersonal.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Personal)) {
      return false;
    }
    Personal other = (Personal) object;
    if ((this.idPersonal == null && other.idPersonal != null) || (this.idPersonal != null && !this.idPersonal.equals(other.idPersonal))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return idPersonal + " - " + getNombreCompleto();
  }
  
}
