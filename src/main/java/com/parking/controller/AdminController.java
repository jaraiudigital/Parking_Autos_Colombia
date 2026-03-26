package com.parking.controller;

import com.parking.model.Celda;
import com.parking.model.Cliente;
import com.parking.model.Usuario;
import com.parking.repository.*;
import com.parking.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.Optional;  // ← IMPORTANTE: agregar

@Controller
public class AdminController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private CeldaRepository celdaRepository;

    @Autowired
    private RegistroRepository registroRepository;

    private boolean esAdministrador(HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        return usuario != null && "administrador".equals(usuario.getRol());
    }

    @PostMapping("/admin/usuarios/crear")
    public String crearUsuario(@RequestParam String username,
                               @RequestParam String password,
                               @RequestParam String nombre,
                               @RequestParam String rol,
                               @RequestParam(required = false) String email,
                               @RequestParam(required = false) String telefono,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        if (!esAdministrador(session)) {
            return "redirect:/login";
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(username);
        usuario.setPassword(password);
        usuario.setNombre(nombre);
        usuario.setRol(rol);
        usuario.setEmail(email);
        usuario.setTelefono(telefono);

        usuarioService.crearUsuario(usuario);
        redirectAttributes.addFlashAttribute("success", "Usuario creado exitosamente");
        return "redirect:/dashboard";
    }

    @PostMapping("/admin/clientes/crear")
    public String crearCliente(@RequestParam String tipoCliente,
                               @RequestParam(required = false) String identificacion,
                               @RequestParam String nombre,
                               @RequestParam(required = false) String telefono,
                               @RequestParam(required = false) String direccion,
                               @RequestParam(required = false) String email,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        if (!esAdministrador(session)) {
            return "redirect:/login";
        }

        Cliente cliente = new Cliente();
        cliente.setTipoCliente(tipoCliente);
        cliente.setIdentificacion(identificacion);
        cliente.setNombre(nombre);
        cliente.setTelefono(telefono);
        cliente.setDireccion(direccion);
        cliente.setEmail(email);
        cliente.setEstado(true);

        if ("mensualidad".equals(tipoCliente)) {
            cliente.setFechaVencimientoMensualidad(java.time.LocalDate.now().plusDays(30));
        }

        clienteRepository.save(cliente);
        redirectAttributes.addFlashAttribute("success", "Cliente registrado exitosamente");
        return "redirect:/dashboard";
    }

    @PostMapping("/admin/celdas/crear")
    public String crearCelda(@RequestParam String numeroCelda,
                             @RequestParam(required = false) String ubicacion,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        if (!esAdministrador(session)) {
            return "redirect:/login";
        }

        if (celdaRepository.existsByNumeroCelda(numeroCelda)) {
            redirectAttributes.addFlashAttribute("error", "La celda ya existe");
            return "redirect:/dashboard";
        }

        Celda celda = new Celda();
        celda.setNumeroCelda(numeroCelda);
        celda.setUbicacion(ubicacion);
        celda.setEstado("disponible");
        celda.setTipoVehiculo("auto");
        celdaRepository.save(celda);

        redirectAttributes.addFlashAttribute("success", "Celda creada exitosamente");
        return "redirect:/dashboard";
    }

    @PostMapping("/admin/celdas/estado")
    public String cambiarEstadoCelda(@RequestParam Long id,
                                     @RequestParam String estado,
                                     HttpSession session,
                                     RedirectAttributes redirectAttributes) {
        if (!esAdministrador(session)) {
            return "redirect:/login";
        }

        Optional<Celda> celdaOpt = celdaRepository.findById(id);
        if (celdaOpt.isPresent()) {
            Celda celda = celdaOpt.get();
            celda.setEstado(estado);
            celdaRepository.save(celda);
            redirectAttributes.addFlashAttribute("success", "Estado actualizado");
        } else {
            redirectAttributes.addFlashAttribute("error", "Celda no encontrada");
        }

        return "redirect:/dashboard";
    }

    @GetMapping("/admin/reporte")
    @ResponseBody
    public Object getReporte(@RequestParam String fecha) {
        java.time.LocalDateTime fechaTime = java.time.LocalDateTime.parse(fecha + "T00:00:00");
        return registroRepository.findRegistrosByFechaRange(fechaTime, fechaTime.withHour(23).withMinute(59).withSecond(59));
    }
}