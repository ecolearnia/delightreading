package com.delightreading.user.hibernate;

import com.delightreading.common.JsonBasedType;
import com.delightreading.user.Experience;

public class ExperienceUserType extends JsonBasedType {
    public ExperienceUserType() {
    }

    @Override
    public Class<Experience> returnedClass() {
        return Experience.class;
    }
}
