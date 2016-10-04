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

/**
 *
 * @author Raul
 */
@Named(value = "resumen")
@SessionScoped
public class Resumen implements Serializable{


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
  
  
  @Inject
  private LoginController login;
  
  private List<Venta> ventaListHoy;
  private List<Operacion> egresoListHoy;
  private List<Operacion> ingresoListHoy;
  private List<Pago> adelantoListHoy;
  
  private List<Pago> pagoDoctorListHoy;
  private List<Pago> deudaDoctorListHoy;
  private Pago deudaDoctorSelectedHoy;
  
  private List<Servicioventa> detalleDeudaDoctorListHoy;
  
  private BigDecimal comisionClinicaHoy;
  private BigDecimal totalVentasContadoHoy;
  private BigDecimal totalVentasCreditoHoy;

  private Date fecha;
  
  @PostConstruct
  private void Once(){
    fecha = new Date();
  }
  
  //preRenderView
  public void init() {
    getVentaListHoy();
    getEgresoListHoy();
    getIngresoListHoy();
    getAdelantoListHoy();
    getPagoDoctorListHoy();
    deudaDoctorSelectedHoy = null;
    deudaDoctorListHoy = new ArrayList<>();
    System.out.println("PRE RENDER VIEW RESUMEN");
  }
  
  public Date getFecha() {
    return fecha;
  }

  public void setFecha(Date fecha) {
    this.fecha = fecha;
  }
  
  public List<Venta> getVentaListHoy() {
    ventaListHoy = ejbFacadeVenta.findByFecha(fecha);
  
    comisionClinicaHoy = new BigDecimal(BigInteger.ZERO).setScale(2, RoundingMode.CEILING);
    totalVentasContadoHoy = new BigDecimal(BigInteger.ZERO).setScale(2, RoundingMode.CEILING);
    totalVentasCreditoHoy = new BigDecimal(BigInteger.ZERO).setScale(2, RoundingMode.CEILING);
    
    
    if ( ventaListHoy != null)
      for (Venta item : ventaListHoy) {
        
        if (item.getEstado().equals("CANCELADO")){
          comisionClinicaHoy = comisionClinicaHoy.add(item.getComisionClinica()).setScale(2, RoundingMode.CEILING);
          totalVentasContadoHoy = totalVentasContadoHoy.add(item.getTotal()).setScale(2, RoundingMode.CEILING);
        } else if (item.getEstado().equals( "CREDITO")){
          totalVentasCreditoHoy = totalVentasCreditoHoy.add(item.getTotal()).setScale(2, RoundingMode.CEILING);
        }
      }
    
    return ventaListHoy;
  }
  
  public BigDecimal getComisionClinicaHoy(){
    return comisionClinicaHoy;
  }
  
  public BigDecimal getTotalVentasContadoHoy(){
    return totalVentasContadoHoy;
  }
  
  public BigDecimal getTotalVentasCreditoHoy(){
    return totalVentasCreditoHoy;
  }
  
  public List<Operacion> getEgresoListHoy() {
    egresoListHoy = ejbFacadeOperacion.findEgresoByFecha(fecha);
    return egresoListHoy;
  }
  
  public List<Operacion> getIngresoListHoy() {
    ingresoListHoy = ejbFacadeOperacion.findIngresoByFecha(fecha);
    
    return ingresoListHoy;
  }
  
  public List<Pago> getAdelantoListHoy(){
    adelantoListHoy = ejbFacadePago.findAdelantosByFecha(fecha); // falta filtrar por tipo = ADELANTO
    return adelantoListHoy;
  }
  
  public boolean checkVentasNoPagadasHoy(){
    List<Object[]> ventasHoy  = ejbFacadeVenta.findPagoDoctorHoy(fecha); //Ventas por doctor, pagado = false and pagarHoy = 'SI'
    if(ventasHoy.isEmpty())
      return true;
    else
      return false;
  }
  
  public List<Pago> getPagoDoctorListHoy() {
    
    //Buscar todos los pagos del día
    pagoDoctorListHoy = ejbFacadePago.findPagoDoctorHoy(this.fecha); //WHERE sv.sePago = 'SI' AND sv.pagado = true AND v.fechaHora BETWEEN :startDate AND :endDate
      
    return pagoDoctorListHoy;
  }

