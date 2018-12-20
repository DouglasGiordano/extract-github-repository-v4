package br.edu.ufsm.requestpostgraphql.repository;

import br.edu.ufsm.requestpostgraphql.entity.PersistenceGithub;
import br.edu.ufsm.requestpostgraphql.entity.PullComment;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author Douglas Giordano
 * @since 19/12/2018
 */
public class PullRequestCommentRepository {

    public boolean save(List<PullComment> comments) {
        List<PullComment> lista = comments;
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
