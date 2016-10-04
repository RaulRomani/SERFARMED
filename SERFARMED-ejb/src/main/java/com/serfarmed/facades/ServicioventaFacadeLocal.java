/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.serfarmed.facades;

import com.serfarmed.entities.Pago;
import com.serfarmed.entities.Personal;
import com.serfarmed.entities.Servicioventa;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Raul
 */
@Local
public interface ServicioventaFacadeLocal {

  void create(Servicioventa productoventa);

  void edit(Servicioventa productoventa);

  void remove(Servicioventa productoventa);

  Servicioventa find(Object id);

  List<Servicioventa> findAll();

  List<Servicioventa> findRange(int[] range);

  int count();
  
  public void updatePagoDoctorHoy(Date fecha);
  
  public void updatePagoByDoctorHoy(Date fecha, Personal doctor, Pago idPago);
  
  public List<Servicioventa> findDetallePagoByDoctorHoy(Date fecha, Personal doctor);
  
  public void posponerPagoByDoctorHoy(Date fecha, Personal doctor);
  
  public void updatePagoAnteriorByDoctor(Date fecha, Personal doctor, Pago idPago);
  
  public List<Servicioventa> findDetalleDeudaAntByDoctorHoy(Date fecha, Personal doctor);
  
  public List<Servicioventa> findDetallePagoHoy(Date fecha, Pago idPago);
  
}
