package com.springcloud.msvc.models.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "cursos_usuarios")
@Getter
@Setter
public class CursoUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario_id", unique=true)
    private Long usuarioId;

    @Column(name = "curso_id")
    private Long cursoId;


    @Override
    public boolean equals(Object o) {
        if(this == o) return true;

        if(!(o instanceof CursoUsuario)) return false;

        CursoUsuario obj = (CursoUsuario) o;
        return this.usuarioId != null && this.usuarioId.equals(obj.usuarioId);
    }

}
