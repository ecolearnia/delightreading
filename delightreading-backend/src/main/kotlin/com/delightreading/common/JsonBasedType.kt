package com.delightreading.common

import com.delightreading.SpringApplicationContextUtil
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.hibernate.HibernateException
import org.hibernate.engine.spi.SharedSessionContractImplementor
import org.hibernate.usertype.UserType
import java.io.*
import java.nio.charset.StandardCharsets
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Types

abstract class JsonBasedType(val deserializationTypeRef: TypeReference<*>? = null): UserType {

    override fun sqlTypes(): IntArray {
        return intArrayOf(Types.JAVA_OBJECT)
    }

    @Throws(HibernateException::class, SQLException::class)
    override fun nullSafeGet(
        rs: ResultSet, names: Array<String>, session: SharedSessionContractImplementor,
        owner: Any
    ): Any? {
        val cellContent = rs.getString(names[0]) ?: return null
        try {
            val mapper = SpringApplicationContextUtil.lookUpBean(ObjectMapper::class.java)
            return if (deserializationTypeRef != null) {
                mapper.readValue(cellContent.toByteArray(StandardCharsets.UTF_8), deserializationTypeRef)
            } else {
                mapper.readValue(cellContent.toByteArray(StandardCharsets.UTF_8), returnedClass())
            }
        } catch (ex: Exception) {
            throw RuntimeException("Failed to convert String to " + returnedClass().simpleName + ": " + ex.message, ex)
        }

    }

    @Throws(HibernateException::class, SQLException::class)
    override fun nullSafeSet(
        ps: PreparedStatement, value: Any?, idx: Int,
        session: SharedSessionContractImplementor
    ) {
        if (value == null) {
            ps.setNull(idx, Types.OTHER)
            return
        }
        try {
            val mapper = SpringApplicationContextUtil.lookUpBean(ObjectMapper::class.java)
            val w = StringWriter()
            mapper.writeValue(w, value)
            w.flush()
            ps.setObject(idx, w.toString(), Types.OTHER)
        } catch (ex: Exception) {
            throw RuntimeException("Failed to convert " + returnedClass().simpleName + " to String : " + ex.message, ex)
        }

    }

    @Throws(HibernateException::class)
    override fun deepCopy(value: Any?): Any? {
        try {
            // use serialization to create a deep copy
            val bos = ByteArrayOutputStream()
            val oos = ObjectOutputStream(bos)
            oos.writeObject(value)
            oos.flush()
            oos.close()
            bos.close()

            val bais = ByteArrayInputStream(bos.toByteArray())
            return ObjectInputStream(bais).readObject()
        } catch (ex: ClassNotFoundException) {
            throw HibernateException(ex)
        } catch (ex: IOException) {
            throw HibernateException(ex)
        }

    }

    override fun isMutable(): Boolean {
        return false
    }

    override fun disassemble(o: Any): Serializable {
        return o as Serializable
    }

    override fun assemble(cached: Serializable, owner: Any): Any {
        return cached
    }

    override fun replace(o: Any, target: Any, owner: Any): Any {
        return o
    }

    override fun equals(x: Any?, y: Any?): Boolean {
        return x === y || x != null && x == y
    }

    override fun hashCode(x: Any): Int {
        return x.hashCode()
    }
}