package com.serfarmed.controllers;

import com.serfarmed.controllers.util.CalcularEdad;
import com.serfarmed.entities.Personal;
import com.serfarmed.controllers.util.JsfUtil;
import com.serfarmed.controllers.util.JsfUtil.PersistAction;
import com.serfarmed.facades.PersonalFacadeLocal;

import java.io.Serializable;
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

@Named("personalController")
@SessionScoped
public class PersonalController implements Serializable {

  @EJB
  private com.serfarmed.facades.PersonalFacadeLocal ejbFacade;
  private List<Personal> items = null;
  private Personal selected;

  public PersonalController() {
  }

  public Personal getSelected() {
    return selected;
  }

  public void setSelected(Personal selected) {
    this.selected = selected;
  }

  protected void setEmbeddableKeys() {
  }

  protected void initializeEmbeddableKey() {
  }

  private PersonalFacadeLocal getFacade() {
    return ejbFacade;
  }

  public Personal prepareCreate() {
    selected = new Personal();
    initializeEmbeddableKey();
    return selected;
  }

  public void create() {
    
    if ( selected.getFechaNacimiento() != null)
      selected.setEdad(CalcularEdad.Calcular(selected.getFechaNacimiento()));
    
    
    persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("PersonalCreated"));
    if (!JsfUtil.isValidationFailed()) {
      items = null;    // Invalidate list of items to trigger re-query.
    }
  }

  public void update() {
    
    if ( selected.getFechaNacimiento() != null)
      selected.setEdad(CalcularEdad.Calcular(selected.getFechaNacimiento()));
    
    persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("PersonalUpdated"));
  }

  public void destroy() {
    persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("PersonalDeleted"));
    if (!JsfUtil.isValidationFailed()) {
      selected = null; // Remove selection
      items = null;    // Invalidate list of items to trigger re-query.
    }
  }

  public List<Personal> getItems() {
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

  public Personal getPersonal(java.lang.Integer id) {
    return getFacade().find(id);
  }

  public List<Personal> getItemsAvailableSelectMany() {
    return getFacade().findAll();
  }

  public List<Personal> getItemsAvailableSelectOne() {
    return getFacade().findAll();
  }

  @FacesConverter(forClass = Personal.class)
  public static class PersonalControllerConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
      if (value == null || value.length() == 0) {
        return null;
      }
      PersonalController controller = (PersonalController) facesContext.getApplication().getELResolver().
              getValue(facesContext.getELContext(), null, "personalController");
      return controller.getPersonal(getKey(value));
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
      if (object instanceof Personal) {
        Personal o = (Personal) object;
        return getStringKey(o.getIdPersonal());
      } else {
        Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Personal.class.getName()});
        return null;
      }
    }

  }

}
