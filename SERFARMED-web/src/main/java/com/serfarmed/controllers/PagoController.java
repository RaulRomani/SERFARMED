package com.serfarmed.controllers;

import com.serfarmed.controllers.process.Pagos;
import com.serfarmed.entities.Pago;
import com.serfarmed.controllers.util.JsfUtil;
import com.serfarmed.controllers.util.JsfUtil.PersistAction;
import com.serfarmed.facades.PagoFacadeLocal;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.slf4j.LoggerFactory;

@Named("pagoController")
@SessionScoped
public class PagoController implements Serializable {

  @EJB
  private com.serfarmed.facades.PagoFacadeLocal ejbFacade;
  private List<Pago> items = null;
  private Pago selected;
  
  private List<Pago> pagoListHoy;
  
  @edu.umd.cs.findbugs.annotations.SuppressWarnings("SE_TRANSIENT_FIELD_NOT_RESTORED")
  private transient final org.slf4j.Logger logger = LoggerFactory.getLogger(Pagos.class);

  public PagoController() {
  }

  public Pago getSelected() {
    return selected;
  }

  public void setSelected(Pago selected) {
    this.selected = selected;
  }

  protected void setEmbeddableKeys() {
  }

  protected void initializeEmbeddableKey() {
  }

  private PagoFacadeLocal getFacade() {
    return ejbFacade;
  }
  
  public List<Pago> getPagoListHoy(){
    pagoListHoy = getFacade().findByFecha(new Date());
    return pagoListHoy;
  }
  
  public BigDecimal getTotalPagosHoy(){
    BigDecimal total = BigDecimal.ZERO ;
    for (Pago item : pagoListHoy) {
      total = total.add(item.getMonto());
    }
    return total;
  }

  public Pago prepareCreate() {
    selected = new Pago();
    initializeEmbeddableKey();
    return selected;
  }

  public void create() {
    persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("PagoCreated"));
    if (!JsfUtil.isValidationFailed()) {
      items = null;    // Invalidate list of items to trigger re-query.
    }
  }

  public void update() {
    persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("PagoUpdated"));
  }

  public void destroy() {
    persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("PagoDeleted"));
    if (!JsfUtil.isValidationFailed()) {
      selected = null; // Remove selection
      items = null;    // Invalidate list of items to trigger re-query.
    }
  }

  public List<Pago> getItems() {
    if (items == null) {
      items = getFacade().findAll();
    }
    logger.info("GET ITEMS PAGO OK");
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

  public Pago getPago(java.lang.Integer id) {
    return getFacade().find(id);
  }

  public List<Pago> getItemsAvailableSelectMany() {
    return getFacade().findAll();
  }

  public List<Pago> getItemsAvailableSelectOne() {
    return getFacade().findAll();
  }

  @FacesConverter(forClass = Pago.class)
  public static class PagoControllerConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
      if (value == null || value.length() == 0) {
        return null;
      }
      PagoController controller = (PagoController) facesContext.getApplication().getELResolver().
              getValue(facesContext.getELContext(), null, "pagoController");
      return controller.getPago(getKey(value));
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
      if (object instanceof Pago) {
        Pago o = (Pago) object;
        return getStringKey(o.getIdPago());
      } else {
        Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Pago.class.getName()});
        return null;
      }
    }

  }

}
