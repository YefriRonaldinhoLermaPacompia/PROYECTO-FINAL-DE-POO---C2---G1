package pe.edu.upeu.syspasteleriadulcesitofx.servicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upeu.syspasteleriadulcesitofx.modelo.Cliente;
import pe.edu.upeu.syspasteleriadulcesitofx.repositorio.ClienteRepository;

import java.util.List;

@Service
public class ClienteService {

    @Autowired
    ClienteRepository repo;

    public Cliente save(Cliente cliente) {
        return repo.save(cliente);
    }

    public List<Cliente> list() {
        return repo.findAll();
    }

    public Cliente update(Cliente cliente, String dniruc) {
        Cliente clienteExistente = repo.findById(dniruc).orElse(null);
        if (clienteExistente != null) {
            clienteExistente.setNombres(cliente.getNombres());
            clienteExistente.setRepLegal(cliente.getRepLegal());
            clienteExistente.setTipoDocumento(cliente.getTipoDocumento());
            return repo.save(clienteExistente);
        }
        return null;
    }

    public void delete(String dniruc) {
        repo.deleteById(dniruc);
    }

    public Cliente searchById(String dniruc) {
        return repo.findById(dniruc).orElse(null);
    }
}