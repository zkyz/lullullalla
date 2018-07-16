package kr.or.knia.cbms.config.mybatis.typehandler;

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

public class DateStringTypeHandler extends BaseTypeHandler<Date> {
  private static final DateTimeFormatter fullDateTime = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
  private static final DateTimeFormatter yearMonthDate = DateTimeFormatter.ofPattern("yyyyMMdd");

  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, Date parameter, JdbcType jdbcType) throws SQLException {
    ps.setString(i, parameter.toInstant().atZone(ZoneId.systemDefault()).format(yearMonthDate));
  }

  @Override
  public Date getNullableResult(ResultSet rs, String columnName) throws SQLException {
    String value = rs.getString(columnName);
    return parse(value);
  }

  @Override
  public Date getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    String value = rs.getString(columnIndex);
    return parse(value);
  }

  @Override
  public Date getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
    String value = cs.getString(columnIndex);
    return parse(value);
  }

  private Date parse(String value) {
    if (StringUtils.isEmpty(value)) return null;
    else if (value.length() == 14) {
      return Date
              .from(
                      LocalDateTime.parse(value, fullDateTime)
                              .atZone(ZoneId.systemDefault()).toInstant());

    }
    else if (value.length() == 8) {
      return Date
              .from(
                      LocalDateTime.parse(value, yearMonthDate)
                              .atZone(ZoneId.systemDefault()).toInstant());
    }

    throw new RuntimeException(String.format("value: %s, didn't parse by any pattern!", value));
  }
}