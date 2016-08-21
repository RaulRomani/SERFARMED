package com.serfarmed.controllers.util;

import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public final class AccesoDB {

    private static Connection con;
    
    static {
      try {
        Context ctx = new InitialContext();
        DataSource ds = (DataSource) ctx.lookup("jdbc/Serfarmed");
        con = ds.getConnection();
      } catch (Exception e) {
          System.out.println(e.getMessage());
      }
      
    }
    
    private AccesoDB() {
    }
    
    public final static Connection getConnection() throws Exception {
//        Context ctx = new InitialContext();
        //Conexion para glassfish
//        DataSource ds = (DataSource) ctx.lookup("jdbc/clientesJNDI");  
        //Conexión para apache tomcat
//        DataSource ds = (DataSource) ctx.lookup("java:comp/env/clientes");
//        con = ds.getConnection();
        return con;
    }
    public static void main(String[] args) {
      try {
        if (AccesoDB.getConnection()!= null)
          System.out.println("Exito");
      } catch (Exception ex) {
        Logger.getLogger(AccesoDB.class.getName()).log(Level.SEVERE, null, ex);
      }
  }

    
}
