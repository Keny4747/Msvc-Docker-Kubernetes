package com.springcloud.msvc.controllers;

import com.springcloud.msvc.models.Usuario;
import com.springcloud.msvc.models.entity.Curso;
import com.springcloud.msvc.service.CursoService;
import feign.FeignException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cursos")
@RequiredArgsConstructor
@Slf4j
public class CursoController {
    private final CursoService cursoService;

    @GetMapping
    public ResponseEntity<List<Curso>> listarCursos() {
        return ResponseEntity.status(HttpStatus.OK).body(cursoService.listarCursos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Curso> obtenerCursoPorId(@PathVariable Long id) {
        return cursoService.obtenerCursoPorId(id)
                .map(curso -> ResponseEntity.status(HttpStatus.OK).body(curso))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<Curso> crearCurso(@Valid @RequestBody Curso curso) {
        Curso nuevoCurso = cursoService.crearCurso(curso);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoCurso);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Curso> actualizarCurso(@PathVariable Long id, @RequestBody Curso curso) {
        return cursoService.obtenerCursoPorId(id)
                .map(existingCourse -> {
                    existingCourse.setNombre(curso.getNombre());
                    Curso updatedCourse = cursoService.crearCurso(existingCourse);
                    return ResponseEntity.status(HttpStatus.OK).body(updatedCourse);
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCurso(@PathVariable Long id) {
        if (cursoService.obtenerCursoPorId(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        cursoService.eliminarCurso(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/asignar-usuario/{idCurso}")
    public ResponseEntity<?> asignarUsuario(@PathVariable Long idCurso, @RequestBody Usuario usuario) {
        Optional<Usuario> o;
        log.info("Asignando usuario {} al curso {}", usuario.getId(), idCurso);
        try {

            o = cursoService.asignarUsuario(idCurso, usuario);
            log.info("Asignando usuario {} al curso {}", usuario.getId(), idCurso);
        }catch (FeignException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error al asignar usuario: " + e.getMessage());
        }

        if(o.isPresent()) {
         return ResponseEntity.status(HttpStatus.CREATED).body(o.get());
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping("/crear-usuario/{idCurso}")
    public ResponseEntity<?> crearUsuario(@PathVariable Long idCurso, @RequestBody Usuario usuario) {
        Optional<Usuario> o;
        try {
            o = cursoService.crearUsuario(idCurso, usuario);
        }catch (FeignException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error al asignar usuario: " + e.getMessage());
        }

        if(o.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(o.get());
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("/eliminar-usuario/{idCurso}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable Long idCurso, @RequestBody Usuario idUsuario) {
        Optional<Usuario> o;
        try {
            o = cursoService.eliminarUsuario(idCurso, idUsuario);
        }catch (FeignException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error al eliminar usuario: " + e.getMessage());
        }

        if(o.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(o.get());
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/usuarios-por-curso/{id}")
    public ResponseEntity<Curso> porIdConUsuarios(@PathVariable Long id) {
        return cursoService.porIdConUsuarios(id)
                .map(curso -> ResponseEntity.status(HttpStatus.OK).body(curso))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/eliminar-curso-usuario/{id}")
    public ResponseEntity<Void> eliminarCursoUsuarioPorId(@PathVariable Long id) {
        if (cursoService.obtenerCursoPorId(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        cursoService.eliminarCursoUsuarioPorId(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
