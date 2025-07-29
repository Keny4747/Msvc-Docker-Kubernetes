package com.spirngcloud.msvc.controllers;

import com.spirngcloud.msvc.model.entity.Usuario;
import com.spirngcloud.msvc.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;


    @GetMapping
    public List<Usuario> listarUsuarios() {
        return usuarioService.listarUsuarios();
    }


    @GetMapping("/{id}")
    public Usuario obtenerUsuarioPorId(@PathVariable Long id) {
        return usuarioService.obtenerUsuarioPorId(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    @PostMapping
    public ResponseEntity<Usuario> crearUsuario(@Valid @RequestBody Usuario usuario) {

        if(usuarioService.obtenerUsuarioPorEmail(usuario.getEmail()).isPresent()) {
           return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.crearUsuario(usuario));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizarUsuario(@Valid @PathVariable Long id, @RequestBody Usuario usuario) {
        if(usuarioService.obtenerUsuarioPorEmail(usuario.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body(null);
        }
        Optional<Usuario> usuarioExistente = usuarioService.obtenerUsuarioPorId(id);
        if (usuarioExistente.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Usuario usuarioActualizado = usuarioService.crearUsuario(usuario);

        return ResponseEntity.status(HttpStatus.OK).body(usuarioActualizado);
    }

    @DeleteMapping("/{id}")
    public void eliminarUsuario(@PathVariable Long id) {

        Optional<Usuario> usuario = usuarioService.obtenerUsuarioPorId(id);
        if (usuario.isEmpty()) {
            throw new RuntimeException("Usuario no encontrado");
        }
        usuarioService.eliminarUsuario(id);
    }

    @GetMapping("/usuarios-por-curso")
    public ResponseEntity<List<Usuario>> listarUsuariosPorId(@RequestParam List<Long> ids) {
        List<Usuario> usuarios = usuarioService.listarUsuariosPorId(ids);
        if (usuarios.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(usuarios);
    }


}
