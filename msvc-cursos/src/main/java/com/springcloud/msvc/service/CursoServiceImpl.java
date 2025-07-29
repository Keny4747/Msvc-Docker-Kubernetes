package com.springcloud.msvc.service;

import com.springcloud.msvc.clients.UsuarioClientRest;
import com.springcloud.msvc.models.Usuario;
import com.springcloud.msvc.models.entity.Curso;
import com.springcloud.msvc.models.entity.CursoUsuario;
import com.springcloud.msvc.repositories.CursoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CursoServiceImpl implements CursoService{

    private final CursoRepository repository;
    private final UsuarioClientRest client;

    @Override
    public List<Curso> listarCursos() {
        return (List<Curso>) repository.findAll();
    }

    @Override
    public Optional<Curso> obtenerCursoPorId(Long id) {
        return repository.findById(id);
    }

    @Override
    public Curso crearCurso(Curso curso) {
        return repository.save(curso);
    }

    @Override
    public void eliminarCurso(Long id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional
    public Optional<Usuario> asignarUsuario(Long idCurso, Usuario usuario) {

        Optional<Curso> o = repository.findById(idCurso);
        log.info("curso: {}", o.get().getNombre());

        log.info("Asignando usuario {} al curso {}", usuario.getId(), idCurso);
        if(o.isPresent()){
            Usuario usuarioMsvc = client.obtenerUsuarioPorId(usuario.getId());
            Curso curso = o.get();
        log.info("Curso encontrado: {}", curso.getNombre());
        log.info("Usuario encontrado: {}", usuarioMsvc.getNombre());
            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setUsuarioId(usuarioMsvc.getId());

            curso.addCursoUsuario(cursoUsuario);
            repository.save(curso);

            return Optional.of(usuarioMsvc);
        }

        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<Usuario> crearUsuario(Long idCurso, Usuario usuario) {
        Optional<Curso> o = repository.findById(idCurso);


        if(o.isPresent()){
            Usuario usuarioMsvc = client.crearUsuario(usuario);
            Curso curso = o.get();

            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setUsuarioId(usuarioMsvc.getId());

            curso.addCursoUsuario(cursoUsuario);
            repository.save(curso);

            return Optional.of(usuarioMsvc);
        }

        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<Usuario> eliminarUsuario(Long idCurso, Usuario usuario) {
        Optional<Curso> o = repository.findById(idCurso);

        if(o.isPresent()){
            Usuario usuarioMsvc = client.obtenerUsuarioPorId(usuario.getId());

            Curso curso = o.get();
            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setUsuarioId(usuarioMsvc.getId());

            curso.remove(cursoUsuario);
            repository.save(curso);

            return Optional.of(usuarioMsvc);
        }

        return Optional.empty();
    }

    @Override
    public Optional<Curso> porIdConUsuarios(Long id) {
        Optional<Curso> curso = repository.findById(id);
        if (curso.isPresent()) {
            Curso cursoFound = curso.get();
            List<Long> ids = cursoFound.getCursoUsuarios().stream()
                    .map(CursoUsuario::getId)
                    .collect(Collectors.toList());
            log.info("Ids de usuarios asociados al curso {}: {}", cursoFound.getId(), ids);
            List<Usuario> usuarios = client.listarUsuariosPorId(ids);

          cursoFound.setUsuarios(usuarios);
          return Optional.of(cursoFound);
        }

        return Optional.empty();
    }

    @Override
    public void eliminarCursoUsuarioPorId(Long id) {
        repository.eliminarCursoUsuarioPorId(id);

    }
}
