package pe.idat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import pe.idat.repository.MovimientoAlmacenRepository;

@Controller
@RequestMapping("/movimientos")
public class MovimientoController {

    @Autowired
    private MovimientoAlmacenRepository movimientoRepository;

    @GetMapping
    public String listarMovimientos(Model model) {
        // Obtenemos todos los movimientos ordenados por fecha descendente (lo más reciente primero)
        model.addAttribute("listaMovimientos", 
            movimientoRepository.findAll(Sort.by(Sort.Direction.DESC, "fechaMovimiento")));
        
        return "movimiento/listar";
    }
}