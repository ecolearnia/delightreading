package com.delightreading.common

import com.fasterxml.jackson.core.type.TypeReference

class HibernateMapListUserType : JsonBasedType(object : TypeReference<List<Map<*, *>>>() {
}) {

    override fun returnedClass(): Class<List<Map<*, *>>> {
        return List::class.java as Class<List<Map<*, *>>>
    }
}