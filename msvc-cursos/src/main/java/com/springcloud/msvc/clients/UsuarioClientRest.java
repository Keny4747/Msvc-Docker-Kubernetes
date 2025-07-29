package com.springcloud.msvc.clients;

import com.springcloud.msvc.models.Usuario;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "msvc-usuarios", url = "msvc-usuarios:8001/usuarios")
public interface UsuarioClientRest {

    @GetMapping("/{id}")
    Usuario obtenerUsuarioPorId(@PathVariable Long id);

    @GetMapping("/usuarios-por-curso")
    List<Usuario> listarUsuariosPorId(@RequestParam List<Long> ids);

    @PostMapping()
    Usuario crearUsuario(@RequestBody Usuario usuario);
}
