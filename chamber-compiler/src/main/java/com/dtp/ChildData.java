package com.dtp;

import com.squareup.javapoet.TypeName;

/**
 * Created by ner on 4/11/17.
 */

class ChildData implements VariableData {

    String variableName;
    TypeName type;
    TypeName parameterTypeName;
    boolean isList;

    ChildData(String variableName, TypeName type, TypeName parameterTypeName, boolean isList) {
        this.variableName = variableName;
        this.type = type;
        this.parameterTypeName = parameterTypeName;
        this.isList = isList;
    }
}
