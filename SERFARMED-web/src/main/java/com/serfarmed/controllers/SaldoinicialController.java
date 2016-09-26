package com.serfarmed.controllers;

import com.serfarmed.entities.Saldoinicial;
import com.serfarmed.controllers.util.JsfUtil;
import com.serfarmed.controllers.util.JsfUtil.PersistAction;
import com.serfarmed.facades.SaldoinicialFacadeLocal;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
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

@ManagedBean(name = "saldoinicialController")
@SessionScoped
public class SaldoinicialController implements Serializable {

  @EJB
  private com.serfarmed.facades.SaldoinicialFacadeLocal ejbFacade;
  private List<Saldoinicial> items = null;
  private Saldoinicial selected;

  public SaldoinicialController() {
  }

  public Saldoinicial getSelected() {
    return selected;
  }

  public void setSelected(Saldoinicial selected) {
    this.selected = selected;
  }

  protected void setEmbeddableKeys() {
  }

  protected void initializeEmbeddableKey() {
  }

  private SaldoinicialFacadeLocal getFacade() {
    return ejbFacade;
  }

  public Saldoinicial prepareCreate() {
    selected = new Saldoinicial();
    initializeEmbeddableKey();
    return selected;
  }

  public void create() {
    persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("SaldoinicialCreated"));
    if (!JsfUtil.isValidationFailed()) {
      items = null;    // Invalidate list of items to trigger re-query.
    }
  }
  public void actualizar() {
    selected.setFecha(new Date());
    persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("SaldoinicialUpdated"));
  }

  public void update() {
    persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("SaldoinicialUpdated"));
  }

  public void destroy() {
    persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("SaldoinicialDeleted"));
    if (!JsfUtil.isValidationFailed()) {
      selected = null; // Remove selection
      items = null;    // Invalidate list of items to trigger re-query.
    }
  }
  
  public void checkSaldoInicialHoy() {
    
    selected = ejbFacade.findByFecha( new Date());
    if (selected == null)
      selected = new Saldoinicial();
//    
//    return saldoInicial.getSaldoinicial();
  }

  public List<Saldoinicial> getItems() {
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

  public List<Saldoinicial> getItemsAvailableSelectMany() {
    return getFacade().findAll();
  }

  public List<Saldoinicial> getItemsAvailableSelectOne() {
    return getFacade().findAll();
  }

  @FacesConverter(forClass = Saldoinicial.class)
  public static class SaldoinicialControllerConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
      if (value == null || value.length() == 0) {
        return null;
      }
      SaldoinicialController controller = (SaldoinicialController) facesContext.getApplication().getELResolver().
              getValue(facesContext.getELContext(), null, "saldoinicialController");
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
      if (object instanceof Saldoinicial) {
        Saldoinicial o = (Saldoinicial) object;
        return getStringKey(o.getIdSaldoinicial());
      } else {
        Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Saldoinicial.class.getName()});
        return null;
      }
    }

  }

}
