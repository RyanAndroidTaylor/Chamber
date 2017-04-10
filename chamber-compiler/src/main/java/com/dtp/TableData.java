package com.dtp;

import com.dtp.annotations.ChamberType;
import com.squareup.javapoet.ClassName;

import java.util.List;

import javax.lang.model.type.TypeMirror;

import static com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl.PACKAGE_NAME;

/**
 * Created by ner on 4/5/17.
 */

public class TableData {

    public ChamberType chamberType;
    public String tableName;
    public TypeMirror typeMirror;
    public List<ColumnData> columns;
    public ClassName modelClass;
    public String modelClassName;
    public String fieldTableName;

    public TableData(ChamberType chamberType, String tableName, TypeMirror typeMirror, List<ColumnData> columns) {
        this.chamberType = chamberType;
        this.tableName = tableName;
        this.typeMirror = typeMirror;
        this.columns = columns;
        modelClassName = tableName + Const.MODEL_CLASS_SUFFIX;
        modelClass = ClassName.get(PACKAGE_NAME, modelClassName);
        fieldTableName = Util.toLowerFistLetter(tableName);
    }
}
