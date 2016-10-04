package com.serfarmed.controllers.process;

import com.serfarmed.controllers.util.AccesoDB;
import com.serfarmed.controllers.util.CantidadLetras;
import com.serfarmed.controllers.util.JsfUtil;
import com.serfarmed.entities.Credito;
import com.serfarmed.entities.Cliente;
import com.serfarmed.entities.Personal;
import com.serfarmed.entities.Servicio;
import com.serfarmed.entities.Servicioventa;
import com.serfarmed.entities.Venta;
import com.serfarmed.entities.util.Carrito;
import com.serfarmed.entities.util.CarritoItem;
import com.serfarmed.entities.util.DoctorItem;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.inject.Inject;
import javax.naming.NamingException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JREmptyDataSource;
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
@Named(value = "ventas")
@SessionScoped
public class Ventas implements Serializable {

  /**
   * Creates a new instance of Ventas
   */
  public Ventas() {
//    FacesContext context = javax.faces.context.FacesContext.getCurrentInstance();
//    HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
//    Personal p =(Personal) session.getAttribute("personal");
//    idPersonal =p.getIdPersonal();

  }

  @EJB
  private com.serfarmed.facades.ClienteFacadeLocal ejbFacadeCliente;
  @EJB
  private com.serfarmed.facades.ServicioFacadeLocal ejbFacadeServicio;
  @EJB
  private com.serfarmed.facades.VentaFacadeLocal ejbFacadeVenta;
  @EJB
  private com.serfarmed.facades.PersonalFacadeLocal ejbFacadePersonal;

  @Inject
  private LoginController personal;

  private List<Cliente> clienteList;
  private Cliente clienteSelected;

  private List<Personal> doctorList;
  private Personal doctorSelected;

  private List<Servicio> servicioList;
  private Servicio servicioSelected = null;

  private Credito credito;

  private Carrito carrito;

  private Venta venta;
  private boolean comprobanteCredito;
  private String tipoCliente;
  private String claseCliente;
  private BigDecimal comisionMedico;
  private BigDecimal comisionDoctor;

  @edu.umd.cs.findbugs.annotations.SuppressWarnings("SE_TRANSIENT_FIELD_NOT_RESTORED")
  private transient final org.slf4j.Logger logger = LoggerFactory.getLogger(Ventas.class);

  @PostConstruct
  void init() {

    carrito = new Carrito();
    credito = new Credito();
    //Default values
    carrito.setComprobante("BOLETA");
    credito.setInicial(BigDecimal.ZERO);
    credito.setPlazo("MENSUAL");
    tipoCliente = "REGISTRADO";
    claseCliente = "Persona";
    venta = new Venta();

  }
  
  public void nuevaVenta(){
    init();
    
    clienteSelected = null;
    servicioSelected = null;
    doctorSelected = null;
    
  }

  public void agregarServicio() {
    if (doctorSelected == null) {
      JsfUtil.addWarnMessage("Primero elige un Doctor.");
      return ;
    }
    if (servicioSelected != null) {

      //BigDecimal precio = servicioSelected.getPrecio();
      BigDecimal precio = servicioSelected.getPrecio();

      if (servicioSelected.getNombre().equals("Talonario Boleta")) {
        //Hacer descuento
        //precio = precio.multiply(new BigDecimal(alto * ancho));
      }

      CarritoItem item = new CarritoItem();
      item.setIdProducto(servicioSelected.getIdServicio());
      item.setIdPersonal(doctorSelected.getIdPersonal());
      //item.setComision();
      item.setTipoComision("PORCENTAJE");
      item.setNombreDoctor(doctorSelected.getNombreCompleto());
      item.setNombreProducto(servicioSelected.getNombre());
      item.setPrecioProducto(servicioSelected.getPrecio());
      item.setComision(doctorSelected.getComision());
      item.setTipoComision(doctorSelected.getTipoComision());

      Integer cantidad = 1;
      BigDecimal importe;

      importe = precio.multiply(new BigDecimal(cantidad));

      item.setPrecioProducto(precio);
      item.setCantidad(cantidad);
      item.setImporte(importe);

      carrito.add(item);
      logger.info("Servicio agregado cantidad: " + carrito.getItems().size());
    } else {
      JsfUtil.addErrorMessage("Primero elige un Servicio.");
    }
  }

  public void eliminarServicio(CarritoItem carritoItem) {
    int index = carrito.getItems().indexOf(carritoItem);

    carrito.getItems().remove(index);
    JsfUtil.addErrorMessage("El servicio se eliminó correctamente.");
    logger.info("Eliminar servicio OK");
  }

  public void handleChangeCantidad(CarritoItem carritoItem) {
    int index = carrito.getItems().indexOf(carritoItem);
    Integer cantidad = carrito.getItems().get(index).getCantidad();
    BigDecimal precio = carrito.getItems().get(index).getPrecioProducto();
    carrito.getItems().get(index).setImporte(precio.multiply(new BigDecimal(cantidad)));
  }

