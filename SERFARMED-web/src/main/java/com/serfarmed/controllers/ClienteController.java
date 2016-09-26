package com.serfarmed.controllers;

import com.serfarmed.controllers.util.CalcularEdad;
import com.serfarmed.entities.Cliente;
import com.serfarmed.controllers.util.JsfUtil;
import com.serfarmed.controllers.util.JsfUtil.PersistAction;
import com.serfarmed.facades.ClienteFacadeLocal;

import java.io.Serializable;
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

@Named("clienteController")
@SessionScoped
public class ClienteController implements Serializable {

  @EJB
  private com.serfarmed.facades.ClienteFacadeLocal ejbFacade;
  private List<Cliente> items = null;
  private Cliente selected;
  
  private String tipoCliente;

  public ClienteController() {
  }
  
  @PostConstruct
  public void init(){
    tipoCliente = "Persona";
    
  }

  public Cliente getSelected() {
    return selected;
  }

  public void setSelected(Cliente selected) {
    this.selected = selected;
  }

  protected void setEmbeddableKeys() {
  }

  protected void initializeEmbeddableKey() {
  }

  private ClienteFacadeLocal getFacade() {
    return ejbFacade;
  }

  public Cliente prepareCreate() {
    selected = new Cliente();
    initializeEmbeddableKey();
    return selected;
  }

  public void create() {
    
    if ( selected.getFechaNacimiento() != null)
      selected.setEdad(CalcularEdad.Calcular(selected.getFechaNacimiento()));
    
    selected.setFechaCreacion(new Date());
    persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("ClienteCreated"));
    if (!JsfUtil.isValidationFailed()) {
      items = null;    // Invalidate list of items to trigger re-query.
    }
  }

  public void update() {
    
    if ( selected.getFechaNacimiento() != null)
      selected.setEdad(CalcularEdad.Calcular(selected.getFechaNacimiento()));
    
    persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("ClienteUpdated"));
  }

  public void destroy() {
    persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("ClienteDeleted"));
    if (!JsfUtil.isValidationFailed()) {
      selected = null; // Remove selection
      items = null;    // Invalidate list of items to trigger re-query.
    }
  }

  public List<Cliente> getItems() {
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

  public Cliente getCliente(java.lang.Integer id) {
    return getFacade().find(id);
  }

  public List<Cliente> getItemsAvailableSelectMany() {
    return getFacade().findAll();
  }

  public List<Cliente> getItemsAvailableSelectOne() {
    return getFacade().findAll();
  }

  public String getTipoCliente() {
    return tipoCliente;
  }

  public void setTipoCliente(String tipoCliente) {
    this.tipoCliente = tipoCliente;
  }
  
  

  @FacesConverter(forClass = Cliente.class)
  public static class ClienteControllerConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
      if (value == null || value.length() == 0) {
        return null;
      }
      ClienteController controller = (ClienteController) facesContext.getApplication().getELResolver().
              getValue(facesContext.getELContext(), null, "clienteController");
      return controller.getCliente(getKey(value));
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
      if (object instanceof Cliente) {
        Cliente o = (Cliente) object;
        return getStringKey(o.getIdCliente());
      } else {
        Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Cliente.class.getName()});
        return null;
      }
    }

  }

}