  public List<Pago> getDeudaDoctorListHoy() {
    List<Object[]> ventasHoy  = ejbFacadeVenta.findPagoDoctorHoy(fecha); //Ventas por doctor, pagado = false and pagarHoy = 'SI' AND estado != anulado
    
    Object[] result;
    Personal doctor;
    BigDecimal monto;
    Date fecha = new Date();
    Pago pagoDoctor;
    deudaDoctorListHoy = new ArrayList<>();
    for (int i = 0; i < ventasHoy.size(); i++){

      result = ventasHoy.get(i);
      doctor = (Personal) result[0];
      monto = (BigDecimal) result[1];

      pagoDoctor = new Pago();
      pagoDoctor.setIdUsuario(login.getUsuario());
      pagoDoctor.setIdPersonal(doctor);
      pagoDoctor.setMonto(monto.setScale(2, RoundingMode.CEILING));
      pagoDoctor.setTipo("PAGODOCTOR");
      pagoDoctor.setFechaHora(fecha);

      deudaDoctorListHoy.add(pagoDoctor);
    }
    return deudaDoctorListHoy;
  }
  
  public void getDetalleDeudaDoctorList() {
    
    if(deudaDoctorSelectedHoy != null){
      detalleDeudaDoctorListHoy = ejbFacadeServicioVenta.findDetallePagoByDoctorHoy(fecha, deudaDoctorSelectedHoy.getIdPersonal());
      System.out.println("EXITO");
    }
    System.out.println("FAIL");
    
  }

  public List<Servicioventa> getDetalleDeudaDoctorListHoy() {
    
    if(deudaDoctorSelectedHoy != null){
      detalleDeudaDoctorListHoy = ejbFacadeServicioVenta.findDetallePagoByDoctorHoy(fecha, deudaDoctorSelectedHoy.getIdPersonal());
      System.out.println("EXITO");
    }
    System.out.println("FAIL");
    
    return detalleDeudaDoctorListHoy;
    
  }
  
  
  
  
  
  public BigDecimal getTotalIngresosHoy() {
    BigDecimal total = BigDecimal.ZERO;
    if ( ingresoListHoy != null)
      for (Operacion item : ingresoListHoy) {
        total = total.add(item.getMonto());
      }
    return total.setScale(2, RoundingMode.CEILING);
  }

  public BigDecimal getTotalEgresosHoy() {
    BigDecimal total = BigDecimal.ZERO;
    if ( egresoListHoy != null)
      for (Operacion item : egresoListHoy) {
        total = total.add(item.getMonto());
      }
    return total.setScale(2, RoundingMode.CEILING);
  }
  
  public BigDecimal getTotalAdelantosHoy(){
    BigDecimal total = BigDecimal.ZERO ;
    if ( adelantoListHoy != null)
      for (Pago item : adelantoListHoy) {
        total = total.add(item.getMonto());
      }
    return total.setScale(2, RoundingMode.CEILING);
  }
  
  public BigDecimal getTotalPagosDoctorHoy() {
    BigDecimal total = BigDecimal.ZERO;
    if ( pagoDoctorListHoy != null)
      for (Pago item : pagoDoctorListHoy) {
        total = total.add(item.getMonto());
      }
    return total.setScale(2, RoundingMode.CEILING);
  }
  
  public BigDecimal getSaldoInicialHoy(){
    Saldoinicial saldoInicial = ejbFacadeSaldoinicial.findByFecha(fecha);
    if( saldoInicial == null)
      return new BigDecimal(BigInteger.ZERO).setScale(2, RoundingMode.CEILING);
    return saldoInicial.getSaldoinicial().setScale(2, RoundingMode.CEILING);
  }
  
  public void pagarDoctor(Pago pago){
    ejbFacadeServicioVenta.updatePagoByDoctorHoy(fecha, pago.getIdPersonal(), pago);
    ejbFacadePago.create(pago);
    JsfUtil.addSuccessMessage("El pago se realizó correctamente.");
  }
  
  public void posponerPagoDoctor(Pago pago){
    ejbFacadeServicioVenta.posponerPagoByDoctorHoy(fecha, pago.getIdPersonal());
  }

  public void generarPagoDoctor(){
    ejbFacadeServicioVenta.updatePagoDoctorHoy(fecha);
    for (Pago pagoDoctor : pagoDoctorListHoy) {
      ejbFacadePago.create(pagoDoctor);
    }
    JsfUtil.addSuccessMessage("Los pagos se generaron exitosamente.");
  }
  
  public void imprimirPagoDoctor(Pago pago) throws JRException, IOException, NamingException, SQLException, Exception {

    Map<String, Object> parametro = new HashMap<>();
    parametro.put("idPago", pago.getIdPago());
    parametro.put("montoLetras", CantidadLetras.convertNumberToLetter(pago.getMonto().doubleValue()));

    File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/pago/pagoDoctor.jasper"));
    
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

  public Pago getDeudaDoctorSelectedHoy() {
    return deudaDoctorSelectedHoy;
  }

  public void setDeudaDoctorSelectedHoy(Pago deudaDoctorSelectedHoy) {
    this.deudaDoctorSelectedHoy = deudaDoctorSelectedHoy;
  }
  
  
  
}
