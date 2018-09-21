package com.delightreading.common;

import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;
import java.util.Map;

public class HibernateMapListUserType extends JsonBasedType {
    public HibernateMapListUserType() {
        super(new TypeReference<List<Map>>() {
        });
    }

    @Override
    public Class<List<Map>> returnedClass() {
        return (Class<List<Map>>) (Object) List.class;
    }
}
