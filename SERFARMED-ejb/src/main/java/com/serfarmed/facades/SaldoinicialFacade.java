/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.serfarmed.facades;

import com.serfarmed.entities.Saldoinicial;
import com.serfarmed.entities.Personal;
import com.serfarmed.entities.Saldoinicial;
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
public class SaldoinicialFacade extends AbstractFacade<Saldoinicial> implements SaldoinicialFacadeLocal {
  @PersistenceContext(unitName = "com.serfarmed_Serfarmed-ejb_ejb_1.0PU")
  private EntityManager em;

  @Override
  protected EntityManager getEntityManager() {
    return em;
  }
  

  public SaldoinicialFacade() {
    super(Saldoinicial.class);
  }
  
  
  @Override
  public Saldoinicial findByFecha(Date fecha) {
    TypedQuery<Saldoinicial> q = getEntityManager().createNamedQuery("Saldoinicial.findByFecha", Saldoinicial.class);
    
    q.setParameter("fecha",fecha );
    
    List<Saldoinicial> list;
    list = q.getResultList();
    
    if(list.isEmpty()){
      return null;
    }
    
    return list.get(0);
  }
  
  @Override
  public List<Saldoinicial> findByMonth(Date fecha) {

    TypedQuery<Saldoinicial> q = getEntityManager().createNamedQuery("Saldoinicial.findByMonth", Saldoinicial.class);

    SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy-MM-dd"); 
    Map<String, String> parametro = EjbUtil.getStartEndMonth(fecha);
    
    try {
      q.setParameter("startDate", dtFormat.parse(parametro.get("startDate")) , TemporalType.TIMESTAMP );
      q.setParameter("endDate", dtFormat.parse(parametro.get("endDate")) , TemporalType.TIMESTAMP );
      
    } catch (ParseException ex) {
      System.out.println(ex.getMessage());
    }

    List<Saldoinicial> list = q.getResultList();
    return list;
  }
  
  
  
}
