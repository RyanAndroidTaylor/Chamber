package com.dtp;

import com.dtp.data_table.TableType;
import com.squareup.javapoet.ClassName;

import java.util.List;

import javax.lang.model.type.TypeMirror;

import static com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl.PACKAGE_NAME;

/**
 * Created by ner on 4/5/17.
 */

class TableData {

    String tableName;
    TableType tableType;
    TypeMirror typeMirror;
    List<ColumnData> columns;
    List<ChildData> childrenData;
    List<VariableData> variables;
    ClassName modelClass;
    String modelClassName;
    String fieldTableName;

    TableData(String tableName, TableType tableType, TypeMirror typeMirror, List<ColumnData> columns, List<ChildData> childrenData, List<VariableData> variables) {
        this.tableName = tableName;
        this.tableType = tableType;
        this.typeMirror = typeMirror;
        this.columns = columns;
        this.childrenData = childrenData;
        this.variables = variables;
        modelClassName = tableName + Const.MODEL_CLASS_SUFFIX;
        modelClass = ClassName.get(PACKAGE_NAME, modelClassName);
        fieldTableName = Util.toLowerFistLetter(tableName);
    }
}
