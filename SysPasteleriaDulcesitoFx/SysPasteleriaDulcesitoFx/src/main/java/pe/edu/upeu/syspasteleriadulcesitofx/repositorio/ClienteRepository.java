package pe.edu.upeu.syspasteleriadulcesitofx.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upeu.syspasteleriadulcesitofx.modelo.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, String> {
}
