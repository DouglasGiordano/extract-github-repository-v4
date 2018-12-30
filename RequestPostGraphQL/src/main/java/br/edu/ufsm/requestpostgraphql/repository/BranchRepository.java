package br.edu.ufsm.requestpostgraphql.repository;

import br.edu.ufsm.requestpostgraphql.entity.Branch;
import br.edu.ufsm.requestpostgraphql.entity.BranchCommit;
import br.edu.ufsm.requestpostgraphql.entity.PersistenceGithub;
import br.edu.ufsm.requestpostgraphql.entity.Repository;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author Douglas Giordano
 */
public class BranchRepository {

    public List<Branch> getBranchIncomplete(Repository r) {
        EntityManager manager = PersistenceGithub.openEntityManager();
        CriteriaBuilder cb = manager.getCriteriaBuilder();

        CriteriaQuery<Branch> q = cb.createQuery(Branch.class);
        Root<Branch> c = q.from(Branch.class);
        q.select(c);
        Predicate owner = cb.equal(c.get("owner"), r.getOwner());
        Predicate name = cb.equal(c.get("repository"), r.getName());
        Predicate notcomplete = cb.equal(c.get("complete"), false);
        q.where(cb.and(owner, name, notcomplete));
        TypedQuery<Branch> query = manager.createQuery(q);
        List<Branch> results = query.getResultList();
        return results;
    }

    public Branch getBranch(String name, Repository r) {
        EntityManager manager = PersistenceGithub.openEntityManager();
        CriteriaBuilder cb = manager.getCriteriaBuilder();

        CriteriaQuery<Branch> q = cb.createQuery(Branch.class);
        Root<Branch> c = q.from(Branch.class);
        q.select(c);
        Predicate ownerP = cb.equal(c.get("owner"), r.getOwner());
        Predicate nameP = cb.equal(c.get("repository"), r.getName());
        Predicate idP = cb.equal(c.get("name"), name);
        q.where(cb.and(ownerP, nameP, idP));
        TypedQuery<Branch> query = manager.createQuery(q);
        try {
            Branch result = query.getSingleResult();
            return result;
        } catch (NoResultException ex) {
            return null;
        }
    }

    public Branch getBranchDefault(Repository r) {
        EntityManager manager = PersistenceGithub.openEntityManager();
        CriteriaBuilder cb = manager.getCriteriaBuilder();

        CriteriaQuery<Branch> q = cb.createQuery(Branch.class);
        Root<Branch> c = q.from(Branch.class);
        q.select(c);
        Predicate ownerP = cb.equal(c.get("owner"), r.getOwner());
        Predicate nameP = cb.equal(c.get("repository"), r.getName());
        Predicate defaultRepository = cb.equal(c.get("defaultRepository"), true);
        q.where(cb.and(ownerP, nameP, defaultRepository));
        TypedQuery<Branch> query = manager.createQuery(q);
        try {
            Branch result = query.getSingleResult();
            return result;
        } catch (NoResultException ex) {
            return null;
        }
    }
    
    public boolean save(List<Branch> lista) {
        EntityManager manager = PersistenceGithub.openEntityManager();
        manager.getTransaction().begin();
        lista.forEach((p) -> {
            manager.merge(p);
        });
        manager.getTransaction().commit();
        manager.clear();
        return true;
    }

    public boolean save(Branch c) {
        EntityManager manager = PersistenceGithub.openEntityManager();
        manager.getTransaction().begin();
        manager.merge(c);
        manager.getTransaction().commit();
        manager.clear();
        return true;
    }

    public boolean save(BranchCommit c) {
        EntityManager manager = PersistenceGithub.openEntityManager();
        manager.getTransaction().begin();
        manager.merge(c);
        manager.getTransaction().commit();
        manager.clear();
        return true;
    }
}
