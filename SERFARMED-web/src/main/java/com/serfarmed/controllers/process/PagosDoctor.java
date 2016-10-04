package com.serfarmed.controllers.process;

import com.serfarmed.controllers.util.JsfUtil;
import com.serfarmed.entities.Personal;
import com.serfarmed.entities.Pago;
import com.serfarmed.entities.Servicioventa;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.inject.Inject;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Raul
 */
@Named(value = "pagosDoctor")
@SessionScoped
public class PagosDoctor implements Serializable {

  @EJB
  private com.serfarmed.facades.VentaFacadeLocal ejbFacadeVenta;
  @EJB
  private com.serfarmed.facades.ServicioventaFacadeLocal ejbFacadeServicioVenta;
  @EJB
  private com.serfarmed.facades.PagoFacadeLocal ejbFacadePago;

  @Inject
  private LoginController login;

  private List<Pago> pagoDoctorListHoy;
  private List<Pago> deudaDoctorListHoy;
  private List<Pago> deudaAntDoctorList;
  
  private Pago deudaDoctorSelectedHoy;
  private Pago deudaAntDoctorSelectedHoy;
  private Pago pagoDoctorSelectedHoy;

  private List<Servicioventa> detalleDeudaDoctorListHoy;
  private List<Servicioventa> detalleDeudaAntDoctorListHoy;
  private List<Servicioventa> detallePagoDoctorListHoy;
  

  private Date fecha;

  @PostConstruct
  private void Once() {
    fecha = new Date();
  }

  @edu.umd.cs.findbugs.annotations.SuppressWarnings("SE_TRANSIENT_FIELD_NOT_RESTORED")
  private transient final org.slf4j.Logger logger = LoggerFactory.getLogger(PagosDoctor.class);

  //preRenderView
  public void init() {
    //getPagoDoctorListHoy();
    deudaDoctorListHoy = null;
    deudaAntDoctorList = null;
    deudaDoctorSelectedHoy = null;
    deudaAntDoctorSelectedHoy = null;
    pagoDoctorSelectedHoy = null;
    System.out.println("PRE RENDER VIEW PAGOS DOCTOR");
  }

  public List<Pago> getPagoDoctorListHoy() {

    //Buscar todos los pagos del día
    pagoDoctorListHoy = ejbFacadePago.findPagoDoctorHoy(this.fecha); //WHERE sv.sePago = 'SI' AND sv.pagado = true AND v.fechaHora BETWEEN :startDate AND :endDate

    return pagoDoctorListHoy;
  }

  public List<Pago> getDeudaDoctorListHoy() {

    if (deudaDoctorListHoy == null) {
      deudaDoctorList();
    }
    return deudaDoctorListHoy;
  }
  
