package com.app.inventory.helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Tuple;
import org.springframework.stereotype.Service;

@Service
public class QuerySql {

    private static EntityManager entityManager;

    @PersistenceContext
    private EntityManager em;

    @PostConstruct
    public void init() {
        entityManager = em;
    }

    @SuppressWarnings("unchecked")
    public static HashMap<String, Object> generateQuery(HashMap<String, Object> bean) {

        // SET DEFAULT
        String sql = "";
        String sql_count = "";
        ArrayList<String> condition = new ArrayList<>();
        String group_by = "";
        String order_by = "";
        Integer limit = 0;
        Integer offset = 0;

        if (bean.get("query_sql") != null) {
            sql = bean.get("query_sql").toString();
        }

        if (bean.get("condition") != null) {
            condition = (ArrayList<String>) bean.get("condition");
        }

        if (bean.get("group_by") != null) {
            group_by = bean.get("group_by").toString();
        }
        if (bean.get("order_by") != null) {
            order_by = bean.get("order_by").toString();
        }

        if (bean.get("limit") != null) {
            limit = Integer.valueOf(bean.get("limit").toString());
            offset = Integer.valueOf(bean.get("offset").toString());
        }

        if (!condition.isEmpty()) {
            for (int i = 0; i < condition.size(); i++) {
                condition.get(i);
                sql += " and " + condition.get(i);
            }
        }

        if (!"".equals(group_by)) {
            sql += " group by " + group_by;
        }

        if (!"".equals(order_by)) {
            sql += " order by " + order_by;
        }

        // COUNT
        sql_count = sql;

        if (limit > 0) {
            sql += " limit " + limit;
            sql += " offset " + offset;
        }

        HashMap<String, Object> query = new HashMap<>();
        query.put("sql", sql);
        query.put("sql_count", sql_count);
        return query;

    }

    @SuppressWarnings("unchecked")
    public static List<?> executeSql(HashMap<String, Object> bean, HashMap<String, Object> bind_values) {
        HashMap<String, Object> query = generateQuery(bean);
        Query execute = entityManager.createNativeQuery(query.get("sql").toString(), Tuple.class);

        for (Map.Entry<String, Object> map_bind : bind_values.entrySet()) {
            execute.setParameter(map_bind.getKey(), map_bind.getValue());
        }
        List<Tuple> result = execute.getResultList();
        return QuerySql.getResultList(result);
    }

    public static Integer executeSqlCount(HashMap<String, Object> bean, HashMap<String, Object> bind_values) {
        HashMap<String, Object> query = generateQuery(bean);
        Query execute = entityManager.createNativeQuery(query.get("sql_count").toString());
        for (Map.Entry<String, Object> map_bind : bind_values.entrySet()) {
            execute.setParameter(map_bind.getKey(), map_bind.getValue());
        }
        return execute.getResultList().size();
    }

    public static List<?> getResultList(List<Tuple> result) {

        List<Map<String, Object>> rows = new ArrayList<>();
        result.forEach(row -> {
            Map<String, Object> data = new HashMap<>();
            row.getElements().forEach(column -> {
                String columnName = column.getAlias();
                Object columnValue = row.get(column);
                data.put(columnName, columnValue);
            });
            rows.add(data);
        });

        if (!rows.isEmpty()) {
            return rows;
        }

        return null;

    }

}
