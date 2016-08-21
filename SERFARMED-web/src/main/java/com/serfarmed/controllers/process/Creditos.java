/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.serfarmed.controllers.process;

import com.serfarmed.controllers.util.AccesoDB;
import com.serfarmed.controllers.util.JsfUtil;
import com.serfarmed.controllers.util.Log4jConfig;
import com.serfarmed.entities.Credito;
import com.serfarmed.entities.Cliente;
import com.serfarmed.entities.Venta;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.naming.NamingException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Raul
 */
@Named(value = "creditos")
@SessionScoped
public class Creditos implements Serializable{

  @EJB
  private com.serfarmed.facades.ClienteFacadeLocal ejbFacadeCliente;
  
  @EJB
  private com.serfarmed.facades.VentaFacadeLocal ejbFacadeVenta;
  
  @EJB
  private com.serfarmed.facades.CreditoFacadeLocal ejbFacadeCredito;
  
  private List<Cliente> clienteList=null;
  private List<Venta> ventaList=null;
  
  private Cliente clienteSelected;
  private Venta ventaSelected;
  private Credito creditoSelected;
  private Integer cuotas;
  
  @edu.umd.cs.findbugs.annotations.SuppressWarnings("SE_TRANSIENT_FIELD_NOT_RESTORED")
  private transient final org.slf4j.Logger logger = LoggerFactory.getLogger(Creditos.class);
  
  @PostConstruct
  void init() {
    clienteSelected = new Cliente();
    ventaSelected = new Venta();
    creditoSelected = new Credito();
    cuotas = 1;
    logger.info("INIT COBROS");
  }
  

  public List<Cliente> getClienteList() {
    if (clienteList == null) {
      clienteList = ejbFacadeCliente.findAll();
    }
    return clienteList;
  }
  
  public List<Venta> getVentaList() {
    if (ventaList == null) {
      ventaList = ejbFacadeVenta.findByFormaPagoCliente(clienteSelected,"CUOTAS");
      logger.info("getVentas, cliente="+ clienteSelected.getNombre());
      logger.info("getVentas, idCliente="+ clienteSelected.getIdCliente());
    }
    logger.info("Get Ventas CUOTAS :" + ventaList.size());
    return ventaList;
  }
  
  public void loadCredito(){
    creditoSelected = ventaSelected.getCreditoList().get(0);
  }
  public void loadCreditos(){
    ventaList = ejbFacadeVenta.findByFormaPagoCliente(clienteSelected,"CUOTAS");
    
    ventaSelected = null;
    creditoSelected = null;
  }
  
  public void Save() throws JRException, IOException, NamingException, SQLException, Exception {
    
    creditoSelected.setCuotaspagado(creditoSelected.getCuotaspagado() + cuotas);
    
    persist(JsfUtil.PersistAction.CREATE, "Guardado correctamente");
    if (!JsfUtil.isValidationFailed()) { //Si la transaccion tuvo exito.
      
    }
    reporteCobroCreditos();
  }
  
  public void reporteCobroCreditos() throws JRException, IOException, NamingException, SQLException, Exception {

    Map<String, Object> parametro = new HashMap<>();
    parametro.put("idVenta", ventaSelected.getIdVenta());
    parametro.put("idCliente", clienteSelected.getIdCliente());
    
    
    File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/ventas/cobros.jasper"));
    logger.info("OK reporte cobros ");

    Connection con = AccesoDB.getConnection();
    //java.sql.Connection co = em.unwrap(java.sql.Connection.class);
    JasperPrint jasperPrint = JasperFillManager.fillReport(jasper.getPath(), parametro, con);
    

    HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
    //response.addHeader("Content-disposition", "attachment; filename=ProgramacionTutores.pdf");
    response.addHeader("Content-disposition", "filename=Credito-"+clienteSelected.getDni()+ ".pdf");  //Works in chrome
    ServletOutputStream stream = response.getOutputStream();

    JasperExportManager.exportReportToPdfStream(jasperPrint, stream);
    //JasperExportManager.exportReportToPdfFile(jasperPrint, "D://clientes.pdf");
    JasperPrintManager.printReport(jasperPrint, false);
    
    stream.flush();
    stream.close();

    FacesContext.getCurrentInstance().responseComplete();
  }
  
  
  private void persist(JsfUtil.PersistAction persistAction, String successMessage) {
    if (creditoSelected != null) {
      try {
        if (persistAction != JsfUtil.PersistAction.DELETE) {
          ejbFacadeCredito.edit(creditoSelected);
        } else {
          ejbFacadeCredito.remove(creditoSelected);
        }
        JsfUtil.addSuccessMessage(successMessage);
      } catch (EJBException ex) {
        String msg = "";
        Throwable cause = ex.getCause();
        if (cause != null) {
          msg = cause.getLocalizedMessage();
        }
        if (msg.length() > 0) {
          JsfUtil.addErrorMessage(msg);
        } else {
          JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
      } catch (Exception ex) {
        Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
      }
    }
  }
  
  public void loadCliente(Cliente cliente){
    clienteSelected = cliente;
  }

  public Cliente getClienteSelected() {
    return clienteSelected;
  }

  public void setClienteSelected(Cliente clienteSelected) {
    this.clienteSelected = clienteSelected;
  }

  public Venta getVentaSelected() {
    return ventaSelected;
  }

  public void setVentaSelected(Venta ventaSelected) {
    this.ventaSelected = ventaSelected;
  }

  public Credito getCreditoSelected() {
    return creditoSelected;
  }

  public void setCreditoSelected(Credito creditoSelected) {
    this.creditoSelected = creditoSelected;
  }

  public Integer getCreditos() {
    return cuotas;
  }

  public void setCreditos(Integer cuotas) {
    this.cuotas = cuotas;
  }
  
  

  


  
}
