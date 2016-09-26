/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.serfarmed.controllers.process;

import com.serfarmed.controllers.util.AccesoDB;
import com.serfarmed.controllers.util.JsfUtil;
import com.serfarmed.entities.Cliente;
import com.serfarmed.entities.Servicio;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.naming.NamingException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Raul
 */
@Named(value = "testAjax")
@SessionScoped
public class TestAjax implements Serializable {

  @EJB
  private com.serfarmed.facades.ClienteFacadeLocal ejbFacadeCliente;
  @EJB
  private com.serfarmed.facades.ServicioFacadeLocal ejbFacadeServicio;

  private List<Cliente> clienteList;
  private Cliente clienteSelected;

  private List<Servicio> servicioList;
  private Servicio servicioSelected;

  private String otherVariablee;
  private String msgReporteServicio;

  @PostConstruct
  private void init() {

    System.out.println("INIT DE TEST AJAX");
    
    

    if (servicioSelected == null) {
      System.out.println("SERVICIO SELECTED NULL BY DEFAULT");
    }

    if (servicioList == null) {
      System.out.println("SERVICIO LIST NULL BY DEFAULT");
    }

  }

  public void buttonCliente() {

    System.out.println("INSIDE BUTTON CLIENTE");
    if (servicioSelected != null) {
      System.out.println("ServicioSelected: " + servicioSelected.getNombre());
    }

    if (clienteSelected != null) {
      System.out.println("ClienteSelected: " + clienteSelected.getNombre());
    }
     // Sleeping current thread for 5 seconds.
    try {
     Thread.sleep(5000);
    } catch (InterruptedException interruptedException) {
     interruptedException.printStackTrace();
    }

    JsfUtil.addSuccessMessage("CLIENT SENDED");

  }

  public void buttonServicio() throws JRException, IOException, NamingException, SQLException, Exception {

    System.out.println("INSIDE BUTTON SERVICIO");
    if (clienteSelected != null) {
      System.out.println("ClienteSelected: " + clienteSelected.getNombre());
    }

    if (servicioSelected != null) {
      System.out.println("ServicioSelected: " + servicioSelected.getNombre());
    }
    
    msgReporteServicio = "REPORTE DE SERVICIO OK";

    //JsfUtil.addSuccessMessage("SERVICE SENDED");
    reportePdf();

//    RequestContext.getCurrentInstance().update("growl");
//    FacesContext context = FacesContext.getCurrentInstance();
//    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "SERVICE SENDED"));

  }

  public void reportePdf() throws JRException, IOException, NamingException, SQLException, Exception {

    Map<String, Object> parametro = new HashMap<>();

    File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/mantenimiento/servicio.jasper"));

    Connection con = AccesoDB.getConnection();
    //java.sql.Connection co = em.unwrap(java.sql.Connection.class);
    JasperPrint jasperPrint = JasperFillManager.fillReport(jasper.getPath(), parametro, con);

    FacesContext context = FacesContext.getCurrentInstance();
    HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
    //response.addHeader("Content-disposition", "attachment; filename=ProgramacionTutores.pdf");
    response.addHeader("Content-disposition", "filename=Servicios.pdf");  //Works in chrome
    ServletOutputStream stream = response.getOutputStream();

    JasperExportManager.exportReportToPdfStream(jasperPrint, stream);
    //JasperExportManager.exportReportToPdfFile(jasperPrint, "D://pacientes.pdf");
    //JasperPrintManager.printReport(jasperPrint, false);

    
    stream.flush();
    stream.close();

    FacesContext.getCurrentInstance().responseComplete();
  }

  public List<Cliente> getClienteList() {
    return ejbFacadeCliente.findAll();
  }

  public List<Servicio> getServicioList() {
    return ejbFacadeServicio.findAll();
  }

  public Cliente getClienteSelected() {
    return clienteSelected;
  }

  public void setClienteSelected(Cliente clienteSelected) {
    this.clienteSelected = clienteSelected;
  }

  public Servicio getServicioSelected() {
    return servicioSelected;
  }

  public void setServicioSelected(Servicio servicioSelected) {
    this.servicioSelected = servicioSelected;
  }

  public String getOtherVariablee() {
    return otherVariablee;
  }

  public void setOtherVariablee(String otherVariablee) {
    this.otherVariablee = otherVariablee;
  }

  public String getMsgReporteServicio() {
    return msgReporteServicio;
  }

  public void setMsgReporteServicio(String msgReporteServicio) {
    this.msgReporteServicio = msgReporteServicio;
  }

  
}
