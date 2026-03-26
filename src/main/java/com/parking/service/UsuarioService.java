package com.parking.service;

import com.parking.model.Usuario;
import com.parking.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario autenticar(String username, String password) {
        System.out.println("=== UsuarioService.autenticar ===");
        System.out.println("Buscando usuario: " + username);

        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(username);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            System.out.println("Usuario encontrado en BD:");
            System.out.println("  - ID: " + usuario.getId());
            System.out.println("  - Username: " + usuario.getUsername());
            System.out.println("  - Password en BD: " + usuario.getPassword());
            System.out.println("  - Estado: " + usuario.getEstado());

            String encryptedPassword = md5(password);
            System.out.println("Password ingresada encriptada: " + encryptedPassword);

            if (usuario.getPassword().equals(encryptedPassword) && usuario.getEstado()) {
                System.out.println("✓ Autenticación exitosa!");
                return usuario;
            } else {
                System.out.println("✗ Contraseña incorrecta o usuario inactivo");
            }
        } else {
            System.out.println("✗ Usuario no encontrado: " + username);
        }
        return null;
    }

    private String md5(String input) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : array) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
            e.printStackTrace();
            return input;
        }
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario crearUsuario(Usuario usuario) {
        usuario.setPassword(md5(usuario.getPassword()));
        usuario.setFechaCreacion(java.time.LocalDateTime.now());
        usuario.setEstado(true);
        return usuarioRepository.save(usuario);
    }

    public Usuario actualizarUsuario(Long id, Usuario usuario) {
        Optional<Usuario> existingOpt = usuarioRepository.findById(id);
        if (existingOpt.isPresent()) {
            Usuario existing = existingOpt.get();
            existing.setNombre(usuario.getNombre());
            existing.setEmail(usuario.getEmail());
            existing.setTelefono(usuario.getTelefono());
            existing.setRol(usuario.getRol());
            return usuarioRepository.save(existing);
        }
        return null;
    }

    public void eliminarUsuario(Long id) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuario.setEstado(false);
            usuarioRepository.save(usuario);
        }
    }
}