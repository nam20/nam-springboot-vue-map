package com.example.demo.DB;

import javax.persistence.AttributeConverter;

public class GradeAttributeConverter  implements AttributeConverter<Grade,String> {

    @Override
    public String convertToDatabaseColumn(Grade grade) {
        return grade.getLegacyCode();
    }

    @Override
    public Grade convertToEntityAttribute(String s) {
        return Grade.ofLegacyCode(s);
    }
}
