package com.parking.controller;

import com.parking.model.Usuario;
import com.parking.model.Vehiculo;
import com.parking.service.ParkingService;
import com.parking.repository.CeldaRepository;
import com.parking.repository.ClienteRepository;
import com.parking.model.Celda;
import com.parking.model.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
public class ParkingController {

    @Autowired
    private ParkingService parkingService;

    @Autowired
    private CeldaRepository celdaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @PostMapping("/registrarEntrada")
    public String registrarEntrada(@RequestParam String placa,
                                   @RequestParam String marca,
                                   @RequestParam(required = false) String modelo,
                                   @RequestParam(required = false) String color,
                                   @RequestParam String tipoCliente,
                                   @RequestParam(required = false) Long clienteId,
                                   @RequestParam Long celdaId,
                                   HttpSession session,
                                   RedirectAttributes redirectAttributes) {

        System.out.println("=== REGISTRAR ENTRADA ===");
        System.out.println("Placa: " + placa);
        System.out.println("Marca: " + marca);
        System.out.println("Celda ID: " + celdaId);

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }

        // Validar formato de placa
        if (!placa.matches("[A-Z]{3}[0-9]{3}")) {
            redirectAttributes.addFlashAttribute("error", "Formato de placa inválido. Use ABC123");
            return "redirect:/dashboard";
        }

        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setPlaca(placa.toUpperCase());
        vehiculo.setMarca(marca);
        vehiculo.setModelo(modelo);
        vehiculo.setColor(color);

        Long clienteFinalId = null;
        if ("mensualidad".equals(tipoCliente)) {
            if (clienteId == null) {
                redirectAttributes.addFlashAttribute("error", "Debe seleccionar un cliente");
                return "redirect:/dashboard";
            }

            Optional<Cliente> clienteOpt = clienteRepository.findById(clienteId);
            if (clienteOpt.isEmpty() || !clienteOpt.get().isMensualidadActiva()) {
                redirectAttributes.addFlashAttribute("error", "Cliente sin mensualidad activa");
                return "redirect:/dashboard";
            }
            clienteFinalId = clienteId;
        }

        // Registrar entrada
        var resultado = parkingService.registrarEntrada(vehiculo, celdaId, clienteFinalId, usuario.getId());

        System.out.println("Resultado registro: " + resultado);

        if ((boolean) resultado.get("success")) {
            redirectAttributes.addFlashAttribute("success", "Entrada registrada exitosamente");
        } else {
            redirectAttributes.addFlashAttribute("error", resultado.get("message"));
        }

        return "redirect:/dashboard";
    }

    @PostMapping("/registrarSalida")
    public String registrarSalida(@RequestParam Long registroId,
                                  @RequestParam Double valorPagado,
                                  HttpSession session,
                                  RedirectAttributes redirectAttributes) {

        System.out.println("=== REGISTRAR SALIDA ===");
        System.out.println("Registro ID: " + registroId);
        System.out.println("Valor pagado: " + valorPagado);

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }

        var resultado = parkingService.registrarSalida(registroId, valorPagado, usuario.getId());

        System.out.println("Resultado salida: " + resultado);

        if ((boolean) resultado.get("success")) {
            redirectAttributes.addFlashAttribute("success", "Salida registrada. Total: $" + valorPagado);
        } else {
            redirectAttributes.addFlashAttribute("error", resultado.get("message"));
        }

        return "redirect:/dashboard";
    }
}