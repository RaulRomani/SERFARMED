/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.serfarmed.facades;

import com.serfarmed.entities.Cliente;
import com.serfarmed.entities.Credito;
import com.serfarmed.entities.Personal;
import com.serfarmed.entities.Usuario;
import com.serfarmed.entities.Venta;
import com.serfarmed.entities.util.Carrito;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Raul
 */
@Local
public interface VentaFacadeLocal {

  void create(Venta venta);

  void edit(Venta venta);

  void remove(Venta venta);

  Venta find(Object id);

  List<Venta> findAll();

  List<Venta> findRange(int[] range);

  int count();
  
  public Integer grabarVentaContado(Carrito carrito, Cliente cliente, Usuario usuario);
  public Integer grabarVentaCreditos(Carrito carrito, Cliente cliente, Usuario usuario, Credito cuota);
  public List<Venta> findByFormaPagoCliente(Cliente cliente, String formaPago);
  public List<Venta> findByFecha(Date fecha);
  
  public List<Object[]> ventasMensualesByServicio(Date fecha);
  public List<Object[]> ventasMensualesByServicioDoctor(String servicio, Date fecha);
  
  public List<Object[]> findPagoDoctorHoy(Date fecha);
  public List<Venta> findVentasMes(Date fecha);
  public List<Object[]> findDeudaAntPagoDoctor(Date fecha);
  public BigDecimal findTotalPagadoByDoctor(Personal doctor, Date fecha);
  
  
}
