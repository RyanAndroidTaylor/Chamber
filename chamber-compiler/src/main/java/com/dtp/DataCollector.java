package com.dtp;

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

public class DataCollector {

    private Messager messager;

    public DataCollector(Messager messager) {
        this.messager = messager;
    }

    public TableData getTableData(TypeElement typeElement) {
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
        String columnName = variableElement.getAnnotation(ChamberColumn.class).columnName();
        String variableName = variableElement.getSimpleName().toString();
        ColumnType type;

        if (columnName.equals("unassigned")) {
            columnName = variableName.substring(0, 1).toUpperCase() + variableName.substring(1);
        }

        switch (variableElement.asType().getKind()) {
            case DECLARED:
                if (variableElement.asType().toString().equals("java.lang.String")) {
                    type = ColumnType.STRING;
                } else {
                    messager.printMessage(Diagnostic.Kind.ERROR, "Type is not supported by Chamber");
                    throw new RuntimeException("Type was not supported by Chamber");
                }
                break;
            case BYTE:
            case SHORT:
            case INT:
                type = ColumnType.INT;
                break;
            case LONG:
                type = ColumnType.LONG;
                break;
            case FLOAT:
                type = ColumnType.FLOAT;
                break;
            case DOUBLE:
                type = ColumnType.DOUBLE;
                break;
            case BOOLEAN:
                type = ColumnType.BOOLEAN;
                break;
            default:
                messager.printMessage(Diagnostic.Kind.ERROR, "Type is not supported by Chamber");
                throw new RuntimeException("Type was not supported by Chamber");
        }

        return new ColumnData(variableName, type, columnName);
    }
}
