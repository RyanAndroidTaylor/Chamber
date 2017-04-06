package com.dtp;

import com.dtp.annotations.ChamberColumn;
import com.dtp.columns.BooleanColumn;
import com.dtp.columns.DoubleColumn;
import com.dtp.columns.FloatColumn;
import com.dtp.columns.IntColumn;
import com.dtp.columns.LongColumn;
import com.dtp.columns.StringColumn;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.tools.Diagnostic;

/**
 * Created by ner on 4/5/17.
 */

class DataCollector {

    private Messager messager;

    DataCollector(Messager messager) {
        this.messager = messager;
    }

    TableData getTableData(TypeElement typeElement) {
        String tableName = typeElement.getSimpleName().toString();

        List<ColumnData> columns = new ArrayList<>();

        for (Element element : typeElement.getEnclosedElements()) {
            if (element instanceof VariableElement && element.getAnnotation(ChamberColumn.class) != null) {
                VariableElement variableElement = (VariableElement) element;

                columns.add(getColumnData(variableElement));
            }
        }

        return new TableData(tableName, columns);
    }

    private ColumnData getColumnData(VariableElement variableElement) {
        ChamberColumn column = variableElement.getAnnotation(ChamberColumn.class);
        String columnName = column.name();
        boolean notNull = column.notNull();
        boolean unique = column.unique();

        Type type = getType(variableElement);

        String variableName = variableElement.getSimpleName().toString();

        if (columnName.equals("undefined"))
            columnName = variableName.substring(0, 1).toUpperCase() + variableName.substring(1);

        return new ColumnData.Builder(formatAsStaticFinalName(variableName), type)
                .setColumnName(columnName)
                .setNotNull(notNull)
                .setUnique(unique)
                .build();
    }

    private Type getType(VariableElement variableElement) {
        switch (variableElement.asType().getKind()) {
            case DECLARED:
                if (variableElement.asType().toString().equals("java.lang.String")) {
                    return StringColumn.class;
                } else {
                    messager.printMessage(Diagnostic.Kind.ERROR, "Type is not supported by Chamber " + variableElement.asType().toString());
                    throw new RuntimeException("Type was not supported by Chamber");
                }
            case BYTE:
            case SHORT:
            case INT:
                return IntColumn.class;
            case LONG:
                return LongColumn.class;
            case FLOAT:
                return FloatColumn.class;
            case DOUBLE:
                return DoubleColumn.class;
            case BOOLEAN:
                return BooleanColumn.class;
            default:
                messager.printMessage(Diagnostic.Kind.ERROR, "Type is not supported by Chamber " + variableElement.asType().toString());
                throw new RuntimeException("Type was not supported by Chamber");
        }
    }

    private String formatAsStaticFinalName(String name) {
        String formattedVariableName = "";

        String[] splitName = name.split("(?=\\p{Upper})");

        for (int i = 0; i < splitName.length; i++) {
            formattedVariableName += splitName[i].toUpperCase();

            if (i < splitName.length - 1)
                formattedVariableName += "_";
        }

        return formattedVariableName;
    }
}
