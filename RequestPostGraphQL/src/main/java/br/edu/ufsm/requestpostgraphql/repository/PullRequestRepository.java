package br.edu.ufsm.requestpostgraphql.repository;

import br.edu.ufsm.requestpostgraphql.entity.PersistenceGithub;
import br.edu.ufsm.requestpostgraphql.entity.PullRequest;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author Douglas Giordano
 * @since 19/12/2018
 */
public class PullRequestRepository {

    public boolean save(List<PullRequest> pulls) {
        List<PullRequest> lista = pulls;
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
