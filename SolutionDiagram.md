# Diagrama de la Solución

El diagrama a continuación ilustra la arquitectura y el flujo de datos de la solución.

                                +-------------------------+
                                |                         |
                                |   Aplicación Cliente    |
                                |                         |
                                +------------+------------+
                                             |
                                             |
                                  POST       |    in: UserRequest / out: UserResponse
                                /nisum/user  |
                                             |
                                             v
                                +-------------------------+
                                |                         |
                                |   REST Controller       |
                                |                         |
                                +------------+------------+
                                             |
                                             |
                                 Objeto de   |      in/out: user
                                dominio      |
                                             v
                                +-------------------------+
                                |                         |
                                |       Caso de uso       |
                                |                         |
                                +------------+------------+
                                             |
                                             |
                                  Objeto de  |      in/out: user
                                dominio      |
                                             |
                                             v
                                +-------------------------+
                                |                         |
                                | Servicio implementación |
                                |       caso de uso       |
                                +------------+------------+
                                             |
                                             |
                                  Objeto     |      in/out: user
                                   de        |
                                Dominio      |
                                             |
                                             v
                                +-------------------------+
                                |                         |
                                |   Puerto de persistencia|
                                |                         |
                                +------------+------------+
                                             |
                                     Objeto  |
                                   de        |      in/out: user
                                Dominio      |
                                             |
                                             v
                                +-------------------------+
                                |                         |
                                |   Proveedor de acceso   |
                                |       a datos (H2)      |
                                +------------+------------+
                                             |
                            Objeto de Acceso |
                                 a Datos     |      in/out: user
                                             |
                                             |
                                             v
                                +-------------------------+
                                |                         |
                                |   Base de Datos H2      |
                                |                         |
                                +-------------------------+

Este diagrama proporciona una visualización más clara de la solución. Representa el flujo de datos e interacciones entre
la aplicación cliente, el controlador REST, el manejador de casos de uso, el puerto de persistencia, los repositorios 
y la base de datos H2.


