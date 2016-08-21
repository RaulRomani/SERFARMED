/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.serfarmed.facades;

import com.serfarmed.entities.Cliente;
import com.serfarmed.entities.Comision;
import com.serfarmed.entities.Credito;
import com.serfarmed.entities.Personal;
import com.serfarmed.entities.Servicio;
import com.serfarmed.entities.Servicioventa;
import com.serfarmed.entities.Usuario;
import com.serfarmed.entities.Venta;
import com.serfarmed.entities.util.Carrito;
import com.serfarmed.entities.util.CarritoItem;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

/**
 *
 * @author Raul
 */
@Stateless
public class VentaFacade extends AbstractFacade<Venta> implements VentaFacadeLocal {

  @PersistenceContext(unitName = "com.serfarmed_Serfarmed-ejb_ejb_1.0PU")
  private EntityManager em;

  @EJB
  private com.serfarmed.facades.ServicioFacadeLocal ejbFacadeProducto;

  @Override
  protected EntityManager getEntityManager() {
    return em;
  }

  public VentaFacade() {
    super(Venta.class);
  }
  
//  public BigInteger getTotal(Strin mes){
//    Query q = em.createQuery ("SELECT AVG(x.price) FROM Magazine x");
//    BigInteger result = (BigInteger) q.getSingleResult ();
//    return result;
//  }
  
  
  @Override
  public List<Object[]> ventasMensualesByServicioDoctor(String servicio, String año,String mes) {
    
    TypedQuery<Object[]> q = getEntityManager().createNamedQuery("Venta.findVentasMensualesByServicioDoctor", Object[].class);
    
    SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
    
    //SimpleDateFormat fechaFormat = new SimpleDateFormat("yyyy-MM-dd"); 
    
    String startDate = año + "-" + mes + "-01 00:00:01";
    String endDate;
    if(!mes.equals("12")){
      Integer endMes = Integer.parseInt(mes)+1;
       endDate = año + "-" + endMes + "-01 00:00:01";
    } else {
      Integer endAño = Integer.parseInt(año)+1;
      endDate = endAño + "-" + mes + "-01 00:00:01";
    }
    
    try {
      q.setParameter("estado", "CANCELADO");
      q.setParameter("startDate", dtFormat.parse(startDate) , TemporalType.TIMESTAMP );
      q.setParameter("endDate", dtFormat.parse(endDate) , TemporalType.TIMESTAMP );
      q.setParameter("servicio", servicio);
      
    } catch (ParseException ex) {
      System.out.println(ex.getMessage());
    }
    
    List<Object[]> list;
    
    try {
      list = q.getResultList();
      
      
    } catch (NoResultException e) {
      list = null;
    }
    return list;
    
  }
  
  @Override
  public List<Object[]> ventasMensualesByServicio(String año,String mes) {
    
    TypedQuery<Object[]> q = getEntityManager().createNamedQuery("Venta.findVentasMensualesByServicio", Object[].class);
    
    SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
    
    //SimpleDateFormat fechaFormat = new SimpleDateFormat("yyyy-MM-dd"); 
    
    String startDate = año + "-" + mes + "-01 00:00:01";
    String endDate;
    if(!mes.equals("12")){
      Integer endMes = Integer.parseInt(mes)+1;
       endDate = año + "-" + endMes + "-01 00:00:01";
    } else {
      Integer endAño = Integer.parseInt(año)+1;
      endDate = endAño + "-" + mes + "-01 00:00:01";
    }
    
    try {
      q.setParameter("estado", "CANCELADO");
      q.setParameter("startDate", dtFormat.parse(startDate) , TemporalType.TIMESTAMP );
      q.setParameter("endDate", dtFormat.parse(endDate) , TemporalType.TIMESTAMP );
      
    } catch (ParseException ex) {
      System.out.println(ex.getMessage());
    }
    
    List<Object[]> list;
    
    try {
      list = q.getResultList();
      
      
    } catch (NoResultException e) {
      list = null;
    }
    return list;
    
  }
  
  @Override
  public List<Venta> findByFecha(Date fecha) {

    TypedQuery<Venta> q = getEntityManager().createNamedQuery("Venta.findByFecha", Venta.class);
    
    SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
    SimpleDateFormat fechaFormat = new SimpleDateFormat("yyyy-MM-dd"); 
    
    String startDate = fechaFormat.format(fecha) + " 00:00:01";
    String endDate = fechaFormat.format(fecha) + " 23:59:59";
    
    
    
    try {
      q.setParameter("startDate", dtFormat.parse(startDate) , TemporalType.TIMESTAMP );
      q.setParameter("endDate", dtFormat.parse(endDate) , TemporalType.TIMESTAMP );
      
      System.out.println(startDate);
      System.out.println(endDate);
    } catch (ParseException ex) {
      System.out.println(ex.getMessage());
    }
    
    List<Venta> list;
    
    try {
      list = q.getResultList();
      
      
    } catch (NoResultException e) {
      list = null;
    }
    return list;
  }