  public void grabarVentaContado() throws JRException, IOException, NamingException, SQLException, Exception {

    
    if (clienteSelected == null) {
      JsfUtil.addWarnMessage("Primero agregue un cliente.");
      return ;
    }
    
    Integer idVenta;
    if (!carrito.getItems().isEmpty()) {
      idVenta = ejbFacadeVenta.grabarVentaContado(carrito, clienteSelected, personal.getUsuario());
      //reporteVentaContado(idVenta);

      //carrito = new Carrito();
      nuevaVenta();
      JsfUtil.addSuccessMessage("La venta al contado se realizo correctamente.");
      
      logger.info("SE AGREGO UNA VENTA Y SU DETALLE");
    } else {
      JsfUtil.addErrorMessage("Primero agregue servicios");
    }
  }

  public void grabarVentaCreditos() throws JRException, IOException, NamingException, SQLException, Exception {

    Integer idVenta;
    if (clienteSelected.getIdCliente() != null) {
      if (!carrito.getItems().isEmpty()) {
        if (credito.getTotalcuotas() != 0) {
          idVenta = ejbFacadeVenta.grabarVentaCreditos(carrito, clienteSelected, personal.getUsuario(), credito);
          //reporteVentaCreditos(idVenta);
          venta.setIdVenta(idVenta);

          nuevaVenta();
          JsfUtil.addSuccessMessage("La venta en cuotas se realizo correctamente.");
          logger.info("SE AGREGO UNA VENTA EN CUOTAS");
        } else {
          JsfUtil.addWarnMessage("Ingreso un numero de cuotas");
        }
      } else {
        JsfUtil.addWarnMessage("Agregue servicios");
      }
    } else {
      JsfUtil.addWarnMessage("Seleccione un cliente registrado");
    }
  }

  public void imprimirComprobanteCredito() throws IOException, NamingException, SQLException, Exception {
    Integer idVenta = venta.getIdVenta();
    reporteVentaContado(idVenta);
    logger.info("comprobante IMPRESO OK");
    //JsfUtil.addSuccessMessage("Primero registre un cliente");
  }

  public void imprimirProforma() throws JRException, IOException {

    //JRBeanCollectionDataSource beanCarritoItems = new JRBeanCollectionDataSource(carrito.getItems());
    if (!carrito.getItems().isEmpty()) {
      Map<String, Object> parametro = new HashMap<>();
      parametro.put("carrito", carrito.getItems());
      parametro.put("cliente_nombre", clienteSelected.getNombreCompleto());
      parametro.put("cliente_direccion", clienteSelected.getDireccion());
      parametro.put("cliente_DNI", clienteSelected.getDni());
      float total = Float.parseFloat(carrito.getTotal().toString());
      parametro.put("total", CantidadLetras.convertNumberToLetter(total));

      File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/ventas/proforma.jasper"));
      logger.info("OK PROFORMA REPORT ");

      //Connection con = AccesoDB.getConnection();
      //java.sql.Connection co = em.unwrap(java.sql.Connection.class);
      JasperPrint jasperPrint = JasperFillManager.fillReport(jasper.getPath(), parametro, new JREmptyDataSource());

      HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
      response.addHeader("Content-disposition", "attachment; filename=Proforma.pdf");  //Works in chrome
      try (ServletOutputStream stream = response.getOutputStream()) {
        JasperExportManager.exportReportToPdfStream(jasperPrint, stream);
        //JasperExportManager.exportReportToPdfFile(jasperPrint, "D://clientes.pdf");
        JasperPrintManager.printReport(jasperPrint, false);

        stream.flush();
        stream.close();
      }

      FacesContext.getCurrentInstance().responseComplete();

    } else {
      JsfUtil.addErrorMessage("Primero agregue servicios");
    }

  }

  public void reporteVentaContado(Integer idVenta) throws JRException, IOException, NamingException, SQLException, Exception {

    Map<String, Object> parametro = new HashMap<>();
    parametro.put("idVenta", idVenta);
    parametro.put("cliente_nombre", clienteSelected.getNombreCompleto());
    parametro.put("cliente_direccion", clienteSelected.getDireccion());
    parametro.put("cliente_DNI", clienteSelected.getDni());
    parametro.put("comprobante", carrito.getComprobante());
    float total = Float.parseFloat(carrito.getTotal().toString());
    parametro.put("total", CantidadLetras.convertNumberToLetter(total));

    File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/ventas/ventas.jasper"));
    logger.info("OK REPORTE VENTA CONTADO");

    Connection con = AccesoDB.getConnection();
    //java.sql.Connection co = em.unwrap(java.sql.Connection.class);
    JasperPrint jasperPrint = JasperFillManager.fillReport(jasper.getPath(), parametro, con);

    HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
    response.addHeader("Content-disposition", " filename=Comprobante.pdf");  //Works in chrome
    ServletOutputStream stream = response.getOutputStream();

    JasperExportManager.exportReportToPdfStream(jasperPrint, stream);
    //JasperExportManager.exportReportToPdfFile(jasperPrint, "D://clientes.pdf");
    //JasperPrintManager.printReport(jasperPrint, false);

    stream.flush();
    stream.close();

    FacesContext.getCurrentInstance().responseComplete();
  }

