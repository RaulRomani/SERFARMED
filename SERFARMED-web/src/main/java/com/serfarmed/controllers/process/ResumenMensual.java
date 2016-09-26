/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.serfarmed.controllers.process;

import com.serfarmed.controllers.util.AccesoDB;
import com.serfarmed.controllers.util.CantidadLetras;
import com.serfarmed.controllers.util.JsfUtil;
import com.serfarmed.entities.Operacion;
import com.serfarmed.entities.Pago;
import com.serfarmed.entities.Personal;
import com.serfarmed.entities.Saldoinicial;
import com.serfarmed.entities.Venta;
import com.serfarmed.entities.Servicioventa;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
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
@Named(value = "resumenMes")
@SessionScoped
public class ResumenMensual implements Serializable{


  @EJB
  private com.serfarmed.facades.VentaFacadeLocal ejbFacadeVenta;
  @EJB
  private com.serfarmed.facades.ServicioventaFacadeLocal ejbFacadeServicioVenta;
  @EJB
  private com.serfarmed.facades.OperacionFacadeLocal ejbFacadeOperacion;
  @EJB
  private com.serfarmed.facades.PagoFacadeLocal ejbFacadePago;
  @EJB
  private com.serfarmed.facades.SaldoinicialFacadeLocal ejbFacadeSaldoinicial;
  
  @edu.umd.cs.findbugs.annotations.SuppressWarnings("SE_TRANSIENT_FIELD_NOT_RESTORED")
  private transient final org.slf4j.Logger logger = LoggerFactory.getLogger(ResumenMensual.class);
  
  
  @Inject
  private LoginController login;
  
  private List<Venta> ventaListMensual;
  private List<Operacion> egresoListMensual;
  private List<Operacion> ingresoListMensual;
  private List<Pago> adelantoListMensual;
  
  private List<Pago> pagoDoctorListMensual;
  private List<Pago> deudaDoctorListMensual;
  
  private List<Saldoinicial> SaldoInicialListMensual;
  
  private BigDecimal comisionClinicaMensual;
  private BigDecimal totalVentasContadoMensual;
  private BigDecimal totalVentasCreditoMensual;

  private Date fecha;
  
  @PostConstruct
  private void Once(){
    fecha = new Date();
  }
  
  //preRenderView
  public void init() {
    
    getVentaListMensual();
    getEgresoListMensual();
    getIngresoListMensual();
    getAdelantoListMensual();
    getDeudaPagoDoctorListMensual();
    getPagoDoctorListMensual();
    getSaldoInicialListMensual();
  }
  
  public Date getFecha() {
    return fecha;
  }

  public void setFecha(Date fecha) {
    this.fecha = fecha;
  }
  
  public List<Venta> getVentaListMensual() {
    ventaListMensual = ejbFacadeVenta.findVentasMes(fecha);
  
    comisionClinicaMensual = new BigDecimal(BigInteger.ZERO).setScale(2, RoundingMode.CEILING);
    totalVentasContadoMensual = new BigDecimal(BigInteger.ZERO).setScale(2, RoundingMode.CEILING);
    totalVentasCreditoMensual = new BigDecimal(BigInteger.ZERO).setScale(2, RoundingMode.CEILING);
    
    
    if ( ventaListMensual != null)
      for (Venta item : ventaListMensual) {
        
        if (item.getEstado().equals("CANCELADO")){
          comisionClinicaMensual = comisionClinicaMensual.add(item.getComisionClinica()).setScale(2, RoundingMode.CEILING);
          totalVentasContadoMensual = totalVentasContadoMensual.add(item.getTotal()).setScale(2, RoundingMode.CEILING);
        } else if (item.getEstado().equals( "CREDITO")){
          totalVentasCreditoMensual = totalVentasCreditoMensual.add(item.getTotal()).setScale(2, RoundingMode.CEILING);
        }
      }
    
    return ventaListMensual;
  }
  
  public BigDecimal getComisionClinicaMensual(){
    return comisionClinicaMensual;
  }
  
  public BigDecimal getTotalVentasContadoMensual(){
    return totalVentasContadoMensual;
  }
  
  public BigDecimal getTotalVentasCreditoMensual(){
    return totalVentasCreditoMensual;
  }
  
  public List<Operacion> getEgresoListMensual() {
    egresoListMensual = ejbFacadeOperacion.findEgresosMes(fecha);
    return egresoListMensual;
  }
  
  public List<Operacion> getIngresoListMensual() {
    ingresoListMensual = ejbFacadeOperacion.findIngresosMes(fecha);
    
    return ingresoListMensual;
  }
  
  public List<Pago> getAdelantoListMensual(){
    adelantoListMensual = ejbFacadePago.findAdelantosMes(fecha); // falta filtrar por tipo = ADELANTO
    return adelantoListMensual;
  }
  
  public boolean checkVentasNoPagadasMensual(){
    List<Object[]> ventasMensual  = ejbFacadeVenta.findDeudaPagoDoctorMes(fecha); //Ventas por doctor, pagado = false and pagarMensual = 'SI'
    if(ventasMensual.isEmpty())
      return true;
    else
      return false;
  }
  
