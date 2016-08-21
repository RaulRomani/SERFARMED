/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.serfarmed.facades;

import com.serfarmed.entities.Cliente;
import com.serfarmed.entities.Proveedor;
import com.serfarmed.entities.Compra;
import com.serfarmed.entities.Servicio;
import com.serfarmed.entities.Detallecompra;
import com.serfarmed.entities.Usuario;
import com.serfarmed.entities.Compra;
import com.serfarmed.entities.Producto;
import com.serfarmed.entities.Venta;
import com.serfarmed.entities.util.Carrito;
import com.serfarmed.entities.util.CarritoItem;
import java.math.BigDecimal;
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
import javax.persistence.TypedQuery;

/**
 *
 * @author Raul
 */
@Stateless
public class CompraFacade extends AbstractFacade<Compra> implements CompraFacadeLocal {

  @PersistenceContext(unitName = "com.serfarmed_Serfarmed-ejb_ejb_1.0PU")
  private EntityManager em;

  @EJB
  private com.serfarmed.facades.ServicioFacadeLocal ejbFacadeProducto;

  @Override
  protected EntityManager getEntityManager() {
    return em;
  }

  public CompraFacade() {
    super(Compra.class);
  }

  @Override
  public Integer grabarPedidoCompra(Carrito carrito, Proveedor proveedor, Usuario usuario) {

    Compra compra = new Compra();

    compra.setTotal(carrito.getTotal());
    compra.setFechaHora(new Date());
    compra.setComprobante("FACTURA");
    compra.setEstado("ESPERA");

    compra.setIdProveedor(proveedor);
    compra.setIdUsuario(usuario);

    try {

      em.persist(compra);
      em.flush();
      List<Detallecompra> detalleCompraList = new ArrayList<>();
      Detallecompra detalleCompra;

      for (CarritoItem i : carrito.getItems()) {
        detalleCompra = new Detallecompra();
        detalleCompra.setCantidad(i.getCantidad());
        detalleCompra.setImporte(i.getImporte());
        detalleCompra.setIdProducto(new Producto(i.getIdProducto()));
        detalleCompra.setRecibido(true);
        detalleCompra.setIdCompra(compra); //para persistencia 1 a muchos

        //detalleCompra.setProductocompraPK(new ProductocompraPK(compra.getIdCompra(), i.getIdProducto()));
        detalleCompraList.add(detalleCompra);

      }
      compra.setDetallecompraList(detalleCompraList);
      em.persist(compra);

    } catch (Exception e) {
      Logger.getLogger(CompraFacade.class.getName()).log(Level.SEVERE, null, "EROOR GUARDAR PEDIDO DE COMPRA: " + e.toString());
    }
    
    return compra.getIdCompra();
  }

  @Override
  public void grabarCompra(Compra compra) {

    try {
      compra.setEstado("PAGADO");


      em.merge(compra);

    } catch (Exception e) {
      Logger.getLogger(CompraFacade.class.getName()).log(Level.SEVERE, null, "EROOR GUARDAR COMPRA: " + e.toString());

    } 

  }

  @Override
  public List<Compra> findByEstado(String estado) {

    TypedQuery<Compra> q = getEntityManager().createNamedQuery("Compra.findByEstado", Compra.class);
    q.setParameter("estado", estado);
    List<Compra> list;
    try {
      list = q.getResultList();
    } catch (NoResultException e) {
      list = null;
    }
    return list;
  }

}
