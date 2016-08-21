package com.serfarmed.controllers;

import com.serfarmed.entities.Venta;
import com.serfarmed.controllers.util.JsfUtil;
import com.serfarmed.controllers.util.JsfUtil.PersistAction;
import com.serfarmed.facades.VentaFacadeLocal;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@Named("ventaController")
@SessionScoped
public class VentaController implements Serializable {

  @EJB
  private com.serfarmed.facades.VentaFacadeLocal ejbFacade;
  private List<Venta> items = null;
  private Venta selected;
  private List<Venta> ventaListHoy;
  
  private Date fecha;

  @PostConstruct
  void init() {
    fecha = new Date();
  }

  public Venta getSelected() {
    return selected;
  }

  public void setSelected(Venta selected) {
    this.selected = selected;
  }

  protected void setEmbeddableKeys() {
  }

  protected void initializeEmbeddableKey() {
  }

  private VentaFacadeLocal getFacade() {
    return ejbFacade;
  }

  public Venta prepareCreate() {
    selected = new Venta();
    initializeEmbeddableKey();
    return selected;
  }

  public void create() {
    persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("VentaCreated"));
    if (!JsfUtil.isValidationFailed()) {
      items = null;    // Invalidate list of items to trigger re-query.
    }
  }

  public void update() {
    persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("VentaUpdated"));
  }

  public void destroy() {
    persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("VentaDeleted"));
    if (!JsfUtil.isValidationFailed()) {
      selected = null; // Remove selection
      items = null;    // Invalidate list of items to trigger re-query.
    }
  }

  public List<Venta> getItems() {
    if (items == null) {
      items = getFacade().findAll();
    }
    return items;
  }

  private void persist(PersistAction persistAction, String successMessage) {
    if (selected != null) {
      setEmbeddableKeys();
      try {
        if (persistAction != PersistAction.DELETE) {
          getFacade().edit(selected);
        } else {
          getFacade().remove(selected);
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

  public Venta getVenta(java.lang.Integer id) {
    return getFacade().find(id);
  }
  
  public List<Venta> getVentaListHoy() {
    ventaListHoy = getFacade().findByFecha(fecha);
    return ventaListHoy;
  }
  
  public BigDecimal getComisionClinicaHoy(){
    
    BigDecimal total = BigDecimal.ZERO ;
    for (Venta item : ventaListHoy) 
      if (item.getEstado().equals("CANCELADO"))
        total = total.add(item.getComisionClinica());
    return total;
  }
  
  
  public BigDecimal getComisionMedicoHoy(){
    
    BigDecimal total = BigDecimal.ZERO ;
    for (Venta item : ventaListHoy) 
      if (item.getEstado().equals("CANCELADO"))
        total = total.add(item.getComisionMedico());
    return total;
  }
  
  public BigDecimal getTotalVentasContadoHoy(){
    
    BigDecimal total = BigDecimal.ZERO ;
    for (Venta item : ventaListHoy) 
      if (item.getEstado().equals("CANCELADO"))
        total = total.add(item.getTotal());
    return total;
  }
  
  public BigDecimal getTotalVentasCreditoHoy(){
    BigDecimal total = BigDecimal.ZERO ;
    for (Venta item : ventaListHoy) {
      if (item.getEstado().equals( "CREDITO")){
        total = total.add(item.getTotal());
      } 
    }
    return total;
  }

  public List<Venta> getItemsAvailableSelectMany() {
    return getFacade().findAll();
  }

  public List<Venta> getItemsAvailableSelectOne() {
    return getFacade().findAll();
  }

  @FacesConverter(forClass = Venta.class)
  public static class VentaControllerConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
      if (value == null || value.length() == 0) {
        return null;
      }
      VentaController controller = (VentaController) facesContext.getApplication().getELResolver().
              getValue(facesContext.getELContext(), null, "ventaController");
      return controller.getVenta(getKey(value));
    }

    java.lang.Integer getKey(String value) {
      java.lang.Integer key;
      key = Integer.valueOf(value);
      return key;
    }

    String getStringKey(java.lang.Integer value) {
      StringBuilder sb = new StringBuilder();
      sb.append(value);
      return sb.toString();
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
      if (object == null) {
        return null;
      }
      if (object instanceof Venta) {
        Venta o = (Venta) object;
        return getStringKey(o.getIdVenta());
      } else {
        Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Venta.class.getName()});
        return null;
      }
    }
    
    

  }

  public Date getFecha() {
    return fecha;
  }

  public void setFecha(Date fecha) {
    this.fecha = fecha;
  }
  
  
  

}