  public void reporteVentaCreditos(Integer idVenta) throws JRException, IOException, NamingException, SQLException, Exception {

    Map<String, Object> parametro = new HashMap<>();
    parametro.put("idCliente", clienteSelected.getIdCliente());
    parametro.put("idVenta", idVenta);

    File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/ventas/cobros.jasper"));
    logger.info("OK reporte venta cuotas ");

    Connection con = AccesoDB.getConnection();
    //java.sql.Connection co = em.unwrap(java.sql.Connection.class);
    JasperPrint jasperPrint = JasperFillManager.fillReport(jasper.getPath(), parametro, con);

    HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
    //response.addHeader("Content-disposition", "attachment; filename=ProgramacionTutores.pdf");  //se descarga solo
    response.addHeader("Content-disposition", "filename=Credito.pdf");  //Works in chrome
    ServletOutputStream stream = response.getOutputStream();

    JasperExportManager.exportReportToPdfStream(jasperPrint, stream);
    //JasperPrintManager.printReport(jasperPrint, false);

    stream.flush();
    stream.close();

    FacesContext.getCurrentInstance().responseComplete();
  }

  public void calcularImporte() {

    logger.info("Inicial: " + credito.getInicial());
    logger.info("Total de cuotas: " + credito.getTotalcuotas());
    BigDecimal total = carrito.getTotal().subtract(credito.getInicial());
    if (credito.getTotalcuotas() == 0) {
      credito.setTotalcuotas(1);
    }
    BigDecimal importe = total.divide(new BigDecimal(credito.getTotalcuotas()), 2, RoundingMode.HALF_UP);
    credito.setImporte(importe);
    logger.info("IMPORTE CALCULADO");
  }

  public List<Cliente> getClienteList() {
//    if (clienteList == null) {
    clienteList = ejbFacadeCliente.findAll();
//    }
    return clienteList;
  }

  public void prepareCliente() {
    clienteSelected = new Cliente();
  }

  public void prepareDoctor() {
    doctorSelected = new Personal();
  }

  public void registrarCliente() {
    if (!clienteSelected.getDni().isEmpty() && !clienteSelected.getNombre().isEmpty()) {
      ejbFacadeCliente.create(clienteSelected);
      //clienteSelected = ejbFacadeCliente.find(this);
      JsfUtil.addSuccessMessage("Cliente registrado exitosamente!");
    }

  }
  
  public void crearCliente() {
    ejbFacadeCliente.create(clienteSelected);
    JsfUtil.addSuccessMessage("Cliente registrado exitosamente!");

  }
  public void modificarCliente() {
    if (clienteSelected != null){
      ejbFacadeCliente.edit(clienteSelected);
      JsfUtil.addSuccessMessage("Cliente modificado exitosamente!");
    } else {
      JsfUtil.addSuccessMessage("Seleccione un cliente por favor!");
    }
  }

  public Cliente getClienteSelected() {
    return clienteSelected;
  }

  public void setClienteSelected(Cliente clienteSelected) {
    this.clienteSelected = clienteSelected;
  }

  public Carrito getCarrito() {
    return carrito;
  }

  public List<Servicio> getServicioList() {
//    if (servicioList == null) {
    servicioList = ejbFacadeServicio.findAll();
//    }
    return servicioList;
  }

  public List<Personal> getDoctorList() {
    doctorList = ejbFacadePersonal.findByCargo("Doctor");
    return doctorList;
  }

  public Servicio getServicioSelected() {
    return servicioSelected;
  }

  public void setServicioSelected(Servicio servicioSelected) {
    this.servicioSelected = servicioSelected;
  }

  public Personal getDoctorSelected() {
    return doctorSelected;
  }

  public void setDoctorSelected(Personal doctorSelected) {
    this.doctorSelected = doctorSelected;
  }

  public LoginController getPersonal() {
    return personal;
  }

  public void setPersonal(LoginController personal) {
    this.personal = personal;
  }

  public Credito getCredito() {
    return credito;
  }

  public void setCredito(Credito credito) {
    this.credito = credito;
  }

  public boolean isComprobanteCredito() {
    return comprobanteCredito;
  }

  public void setComprobanteCredito(boolean comprobanteCredito) {
    this.comprobanteCredito = comprobanteCredito;
  }

  public String getTipoCliente() {
    return tipoCliente;
  }

  public void setTipoCliente(String tipoCliente) {
    this.tipoCliente = tipoCliente;
  }

  public String getClaseCliente() {
    return claseCliente;
  }

  public void setClaseCliente(String claseCliente) {
    this.claseCliente = claseCliente;
  }
  
  

}
