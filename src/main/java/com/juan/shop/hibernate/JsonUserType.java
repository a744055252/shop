package com.juan.shop.hibernate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.internal.util.ReflectHelper;
import org.hibernate.usertype.ParameterizedType;
import org.hibernate.usertype.UserType;

import java.io.IOException;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Objects;
import java.util.Properties;

/**
 * Hibernate {@link UserType} implementation to handle JSON objects
 *
 * @author liguanhuan
 */
public class JsonUserType implements UserType, ParameterizedType, Serializable {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /** 实体的class */
    public static final String CLASS_TYPE = "classType";
    /** 数据库中的类型 */
    public static final String TYPE = "type";

//    private static final int[] SQL_TYPES = new int[] { Types.LONGVARCHAR, Types.CLOB, Types.BLOB };

    private static final long serialVersionUID = -858937446031745477L;

    private Class<?> classType;

    private int sqlType = Types.LONGVARCHAR;

    @Override
    public void setParameterValues(Properties params) {
        String classTypeName = params.getProperty(CLASS_TYPE);
        try {
            this.classType = ReflectHelper.classForName(classTypeName, this.getClass());
        } catch (ClassNotFoundException cnfe) {
            throw new HibernateException("classType not found", cnfe);
        }
        String type = params.getProperty(TYPE);
        if (type != null) {
            this.sqlType = Integer.decode(type);
        }

    }

    @Override
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return this.deepCopy(cached);
    }

    @Override
    public Object deepCopy(Object value) throws HibernateException {
        if (value != null) {
            try {
                return MAPPER.readValue(MAPPER.writeValueAsString(value), this.classType);
            } catch (IOException e) {
                throw new HibernateException("unable to deep copy object", e);
            }
        }
        return null;
    }

    @Override
    public Serializable disassemble(Object value) throws HibernateException {
        try {
            return MAPPER.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new HibernateException("unable to disassemble object", e);
        }
    }

    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        return Objects.equals(x, y);
    }

    @Override
    public int hashCode(Object x) throws HibernateException {
        return Objects.hashCode(x);
    }

    @Override
    public Object nullSafeGet(ResultSet resultSet, String[] strings, SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException, SQLException {
        Object obj = null;
        if (this.sqlType == Types.CLOB || this.sqlType == Types.BLOB) {
            byte[] bytes = resultSet.getBytes(strings[0]);
            if (bytes != null && !resultSet.wasNull()) {
                try {
                    obj = MAPPER.readValue(bytes, this.classType);
                } catch (IOException e) {
                    throw new HibernateException("unable to read object from result set", e);
                }
            }
        } else {
            try {
                String content = resultSet.getString(strings[0]);
                if (content != null && !resultSet.wasNull()) {
                    obj = MAPPER.readValue(content, this.classType);
                }
            } catch (IOException e) {
                throw new HibernateException("unable to read object from result set", e);
            }
        }
        return obj;
    }

    @Override
    public void nullSafeSet(PreparedStatement preparedStatement, Object o, int i, SharedSessionContractImplementor sharedSessionContractImplementor) throws HibernateException, SQLException {
        if (o == null) {
            preparedStatement.setNull(i, this.sqlType);
        } else {

            if (this.sqlType == Types.CLOB || this.sqlType == Types.BLOB) {
                try {
                    preparedStatement.setBytes(i, MAPPER.writeValueAsBytes(o));
                } catch (JsonProcessingException e) {
                    throw new HibernateException("unable to set object to result set", e);
                }
            } else {
                try {
                    preparedStatement.setString(i, MAPPER.writeValueAsString(o));
                } catch (JsonProcessingException e) {
                    throw new HibernateException("unable to set object to result set", e);
                }
            }
        }
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return this.deepCopy(original);
    }

    @Override
    public Class<?> returnedClass() {
        return this.classType;
    }

    @Override
    public int[] sqlTypes() {
        return new int[]{sqlType};
    }
}