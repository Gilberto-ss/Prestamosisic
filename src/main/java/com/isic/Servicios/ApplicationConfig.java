package com.isic.Servicios;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author Gilberto
 */
@javax.ws.rs.ApplicationPath("api") // Puedes cambiar "api" si necesitas otra ruta
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * No modificar el m�todo addRestResourceClasses().
     * Se llena autom�ticamente con todos los recursos REST en el proyecto.
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(Servicios.Usuarios.class);

    }
}
