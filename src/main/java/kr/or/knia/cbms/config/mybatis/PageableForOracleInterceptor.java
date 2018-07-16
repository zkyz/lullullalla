package kr.or.knia.cbms.config.mybatis;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.MappedStatement.Builder;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.data.domain.Pageable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Intercepts({
        @Signature(type = Executor.class, method = "query", args = {
                MappedStatement.class, Object.class, RowBounds.class,
                ResultHandler.class}),
        @Signature(type = Executor.class, method = "query", args = {
                MappedStatement.class, Object.class, RowBounds.class,
                ResultHandler.class, CacheKey.class, BoundSql.class})})
public class PageableForOracleInterceptor implements Interceptor {

  public Object intercept(Invocation invocation) throws Throwable {
    MappedStatement ms_page;
    MappedStatement ms = (MappedStatement) invocation.getArgs()[0];
    Object params = invocation.getArgs()[1];
    ResultHandler<?> resultHandler = (ResultHandler<?>) invocation.getArgs()[3];

    Pageable pageable = null;
    List<Object> parameters = new ArrayList<>(1);

    if (params != null) {
      for (Object param : parameters) { //params) {
        if (param instanceof Pageable) pageable = (Pageable) param;
        else parameters.add(param);
      }

      if (pageable != null) {
        BoundSql boundSql = ms.getBoundSql(parameters.toArray());
        List<ResultMap> resultMap = ms.getResultMaps();

        ms_page = createNewMappedStatementForTotalCount(ms, parameters);

        Executor executor = (Executor) invocation.getTarget();
        Object counting = executor.query(ms_page, parameters, RowBounds.DEFAULT, resultHandler);
        long totalCount = 0;

        if (counting instanceof List) {
          @SuppressWarnings("unchecked")
          List<Long> resultList = (List<Long>) counting;
          totalCount = resultList.get(0);
        }
        else if (counting instanceof Number) {
          totalCount = ((Number) counting).longValue();
        }

        // set page count
        //pageable.setRowCount(totalCount);

        if (totalCount < 1)
          return executor.query(ms, parameters, RowBounds.DEFAULT, resultHandler);

        ms = createNewMappedStatementForPagination(ms, boundSql,
                resultMap, parameters, pageable);
      }
    }

    return invocation.proceed();
  }

  public Object plugin(Object target) {
    return Plugin.wrap(target, this);
  }

  public void setProperties(Properties properties) {
  }

  private MappedStatement createNewMappedStatementForTotalCount(
          final MappedStatement ms, Object param) throws Exception {
    final BoundSql boundSql = ms.getBoundSql(param);
    final StringBuilder sql = new StringBuilder();
    sql.append("SELECT COUNT(*) FROM (");
    sql.append(boundSql.getSql());
    sql.append(")");

    Builder builder = new Builder(ms.getConfiguration(),
            ms.getId() + "_Count", new SqlSource() {
      public BoundSql getBoundSql(Object parameterObject) {
        return new BoundSql(ms.getConfiguration(),
                sql.toString(),
                boundSql.getParameterMappings(),
                boundSql.getParameterObject());
      }
    }, ms.getSqlCommandType());

    List<ResultMap> resultMaps = new ArrayList<ResultMap>(1);
    resultMaps.add(new ResultMap.Builder(ms.getConfiguration(), ms.getId()
            + "_Count_Inline", Integer.class, ms.getResultMaps()
            .get(0).getResultMappings()).build());

    builder.resource(ms.getResource())
            .fetchSize(ms.getFetchSize())
            .statementType(ms.getStatementType())
            .keyGenerator(ms.getKeyGenerator())
            .timeout(ms.getTimeout())
            .parameterMap(ms.getParameterMap())
            // .resultMaps(ms.getResultMaps())
            .resultMaps(resultMaps).resultSetType(ms.getResultSetType())
            .cache(ms.getCache())
            .flushCacheRequired(ms.isFlushCacheRequired())
            .useCache(ms.isUseCache());

    return builder.build();
  }

  private MappedStatement createNewMappedStatementForPagination(
          MappedStatement ms, final BoundSql boundSql,
          List<ResultMap> resultMap, Object parameters, Pageable pageable)
          throws Exception {

    final StringBuilder sql = new StringBuilder();
    sql.append("SELECT * FROM (SELECT CEI_PAGINATION.*, ROWNUM AS ROW_NUM FROM (");

    if (pageable.getSort() == null) {
      sql.append(boundSql.getSql());
    }
    else {
      sql.append("SELECT * FROM (")
         .append(boundSql.getSql())
         .append(") ORDER BY ");

      pageable.getSort().iterator().forEachRemaining(
              order -> sql.append(order.getProperty()).append(' ')
                          .append(order.getDirection()).append(' '));
    }

    sql.append(") CEI_PAGINATION WHERE ROWNUM <= ")
            .append(pageable.getPageSize() * pageable.getPageNumber())
            .append(") WHERE ROW_NUM > ")
            .append(pageable.getPageSize() * (pageable.getPageNumber() - 1));

    try {
      Field field = BoundSql.class.getDeclaredField("sql");
      field.setAccessible(true);
      field.set(boundSql, sql.toString());
    } catch (Exception e) {
      e.printStackTrace();
    }

    Builder builder = new Builder(ms.getConfiguration(),
            ms.getId(), parameterObject -> boundSql, ms.getSqlCommandType());

    builder.resource(ms.getResource()).fetchSize(ms.getFetchSize())
            .statementType(ms.getStatementType())
            .keyGenerator(ms.getKeyGenerator()).timeout(ms.getTimeout())
            .parameterMap(ms.getParameterMap()).resultMaps(resultMap)
            .resultSetType(ms.getResultSetType()).cache(ms.getCache())
            .flushCacheRequired(ms.isFlushCacheRequired())
            .useCache(ms.isUseCache());

    return builder.build();
  }
}