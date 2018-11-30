package com.delightreading.user.hibernate

import com.delightreading.common.JsonBasedType
import com.delightreading.user.model.Experience
import com.fasterxml.jackson.core.type.TypeReference

class ExperienceListUserType : JsonBasedType(object : TypeReference<List<Experience>>() {
}) {

    override fun returnedClass(): Class<List<*>> {
        return List::class.java
    }
}