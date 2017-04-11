package com.dtp;

import java.lang.reflect.Type;

import com.squareup.javapoet.TypeName;

/**
 * Created by ner on 4/5/17.
 */

class ColumnData {

    final String variableElementName;
    final String variableName;
    final Type columnType;
    final TypeName dataType;
    final String columnName;
    final boolean notNull;
    final boolean unique;

    private ColumnData(String variableElementName, String variableName, Type columnType, TypeName dataType, String columnName, boolean notNull, boolean unique) {
        this.variableElementName = variableElementName;
        this.variableName = variableName;
        this.columnType = columnType;
        this.dataType = dataType;
        this.columnName = columnName;
        this.notNull = notNull;
        this.unique = unique;
    }

    static class Builder {

        private String variableElementName;
        private String variableName;
        private Type type;
        private TypeName dataType;
        private String columnName;
        private boolean notNull;
        private boolean unique;

        Builder(String variableElementName, String variableName, Type type, TypeName dataType)  {
            this.variableElementName = variableElementName;
            this.variableName = variableName;
            this.type = type;
            this.dataType = dataType;
        }

        Builder setColumnName(String columnName) {
            this.columnName = columnName;

            return this;
        }

        Builder setNotNull(boolean notNull) {
            this.notNull = notNull;

            return this;
        }

        Builder setUnique(boolean unique) {
            this.unique = unique;

            return this;
        }

        ColumnData build() {
            return new ColumnData(variableElementName, variableName, type, dataType, columnName, notNull, unique);
        }
    }
}
