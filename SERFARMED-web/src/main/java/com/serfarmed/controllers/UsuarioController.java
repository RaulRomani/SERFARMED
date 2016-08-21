package com.serfarmed.controllers;

import com.serfarmed.controllers.process.LoginController;
import com.serfarmed.entities.Usuario;
import com.serfarmed.controllers.util.JsfUtil;
import com.serfarmed.controllers.util.JsfUtil.PersistAction;
import static com.serfarmed.controllers.util.ShaHashGeneratorApp.sha512;
import com.serfarmed.facades.UsuarioFacadeLocal;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.io.Serializable;
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
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import org.slf4j.LoggerFactory;

@Named("usuarioController")
@SessionScoped
public class UsuarioController implements Serializable {

  @EJB
  private com.serfarmed.facades.UsuarioFacadeLocal ejbFacade;
  private List<Usuario> items = null;
  private Usuario selected;
  
  private UploadedFile file;
  private StreamedContent image;
  private String pathImage;
  
  @edu.umd.cs.findbugs.annotations.SuppressWarnings("SE_TRANSIENT_FIELD_NOT_RESTORED")
  private transient final org.slf4j.Logger logger = LoggerFactory.getLogger(LoginController.class);

  @PostConstruct
  public void init() {
    file = null;
    String projectStage = FacesContext.getCurrentInstance().getApplication().getProjectStage().toString();
    if (projectStage.equals("Production")) {
      pathImage = ResourceBundle.getBundle("/setup/deploy").getString("productionUsuarioPhotoPath");
    } else if (projectStage.equals("Development")) {
      pathImage = ResourceBundle.getBundle("/setup/deploy").getString("developmentUsuarioPhotoPath");
    }
  }

  public Usuario getSelected() {
    return selected;
  }

  public void setSelected(Usuario selected) {
    this.selected = selected;
  }

  protected void setEmbeddableKeys() {
  }

  protected void initializeEmbeddableKey() {
  }

  private UsuarioFacadeLocal getFacade() {
    return ejbFacade;
  }

  public Usuario prepareCreate() {
    selected = new Usuario();
    initializeEmbeddableKey();
    return selected;
  }

  public void create() {
    
    String salt = selected.getUsername(); // username field in db
    selected.setPassword(sha512(selected.getPassword(),salt) );
    
    persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("UsuarioCreated"));
    if (!JsfUtil.isValidationFailed()) {
      items = null;    // Invalidate list of items to trigger re-query.
    }
  }

  public void update() {
    
    if (file != null) {
      uploadFoto();
    }
    
    persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("UsuarioUpdated"));
  }
  
  public void uploadFoto() {

    try {
      String fileName = selected.getIdUsuario() + ".jpg";
      
      InputStream in = file.getInputstream();
      String destination = pathImage;

      // write the inputStream to a FileOutputStream
      OutputStream out = new FileOutputStream(new File(destination + fileName));

      int read = 0;
      byte[] bytes = new byte[1024];

      while ((read = in.read(bytes)) != -1) {
        out.write(bytes, 0, read);
      }

      in.close();
      out.flush();
      out.close();
      logger.info("UPLOAD FOTO OK");

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void destroy() {
    persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("UsuarioDeleted"));
    if (!JsfUtil.isValidationFailed()) {
      selected = null; // Remove selection
      items = null;    // Invalidate list of items to trigger re-query.
    }
  }

  public List<Usuario> getItems() {
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
  
  public void prepareUpdateCredentials(){
    selected.setPassword("");
    
  }
  
  public void updateCredentials() throws FileNotFoundException {
    
    String password =selected.getPassword();
    String salt = selected.getUsername();
    selected.setPassword(sha512(password,salt));
//    if (file != null) 
//      uploadFoto();
    
    persist(JsfUtil.PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("UsuarioUpdated"));
  }
  
  public StreamedContent getImage() {

    File foto = new File(pathImage + selected.getIdUsuario() + ".jpg");
    if (foto.exists() && !foto.isDirectory()) {
      try {
        FileInputStream stream = new FileInputStream(foto);
        image = new DefaultStreamedContent(stream, "image/jpg");
      } catch (FileNotFoundException ex) {
        ex.printStackTrace();
      }
    } else { // cargar photo por defecto

    }
    return image;
  }

  public UploadedFile getFile() {
    return file;
  }

  public void setFile(UploadedFile file) {
    this.file = file;
  }
  
  
  

  public Usuario getUsuario(java.lang.Integer id) {
    return getFacade().find(id);
  }

  public List<Usuario> getItemsAvailableSelectMany() {
    return getFacade().findAll();
  }

  public List<Usuario> getItemsAvailableSelectOne() {
    return getFacade().findAll();
  }

  @FacesConverter(forClass = Usuario.class)
  public static class UsuarioControllerConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
      if (value == null || value.length() == 0) {
        return null;
      }
      UsuarioController controller = (UsuarioController) facesContext.getApplication().getELResolver().
              getValue(facesContext.getELContext(), null, "usuarioController");
      return controller.getUsuario(getKey(value));
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
      if (object instanceof Usuario) {
        Usuario o = (Usuario) object;
        return getStringKey(o.getIdUsuario());
      } else {
        Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Usuario.class.getName()});
        return null;
      }
    }

  }

}
