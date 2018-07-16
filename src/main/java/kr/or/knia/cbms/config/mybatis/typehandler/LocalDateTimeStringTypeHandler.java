package kr.or.knia.cbms.config.mybatis;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.springframework.util.StringUtils;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class LocalDateTimeStringTypeHandler extends BaseTypeHandler<LocalDateTime> {
  private static final DateTimeFormatter fullDateTime = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
  private static final DateTimeFormatter yearMonthDate = DateTimeFormatter.ofPattern("yyyyMMdd");

  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, LocalDateTime parameter, JdbcType jdbcType) throws SQLException {
    ps.setString(i, parameter.format(yearMonthDate));
  }

  @Override
  public LocalDateTime getNullableResult(ResultSet rs, String columnName) throws SQLException {
    String value = rs.getString(columnName);
    return parse(value);
  }

  @Override
  public LocalDateTime getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    String value = rs.getString(columnIndex);
    return parse(value);
  }

  @Override
  public LocalDateTime getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
    String value = cs.getString(columnIndex);
    return parse(value);
  }

  private LocalDateTime parse(String value) {
    if (StringUtils.isEmpty(value))
      return null;
    else if (value.length() == 14) {
      return LocalDateTime.parse(value, fullDateTime);
    }
    else if (value.length() == 8) {
      return LocalDateTime.parse(value, yearMonthDate);
    }

    throw new RuntimeException(String.format("value: %s, didn't parse by any pattern!", value));
  }
}