package br.edu.ufsm.requestpostgraphql.repository;

import br.edu.ufsm.requestpostgraphql.entity.PersistenceGithub;
import br.edu.ufsm.requestpostgraphql.entity.Status;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Douglas Giordano
 * @since 18/12/2018
 */
public class StatusIssuePullRepository {
    
    public Status find(String owner, String name) {
        EntityManager manager = PersistenceGithub.openEntityManager();
        CriteriaBuilder builder = manager.getCriteriaBuilder();

        CriteriaQuery<Status> criteria;
        criteria = builder.createQuery(Status.class);

        Root<Status> root = criteria.from(Status.class);

        criteria.where(
                builder.equal(root.get("owner"), owner),
                builder.equal(root.get("name"), name)
        );
        try {
            Status status = manager
                    .createQuery(criteria)
                    .getSingleResult();
            return status;
        } catch (javax.persistence.NoResultException ex) {
            return null;
        }

    }
    
    
    public Status save(Status status) {
        EntityManager manager = PersistenceGithub.openEntityManager();
        manager.getTransaction().begin();
        status = manager.merge(status);
        manager.getTransaction().commit();
        return status;
    }
}
