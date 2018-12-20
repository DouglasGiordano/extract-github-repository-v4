package br.edu.ufsm.requestpostgraphql.repository;

import br.edu.ufsm.requestpostgraphql.entity.Issue;
import br.edu.ufsm.requestpostgraphql.entity.PersistenceGithub;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author Douglas Giordano
 * @since 18/12/2018
 */
public class IssueRepository {

    public boolean save(List<Issue> issues) {
        List<Issue> lista = issues;
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
