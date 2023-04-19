package com.education.paygoal.repository;

import com.education.paygoal.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ProductoRepository extends JpaRepository<Producto,Long> {

    @Query(value = "SELECT * FROM producto WHERE nombre = :nombre",nativeQuery = true)
    List<Producto> encontrarProductoPorNombre(@Param("nombre")String nombre);

    @Query(value = "SELECT * FROM producto WHERE id = :id",nativeQuery = true)
    Producto encontrarEmpleadoPorId(@Param("id")Long id);

    @Query(value = "SELECT id,precio,nombre,descripcion,cantidad FROM producto ORDER BY precio",nativeQuery = true)
    List<Producto> findByOrdenByPrecio();

}
