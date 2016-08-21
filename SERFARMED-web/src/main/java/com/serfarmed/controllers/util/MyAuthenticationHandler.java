/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.serfarmed.controllers.util;

import java.io.IOException;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

/**
 *
 * @author jlombardo
 */
public class MyAuthenticationHandler extends SimpleUrlAuthenticationSuccessHandler {

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
//        String salerTargetUrl = "/faces/modulos/ventas/index.xhtml";
//        String adminTargetUrl = "/faces/modulos/mantenimiento/index.xhtml";
    String mainTargetUrl = "/faces/main.xhtml";
    Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

    if (roles.contains("ROLE_ADMIN")) { //Si es administrador
      getRedirectStrategy().sendRedirect(request, response, mainTargetUrl);
    } else if (roles.contains("ROLE_CAJERO")) { //Si es vendedor
      getRedirectStrategy().sendRedirect(request, response, mainTargetUrl);
    } else {
      super.onAuthenticationSuccess(request, response, authentication);
      return;
    }
  }
}
