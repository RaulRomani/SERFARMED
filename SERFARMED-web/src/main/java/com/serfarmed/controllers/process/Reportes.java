/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.serfarmed.controllers.process;

import com.serfarmed.controllers.util.JsfUtil;
import com.serfarmed.entities.Operacion;
import com.serfarmed.entities.Pago;
import com.serfarmed.entities.Personal;
import com.serfarmed.entities.Servicio;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import org.primefaces.model.chart.PieChartModel;

/**
 *
 * @author Raul
 */
@Named(value = "reportes")
@SessionScoped
public class Reportes implements Serializable {

  /**
   * Creates a new instance of Reportes
   */
  public Reportes() {
  }

  
  @EJB
  private com.serfarmed.facades.VentaFacadeLocal ejbFacadeVenta;
  
  @EJB
  private com.serfarmed.facades.ServicioFacadeLocal ejbFacadeServicio;
  
  @EJB
  private com.serfarmed.facades.PagoFacadeLocal ejbFacadePago;
  
  @EJB
  private com.serfarmed.facades.PersonalFacadeLocal ejbFacadeDoctor;
  
  
  private Personal doctorSelected;
  private List<Pago> pagoDoctorList;
  private List<Personal> doctorList;
  
  private PieChartModel pieServicioTotal;
  private PieChartModel pieServicioDoctorTotal;
  
  private List<Object[]> servicioTotalList;
  private List<Object[]> servicioDoctorTotalList;
  
  
  private List<Servicio> servicioList = null;
  private Servicio servicioSelected;
  
  private BigDecimal totalComisionClinica;
  
  Date fechaServicio;
  Date fechaServicioDoctor;
  Date fechaPagoDoctor;

  @PostConstruct
  public void init() {
    
    fechaServicio = new Date();
    fechaServicioDoctor = new Date();
    fechaPagoDoctor = new Date();
  }
  
  public void reporteMensualServicioDoctor(){
    
    pieServicioDoctorTotal = null;
    
    servicioDoctorTotalList = ejbFacadeVenta.ventasMensualesByServicioDoctor(servicioSelected.getNombre(), fechaServicioDoctor);
    
    if (servicioDoctorTotalList.isEmpty()){
      
      JsfUtil.addErrorMessage("No hay registros, cambie los datos y vuelva a intentarlo");
      return;
    }
    
    pieServicioDoctorTotal = new PieChartModel();
    
    Object[] result;
    Personal personal;
    for (int i = 0; i < servicioDoctorTotalList.size(); i++){
      
      result = servicioDoctorTotalList.get(i);
      personal = (Personal) result[0];
      pieServicioDoctorTotal.set(i+1 + " " + personal.getNombreCompleto() ,  (BigDecimal) result[1]);
      
      //settear result
      result[0] = personal.getNombreCompleto();
      servicioDoctorTotalList.set(i, result);
      //i++;
    }

    pieServicioDoctorTotal.setTitle("Reporte mensual por servicio y doctor");
    pieServicioDoctorTotal.setLegendPosition("w");
    pieServicioDoctorTotal.setShowDataLabels(true);
    pieServicioDoctorTotal.setDiameter(700);
  
  }

  public void reporteMensualServicio() {
    
    pieServicioTotal = null;
    
    servicioTotalList = ejbFacadeVenta.ventasMensualesByServicio(fechaServicio);
    
    if (servicioTotalList.isEmpty()){
      
      JsfUtil.addErrorMessage("No hay registros, cambie los datos y vuelva a intentarlo");
      return;
    }
    pieServicioTotal = new PieChartModel();
    
    
    
    int i = 1;
    for (Object[] result : servicioTotalList) {
      Object  obj = result[0];
      pieServicioTotal.set(i + " " + (String)result[0] ,  (BigDecimal) result[1]);
      i++;
    }

//    Random number = new Random();
//    for (int i = 0; i< 32; i++){
//      pieServicioTotal.set("Servicio " + i ,  number.nextInt(2000));
//    }

    pieServicioTotal.setTitle("Reporte mensual por servicio");
    pieServicioTotal.setLegendPosition("w");
    //pieServicioTotal.setFill(false);
    pieServicioTotal.setShowDataLabels(true);
    pieServicioTotal.setDiameter(700);
    //pieServicioTotal.setDiameter(diameter);
    //pieServicioTotal.setSliceMargin(x);
  }

  public List<Servicio> getServicioList() {
    servicioList = ejbFacadeServicio.findAll();
    return servicioList;
  }

  public void reporteMensualPagoDoctor(){
    pagoDoctorList = ejbFacadePago.findPagoByDoctorMes(doctorSelected, fechaPagoDoctor);
    if ( pagoDoctorList.isEmpty()){
      JsfUtil.addSuccessMessage("Hoy hay registros, intente con otros datos");
      return ;
    }
    totalComisionClinica = ejbFacadeVenta.findTotalPagadoByDoctor(doctorSelected, fechaPagoDoctor)
                            .setScale(2, RoundingMode.CEILING);
    if ( totalComisionClinica == null)
      totalComisionClinica = new BigDecimal(BigInteger.ZERO).setScale(2, RoundingMode.CEILING);
    
  }
  
  
  public BigDecimal getTotalPagoDoctor() {
    BigDecimal total = BigDecimal.ZERO;
    if ( pagoDoctorList != null)
      for (Pago item : pagoDoctorList) {
        total = total.add(item.getMonto());
      }
    return total.setScale(2, RoundingMode.CEILING);
  }

  public List<Personal> getDoctorList() {
    return doctorList = ejbFacadeDoctor.findByCargo("Doctor");
  }
  
  

  public BigDecimal getTotalComisionClinica() {
    return totalComisionClinica;
  }

  public void setTotalComisionClinica(BigDecimal totalComisionClinica) {
    this.totalComisionClinica = totalComisionClinica;
  }

  
  public List<Pago> getPagoDoctorList() {
    return pagoDoctorList;
  }
  
  public List<Object[]> getServicioTotalList() {
    return servicioTotalList;
  }

  public List<Object[]> getServicioDoctorTotalList() {
    return servicioDoctorTotalList;
  }
  
  public PieChartModel getPieServicioTotal() {
    return pieServicioTotal;
  }

  public PieChartModel getPieServicioDoctorTotal() {
    return pieServicioDoctorTotal;
  }
  
  public Servicio getServicioSelected() {
    return servicioSelected;
  }

  public void setServicioSelected(Servicio servicioSelected) {
    this.servicioSelected = servicioSelected;
  }

  public Date getFechaServicio() {
    return fechaServicio;
  }

  public void setFechaServicio(Date fechaServicio) {
    this.fechaServicio = fechaServicio;
  }

  public Date getFechaServicioDoctor() {
    return fechaServicioDoctor;
  }

  public void setFechaServicioDoctor(Date fechaServicioDoctor) {
    this.fechaServicioDoctor = fechaServicioDoctor;
  }

  public Personal getDoctorSelected() {
    return doctorSelected;
  }

  public void setDoctorSelected(Personal doctorSelected) {
    this.doctorSelected = doctorSelected;
  }

  public Date getFechaPagoDoctor() {
    return fechaPagoDoctor;
  }

  public void setFechaPagoDoctor(Date fechaPagoDoctor) {
    this.fechaPagoDoctor = fechaPagoDoctor;
  }
  
  
  


}
