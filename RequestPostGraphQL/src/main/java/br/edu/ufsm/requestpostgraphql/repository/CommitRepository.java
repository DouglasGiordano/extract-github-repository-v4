package br.edu.ufsm.requestpostgraphql.repository;

import br.edu.ufsm.requestpostgraphql.entity.Commit;
import br.edu.ufsm.requestpostgraphql.entity.PersistenceGithub;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author Douglas Giordano
 */
public class CommitRepository {

    public boolean save(List<Commit> lista) {
        EntityManager manager = PersistenceGithub.openEntityManager();
        manager.getTransaction().begin();
        lista.forEach((p) -> {
            manager.merge(p);
        });
        manager.getTransaction().commit();
        manager.clear();
        return true;
    }
}
