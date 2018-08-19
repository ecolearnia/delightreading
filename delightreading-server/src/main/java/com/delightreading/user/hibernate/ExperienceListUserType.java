package com.delightreading.user.hibernate;

import com.delightreading.common.JsonBasedType;
import com.delightreading.user.Experience;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;

public class ExperienceListUserType extends JsonBasedType {
    public ExperienceListUserType() {
        super(new TypeReference<List<Experience>>(){});
    }

    @Override
//    public Class<List<Experience>> returnedClass() {
//        return Experience.class;
//    }
    public Class<List> returnedClass() {
        return List.class;
    }
}
