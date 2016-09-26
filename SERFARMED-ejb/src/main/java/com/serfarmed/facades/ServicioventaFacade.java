/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.serfarmed.facades;

import com.serfarmed.entities.Personal;
import com.serfarmed.entities.Servicioventa;
import com.serfarmed.entities.Venta;
import com.serfarmed.entities.util.EjbUtil;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

/**
 *
 * @author Raul
 */
@Stateless
public class ServicioventaFacade extends AbstractFacade<Servicioventa> implements ServicioventaFacadeLocal {

  @PersistenceContext(unitName = "com.serfarmed_Serfarmed-ejb_ejb_1.0PU")
  private EntityManager em;

  @Override
  protected EntityManager getEntityManager() {
    return em;
  }

  public ServicioventaFacade() {
    super(Servicioventa.class);
  }

  @Override
  public void updatePagoDoctorHoy(Date fecha) {

    TypedQuery<Servicioventa> q = getEntityManager().createNamedQuery("Servicioventa.updatePagoDoctorHoy", Servicioventa.class);

    SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat fechaFormat = new SimpleDateFormat("yyyy-MM-dd");

    String startDate = fechaFormat.format(fecha) + " 00:00:01";
    String endDate = fechaFormat.format(fecha) + " 23:59:59";

    List<Servicioventa> list;
    try {

      q.setParameter("startDate", dtFormat.parse(startDate), TemporalType.TIMESTAMP);
      q.setParameter("endDate", dtFormat.parse(endDate), TemporalType.TIMESTAMP);

      list = q.getResultList();

      for (Servicioventa item : list) {
        item.setPagado(Boolean.TRUE);
        em.merge(item);
      }

    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }
  
  @Override
  public void updatePagoByDoctorHoy(Date fecha, Personal doctor) {

    TypedQuery<Servicioventa> q = getEntityManager().createNamedQuery("Servicioventa.updatePagoByDoctorHoy", Servicioventa.class);

    SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat fechaFormat = new SimpleDateFormat("yyyy-MM-dd");

    String startDate = fechaFormat.format(fecha) + " 00:00:01";
    String endDate = fechaFormat.format(fecha) + " 23:59:59";

    List<Servicioventa> list;
    try {

      q.setParameter("startDate", dtFormat.parse(startDate), TemporalType.TIMESTAMP);
      q.setParameter("endDate", dtFormat.parse(endDate), TemporalType.TIMESTAMP);
      q.setParameter("idPersonal", doctor);

      list = q.getResultList();

      for (Servicioventa item : list) {
        item.setPagado(Boolean.TRUE);
        em.merge(item);
      }

    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }
  
  @Override
  public void posponerPagoByDoctorHoy(Date fecha, Personal doctor) {

    TypedQuery<Servicioventa> q = getEntityManager().createNamedQuery("Servicioventa.updatePagoByDoctorHoy", Servicioventa.class);

    SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat fechaFormat = new SimpleDateFormat("yyyy-MM-dd");

    String startDate = fechaFormat.format(fecha) + " 00:00:01";
    String endDate = fechaFormat.format(fecha) + " 23:59:59";

    List<Servicioventa> list;
    try {

      q.setParameter("startDate", dtFormat.parse(startDate), TemporalType.TIMESTAMP);
      q.setParameter("endDate", dtFormat.parse(endDate), TemporalType.TIMESTAMP);
      q.setParameter("idPersonal", doctor);

      list = q.getResultList();

      for (Servicioventa item : list) {
        item.setSePago("NO");
        em.merge(item);
      }

    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }
  
  @Override
  public List<Servicioventa> findDetallePagoByDoctorHoy(Date fecha, Personal doctor) {

    TypedQuery<Servicioventa> q = getEntityManager().createNamedQuery("Servicioventa.findDetallePagoByDoctorHoy", Servicioventa.class);

    SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat fechaFormat = new SimpleDateFormat("yyyy-MM-dd");

    String startDate = fechaFormat.format(fecha) + " 00:00:01";
    String endDate = fechaFormat.format(fecha) + " 23:59:59";

    List<Servicioventa> list = new ArrayList<>();
    try {

      q.setParameter("startDate", dtFormat.parse(startDate), TemporalType.TIMESTAMP);
      q.setParameter("endDate", dtFormat.parse(endDate), TemporalType.TIMESTAMP);
      q.setParameter("idPersonal", doctor);

      list = q.getResultList();


    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return list;
  }

  @Override
  public void updatePagoDoctorMes(Date fecha) {

    TypedQuery<Servicioventa> q = getEntityManager().createNamedQuery("Servicioventa.updatePagoDoctorMes", Servicioventa.class);

    SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Map<String, String> parametro = EjbUtil.getStartEndMonth(fecha);

    try {
      q.setParameter("startDate", dtFormat.parse(parametro.get("startDate")), TemporalType.TIMESTAMP);
      q.setParameter("endDate", dtFormat.parse(parametro.get("endDate")), TemporalType.TIMESTAMP);

    } catch (ParseException ex) {
      System.out.println(ex.getMessage());
    }

    List<Servicioventa> list;
    list = q.getResultList();

    for (Servicioventa item : list) {
      item.setPagado(Boolean.TRUE);
      item.setSePago("SI");
      em.merge(item);
    }

  }

}
