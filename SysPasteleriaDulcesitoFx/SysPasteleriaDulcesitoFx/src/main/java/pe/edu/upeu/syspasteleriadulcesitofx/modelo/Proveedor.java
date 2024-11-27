package pe.edu.upeu.syspasteleriadulcesitofx.modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "upeu_proveedor")
public class Proveedor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_proveedor")
    private Long idProveedor;
    @Column(name = "dniruc", nullable = false, length = 12)
    private String dniRuc;
    @Column(name = "nombres_raso", nullable = false, length = 120)
    private String nombresRaso;
    @Column(name = "razon_social", nullable = false, length = 12)
    private String razonSosial;
    @Column(name = "telefono", nullable = false, length = 10)
    private String telefono;
    @Column(name = "direccion", nullable = false, length = 120)
    private String direccion;
}