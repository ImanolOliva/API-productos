package com.education.paygoal.service;


import com.education.paygoal.errores.Errores;
import com.education.paygoal.model.Producto;
import com.education.paygoal.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {


    @Autowired
    ProductoRepository productoRepository;


    /**
     *
     * @param producto
     * @return Integer
     * Verifica que el nombre y la descripcion esten correctamente escritos.
     * Guarda el producto que llega a la base de datos
     * Retorna 1 = salio todo ok
     * Retorna 2 = error en el ingreso de datos
     */

    public Integer postProducto(Producto producto){
        try{
            if(soloLetras(producto.getNombre()) == false ||
               producto.getDescripcion().isEmpty() || producto.getNombre().isEmpty()){
                    throw new Errores("");
            }
            this.productoRepository.save(producto);
            return 1;
        }catch(Errores errores){
            errores.getMessage();
            return 0;
        }
    }

    /**
     *
     *
     * @param nombre
     * @return Producto
     * Recibe un nombre y lo envia a la capa Repository para
     * verificar si existe en los registros.
     * Retorna el nombre en caso de ser valido.
     *
     * Se trabaja en una Lista por si hay productos repetidos con el mismo nombre
     * pero con distinta descripcion
     *
     */

    public  List<Producto> getNombreProducto(String nombre){
        try{
        if(nombre.isEmpty() || nombre == null){
            throw  new Errores("El nombre no es valido");
        }
        return this.productoRepository.encontrarProductoPorNombre(nombre);
    }catch(Errores errores){
            errores.getMessage();
            return null;
        }
    }

    /**
     *
     * @param id
     * @return Producto
     *  Recibe un id y lo envia a la capa Repository para
     *   verificar si existe en los registros. De ser asi,
     *   retorna el Producto vacio en caso de ser valido.
     *
     */
    public Producto deleteProducto(Long id){
        try{
            Producto producto = this.productoRepository.encontrarEmpleadoPorId(id);
            if(producto.getId() == null){
                throw new Throwable("El Producto no existe");
            }
            this.productoRepository.deleteById(producto.getId());
            return producto;
        }catch(Throwable tr){
            tr.getMessage();
            return null;
        }
    }

    /**
     *
     * @param id
     * @return Producto
     * Recibe un Id y lo envia a la capa repositorio
     * para verificar si existe o no.
     * En caso que exista retorna el producto con el ID que se envio por parametro.
     *
     */
    public Producto getIdProducto(Long id) {
      try{
          Producto producto = this.productoRepository.encontrarEmpleadoPorId(id);
          if(producto.getId() == null){
              throw new Throwable("El Producto no existe");
          }
          this.productoRepository.findById(id).get();
          return producto;
      }catch(Throwable tr){
          tr.getMessage();
          return  null;
      }
    }

    /**
     *
     * @param cadena
     * @return booleano
     * Verifica que los caracteres ingresados sean validos.
     * Por ejemplo en nombres y descripcion acepte solo letras.
     *
     */
    public boolean soloLetras(String cadena){
        for (int i = 0; i < cadena.length(); i++)
        {
            char caracter = cadena.toUpperCase().charAt(i);
            int valorASCII = (int)caracter;
            if (valorASCII != 165 && (valorASCII < 65 || valorASCII > 90) && valorASCII != 32)
                return false;
        }
        return true;
    }

    /**
     *
     * @param producto
     * @param id
     * @return booleano
     * Recibe un producto y un ID.
     * Envia el ID a la capa repositorio para verificar que exista.
     * Si existe. Actualiza los campos con los nuevos datos ingresados.
     * Guarda los nuevos datos con el metodo Save.
     * Retorna un booleano a la capa Controller
     */
    public boolean updateProductos(Producto producto, Long id){
        try{
            Producto updateProducto = this.productoRepository.findById(id).get();

            updateProducto.setCantidad(producto.getCantidad());
            updateProducto.setDescripcion(producto.getDescripcion());
            updateProducto.setNombre(producto.getNombre());
            updateProducto.setPrecio(producto.getPrecio());

            this.productoRepository.save(updateProducto);
            return true;
        }catch(Exception exception){
            return false;
        }
    }

    /**
     *
     * @return Lista Productos
     * Envia la lista de productos vacia a la capa repositorio
     * Retorna la lista de productos con datos a la capa controller
     */
    public List<Producto> listaProductos(){
        List<Producto> list = this.productoRepository.findAll();
        return  list;
    }







}

