
package br.edu.ufsm.requestpostgraphql.repository;

import br.edu.ufsm.requestpostgraphql.entity.IssueComment;
import br.edu.ufsm.requestpostgraphql.entity.PersistenceGithub;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author Douglas Giordano
 * @since 19/12/2018
 */
public class IssueCommentRepository {
    
    public boolean save(List<IssueComment> comments) {
        List<IssueComment> lista = comments;
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
