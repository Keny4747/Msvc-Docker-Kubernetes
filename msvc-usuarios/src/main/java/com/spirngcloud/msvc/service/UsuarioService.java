package com.spirngcloud.msvc.service;

import com.spirngcloud.msvc.model.entity.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {

    List<Usuario> listarUsuarios();
    Optional<Usuario> obtenerUsuarioPorId(Long id);
    Usuario crearUsuario(Usuario usuario);
    void eliminarUsuario(Long id);
    List<Usuario> listarUsuariosPorId(Iterable<Long> ids);


    Optional<Usuario> obtenerUsuarioPorEmail(String email);

}