  private void deudaDoctorList(){
    List<Object[]> ventasHoy = ejbFacadeVenta.findPagoDoctorHoy(fecha); //Ventas por doctor, pagado = false and pagarHoy = 'SI' AND estado != anulado

      Object[] result;
      Personal doctor;
      BigDecimal monto;
      Date fecha = new Date();
      Pago pagoDoctor;
      deudaDoctorListHoy = new ArrayList<>();
      for (int i = 0; i < ventasHoy.size(); i++) {

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
  }

  public List<Servicioventa> getDetalleDeudaDoctorListHoy() {
    if (deudaDoctorSelectedHoy != null) {
      detalleDeudaDoctorListHoy = ejbFacadeServicioVenta.findDetallePagoByDoctorHoy(fecha, deudaDoctorSelectedHoy.getIdPersonal());
    }
    return detalleDeudaDoctorListHoy;
  }

  public List<Servicioventa> getDetalleDeudaAntDoctorListHoy() {
    if (deudaAntDoctorSelectedHoy != null) {
      detalleDeudaAntDoctorListHoy = ejbFacadeServicioVenta.findDetalleDeudaAntByDoctorHoy(fecha, deudaAntDoctorSelectedHoy.getIdPersonal());
    }
    return detalleDeudaAntDoctorListHoy;
  }

  public List<Servicioventa> getDetallePagoDoctorListHoy() {
    if (pagoDoctorSelectedHoy != null) {
      detallePagoDoctorListHoy = ejbFacadeServicioVenta.findDetallePagoHoy(fecha, pagoDoctorSelectedHoy);
    }
    return detallePagoDoctorListHoy;
  }
  
  public void checkDeudaSelected(){
    if (deudaDoctorSelectedHoy == null) 
      JsfUtil.addWarnMessage("Primero tiene que seleccionar un registro.");
  }
  
  public void checkDeudaAntSelected(){
    if (deudaAntDoctorSelectedHoy == null) 
      JsfUtil.addWarnMessage("Primero tiene que seleccionar un registro.");
  }
  public void checkPagoSelected(){
    if (pagoDoctorSelectedHoy == null) 
      JsfUtil.addWarnMessage("Primero tiene que seleccionar un registro.");
  }
  
  

  public void pagarDoctor() {
    if (deudaDoctorSelectedHoy != null) {
      ejbFacadePago.create(deudaDoctorSelectedHoy);
      logger.info("IdPago: " + deudaDoctorSelectedHoy.getIdPago());
      ejbFacadeServicioVenta.updatePagoByDoctorHoy(fecha, deudaDoctorSelectedHoy.getIdPersonal(), deudaDoctorSelectedHoy);
      logger.info("Nro de comprobante: " + deudaDoctorSelectedHoy.getNroComprobante());
      logger.info("Descripción: " + deudaDoctorSelectedHoy.getDescripcion());

      //Limpiar deuda selected
      deudaDoctorSelectedHoy = null;
      //Refrescar lista de deudas
      deudaDoctorList();

      JsfUtil.addSuccessMessage("El pago se realizó correctamente.");
    } else {
      JsfUtil.addWarnMessage("Primero seleccione un registro.");
    }
  }

  public void pagarDoctor(Pago pago) {

    ejbFacadePago.create(pago);
    logger.info("IdPago: " + pago.getIdPago());
    ejbFacadeServicioVenta.updatePagoByDoctorHoy(fecha, pago.getIdPersonal(), pago);
    logger.info("Nro de comprobante: " + pago.getNroComprobante());
    logger.info("Descripción: " + pago.getDescripcion());
    JsfUtil.addSuccessMessage("El pago se realizó correctamente.");
  }

  public void posponerPagoDoctor(Pago pago) {
    ejbFacadeServicioVenta.posponerPagoByDoctorHoy(fecha, pago.getIdPersonal());
    JsfUtil.addSuccessMessage("El pago se pospuso correctamente.");
    
    //refresh de ambas listas de deuda
    deudaAntDoctorList();
    deudaDoctorList();
    
  }

  public BigDecimal getTotalPagosDoctorHoy() {
    BigDecimal total = BigDecimal.ZERO;
    if (pagoDoctorListHoy != null) {
      for (Pago item : pagoDoctorListHoy) {
        total = total.add(item.getMonto());
      }
    }
    return total.setScale(2, RoundingMode.CEILING);
  }

  public List<Pago> getDeudaAntPagoDoctorList() {

    if (deudaAntDoctorList == null) {
      deudaAntDoctorList();
    }
    return deudaAntDoctorList;
  }
  private void deudaAntDoctorList(){
    List<Object[]> ventasMensual = ejbFacadeVenta.findDeudaAntPagoDoctor(fecha); //Ventas por doctor, pagado = false and pagarMensual = 'SI'

      Object[] result;
      Personal doctor;
      BigDecimal monto;
      Date fecha = new Date();
      Pago pagoDoctor;
      deudaAntDoctorList = new ArrayList<>();
      if (ventasMensual.size() > 0) {
        for (int i = 0; i < ventasMensual.size(); i++) {

          result = ventasMensual.get(i);
          doctor = (Personal) result[0];
          monto = (BigDecimal) result[1];

          pagoDoctor = new Pago();
          pagoDoctor.setIdUsuario(login.getUsuario());
          pagoDoctor.setIdPersonal(doctor);
          pagoDoctor.setMonto(monto.setScale(2, RoundingMode.CEILING));
          pagoDoctor.setTipo("PAGODOCTOR");
          pagoDoctor.setFechaHora(fecha);

          deudaAntDoctorList.add(pagoDoctor);
        }
      }
  }

  public void pagarDeudaAnteriorDoctor() {

    if (deudaAntDoctorSelectedHoy != null) {
    ejbFacadePago.create(deudaAntDoctorSelectedHoy);
    logger.info("IdPago: " + deudaAntDoctorSelectedHoy.getIdPago());
    ejbFacadeServicioVenta.updatePagoAnteriorByDoctor(fecha, deudaAntDoctorSelectedHoy.getIdPersonal(), deudaAntDoctorSelectedHoy); // change sePago = 'SI' AND pagado = true

    logger.info("Nro de comprobante: " + deudaAntDoctorSelectedHoy.getNroComprobante());
    logger.info("Descripción: " + deudaAntDoctorSelectedHoy.getDescripcion());
    
    
    
    //Limpiar deuda selected
      deudaAntDoctorSelectedHoy = null;
    //Refrescar lista de deuda anterior
      deudaAntDoctorList();
    JsfUtil.addSuccessMessage("El pago se realizó exitosamente.");
    } else {
      JsfUtil.addWarnMessage("Primero seleccione un registro.");
    }
  }

  public void handleChangeVariables(Pago pago) {

    int index = deudaDoctorListHoy.indexOf(pago);

    logger.info("Nro de comprobante: " + pago.getNroComprobante());
    logger.info("Descripción: " + pago.getDescripcion());

    //deudaDoctorListHoy.get(index).setDescripcion(pago.getDescripcion());
    //deudaDoctorListHoy.get(index).setNroComprobante(pago.getNroComprobante());
  }

  public Pago getDeudaDoctorSelectedHoy() {
    return deudaDoctorSelectedHoy;
  }

  public void setDeudaDoctorSelectedHoy(Pago deudaDoctorSelectedHoy) {
    this.deudaDoctorSelectedHoy = deudaDoctorSelectedHoy;
  }

  public Pago getDeudaAntDoctorSelectedHoy() {
    return deudaAntDoctorSelectedHoy;
  }

  public void setDeudaAntDoctorSelectedHoy(Pago deudaAntDoctorSelectedHoy) {
    this.deudaAntDoctorSelectedHoy = deudaAntDoctorSelectedHoy;
  }

  public Date getFecha() {
    return fecha;
  }

  public void setFecha(Date fecha) {
    this.fecha = fecha;
  }

  public Pago getPagoDoctorSelectedHoy() {
    return pagoDoctorSelectedHoy;
  }

  public void setPagoDoctorSelectedHoy(Pago pagoDoctorSelectedHoy) {
    this.pagoDoctorSelectedHoy = pagoDoctorSelectedHoy;
  }
  
  

}
