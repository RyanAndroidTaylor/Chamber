package com.dtp;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;

import static com.squareup.javapoet.TypeSpec.classBuilder;

/**
 * Created by ner on 4/5/17.
 */

public class FileGenerator {

    private Filer filer;

    public FileGenerator(Filer filer) {
        this.filer = filer;
    }

    public void generateFiles(List<TableData> tablesData) {
        for (TableData tableData : tablesData) {
            String packageName = "com.dtp.chamber";

            TypeSpec.Builder builder = classBuilder(tableData.tableName + Const.MANAGER_CLASS_SUFFIX)
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL);

            for (ColumnData columnData : tableData.columns) {
                if (columnData.type == ColumnType.STRING) {
                    builder.addField(getFieldSpec(StringColumn.class, columnData));
                } else if (columnData.type == ColumnType.INT) {
                    builder.addField(getFieldSpec(IntColumn.class, columnData));
                }
            }

            TypeSpec typeSpec = builder.build();

            JavaFile javaFile = JavaFile.builder(packageName, typeSpec).build();

            try {
                javaFile.writeTo(filer);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }

    private FieldSpec getFieldSpec(Type type, ColumnData columnData) {
        String formattedName = "";

        String[] splitName = columnData.columnName.split("(?=\\p{Upper})");

        for (int i = 0; i < splitName.length; i++) {
            formattedName += splitName[i].toUpperCase();

            if (i < splitName.length -1)
                formattedName += "_";
        }

        FieldSpec.Builder builder = FieldSpec.builder(type, formattedName, Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL);

        if (type == StringColumn.class) {
            builder.initializer("$N$S$N", "new StringColumn(", columnData.columnName, ")");
        } else if (type == IntColumn.class) {
            builder.initializer("$N$S$N", "new IntColumn(", columnData.columnName, ")");
        }

        return builder.build();
    }
}
