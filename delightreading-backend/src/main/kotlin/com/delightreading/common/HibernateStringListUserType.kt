package com.delightreading.common

import com.fasterxml.jackson.core.type.TypeReference

class HibernateStringListUserType : JsonBasedType(object : TypeReference<List<String>>() {
}) {

    override fun returnedClass(): Class<List<String>> {
        return List::class.java as Class<List<String>>
    }
}