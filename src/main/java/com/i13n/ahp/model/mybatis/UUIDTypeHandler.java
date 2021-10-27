package com.i13n.ahp.model.mybatis;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

@MappedTypes(UUID.class)
public class UUIDTypeHandler implements TypeHandler<UUID> {

    @Override
    public void setParameter(PreparedStatement ps, int i, UUID parameter, JdbcType jdbcType) throws SQLException {
        ps.setObject(i, parameter);
    }

    @Override
    public UUID getResult(ResultSet rs, String columnName) throws SQLException {
        return (UUID) Optional.ofNullable(rs.getObject(columnName)).orElse(null);
    }

    @Override
    public UUID getResult(ResultSet rs, int columnIndex) throws SQLException {
        return (UUID) Optional.ofNullable(rs.getObject(columnIndex)).orElse(null);
    }

    @Override
    public UUID getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return (UUID) Optional.ofNullable(cs.getObject(columnIndex)).orElse(null);
    }
}
