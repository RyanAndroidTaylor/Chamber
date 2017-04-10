package com.dtp;

import com.dtp.annotations.ChamberType;
import com.dtp.columns.Column;
import com.dtp.columns.LongColumn;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.List;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;

import static com.squareup.javapoet.TypeSpec.classBuilder;

/**
 * Created by ner on 4/5/17.
 */

class FileGenerator {
    private static final String PACKAGE_NAME = "com.dtp.chamber";

    private Filer filer;

    FileGenerator(Filer filer) {
        this.filer = filer;
    }

    void generateFiles(List<TableData> tablesData) {
        for (TableData tableData : tablesData) {
            TypeSpec.Builder builder = classBuilder(tableData.tableName + Const.TABLE_CLASS_SUFFIX)
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL);

            builder.addField(generateTableNameSpec(tableData.tableName));
            builder.addField(generateChamberIdSpec(tableData.tableName));

            for (ColumnData columnData : tableData.columns) {
                builder.addField(generateFieldSpec(tableData.tableName, columnData));
            }

            builder.addField(generateColumnsArraySpec(tableData.columns));

            if (tableData.chamberType == ChamberType.ANDROID)
                builder.addMethod(generateContentValuesMethodSpec(tableData));

            TypeSpec typeSpec = builder.build();

            JavaFile javaFile = JavaFile.builder(PACKAGE_NAME, typeSpec)
                    .indent("    ")
                    .build();


            try {
                javaFile.writeTo(filer);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }

    private FieldSpec generateTableNameSpec(String tableName) {
        return FieldSpec.builder(String.class, "NAME", Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .initializer("$S", tableName)
                .build();
    }

    private FieldSpec generateChamberIdSpec(String tableName) {
        ColumnData columnData = new ColumnData.Builder("chamberId", "CHAMBER_ID", LongColumn.class)
                .setColumnName(Column.CHAMBER_ID)
                .setNotNull(true)
                .setUnique(true)
                .build();

        return generateFieldSpec(tableName, columnData);
    }

    private FieldSpec generateFieldSpec(String tableName, ColumnData columnData) {
        FieldSpec.Builder builder = FieldSpec.builder(columnData.type, columnData.variableName, Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL);

        builder.initializer("$N", buildColumnInitializer(tableName, columnData));

        return builder.build();
    }

    private String buildColumnInitializer(String tableName, ColumnData columnData) {
        return String.format("new %s(\"%s%s\", %b, %b)", ((Class) columnData.type).getSimpleName(), tableName, columnData.columnName, columnData.notNull, columnData.unique);
    }

    private FieldSpec generateColumnsArraySpec(List<ColumnData> columns) {
        FieldSpec.Builder builder = FieldSpec.builder(Column[].class, "COLUMNS", Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL);

        StringBuilder columnsBuilder = new StringBuilder("new Column[] {");

        columnsBuilder.append("CHAMBER_ID");

        for (int i = 0; i < columns.size(); i++) {
            if (i == 0)
                columnsBuilder.append(", ");

            columnsBuilder.append(columns.get(i).variableName);

            if (i < columns.size() -1)
                columnsBuilder.append(", ");
            else
                columnsBuilder.append("}");
        }

        return builder.initializer("$N", columnsBuilder.toString()).build();
    }

    private MethodSpec generateContentValuesMethodSpec(TableData tableData) {
        ClassName className = ClassName.get("android.content", "ContentValues");
        String variableName = Util.toLowerFistLetter(className.simpleName());

        MethodSpec.Builder builder = MethodSpec.methodBuilder("getContentValuesFor")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(className)
                .addParameter(TypeName.get(tableData.typeMirror), tableData.fieldTableName)
                .addStatement("$T $N = new $T()", className, variableName, className);

        for (ColumnData columnData : tableData.columns) {
            builder.addStatement("$N.put($N.name, person.get$N())", variableName, columnData.variableName, Util.toUpperFirstLetter(columnData.variableElementName));
        }

        builder.addStatement("return $N", variableName);

        return builder.build();
    }
}
