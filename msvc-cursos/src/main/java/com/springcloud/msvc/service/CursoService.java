package com.springcloud.msvc.service;

import com.springcloud.msvc.models.Usuario;
import com.springcloud.msvc.models.entity.Curso;

import java.util.List;
import java.util.Optional;

public interface CursoService {

    List<Curso> listarCursos();
    Optional<Curso> obtenerCursoPorId(Long id);
    Curso crearCurso(Curso curso);
    void eliminarCurso(Long id);
    Optional<Curso> porIdConUsuarios(Long id);

    void eliminarCursoUsuarioPorId(Long id);

    Optional<Usuario> asignarUsuario(Long idCurso, Usuario usuario);
    Optional<Usuario> crearUsuario(Long idCurso, Usuario usuario);
    Optional<Usuario> eliminarUsuario(Long idCurso, Usuario usuario);
}
