package com.dtp;

import com.dtp.annotations.ChamberChild;
import com.dtp.annotations.ChamberColumn;
import com.dtp.columns.BooleanColumn;
import com.dtp.columns.DoubleColumn;
import com.dtp.columns.FloatColumn;
import com.dtp.columns.IntColumn;
import com.dtp.columns.LongColumn;
import com.dtp.columns.StringColumn;
import com.dtp.columns.StringListColumn;
import com.dtp.data_table.ChildDataTable;
import com.dtp.data_table.ParentDataTable;
import com.dtp.data_table.TableType;
import com.squareup.javapoet.TypeName;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

/**
 * Created by ner on 4/5/17.
 */

class DataCollector {

    private Messager messager;
    private Types typeUtils;
    private Elements elemenUtils;

    DataCollector(Messager messager, Types typeUtils, Elements elementUtils) {
        this.messager = messager;
        this.typeUtils = typeUtils;
        this.elemenUtils = elementUtils;
    }

    TableData getTableData(TypeElement typeElement) {
        String tableName = typeElement.getSimpleName().toString();
        TableType tableType = TableType.NORMAL;

        for (TypeMirror typeMirror : typeElement.getInterfaces()) {
            TypeName typeName = TypeName.get(typeMirror);

            if (typeName.equals(TypeName.get(ParentDataTable.class))) {
                tableType = TableType.PARENT;
            } else if (typeName.equals(TypeName.get(ChildDataTable.class))) {
                tableType = TableType.CHILD;
            }
        }

        List<VariableData> variables = new ArrayList<>();
        List<ColumnData> columns = new ArrayList<>();
        List<ChildData> childrenData = new ArrayList<>();

        for (Element element : typeElement.getEnclosedElements()) {
            if (element instanceof VariableElement && element.getAnnotation(ChamberColumn.class) != null) {
                VariableElement variableElement = (VariableElement) element;

                ColumnData columnData = getColumnData(variableElement);

                columns.add(columnData);
                variables.add(columnData);
            } else if (element instanceof VariableElement && element.getAnnotation(ChamberChild.class) != null) {
                VariableElement variableElement = (VariableElement) element;

                ChildData childData = getChildData(variableElement);
                childrenData.add(childData);
                variables.add(childData);
            }
        }

        return new TableData(tableName, tableType, typeElement.asType(), columns, childrenData, variables);
    }

    private ColumnData getColumnData(VariableElement variableElement) {
        ChamberColumn column = variableElement.getAnnotation(ChamberColumn.class);
        String columnName = column.name();
        boolean notNull = column.notNull();
        boolean unique = column.unique();

        Type type = getType(variableElement);
        TypeName dataType = TypeName.get(variableElement.asType());

        String variableName = variableElement.getSimpleName().toString();

        if (columnName.equals("undefined"))
            columnName = variableName.substring(0, 1).toUpperCase() + variableName.substring(1);

        return new ColumnData.Builder(variableElement.getSimpleName().toString(), Util.formatAsStaticFinalName(variableName), type, dataType)
                .setColumnName(columnName)
                .setNotNull(notNull)
                .setUnique(unique)
                .build();
    }

    private ChildData getChildData(VariableElement variableElement) {
        String variableName = variableElement.getSimpleName().toString();
        boolean isList = variableElement.asType().toString().contains("java.util.List");

        TypeName type = TypeName.get(variableElement.asType());

        TypeName parameterType = null;

        TypeMirror typeMirror = variableElement.asType();

        if (typeMirror instanceof DeclaredType) {
            List<? extends TypeMirror> typeArguments = ((DeclaredType) typeMirror).getTypeArguments();

            if (typeArguments.size() > 0)
                parameterType = TypeName.get(typeArguments.get(0));
        }

        return new ChildData(variableName, type, parameterType, isList);
    }

    private Type getType(VariableElement variableElement) {
        switch (variableElement.asType().getKind()) {
            case DECLARED:
                return getDeclaredType(variableElement.asType().toString());
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

    private Type getDeclaredType(String typeName) {
        switch (typeName) {
            case "java.lang.String":
                return StringColumn.class;
            case "java.lang.Long":
                return LongColumn.class;
            case "java.lang.Integer":
            case "java.lang.Byte":
            case "java.lang.Short":
                return IntColumn.class;
            case "java.lang.Float":
                return FloatColumn.class;
            case "java.lang.Double":
                return DoubleColumn.class;
            case "java.lang.Boolean":
                return BooleanColumn.class;
            case "java.util.List<java.lang.String>":
                return StringListColumn.class;
            default:
                messager.printMessage(Diagnostic.Kind.ERROR, "Type is not supported by Chamber " + typeName);
                throw new RuntimeException("Type was not supported by Chamber");
        }
    }
}
