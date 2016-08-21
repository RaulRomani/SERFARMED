/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.serfarmed.facades;

import com.serfarmed.entities.Personal;
import com.serfarmed.entities.Saldoinicial;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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
  public Saldoinicial findByFechaHoy() {
    TypedQuery<Saldoinicial> q = getEntityManager().createNamedQuery("Saldoinicial.findByFechaHoy", Saldoinicial.class);
    
    //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
    Date fechaHoy = new Date();
    q.setParameter("fechaHoy",fechaHoy );
    
    List<Saldoinicial> list;
    try {
      list = q.getResultList();
    } catch (NoResultException e) {
      list = null;
      System.out.println("THERE IS'NT ACTUAL SALDO INICIAL ");
    }
    if(list.isEmpty())
      return null;
    System.out.println("FIND SALDO INICIAL DE HOY OK");
    return list.get(0);
  }
  
}
