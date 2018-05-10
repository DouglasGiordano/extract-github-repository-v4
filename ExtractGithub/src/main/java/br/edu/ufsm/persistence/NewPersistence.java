package br.edu.ufsm.persistence;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

/**
 * Classe resolve os métodos básicos de cadastro (CRUD) com API da
 * <code>JPA</code>.
 *
 * @author Miguel
 * @author Luciano
 * @author Douglas v 1.1 alter criteria to criteria builder
 */
public abstract class NewPersistence<T extends EntityBD, PK extends Object> {

//    @PersistenceContext
//    private EntityManager entityManager;
    protected T object;

    public abstract void init();

    public Class getObjectClass() {
        return getObject().getClass();
    }

    public T findByCriteria(String column, Object value) {
        CriteriaBuilder builder = getEntity().getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery();
        Root from = query.from(this.getObjectClass());
        query.select(from);
        query.where(addCriteriaQueryEq(column, value, query, builder, from));
        TypedQuery qB = getEntity().createQuery(query);
        return (T) qB.getSingleResult();
    }

    public Predicate addCriteriaQueryEq(String column, Object value, CriteriaQuery query, CriteriaBuilder builder, Root from) {
        String campo = column;
        Object valor = value;
        if (campo.contains(".")) {
            String[] fields = campo.split("\\.");
            Path now = from;
            for (String field : fields) {
                now = now.get(field);
            }
            return builder.equal(now, valor);
        } else {
            return builder.equal(from.get(campo), valor);
        }
    }

    public Predicate addCriteriaQueryIn(String column, List values, CriteriaQuery query, CriteriaBuilder builder, Root from) {
        if (column.contains(".")) {
            String[] fields = column.split("\\.");
            Path now = from;
            for (String field : fields) {
                now = now.get(field);
            }
            return now.in(values);
        } else {
            return from.get(column).in(values);
        }
    }

    public void addCriteriaQueryOrderAsc(String column, CriteriaQuery query, CriteriaBuilder builder, Root from) {
        if (column.contains(".")) {
            String[] fields = column.split("\\.");
            Path now = from;
            for (String field : fields) {
                now = now.get(field);
            }
            query.orderBy(builder.asc(now));
        } else {
            query.orderBy(builder.asc(from.get(column)));
        }
    }

    public void addCriteriaQueryOrderAsc(List<String> orders, CriteriaQuery query, CriteriaBuilder builder, Root from) {
        List<Order> predicates = new ArrayList<>();
        orders.forEach((column) -> {
            if (column.contains(".")) {
                String[] fields = column.split("\\.");
                Path now = from;
                for (String field : fields) {
                    now = now.get(field);
                }
                predicates.add(builder.asc(now));
            } else {
                predicates.add(builder.asc(from.get(column)));
            }

        });
        query.orderBy(predicates);
    }

    public void addCriteriaQueryOrder(Entry<String, EnumOrder> entry, CriteriaQuery query, CriteriaBuilder builder, Root from) {
        String column = entry.getKey();
        EnumOrder methodOrder = entry.getValue();
        Path columnPath = null;
        if (column.contains(".")) {
            String[] fields = column.split("\\.");
            Path now = from;
            for (String field : fields) {
                now = now.get(field);
            }
            columnPath = now;
        } else {
            columnPath = from.get(column);
        }
        if (methodOrder == EnumOrder.ASC) {
            query.orderBy(builder.asc(columnPath));
        } else if (methodOrder == EnumOrder.DESC) {
            query.orderBy(builder.desc(columnPath));
        }
    }

    public void addCriteriaQueryOrder(HashMap<String, EnumOrder> orders, CriteriaQuery query, CriteriaBuilder builder, Root from) {
        List<Order> predicates = new ArrayList<>();
        orders.entrySet().forEach((entry) -> {
            String column = entry.getKey();
            EnumOrder methodOrder = entry.getValue();
            Path columnPath = null;
            if (column.contains(".")) {
                String[] fields = column.split("\\.");
                Path now = from;
                for (String field : fields) {
                    now = now.get(field);
                }
                columnPath = now;
            } else {
                columnPath = from.get(column);
            }
            if (methodOrder == EnumOrder.ASC) {
                predicates.add(builder.asc(columnPath));
            } else if (methodOrder == EnumOrder.DESC) {
                predicates.add(builder.desc(columnPath));
            }
        });
        query.orderBy(predicates);
    }