  @Override
  public Integer grabarVentaContado(Carrito carrito, Cliente cliente, Usuario usuario) {

    Venta venta = new Venta();
    venta.setComprobante(carrito.getComprobante());
    venta.setFormapago("CONTADO");
    venta.setFechaHora(new Date());
    venta.setDescuento(BigDecimal.ZERO);
    venta.setSubtotal(carrito.getTotal());
    venta.setTotal(carrito.getTotal());
    if(cliente.getIdCliente() != null) // si es cliente registrado
      venta.setIdCliente(cliente);
    venta.setIdUsuario(usuario);
    venta.setEstado("CANCELADO"); 

    try {
      em.persist(venta);
      em.flush();
      List<Servicioventa> servicioVentaList = new ArrayList<>();
      Servicioventa servicioVenta;

      for (CarritoItem i : carrito.getItems()) {
        servicioVenta = new Servicioventa();
        servicioVenta.setCantidad(i.getCantidad());
        servicioVenta.setImporte(i.getImporte());
        servicioVenta.setIdServicio(new Servicio(i.getIdProducto()));
        servicioVenta.setIdComision(new Comision(i.getIdComision()));
        servicioVenta.setIdVenta(venta); //para persistencia 1 a muchos

        //servicioVenta.setProductoventaPK(new ProductoventaPK(venta.getIdVenta(), i.getIdProducto()));
        servicioVentaList.add(servicioVenta);

      }
      venta.setProductoventaList(servicioVentaList);
      em.persist(venta);
      
      //save jpa many to one
//    ejbFacadeVenta.create(venta);
//    utx.commit();
    } catch (Exception e) {
      Logger.getLogger(VentaFacade.class.getName()).log(Level.SEVERE, null, "EROOR GUARDAR VENTA AL CONTADO: " + e.toString());
//      try {
//        utx.rollback();
//      } catch (IllegalStateException ex) {
//        Logger.getLogger(VentaFacade.class.getName()).log(Level.SEVERE, null, ex);
//      } catch (SecurityException ex) {
//        Logger.getLogger(VentaFacade.class.getName()).log(Level.SEVERE, null, ex);
//      } catch (SystemException ex) {
//        Logger.getLogger(VentaFacade.class.getName()).log(Level.SEVERE, null, ex);
//      }
    }
    return venta.getIdVenta();
  }

  @Override
  public Integer grabarVentaCreditos(Carrito carrito, Cliente cliente, Usuario usuario, Credito cuota) {

    Venta venta = new Venta();
    venta.setComprobante("");  //No se asigna comprobante hasta que se cancele las cuotas
    venta.setFormapago("CUOTAS");
    venta.setFechaHora(new Date());
    venta.setDescuento(BigDecimal.ZERO);
    venta.setSubtotal(carrito.getTotal());
    venta.setTotal(carrito.getTotal());
    if(cliente.getIdCliente() != null) // si es cliente registrado
      venta.setIdCliente(cliente);
    venta.setIdUsuario(usuario);
    venta.setEstado("CREDITO");

    try {
      em.persist(venta);
      em.flush();
      List<Servicioventa> servicioVentaList = new ArrayList<>();
      Servicioventa servicioVenta;

      for (CarritoItem i : carrito.getItems()) {
        servicioVenta = new Servicioventa();
        servicioVenta.setCantidad(i.getCantidad());
        servicioVenta.setImporte(i.getImporte());
        servicioVenta.setIdServicio(new Servicio(i.getIdProducto()));
        servicioVenta.setIdComision(new Comision(i.getIdComision()));
        servicioVenta.setIdVenta(venta); //para persistencia 1 a muchos
        //servicioVenta.setProductoventaPK(new ProductoventaPK(venta.getIdVenta(), i.getIdProducto()));

        servicioVentaList.add(servicioVenta);
        
      }

      venta.setProductoventaList(servicioVentaList);

      //Agregar cuota
      List<Credito> cuotaList = new ArrayList<>();
      cuota.setFechaHora(venta.getFechaHora());
      cuota.setCuotaspagado(0);
      cuota.setIdVenta(venta);
      cuotaList.add(cuota);
      venta.setCreditoList(cuotaList);
      System.out.println("Se agregó la cuota");

      em.persist(venta);
      System.out.println("Se guardó la venta en cuotas");
      
    } catch (Exception e) {
      System.out.println(e.toString());
    }
    return venta.getIdVenta();

  }

  @Override
  public List<Venta> findByFormaPagoCliente(Cliente cliente, String formaPago) {

    TypedQuery<Venta> q = getEntityManager().createNamedQuery("Venta.findByformaPagoCliente", Venta.class);
    q.setParameter("formapago", formaPago);
    q.setParameter("idCliente", cliente);
    List<Venta> list;
    try {
      list = q.getResultList();
    } catch (NoResultException e) {
      list = null;
    }
    return list;
  }

}
