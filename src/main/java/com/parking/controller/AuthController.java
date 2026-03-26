package com.parking.controller;

import com.parking.model.Usuario;
import com.parking.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/")
    public String index() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {
        System.out.println("=== INTENTO DE LOGIN ===");
        System.out.println("Usuario: " + username);
        System.out.println("Contraseña ingresada: " + password);

        Usuario usuario = usuarioService.autenticar(username, password);

        if (usuario != null) {
            System.out.println("Login exitoso para: " + usuario.getNombre());
            session.setAttribute("usuario", usuario);
            return "redirect:/dashboard";
        } else {
            System.out.println("Login fallido para: " + username);
            model.addAttribute("error", "Usuario o contraseña incorrectos");
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }
        model.addAttribute("usuario", usuario);
        return "dashboard";
    }
}