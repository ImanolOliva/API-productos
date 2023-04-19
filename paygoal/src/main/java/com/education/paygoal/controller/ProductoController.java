package com.education.paygoal.controller;


import com.education.paygoal.Errores.Errores;
import com.education.paygoal.Errores.ListaVacia;
import com.education.paygoal.Errores.ValidacionLetras;
import com.education.paygoal.model.Producto;
import com.education.paygoal.repository.ProductoRepository;
import com.education.paygoal.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api")
public class ProductoController {

    @Autowired
    ProductoService productoService;

    @Autowired
    ProductoRepository repository;

    /**
     *
     * @param producto
     * @return ResponseEntity
     * Recibe por @RequestBody un Producto desde Postman en Formato JSON
     * lo valida y lo retorna segun resultado.
     */
    @RequestMapping(
            method = RequestMethod.POST,
            path = "/producto",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Producto> crearProducto(@RequestBody Producto producto){
        try{
            if(productoService.postProducto(producto) == 0){
                throw new ValidacionLetras();
            }
            return new ResponseEntity("Producto cargado", HttpStatusCode.valueOf(200));
        }catch(Errores errores){
            return new ResponseEntity(errores.getMessage(), HttpStatusCode.valueOf(400));
        }catch(Exception exception){
            return new ResponseEntity(exception.getMessage(), HttpStatusCode.valueOf(400));
        }
    }


    /**
     *
     * @param id
     * @param producto
     * @return ResponseEntity
     * Recibe un ID desde Postam y segun si existe ese ID
     * retorna si el producto esta registrado
     */
    @RequestMapping(
            method = RequestMethod.GET,
            path = "/producto/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE

    )
    public ResponseEntity<?> obtenerProductoPorId(@PathVariable("id") Long id,Producto producto){
        try{
            if(productoService.getIdProducto(id)== null){
                throw new Throwable("El producto no existe");
            }
             producto = productoService.getIdProducto(id);
            return new ResponseEntity<>(producto,HttpStatusCode.valueOf(200));
        }catch (Throwable throwable){
            return new ResponseEntity<>(throwable.getMessage(),HttpStatusCode.valueOf(400));
        }
    }

    /**
     *
     * @param nombre
     * @param producto
     * @return ResponseEntity
     * Recibe un nombre desde postman, y segun si existe ese nombre
     * retorna si el producto esta registrado.
     *
     * ACLARACION: Se agrega el path "/nombre" por un problema
     * de ambiguedad con el metodo get que busca por ID.
     * Spring no lo puede procesar.
     */
    @RequestMapping(
            method = RequestMethod.GET,
            path = "/productos/nombre/{nombre}",
            produces = MediaType.APPLICATION_JSON_VALUE

    )
    public ResponseEntity<?> obtenerProductoPorNombre(@PathVariable("nombre") String nombre,Producto producto){
        try{
            if(productoService.getNombreProducto(nombre) == null){
                throw new Errores("El producto no existe");
            }
            List<Producto> productoList = productoService.getNombreProducto(nombre);
            return new ResponseEntity<>(productoList,HttpStatusCode.valueOf(200));

        }catch (Errores errores){
            return new ResponseEntity<>(errores.getMessage(),HttpStatusCode.valueOf(400));
        }
    }

    /**
     *
     * @return List de productos
     * Recorre la lista de productos y los muestra
     */
    @RequestMapping(
            method = RequestMethod.GET,
            path = "/producto",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<Producto> listarProductos(){
        List<Producto> list = productoService.listaProductos();
        return  list;
    }


    /**
     *
     * @param id
     * @param producto
     * @return ResponseEntity
     * Recibe un ID, y un producto.
     * Se comunica con la capa servicio.
     * Este devuelve si existe tal registro para poder actualizarlo.
     * Si existe, lo actualiza y lo devuelve actualizado.
     */
    @RequestMapping(
            method = RequestMethod.PUT,
            path = "/producto/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Optional> actualizarProductos(@Valid @PathVariable("id")Long id, @RequestBody Producto producto)
    {
        try{
            if(this.productoService.updateProductos(producto,id) == false){
                    throw  new Exception("El pedido no pudo ser actualizado");
            }

            return new ResponseEntity(producto,HttpStatusCode.valueOf(200));
        }catch(Exception exception){
            return new ResponseEntity(exception.getMessage(),HttpStatusCode.valueOf(400));
        }
    }

    /**
     *
     * @param id
     * @return ResponseEntity
     * Recibe un ID desde Postman.
     * Se comunica con la capa servicio para desarrollar la logica de si existe o no.
     * En base a lo que responda el servicio borra el ID, o muestra mensaje error.
     */
    @RequestMapping(
            method = RequestMethod.DELETE,
            path = "/productos/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE

    )
    public ResponseEntity<?> borrarProducto(@PathVariable("id") Long id){
        try{
            if(productoService.deleteProducto(id) == null){
                throw new Throwable("El producto no existe");
            }
            return new ResponseEntity<>("Producto eliminado",HttpStatusCode.valueOf(200));
        }catch (Throwable throwable){
            return new ResponseEntity<>(throwable.getMessage(),HttpStatusCode.valueOf(400));
        }
    }


    /***
     *
     * @return ResponseEntity
     * findByOrdenByPrecio() devuelve mediante una consulta SQL
     * en la capa repository los productos ordenados por precio de manera ascendente.
     * retorna la lista ordenada de manera ascendiente
     */

    @RequestMapping(
            method = RequestMethod.GET,
            path = "/productosOrdenados"
    )
    public ResponseEntity<List<?>> ordenarProductos(){
        try{
            List<Producto> listResponse = repository.findByOrdenByPrecio();
            if(listResponse.isEmpty()){
                throw new ListaVacia();
            }
            return  ResponseEntity.ok(listResponse);
        }catch(Errores errores){
            return new  ResponseEntity(errores.getMessage(),HttpStatusCode.valueOf(400));
        }

    }

}