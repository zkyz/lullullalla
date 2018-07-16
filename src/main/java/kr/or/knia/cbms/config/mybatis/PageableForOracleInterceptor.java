package kr.or.knia.gistat.starter.legacy.config.mybatis.page;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.MappedStatement.Builder;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kr.or.knia.gistat.starter.legacy.config.mybatis.MybatisDaoSupport;

@Intercepts({
		@Signature(type = Executor.class, method = "query", args = {
				MappedStatement.class, Object.class, RowBounds.class,
				ResultHandler.class }),
		@Signature(type = Executor.class, method = "query", args = {
				MappedStatement.class, Object.class, RowBounds.class,
				ResultHandler.class, CacheKey.class, BoundSql.class }) })
public class PageableForOracleInterceptor implements Interceptor {
	private static final Logger log = LoggerFactory
			.getLogger("--- mybatis execute ---");

	public Object intercept(Invocation invocation) throws Throwable {
		MappedStatement ms_page;
		MappedStatement ms = (MappedStatement) invocation.getArgs()[0];
		Object param = invocation.getArgs()[1];
		ResultHandler<?> resultHandler = (ResultHandler<?>) invocation.getArgs()[3];

		Executor executor = (Executor) invocation.getTarget();

		if (param instanceof MybatisDaoSupport.PageableParam) {
			MybatisDaoSupport.PageableParam pp = (MybatisDaoSupport.PageableParam) param;
			Object parameters = pp.getParam();
			Pageable pageable = pp.getPageable();
			
			
			if (pageable != null) {
				boolean isNumberResultType = false;
				for (ResultMap resultMap : ms.getResultMaps()) {
					Class<?> type = resultMap.getType();
					isNumberResultType = Integer.class.equals(type) || type.isPrimitive();
					if (isNumberResultType)
						break;
				}
	
				if (!isNumberResultType) {
					BoundSql boundSql = ms.getBoundSql(parameters);
					List<ResultMap> resultMap = ms.getResultMaps();
	
					ms_page = createNewMappedStatementForTotalCount(ms, parameters);
	
					Object counting = executor.query(ms_page, parameters, RowBounds.DEFAULT, resultHandler);
					int totalCount = 0;
	
					if (counting instanceof List) {
						@SuppressWarnings("unchecked")
						List<Integer> resultList = (List<Integer>) counting;
						totalCount = resultList.get(0);
					} else if (counting instanceof Number) {
						totalCount = ((Number) counting).intValue();
					} else {
						log.debug("totalCount: {}", counting);
						throw new RuntimeException("page count is not found!");
					}
	
					pageable.setRowCount(totalCount);
	
					if (totalCount < 1)
						return executor.query(ms, parameters, RowBounds.DEFAULT, resultHandler);
	
					ms = createNewMappedStatementForPagination(ms, boundSql,
							resultMap, parameters, pageable);
				}
			}

			return executor.query(ms, parameters, RowBounds.DEFAULT, resultHandler);
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

		if (pageable.getSort() == null || "".equals(pageable.getSort()))
			sql.append(boundSql.getSql());
		else {
			sql.append("SELECT * FROM (");
			sql.append(boundSql.getSql());
			sql.append(") ORDER BY ").append(pageable.getSort());
		}

		sql.append(") CEI_PAGINATION WHERE ROWNUM <= ")
				.append(pageable.getRowPerPage() * pageable.getPage())
				.append(") WHERE ROW_NUM > ")
				.append(pageable.getRowPerPage() * (pageable.getPage() - 1));

		try {
			Field field = BoundSql.class.getDeclaredField("sql");
			field.setAccessible(true);
			field.set(boundSql, sql.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

		Builder builder = new Builder(ms.getConfiguration(),
				ms.getId(), new SqlSource() {
					public BoundSql getBoundSql(Object parameterObject) {
						return boundSql;
					}
				}, ms.getSqlCommandType());

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