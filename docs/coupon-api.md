## Coupon API

- Esta solución fue diseñada usando una arquitectura Ports & Adapters.
  - Deberia facilitar futuros improvements sin afectar la logica de negocio.
  
- Se uso el stack de Spring boot reactive para mejorar el uso de recursos y escalamiento.
 
- El core de la app se encuentra en el package domain
  - Para el algoritmo de calculo de items se siguio un aproach basico (SimpleMaxValueItemResolver), sin embargo 
    se deja como punto de extension para incluir mas algoritmos.

- Para el despliegue en AWS se uso una imagen de docker y fargate.