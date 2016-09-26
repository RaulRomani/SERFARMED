/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.serfarmed.facades;

import com.serfarmed.entities.Operacion;
import com.serfarmed.entities.util.EjbUtil;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
public class OperacionFacade extends AbstractFacade<Operacion> implements OperacionFacadeLocal {

  @PersistenceContext(unitName = "com.serfarmed_Serfarmed-ejb_ejb_1.0PU")
  private EntityManager em;

  @Override
  protected EntityManager getEntityManager() {
    return em;
  }

  public OperacionFacade() {
    super(Operacion.class);
  }

  @Override
  public List<Operacion> findEgresoByFecha(Date fecha) {

    TypedQuery<Operacion> q = getEntityManager().createNamedQuery("Operacion.findEgresoByFecha", Operacion.class);

    SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat fechaFormat = new SimpleDateFormat("yyyy-MM-dd");

    String startDate = fechaFormat.format(fecha) + " 00:00:01";
    String endDate = fechaFormat.format(fecha) + " 23:59:59";

    try {
      q.setParameter("startDate", dtFormat.parse(startDate), TemporalType.TIMESTAMP);
      q.setParameter("endDate", dtFormat.parse(endDate), TemporalType.TIMESTAMP);

    } catch (ParseException ex) {
      System.out.println(ex.getMessage());
    }

    List<Operacion> list;
    list = q.getResultList();

    return list;
  }
  
  @Override
  public List<Operacion> findEgresosMes(Date fecha) {

    TypedQuery<Operacion> q = getEntityManager().createNamedQuery("Operacion.findEgresoByFecha", Operacion.class);

    SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
    Map<String, String> parametro = EjbUtil.getStartEndMonth(fecha);
    
    try {
      q.setParameter("startDate", dtFormat.parse(parametro.get("startDate")) , TemporalType.TIMESTAMP );
      q.setParameter("endDate", dtFormat.parse(parametro.get("endDate")) , TemporalType.TIMESTAMP );
      
    } catch (ParseException ex) {
      System.out.println(ex.getMessage());
    }

    List<Operacion> list;
    list = q.getResultList();

    return list;
  }

  @Override
  public List<Operacion> findIngresoByFecha(Date fecha) {

    TypedQuery<Operacion> q = getEntityManager().createNamedQuery("Operacion.findIngresoByFecha", Operacion.class);

    SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat fechaFormat = new SimpleDateFormat("yyyy-MM-dd");

    String startDate = fechaFormat.format(fecha) + " 00:00:01";
    String endDate = fechaFormat.format(fecha) + " 23:59:59";

    try {
      q.setParameter("startDate", dtFormat.parse(startDate), TemporalType.TIMESTAMP);
      q.setParameter("endDate", dtFormat.parse(endDate), TemporalType.TIMESTAMP);

    } catch (ParseException ex) {
      System.out.println(ex.getMessage());
    }

    List<Operacion> list;
    list = q.getResultList();

    return list;
  }
  
  @Override
  public List<Operacion> findIngresosMes(Date fecha) {

    TypedQuery<Operacion> q = getEntityManager().createNamedQuery("Operacion.findIngresoByFecha", Operacion.class);

    SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
    Map<String, String> parametro = EjbUtil.getStartEndMonth(fecha);
    
    try {
      q.setParameter("startDate", dtFormat.parse(parametro.get("startDate")) , TemporalType.TIMESTAMP );
      q.setParameter("endDate", dtFormat.parse(parametro.get("endDate")) , TemporalType.TIMESTAMP );
      
    } catch (ParseException ex) {
      System.out.println(ex.getMessage());
    }

    List<Operacion> list;
    list = q.getResultList();

    return list;
  }

}
