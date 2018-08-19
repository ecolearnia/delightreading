package com.delightreading.common;

import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;

public class HibernateStringListUserType extends JsonBasedType {
    public HibernateStringListUserType() {
        super(new TypeReference<List<String>>() {
        });
    }

    @Override
    public Class<List<String>> returnedClass() {
        return (Class<List<String>>) (Object) List.class;
    }
}
