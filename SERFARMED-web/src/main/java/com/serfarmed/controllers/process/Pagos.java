package com.serfarmed.controllers.process;

import com.serfarmed.controllers.util.AccesoDB;
import com.serfarmed.controllers.util.JsfUtil;
import com.serfarmed.entities.Personal;
import com.serfarmed.entities.Pago;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
@Named(value = "pagos")
@SessionScoped
public class Pagos implements Serializable {

  @EJB
  private com.serfarmed.facades.PersonalFacadeLocal ejbFacadePersonal;
  @EJB
  private com.serfarmed.facades.ServicioFacadeLocal ejbFacadeProducto;
  @EJB
  private com.serfarmed.facades.PagoFacadeLocal ejbFacadePago;

  @Inject
  private LoginController personal;

  private List<Personal> personalList = null;
  private Personal personalSelected;

  private List<Pago> pagoList = null;
  private Pago pagoSelected;

  private String tipoPersonal;

  private String mes;

  @edu.umd.cs.findbugs.annotations.SuppressWarnings("SE_TRANSIENT_FIELD_NOT_RESTORED")
  private transient final org.slf4j.Logger logger = LoggerFactory.getLogger(Pagos.class);

  @PostConstruct
  void init() {
    //Default values
    Date fechaActual = new Date();
    SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
    mes = monthFormat.format(fechaActual);
  }

  public void reportePedidoPago(Integer idPago) throws JRException, IOException, NamingException, SQLException, Exception {

    Map<String, Object> parametro = new HashMap<>();
    parametro.put("idPago", idPago);
    parametro.put("personal_direccion", personalSelected.getDireccion());

    File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/pagos/pagos.jasper"));
    logger.info("OK REPORTE PEDIDO DE COMPRA");

    Connection con = AccesoDB.getConnection();
    //java.sql.Connection co = em.unwrap(java.sql.Connection.class);
    JasperPrint jasperPrint = JasperFillManager.fillReport(jasper.getPath(), parametro, con);

    HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
    response.addHeader("Content-disposition", "attachment; filename=Proforma-" + personalSelected.getDni() + ".pdf");  //Works in chrome
    ServletOutputStream stream = response.getOutputStream();

    JasperExportManager.exportReportToPdfStream(jasperPrint, stream);
    //JasperExportManager.exportReportToPdfFile(jasperPrint, "D://personals.pdf");
    JasperPrintManager.printReport(jasperPrint, false);

    stream.flush();
    stream.close();

    FacesContext.getCurrentInstance().responseComplete();
  }

  public List<Personal> getPersonalList() {
    if (personalList == null) {
      personalList = ejbFacadePersonal.findAll();
      logger.info("GET PERSONAL LIST OK");
    }
    return personalList;
  }

  public List<Pago> getPagoList() {
//    if (pagoList == null && personalSelected.getIdPersonal() != null) {
//      pagoList = ejbFacadePago.findByPersonal(personalSelected);
//      //logger.info("GET PAGO LIST OK" + pagoList + " size:" +pagoList.size());
//      logger.info("GET PAGO LIST OK");
//    }
    return pagoList;
  }

  public void loadAdelantos() {

    if (personalSelected.getIdPersonal() != null) {
      pagoList = ejbFacadePago.findAdelantosByPersonal(personalSelected, mes);
      //logger.info("GET PAGO LIST OK" + pagoList + " size:" +pagoList.size());
      logger.info("GET PAGO LIST OK");
    } else {
      JsfUtil.addErrorMessage("Seleccione un personal");
    }

  }

  public void prepareCreatePago() {
    pagoSelected = new Pago();
    pagoSelected.setTipo("ADELANTO");
  }

  public void prepareFindPersonal() {
    personalList = null; // to requery list
  }

  public void createPago() {
    pagoSelected.setIdPersonal(personalSelected);
    pagoSelected.setIdUsuario(personal.getUsuario());
    persistPago(pagoSelected, JsfUtil.PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("PagoCreated"));
    if (!JsfUtil.isValidationFailed()) {
      // pagoList = null;    // Invalidate list of items to trigger re-query.

      pagoList = ejbFacadePago.findAdelantosByPersonal(personalSelected, mes);
      logger.info("Invalidate list of items to trigger re-query.");

    }
    //loadAdelantos();
    logger.info("Pago create OK.");
  }

  public void updatePago() {
    persistPago(pagoSelected, JsfUtil.PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("PagoUpdated"));
  }

  public void destroyPago() {
    persistPago(pagoSelected, JsfUtil.PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("PagoDeleted"));
    if (!JsfUtil.isValidationFailed()) {
      pagoSelected = null; // Remove selection
      pagoList = null;    // Invalidate list of items to trigger re-query.
    }
  }

  private void persistPago(Pago pagoSelected, JsfUtil.PersistAction persistAction, String successMessage) {
    if (pagoSelected != null) {
      try {
        if (persistAction != JsfUtil.PersistAction.DELETE) {
          ejbFacadePago.edit(pagoSelected);
        } else {
          ejbFacadePago.remove(pagoSelected);
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

  public BigDecimal getTotalAdelanto() {
    BigDecimal total = new BigDecimal(BigInteger.ZERO);

    if (pagoList != null) {
      for (Pago pago : pagoList) {
        total = total.add(pago.getMonto());
      }
    }
    return total;
  }

  public void pagarMensualidad() throws ParseException {
    Pago pago = new Pago();
    pago.setIdPersonal(personalSelected);
    pago.setIdUsuario(personal.getUsuario());
    pago.setMonto(personalSelected.getSueldo().subtract(getTotalAdelanto()));
    pago.setDescripcion("");
    pago.setTipo("MENSUALIDAD");

    // set fecha hora
    Date fecha = new Date();
    SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");

    //Pagar a fin de mes
    String fechaHora;
    if (mes.equals("02")) {
      fechaHora = yearFormat.format(fecha) + "-" + mes + "-29 12:00:00";
    } else if (mes.equals("04") || mes.equals("06") || mes.equals("09") || mes.equals("11")) {
      fechaHora = yearFormat.format(fecha) + "-" + mes + "-30 12:00:00";
    } else {
      fechaHora = yearFormat.format(fecha) + "-" + mes + "-31 12:00:00";
    }

    pago.setFechaHora(dtFormat.parse(fechaHora));

    persistPago(pago, JsfUtil.PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("PagoCreated"));
    if (!JsfUtil.isValidationFailed()) {
      pagoList = ejbFacadePago.findAdelantosByPersonal(personalSelected, mes);
      logger.info("Invalidate list of items to trigger re-query.");
    }
    //loadAdelantos();
    logger.info("Pago MENSUALIDAD create OK.");
  }

  public Personal getPersonalSelected() {
    return personalSelected;
  }

  public void setPersonalSelected(Personal personalSelected) {
    this.personalSelected = personalSelected;
  }

  public LoginController getPersonal() {
    return personal;
  }

  public void setPersonal(LoginController personal) {
    this.personal = personal;
  }

  public String getTipoPersonal() {
    return tipoPersonal;
  }

  public void setTipoPersonal(String tipoPersonal) {
    this.tipoPersonal = tipoPersonal;
  }

  public Pago getPagoSelected() {
    return pagoSelected;
  }

  public void setPagoSelected(Pago pagoSelected) {
    this.pagoSelected = pagoSelected;
  }

  public String getMes() {
    return mes;
  }

  public void setMes(String mes) {
    this.mes = mes;
  }

}
