package in.clouthink.daas.fss.mysql.repository.custom.impl;

import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JHqlBuilder {

    private StringBuilder       countHql;
    private StringBuilder       queryHql;
    private StringBuilder       whereHql;
    private Map<String, Object> parameters;

    public JHqlBuilder(String countInit, String queryInit) {
        countHql = new StringBuilder(countInit);
        queryHql = new StringBuilder(queryInit);
        whereHql = new StringBuilder();
        parameters = new HashMap<>();
    }

    public String getCountJHql() {
        return this.countHql.toString().concat(" where 1=1 ").concat(this.whereHql.toString());
    }

    public String getQueryHql(String sortHql) {
        String result = this.queryHql.toString().concat(" where 1=1 ").concat(this.whereHql.toString());
        if (!StringUtils.isEmpty(sortHql)) {
            result = result.concat(sortHql);
        }
        return result;
    }

    public Map<String, Object> getParameters() {
        return this.parameters;
    }

    public JHqlBuilder andEquals(String fieldName, String fieldAlias, Object fieldValue) {
        if (null != fieldValue) {
            if (fieldValue instanceof String) {
                fieldValue = ((String) fieldValue).trim();
            }
            this.whereHql.append(" and ").append(fieldName).append("=:").append(fieldAlias);
            this.parameters.put(fieldAlias, fieldValue);
        }
        return this;
    }

    public JHqlBuilder appendWhereHql(String appendStr) {
        this.whereHql.append(appendStr);
        return this;
    }

    public JHqlBuilder addQueryPar(String name, Object value) {
        this.parameters.put(name, value);
        return this;
    }


    public JHqlBuilder andGreaterThan(String fieldName, String fieldAlias, Object fieldValue) {
        if (null != fieldValue) {
            this.whereHql.append(" and ").append(fieldName).append(">=:").append(fieldAlias);
            this.parameters.put(fieldAlias, fieldValue);
        }
        return this;
    }

    public JHqlBuilder andLessThan(String fieldName, String fieldAlias, Object fieldValue) {
        if (null != fieldValue) {
            this.whereHql.append(" and ").append(fieldName).append("<=:").append(fieldAlias);
            this.parameters.put(fieldAlias, fieldValue);
        }
        return this;
    }

    public JHqlBuilder andNotEquals(String fieldName, String fieldAlias, String fieldValue) {
        if (!StringUtils.isEmpty(fieldValue)) {
            if (fieldValue instanceof String) {
                fieldValue = ((String) fieldValue).trim();
            }
            this.whereHql.append(" and ").append(fieldName).append("!=:").append(fieldAlias);
            this.parameters.put(fieldAlias, fieldValue);
        }
        return this;
    }

    public JHqlBuilder in(String fieldName, String fieldAlias, List fieldValue) {
        if (null != fieldValue && fieldValue.size() > 0) {
            this.whereHql.append(" and ").append(fieldName).append(" in :").append(fieldAlias + " ");
            this.parameters.put(fieldAlias, fieldValue);
        }
        return this;
    }

    public JHqlBuilder andIsNull(String fieldName) {
        this.whereHql.append(" and ").append(fieldName).append(" is null ");
        return this;
    }

    public JHqlBuilder andIsNotNull(String fieldName) {
        this.whereHql.append(" and ").append(fieldName).append(" is not null ");
        return this;
    }

    public JHqlBuilder andLike(String[] fieldNames, String paramName, String paramValue) {
        if (null != fieldNames && fieldNames.length > 0) {
            if (paramValue.startsWith("%") && paramValue.endsWith("%")) {
                paramValue = paramValue.substring(1);
                paramValue = paramValue.substring(0, paramValue.length() - 1);
                paramValue = "%" + paramValue.trim() + "%";
            }
            StringBuilder jHql = this.whereHql;
            jHql.append(" and (");
            Arrays.stream(fieldNames).forEach(n -> jHql.append(n).append(" like :").append(paramName + " or "));
            jHql.replace(jHql.length() - 3, jHql.length(), "");
            jHql.append(" )");
            this.parameters.put(paramName, paramValue);
        }
        return this;
    }

    public JHqlBuilder joinTable(String joinHql) {
        if (this.countHql.indexOf(joinHql) < 0) {
            this.countHql.append(" " + joinHql + " ");
            this.queryHql.append(" " + joinHql + " ");
        }
        return this;
    }

}
