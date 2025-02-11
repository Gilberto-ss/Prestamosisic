/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servicios;

import ModelosBD.UsuariosBD;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.SimpleDateFormat;
import java.util.Date;

@Path("Usuarios")
public class Usuarios {

    private static SessionFactory sessionFactory;

    static {
        sessionFactory = new Configuration().configure().buildSessionFactory();
    }

    @POST
    @Path("guardar")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response guardar(@FormParam("nombreUsuario") String nombreUsuario,
                            @FormParam("contraseña") String contraseña,
                            @FormParam("primerNombre") String primerNombre,
                            @FormParam("segundoNombre") String segundoNombre,
                            @FormParam("apellidoPaterno") String apellidoPaterno,
                            @FormParam("apellidoMaterno") String apellidoMaterno,
                            @FormParam("correo") String correo,
                            @FormParam("telefono") String telefono,
                            @FormParam("rol") String rol,
                            @FormParam("activo") int activo,
                            @FormParam("fechaCreacion") String fechaCreacion) {

        if (nombreUsuario == null || nombreUsuario.trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"nombreUsuario es obligatorio.\"}")
                    .build();
        }
        if (contraseña == null || contraseña.trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"contraseña es obligatorio.\"}")
                    .build();
        }

        Session session = null;
        Transaction transaction = null;

        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            // Parse the fechaCreacion string
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date fechaCreacionParsed = dateFormat.parse(fechaCreacion);

            // Crear objeto UsuariosBD
            UsuariosBD usuario = new UsuariosBD();
            usuario.setNombreUsuario(nombreUsuario);
            usuario.setContraseña(contraseña);
            usuario.setPrimerNombre(primerNombre);
            usuario.setSegundoNombre(segundoNombre);
            usuario.setApellidoPaterno(apellidoPaterno);
            usuario.setApellidoMaterno(apellidoMaterno);
            usuario.setCorreo(correo);
            usuario.setTelefono(telefono);
            usuario.setRol(rol);
            usuario.setActivo(activo == 1);
            usuario.setFechaCreacion(fechaCreacionParsed.toString());

            session.save(usuario);
            transaction.commit();

            return Response.ok("{\"message\":\"Usuario guardado exitosamente\"}")
                    .build();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"No se pudo guardar el usuario: " + e.getMessage() + "\"}")
                    .build();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @GET
    @Path("eliminar")
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminar(@QueryParam("id") Long id) {
        Session session = null;
        Transaction transaction = null;

        if (id == null || id <= 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"El campo 'id' es obligatorio y debe ser mayor a cero.\"}")
                    .build();
        }

        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            // Buscar el usuario por id
            UsuariosBD usuario = session.get(UsuariosBD.class, id);

            if (usuario == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\":\"Usuario no encontrado con id: " + id + "\"}")
                        .build();
            }

            usuario.setActivo(false);
            session.update(usuario);
            transaction.commit();

            return Response.ok("{\"message\":\"Usuario desactivado exitosamente\"}")
                    .build();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Error al desactivar el usuario: " + e.getMessage() + "\"}")
                    .build();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @POST
    @Path("editar")
    @Produces(MediaType.APPLICATION_JSON)
    public Response editar(@FormParam("id") long id,
                           @FormParam("nombreUsuario") String nombreUsuario,
                           @FormParam("contraseña") String contraseña,
                           @FormParam("primerNombre") String primerNombre,
                           @FormParam("segundoNombre") String segundoNombre,
                           @FormParam("apellidoPaterno") String apellidoPaterno,
                           @FormParam("apellidoMaterno") String apellidoMaterno,
                           @FormParam("correo") String correo,
                           @FormParam("telefono") String telefono,
                           @FormParam("rol") String rol,
                           @FormParam("activo") int activo,
                           @FormParam("fechaCreacion") String fechaCreacion) {

        Session session = null;
        Transaction transaction = null;

        if (id <= 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"El campo 'id' es obligatorio y debe ser mayor a cero.\"}")
                    .build();
        }

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date fechaCreacionParsed = fechaCreacion != null ? dateFormat.parse(fechaCreacion) : null;

            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            // Buscar el usuario por id
            UsuariosBD usuario = session.get(UsuariosBD.class, id);
            if (usuario == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\":\"Usuario no encontrado.\"}")
                        .build();
            }

            // Actualizar los datos del usuario si fueron proporcionados
            if (nombreUsuario != null && !nombreUsuario.trim().isEmpty()) {
                usuario.setNombreUsuario(nombreUsuario);
            }
            if (contraseña != null && !contraseña.trim().isEmpty()) {
                usuario.setContraseña(contraseña);
            }
            if (primerNombre != null && !primerNombre.trim().isEmpty()) {
                usuario.setPrimerNombre(primerNombre);
            }
            if (segundoNombre != null && !segundoNombre.trim().isEmpty()) {
                usuario.setSegundoNombre(segundoNombre);
            }
            if (apellidoPaterno != null && !apellidoPaterno.trim().isEmpty()) {
                usuario.setApellidoPaterno(apellidoPaterno);
            }
            if (apellidoMaterno != null && !apellidoMaterno.trim().isEmpty()) {
                usuario.setApellidoMaterno(apellidoMaterno);
            }
            if (correo != null && !correo.trim().isEmpty()) {
                usuario.setCorreo(correo);
            }
            if (telefono != null && !telefono.trim().isEmpty()) {
                usuario.setTelefono(telefono);
            }
            if (rol != null && !rol.trim().isEmpty()) {
                usuario.setRol(rol);
            }
            if (fechaCreacionParsed != null) {
                usuario.setFechaCreacion(fechaCreacionParsed.toString());
            }
            usuario.setActivo(activo == 1);

            session.update(usuario);
            transaction.commit();

            return Response.ok("{\"message\":\"Usuario actualizado exitosamente\"}")
                    .build();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Error al actualizar el usuario: " + e.getMessage() + "\"}")
                    .build();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
