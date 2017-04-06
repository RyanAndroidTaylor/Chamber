package com.dtp;

import java.lang.reflect.Type;

/**
 * Created by ner on 4/5/17.
 */

public class ColumnData {

    public final String variableName;
    public final Type type;
    public final String columnName;
    public final boolean notNull;
    public final boolean unique;

    private ColumnData(String variableName, Type type, String columnName, boolean notNull, boolean unique) {
        this.variableName = variableName;
        this.type = type;
        this.columnName = columnName;
        this.notNull = notNull;
        this.unique = unique;
    }

    static class Builder {

        private String variableName;
        private Type type;
        private String columnName;
        private boolean notNull;
        private boolean unique;

        public Builder(String variableName, Type type)  {
            this.variableName = variableName;
            this.type = type;
        }

        public Builder setColumnName(String columnName) {
            this.columnName = columnName;

            return this;
        }

        public Builder setNotNull(boolean notNull) {
            this.notNull = notNull;

            return this;
        }

        public Builder setUnique(boolean unique) {
            this.unique = unique;

            return this;
        }

        public ColumnData build() {
            return new ColumnData(variableName, type, columnName, notNull, unique);
        }
    }
}