    public void addCriteriaQueryOrderDesc(String column, CriteriaQuery query, CriteriaBuilder builder, Root from) {
        if (column.contains(".")) {
            String[] fields = column.split("\\.");
            Path now = from;
            for (String field : fields) {
                now = now.get(field);
            }
            query.orderBy(builder.desc(now));
        } else {
            query.orderBy(builder.desc(from.get(column)));
        }
    }

    public void addCriteriaQueryOrderDesc(List<String> orders, CriteriaQuery query, CriteriaBuilder builder, Root from) {
        List<Order> predicates = new ArrayList<>();
        orders.forEach((column) -> {
            if (column.contains(".")) {
                String[] fields = column.split("\\.");
                Path now = from;
                for (String field : fields) {
                    now = now.get(field);
                }
                predicates.add(builder.asc(now));
            } else {
                predicates.add(builder.desc(from.get(column)));
            }

        });
        query.orderBy(predicates);
    }

    public Object returnObjectSingleResult(TypedQuery query) {
        try {
            return query.setMaxResults(1).getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    public List listByCriteria(String column, Object value, Class myClass) {
        CriteriaBuilder builder = getEntity().getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery();
        Root from = query.from(myClass);
        query.select(from);
        query.where(addCriteriaQueryEq(column, value, query, builder, from));
        TypedQuery qB = getEntity().createQuery(query);
        return qB.getResultList();
    }

    public List listByCriteria(String columnEq, Object valueEq, String columnIn, List values, Class myClass) {
        CriteriaBuilder builder = getEntity().getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery();
        Root from = query.from(myClass);
        query.select(from);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(addCriteriaQueryEq(columnEq, valueEq, query, builder, from));
        predicates.add(addCriteriaQueryIn(columnIn, values, query, builder, from));
        query.where(predicates.toArray(new Predicate[]{}));
        TypedQuery qB = getEntity().createQuery(query);
        return qB.getResultList();
    }

    public List listByCriteria(Class myClass, String order) {
        CriteriaBuilder builder = getEntity().getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery();
        Root from = query.from(myClass);
        query.select(from);
        addCriteriaQueryOrderAsc(order, query, builder, from);
        TypedQuery qB = getEntity().createQuery(query);
        return qB.getResultList();
    }

    public List listByCriteria(HashMap<String, Object> filtros, Class classe) {
        CriteriaBuilder builder = getEntity().getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery();
        Root from = query.from(classe);
        query.select(from);
        List<Predicate> predicates = new ArrayList<>();
        filtros.entrySet().forEach((entry) -> {
            predicates.add(addCriteriaQueryEq(entry.getKey(), entry.getValue(), query, builder, from));
        });
        query.where(predicates.toArray(new Predicate[]{}));
        TypedQuery qB = getEntity().createQuery(query);
        return qB.getResultList();
    }

    public List listByCriteriaDesc(Class myClass, String order) {
        CriteriaBuilder builder = getEntity().getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery();
        Root from = query.from(myClass);
        query.select(from);
        addCriteriaQueryOrderDesc(order, query, builder, from);
        TypedQuery qB = getEntity().createQuery(query);
        return qB.getResultList();
    }

    public List listByCriteria(String column, Object value, Class myClass, String order) {
        CriteriaBuilder builder = getEntity().getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery();
        Root from = query.from(myClass);
        query.select(from);
        query.where(addCriteriaQueryEq(column, value, query, builder, from));
        addCriteriaQueryOrderAsc(order, query, builder, from);
        TypedQuery qB = getEntity().createQuery(query);
        return qB.getResultList();
    }

    public List listByCriteriaDesc(String column, Object value, Class myClass, String order) {
        CriteriaBuilder builder = getEntity().getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery();
        Root from = query.from(myClass);
        query.select(from);
        query.where(addCriteriaQueryEq(column, value, query, builder, from));
        addCriteriaQueryOrderDesc(order, query, builder, from);
        TypedQuery qB = getEntity().createQuery(query);
        return qB.getResultList();
    }

    public List listByCriteria(HashMap<String, Object> filtros, Class myClass, String order, String columnBetween, Date start, Date end) {
        CriteriaBuilder builder = getEntity().getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery();
        Root from = query.from(myClass);
        query.select(from);
        List<Predicate> predicates = new ArrayList<>();
        filtros.entrySet().forEach((entry) -> {
            predicates.add(addCriteriaQueryEq(entry.getKey(), entry.getValue(), query, builder, from));
        });
        predicates.add(builder.between(from.get(columnBetween), start, end));
        query.where(predicates.toArray(new Predicate[]{}));
        addCriteriaQueryOrderDesc(order, query, builder, from);

        TypedQuery qB = getEntity().createQuery(query);
        return qB.getResultList();
    }

    public List listByCriteria(HashMap<String, Object> filtros, Class myClass, HashMap<String, EnumOrder> orders, String columnBetween, Date start, Date end) {
        CriteriaBuilder builder = getEntity().getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery();
        Root from = query.from(myClass);
        query.select(from);
        List<Predicate> predicates = new ArrayList<>();
        filtros.entrySet().forEach((entry) -> {
            predicates.add(addCriteriaQueryEq(entry.getKey(), entry.getValue(), query, builder, from));
        });
        predicates.add(builder.between(from.get(columnBetween), start, end));
        query.where(predicates.toArray(new Predicate[]{}));
        addCriteriaQueryOrder(orders, query, builder, from);
        TypedQuery qB = getEntity().createQuery(query);
        return qB.getResultList();
    }

    public List listByCriteriaDesc(String column, Object value, Class myClass, String order, String columnBetween, Date start, Date end) {
        CriteriaBuilder builder = getEntity().getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery();
        Root from = query.from(myClass);
        query.select(from);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(addCriteriaQueryEq(column, value, query, builder, from));
        predicates.add(builder.between(from.get(columnBetween), start, end));
        query.where(predicates.toArray(new Predicate[]{}));
        addCriteriaQueryOrderDesc(order, query, builder, from);
        TypedQuery qB = getEntity().createQuery(query);
        return qB.getResultList();
    }

    public List listByCriteria(String column, Object value, Class myClass, String columnBetween, Date start, Date end) {
        CriteriaBuilder builder = getEntity().getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery();
        Root from = query.from(myClass);
        query.select(from);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(addCriteriaQueryEq(column, value, query, builder, from));
        predicates.add(builder.between(from.get(columnBetween), start, end));
        query.where(predicates.toArray(new Predicate[]{}));
        TypedQuery qB = getEntity().createQuery(query);
        return qB.getResultList();
    }

    public List listByCriteriaDesc(String columnEq, Object value, String columnIn, List valuesIn, Class myClass, List<String> orders, String columnBetween, Date start, Date end) {
        CriteriaBuilder builder = getEntity().getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery();
        Root from = query.from(myClass);
        query.select(from);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(addCriteriaQueryEq(columnEq, value, query, builder, from));
        predicates.add(addCriteriaQueryIn(columnIn, valuesIn, query, builder, from));
        predicates.add(builder.between(from.get(columnBetween), start, end));
        query.where(predicates.toArray(new Predicate[]{}));
        addCriteriaQueryOrderDesc(orders, query, builder, from);
        TypedQuery qB = getEntity().createQuery(query);
        return qB.getResultList();
    }

    public List listByCriteriaDesc(HashMap<String, Object> filtros, Class myClass, List<String> orders, String columnBetween, Date start, Date end) {
        CriteriaBuilder builder = getEntity().getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery();
        Root from = query.from(myClass);
        query.select(from);
        List<Predicate> predicates = new ArrayList<>();
        filtros.entrySet().forEach((entry) -> {
            predicates.add(addCriteriaQueryEq(entry.getKey(), entry.getValue(), query, builder, from));
        });
        predicates.add(builder.between(from.get(columnBetween), start, end));
        query.where(predicates.toArray(new Predicate[]{}));
        addCriteriaQueryOrderDesc(orders, query, builder, from);
        TypedQuery qB = getEntity().createQuery(query);
        return qB.getResultList();
    }

    public List listByCriteriaDesc(HashMap<String, Object> filtros, Class myClass, String columnBetween, Date start, Date end) {
        CriteriaBuilder builder = getEntity().getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery();
        Root from = query.from(myClass);
        query.select(from);
        List<Predicate> predicates = new ArrayList<>();
        filtros.entrySet().forEach((entry) -> {
            predicates.add(addCriteriaQueryEq(entry.getKey(), entry.getValue(), query, builder, from));
        });
        predicates.add(builder.between(from.get(columnBetween), start, end));
        query.where(predicates.toArray(new Predicate[]{}));
        TypedQuery qB = getEntity().createQuery(query);
        return qB.getResultList();
    }

    public List listByCriteriaDesc(HashMap<String, Object> filtros, Class myClass, String order) {
        CriteriaBuilder builder = getEntity().getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery();
        Root from = query.from(myClass);
        query.select(from);
        List<Predicate> predicates = new ArrayList<>();
        filtros.entrySet().forEach((entry) -> {
            predicates.add(addCriteriaQueryEq(entry.getKey(), entry.getValue(), query, builder, from));
        });
        query.where(predicates.toArray(new Predicate[]{}));
        addCriteriaQueryOrderDesc(order, query, builder, from);
        TypedQuery qB = getEntity().createQuery(query);
        return qB.getResultList();
    }

    public List listByCriteria(String column, Object value, Class myClass, List<String> orders) {
        CriteriaBuilder builder = getEntity().getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery();
        Root from = query.from(myClass);
        query.select(from);
        query.where(addCriteriaQueryEq(column, value, query, builder, from));
        addCriteriaQueryOrderAsc(orders, query, builder, from);
        TypedQuery qB = getEntity().createQuery(query);
        return qB.getResultList();
    }

    public List listByCriteria(Class myClass, HashMap<String, Object> filtros, List<String> orders) {
        CriteriaBuilder builder = getEntity().getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery();
        Root from = query.from(myClass);
        query.select(from);
        List<Predicate> predicates = new ArrayList<>();
        filtros.entrySet().forEach((entry) -> {
            predicates.add(addCriteriaQueryEq(entry.getKey(), entry.getValue(), query, builder, from));
        });
        query.where(predicates.toArray(new Predicate[]{}));
        addCriteriaQueryOrderAsc(orders, query, builder, from);
        TypedQuery qB = getEntity().createQuery(query);
        return qB.getResultList();
    }

    /**
     *
     * @param myClass classe principal da consulta
     * @param filtros map contendo nome do campo e valor para ser consultado com
     * equals
     * @param orders mapa contendo o campo a ser ordenado e o método para ordem
     * do mesmo (asc ou desc)
     * @return
     */
    public List listByCriteria(Class myClass, HashMap<String, Object> filtros, HashMap<String, EnumOrder> orders) {
        CriteriaBuilder builder = getEntity().getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery();
        Root from = query.from(myClass);
        query.select(from);
        List<Predicate> predicates = new ArrayList<>();
        filtros.entrySet().forEach((entry) -> {
            predicates.add(addCriteriaQueryEq(entry.getKey(), entry.getValue(), query, builder, from));
        });
        query.where(predicates.toArray(new Predicate[]{}));
        addCriteriaQueryOrder(orders, query, builder, from);
        TypedQuery qB = getEntity().createQuery(query);
        return qB.getResultList();
    }

    /**
     *
     * @param myClass classe principal da consulta
     * @param filtros map contendo nome do campo e valor para ser consultado com
     * equals
     * @param orders mapa contendo o campo a ser ordenado e o método para ordem
     * do mesmo (asc ou desc)
     * @return
     */
    public List listByCriteria(Class myClass, HashMap<String, EnumOrder> orders) {
        CriteriaBuilder builder = getEntity().getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery();
        Root from = query.from(myClass);
        query.select(from);
        addCriteriaQueryOrder(orders, query, builder, from);
        TypedQuery qB = getEntity().createQuery(query);
        return qB.getResultList();
    }

    public List listByCriteria(Class myClass, List<String> orders) {
        CriteriaBuilder builder = getEntity().getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery();
        Root from = query.from(myClass);
        query.select(from);
        addCriteriaQueryOrderAsc(orders, query, builder, from);
        TypedQuery qB = getEntity().createQuery(query);
        return qB.getResultList();
    }

    public List listByCriteria(Class myClass, HashMap<String, Object> filtros, String order) {
        CriteriaBuilder builder = getEntity().getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery();
        Root from = query.from(myClass);
        query.select(from);
        List<Predicate> predicates = new ArrayList<>();
        filtros.entrySet().forEach((entry) -> {
            predicates.add(addCriteriaQueryEq(entry.getKey(), entry.getValue(), query, builder, from));
        });
        query.where(predicates.toArray(new Predicate[]{}));
        addCriteriaQueryOrderAsc(order, query, builder, from);
        TypedQuery qB = getEntity().createQuery(query);
        return qB.getResultList();
    }

    public Object objectByCriteria(Class myClass, HashMap<String, Object> filtros, List<String> orders) {
        CriteriaBuilder builder = getEntity().getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery();
        Root from = query.from(myClass);
        query.select(from);
        List<Predicate> predicates = new ArrayList<>();
        filtros.entrySet().forEach((entry) -> {
            predicates.add(addCriteriaQueryEq(entry.getKey(), entry.getValue(), query, builder, from));
        });
        query.where(predicates.toArray(new Predicate[]{}));
        addCriteriaQueryOrderAsc(orders, query, builder, from);
        TypedQuery qB = getEntity().createQuery(query);
        return returnObjectSingleResult(qB);
    }

    public Object objectByCriteria(Class myClass, HashMap<String, Object> filtros, String order) {
        CriteriaBuilder builder = getEntity().getCriteriaBuilder();

        CriteriaQuery query = builder.createQuery();
        Root from = query.from(myClass);
        query.select(from);
        List<Predicate> predicates = new ArrayList<>();
        filtros.entrySet().forEach((entry) -> {
            predicates.add(addCriteriaQueryEq(entry.getKey(), entry.getValue(), query, builder, from));
        });
        query.where(predicates.toArray(new Predicate[]{}));
        addCriteriaQueryOrderAsc(order, query, builder, from);
        TypedQuery qB = getEntity().createQuery(query);
        return returnObjectSingleResult(qB);
    }

    public Object objectByCriteriaDesc(Class myClass, HashMap<String, Object> filtros, String order) {
        CriteriaBuilder builder = getEntity().getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery();
        Root from = query.from(myClass);
        query.select(from);
        List<Predicate> predicates = new ArrayList<>();
        filtros.entrySet().forEach((entry) -> {
            predicates.add(addCriteriaQueryEq(entry.getKey(), entry.getValue(), query, builder, from));
        });
        query.where(predicates.toArray(new Predicate[]{}));
        addCriteriaQueryOrderDesc(order, query, builder, from);
        TypedQuery qB = getEntity().createQuery(query);
        return returnObjectSingleResult(qB);
    }

    public Object objectByCriteria(String column, Object value, Class classe) {
        CriteriaBuilder builder = getEntity().getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery();
        Root from = query.from(classe);
        query.select(from);
        query.where(addCriteriaQueryEq(column, value, query, builder, from));
        TypedQuery qB = getEntity().createQuery(query);
        return returnObjectSingleResult(qB);
    }

    public Object objectByCriteria(HashMap<String, Object> filtros, Class classe) {
        CriteriaBuilder builder = getEntity().getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery();
        Root from = query.from(classe);
        query.select(from);
        List<Predicate> predicates = new ArrayList<>();
        filtros.entrySet().forEach((entry) -> {
            predicates.add(addCriteriaQueryEq(entry.getKey(), entry.getValue(), query, builder, from));
        });
        query.where(predicates.toArray(new Predicate[]{}));
        TypedQuery qB = getEntity().createQuery(query);
        return returnObjectSingleResult(qB);
    }

    public Object objectByCriteria(HashMap<String, Object> filtros, Class myClass, String columnBetween, Date start, Date end) {
        CriteriaBuilder builder = getEntity().getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery();
        Root from = query.from(myClass);
        query.select(from);
        List<Predicate> predicates = new ArrayList<>();
        filtros.entrySet().forEach((entry) -> {
            predicates.add(addCriteriaQueryEq(entry.getKey(), entry.getValue(), query, builder, from));
        });
        predicates.add(builder.between(from.get(columnBetween), start, end));
        query.where(predicates.toArray(new Predicate[]{}));
        TypedQuery qB = getEntity().createQuery(query);
        return returnObjectSingleResult(qB);
    }

    public Object objectByCriteria(String column, Object value, Class myClass, String columnBetween, Date start, Date end) {
        CriteriaBuilder builder = getEntity().getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery();
        Root from = query.from(myClass);
        query.select(from);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(addCriteriaQueryEq(column, value, query, builder, from));
        predicates.add(builder.between(from.get(columnBetween), start, end));
        query.where(predicates.toArray(new Predicate[]{}));
        TypedQuery qB = getEntity().createQuery(query);
        return returnObjectSingleResult(qB);
    }

    public CriteriaQuery getCriteria(Class classe) {
        CriteriaBuilder builder = getEntity().getCriteriaBuilder();
        CriteriaQuery criteria = builder.createQuery(classe);
        return criteria;
    }

    public CriteriaQuery getCriteriaQuery(Class classe) {
        CriteriaBuilder builder = getEntity().getCriteriaBuilder();
        CriteriaQuery criteria = builder.createQuery(classe);
        return criteria;
    }

//    @TransactionTimeout(value = 1000000, unit = TimeUnit.HOURS)
    public T save(T myEntity) {
        this.object = myEntity;
        if (this.getObject().getPk() != null) {
            T entity = getEntity().merge(this.getObject());
            getEntity().flush();
            init();
            return entity;
        } else {
            getEntity().persist(this.getObject());
            return this.object;
        }
    }

    public boolean remove(T myEntity) {
        this.object = myEntity;
        getEntity().remove(this.object);
        if (this.object.getPk() == null) {
            return true;
        } else {
            return false;
        }
    }

    public T getById(Object id, Class classe) {
        return (T) getEntity().find(classe, id);
    }

    public Object getByIdO(Object id, Class classe) {
        return getEntity().find(classe, id);
    }

    public List<T> findAll() {
        CriteriaQuery cq = getEntity().getCriteriaBuilder().createQuery();
        cq.select(cq.from(this.getObjectClass()));
        TypedQuery qB = getEntity().createQuery(cq);
        return qB.getResultList();
    }

    public List findAll(Class classe) {
        CriteriaQuery cq = getEntity().getCriteriaBuilder().createQuery();
        cq.select(cq.from(classe));
        return getEntity().createQuery(cq).getResultList();
    }

    public List<T> findRange(int[] range) {
        CriteriaQuery cq = getEntity().getCriteriaBuilder().createQuery();
        cq.select(cq.from(this.getObjectClass()));
        javax.persistence.Query q = getEntity().createQuery(cq);
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    //<editor-fold defaultstate="collapsed" desc="GET/SET">
    public abstract EntityManager getEntity();

    public abstract T getObject();

    public void setObject(T object) {
        this.object = object;
    }

}
//</editor-fold>
