/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.serfarmed.controllers.process;

import com.serfarmed.controllers.util.JsfUtil;
import com.serfarmed.entities.Comision;
import com.serfarmed.entities.Servicio;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
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
  
  private PieChartModel pieServicioTotal;
  private PieChartModel pieServicioDoctorTotal;
  
  private List<Object[]> servicioTotalList;
  private List<Object[]> servicioDoctorTotalList;
  
  
  private List<Servicio> servicioList = null;
  private Servicio servicioSelected;
  
  
  Date fechaServicio;
  Date fechaServicioDoctor;

  @PostConstruct
  public void init() {
    
    fechaServicio = new Date();
    fechaServicioDoctor = new Date();
    
  }
  
  public void reporteMensualServicioDoctor(){
    
    pieServicioDoctorTotal = null;
    
    servicioDoctorTotalList = ejbFacadeVenta.ventasMensualesByServicioDoctor(servicioSelected.getNombre(), "2016", "08");
    
    if (servicioDoctorTotalList.isEmpty()){
      
      JsfUtil.addErrorMessage("No hay registros, cambie los datos y vuelva a intentarlo");
      return;
    }
    
    pieServicioDoctorTotal = new PieChartModel();
    
    Object[] result;
    Comision comision;
    for (int i = 0; i < servicioDoctorTotalList.size(); i++){
      
      result = servicioDoctorTotalList.get(i);
      comision = (Comision) result[0];
      pieServicioDoctorTotal.set(i+1 + " " + comision.getIdPersonal().getNombreCompleto() ,  (BigDecimal) result[1]);
      
      //settear result
      result[0] = comision.getIdPersonal().getNombreCompleto();
      servicioDoctorTotalList.set(i, result);
      //i++;
    }

    pieServicioDoctorTotal.setTitle("Reporte mensual por servicio y doctor");
    pieServicioDoctorTotal.setLegendPosition("w");
    pieServicioDoctorTotal.setShowDataLabels(true);
    pieServicioDoctorTotal.setDiameter(700);
  
  }

  private void reporteMensualServicio() {
    pieServicioTotal = new PieChartModel();
    
    servicioTotalList = ejbFacadeVenta.ventasMensualesByServicio("2016", "08");
    
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

  
  
  

  

  public List<Object[]> getServicioTotalList() {
    reporteMensualServicio();
    return servicioTotalList;
  }

  public List<Object[]> getServicioDoctorTotalList() {
    return servicioDoctorTotalList;
  }
  
  public PieChartModel getPieServicioTotal() {
    reporteMensualServicio();
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


}