  public List<Pago> getPagoDoctorListMensual() {
    pagoDoctorListMensual = ejbFacadePago.findPagoDoctorMes(fecha);
    return pagoDoctorListMensual;
  }

  public BigDecimal getTotalPagosDoctorMensual() {
    
    BigDecimal total = new BigDecimal(BigInteger.ZERO);
    for (Pago pago : pagoDoctorListMensual) {
      total = total.add(pago.getMonto());
    }
    return total.setScale(2, RoundingMode.CEILING);
  }
  
  
  
  public List<Pago> getDeudaPagoDoctorListMensual() {
    
    List<Object[]> ventasMensual  = ejbFacadeVenta.findDeudaPagoDoctorMes(fecha); //Ventas por doctor, pagado = false and pagarMensual = 'SI'
    
    Object[] result;
    Personal doctor;
    BigDecimal monto;
    Date fecha = new Date();
    Pago pagoDoctor;
    deudaDoctorListMensual = new ArrayList<>();
    if (ventasMensual.size()> 0)
      for (int i = 0; i < ventasMensual.size(); i++){

        result = ventasMensual.get(i);
        doctor = (Personal) result[0];
        monto = (BigDecimal) result[1];

        pagoDoctor = new Pago();
        pagoDoctor.setIdUsuario(login.getUsuario());
        pagoDoctor.setIdPersonal(doctor);
        pagoDoctor.setMonto(monto.setScale(2, RoundingMode.CEILING));
        pagoDoctor.setTipo("PAGODOCTOR");
        pagoDoctor.setFechaHora(fecha);

        deudaDoctorListMensual.add(pagoDoctor);
      }
      
    return deudaDoctorListMensual;
  }

  public List<Saldoinicial> getSaldoInicialListMensual() {
    return SaldoInicialListMensual = ejbFacadeSaldoinicial.findByMonth(fecha);
  }

  public BigDecimal getTotalSaldoInicialMensual() {
    BigDecimal total = BigDecimal.ZERO;
    for (Saldoinicial item : SaldoInicialListMensual) {
      total = total.add(item.getSaldoinicial());
    }
    return total.setScale(2, RoundingMode.CEILING);
  }
  
  public BigDecimal getTotalIngresosMensual() {
    BigDecimal total = BigDecimal.ZERO;
    if ( ingresoListMensual != null)
      for (Operacion item : ingresoListMensual) {
        total = total.add(item.getMonto());
      }
    return total.setScale(2, RoundingMode.CEILING);
  }

  public BigDecimal getTotalEgresosMensual() {
    BigDecimal total = BigDecimal.ZERO;
    if ( egresoListMensual != null)
      for (Operacion item : egresoListMensual) {
        total = total.add(item.getMonto());
      }
    return total.setScale(2, RoundingMode.CEILING);
  }
  
  public BigDecimal getTotalAdelantosMensual(){
    BigDecimal total = BigDecimal.ZERO ;
    if ( adelantoListMensual != null)
      for (Pago item : adelantoListMensual) {
        total = total.add(item.getMonto());
      }
    return total.setScale(2, RoundingMode.CEILING);
  }
  
  public BigDecimal getTotalDeudasDoctorMensual() {
    BigDecimal total = BigDecimal.ZERO;
    if ( deudaDoctorListMensual != null)
      for (Pago item : deudaDoctorListMensual) {
        total = total.add(item.getMonto());
      }
    return total.setScale(2, RoundingMode.CEILING);
  }
  
  public void generarPagoDoctor(){
    for (Pago pagoDoctor : deudaDoctorListMensual) {
      ejbFacadeServicioVenta.updatePagoDoctorMes(fecha); // change sePago = 'SI' AND pagado = true
      ejbFacadePago.create(pagoDoctor);
    }
    JsfUtil.addSuccessMessage("Los pagos se generaron exitosamente.");
  }
  
  public void imprimirPagoDoctor(Pago pago) throws JRException, IOException, NamingException, SQLException, Exception {

    Map<String, Object> parametro = new HashMap<>();
    parametro.put("idPago", pago.getIdPago());
    parametro.put("montoLetras", CantidadLetras.convertNumberToLetter(pago.getMonto().doubleValue()));

    File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/pago/pagoDoctor.jasper"));
    logger.info("OK REPORTE PAGO DOCTOR");
    Connection con = AccesoDB.getConnection();
    JasperPrint jasperPrint = JasperFillManager.fillReport(jasper.getPath(), parametro, con);

    HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
    response.addHeader("Content-disposition", " filename=Pago-"+String.format("%06d", Long.valueOf( pago.getIdPago() )) +".pdf");  //Works in chrome
    ServletOutputStream stream = response.getOutputStream();

    JasperExportManager.exportReportToPdfStream(jasperPrint, stream);
    //JasperExportManager.exportReportToPdfFile(jasperPrint, "D://clientes.pdf");
    //JasperPrintManager.printReport(jasperPrint, false);

    stream.flush();
    stream.close();

    FacesContext.getCurrentInstance().responseComplete();
  }
  
  
  
}
