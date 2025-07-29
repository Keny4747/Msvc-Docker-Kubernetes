package com.spirngcloud.msvc.service;

import com.spirngcloud.msvc.clients.CursoClientRest;
import com.spirngcloud.msvc.model.entity.Usuario;
import com.spirngcloud.msvc.respositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService{

    private final UsuarioRepository repository;
    private final CursoClientRest cursoClient;

    @Override
    public List<Usuario> listarUsuarios() {
        return (List<Usuario>) repository.findAll();
    }

    @Override
    public Optional<Usuario> obtenerUsuarioPorId(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional
    public Usuario crearUsuario(Usuario usuario) {
        return repository.save(usuario);
    }

    @Override
    public void eliminarUsuario(Long id) {
        repository.deleteById(id);
        cursoClient.eliminarCursoUsuarioPorId(id);
    }

    @Override
    public Optional<Usuario> obtenerUsuarioPorEmail(String email) {
        return Optional.empty();
    }

    @Override
    public List<Usuario> listarUsuariosPorId(Iterable<Long> ids) {
        return (List<Usuario>) repository.findAllById(ids);
    }



}
