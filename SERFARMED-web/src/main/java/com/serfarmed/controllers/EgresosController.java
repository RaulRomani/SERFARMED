package com.serfarmed.controllers;

import com.serfarmed.controllers.process.LoginController;
import com.serfarmed.entities.Egresos;
import com.serfarmed.controllers.util.JsfUtil;
import com.serfarmed.controllers.util.JsfUtil.PersistAction;
import com.serfarmed.facades.EgresosFacadeLocal;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

@ManagedBean(name = "egresosController")
@SessionScoped
public class EgresosController implements Serializable {

  @EJB
  private com.serfarmed.facades.EgresosFacadeLocal ejbFacade;
  private List<Egresos> items = null;
  private Egresos selected;
  private List<Egresos> egresoListHoy;
  
  @Inject
  private LoginController personal;

  public EgresosController() {
  }

  public Egresos getSelected() {
    return selected;
  }

  public void setSelected(Egresos selected) {
    this.selected = selected;
  }

  protected void setEmbeddableKeys() {
  }

  protected void initializeEmbeddableKey() {
  }

  private EgresosFacadeLocal getFacade() {
    return ejbFacade;
  }
  
  
  public List<Egresos> getEgresoListHoy(){
    egresoListHoy = getFacade().findByFecha(new Date());
    return egresoListHoy; 
  }
  
  public BigDecimal getTotalEgresosHoy(){
    BigDecimal total = BigDecimal.ZERO ;
    for (Egresos item : egresoListHoy) {
      total = total.add(item.getMonto());
    }
    return total;
  }

  public Egresos prepareCreate() {
    selected = new Egresos();
    initializeEmbeddableKey();
    return selected;
  }

  public void create() {
    persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("EgresosCreated"));
    if (!JsfUtil.isValidationFailed()) {
      items = null;    // Invalidate list of items to trigger re-query.
    }
  }
  
  public void crear() {
    selected.setFechaHora(new Date());
    selected.setIdUsuario(personal.getUsuario());
    
    persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("EgresosCreated"));
    if (!JsfUtil.isValidationFailed()) {
      items = null;    // Invalidate list of items to trigger re-query.
    }
  }

  public void update() {
    persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("EgresosUpdated"));
  }

  public void destroy() {
    persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("EgresosDeleted"));
    if (!JsfUtil.isValidationFailed()) {
      selected = null; // Remove selection
      items = null;    // Invalidate list of items to trigger re-query.
    }
  }

  public List<Egresos> getItems() {
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

  public List<Egresos> getItemsAvailableSelectMany() {
    return getFacade().findAll();
  }

  public List<Egresos> getItemsAvailableSelectOne() {
    return getFacade().findAll();
  }

  @FacesConverter(forClass = Egresos.class)
  public static class EgresosControllerConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
      if (value == null || value.length() == 0) {
        return null;
      }
      EgresosController controller = (EgresosController) facesContext.getApplication().getELResolver().
              getValue(facesContext.getELContext(), null, "egresosController");
      return controller.getFacade().find(getKey(value));
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
      if (object instanceof Egresos) {
        Egresos o = (Egresos) object;
        return getStringKey(o.getIdEgresos());
      } else {
        Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Egresos.class.getName()});
        return null;
      }
    }

  }

}
