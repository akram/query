package com.ctp.cdi.query.builder.part;

import org.jboss.logging.Logger;
import com.ctp.cdi.query.builder.BuilderContext;
import com.ctp.cdi.query.builder.QueryBuilder;
import static com.ctp.cdi.query.util.QueryUtils.splitByKeyword;

/**
 * Root of the query tree. Also the only exposed class in the package.
 * @author thomashug
 */
public class QueryRoot extends QueryPart {
    
    public static final String QUERY_PREFIX = "findBy";
    
    private static final Logger log = Logger.getLogger(QueryRoot.class);
    
    private final StringBuilder result = new StringBuilder();
    private final String entityName;
    
    public static QueryRoot create(String method, String entityName) {
        QueryRoot root = new QueryRoot(entityName);
        root.build(method);
        return root;
    }
    
    public String createJpql() {
        BuilderContext ctx = new BuilderContext();
        buildQuery(result, ctx);
        String jpql = result.toString();
        log.debugv("createJpql: Query is {0}", jpql);
        return jpql;
    }
    
    protected QueryRoot(String entityName) {
        this.entityName = entityName;
    }

    @Override
    protected QueryPart build(String queryPart) {
        String[] orParts = splitByKeyword(removePrefix(queryPart), "Or");
        boolean first = true;
        for (String or : orParts) {
            OrQueryPart orPart = new OrQueryPart();
            orPart.setIsFirst(first);
            first = false;
            children.add(orPart.build(or));
        }
        return this;
    }
    
    @Override
    protected QueryPart buildQuery(StringBuilder builder, BuilderContext ctx) {
        builder.append(QueryBuilder.selectQuery(entityName));
        if (hasChildren()) {
            builder.append(" where ");
        }
        buildQueryForChildren(builder, ctx);
        return this;
    }
    
    private String removePrefix(String queryPart) {
        if (queryPart.startsWith(QUERY_PREFIX))
            return queryPart.substring(QUERY_PREFIX.length());
        return queryPart;
    }

}