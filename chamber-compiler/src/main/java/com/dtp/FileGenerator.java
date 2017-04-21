package com.dtp;

import com.dtp.columns.Column;
import com.dtp.columns.IntListColumn;
import com.dtp.columns.LongColumn;
import com.dtp.columns.StringListColumn;
import com.dtp.data_table.ChildDataTable;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;

import static com.dtp.Const.CHAMBER_ID_COLUMN_NAME;
import static com.dtp.Const.CHAMBER_ID_COLUMN_VARIABLE_NAME;
import static com.dtp.Const.CHAMBER_ID_VARIABLE_NAME;
import static com.dtp.Const.COLUMNS;
import static com.dtp.Const.PACKAGE_NAME;
import static com.dtp.Const.PARENT_CHAMBER_ID_COLUMN_NAME;
import static com.dtp.Const.PARENT_CHAMBER_ID_COLUMN_VARIABLE_NAME;
import static com.dtp.Const.PARENT_CHAMBER_ID_VARIABLE_NAME;
import static com.dtp.Const.TABLE_CLASS_SUFFIX;
import static com.dtp.Const.TABLE_NAME;
import static com.squareup.javapoet.TypeSpec.classBuilder;

/**
 * Created by ner on 4/5/17.
 */

class FileGenerator {
    private Filer filer;

    FileGenerator(Filer filer) {
        this.filer = filer;
    }

    void generateFiles(List<TableData> tablesData) {
        for (TableData tableData : tablesData) {
            TypeSpec.Builder builder = classBuilder(tableData.tableName + TABLE_CLASS_SUFFIX)
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL);

            builder.addField(generateTableNameSpec(tableData.tableName));
            builder.addField(generateChamberIdSpec());

            if (tableData.isChild())
                builder.addField(generateParentChamberIdSpec());

            for (ColumnData columnData : tableData.columns) {
                builder.addField(generateFieldSpec(tableData.tableName, columnData));
            }

            builder.addField(generateColumnsArraySpec(tableData));

            builder.addMethod(generateDataStoreMethodSpec(tableData));

            if (tableData.isParent())
                builder.addMethod(generateChildrenMethodSpec(tableData));

            if (!tableData.overridesBuilder)
                builder.addType(ItemBuilderFileGenerator.generateBuilder(tableData));

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
        return FieldSpec.builder(String.class, TABLE_NAME, Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .initializer("$S", tableName)
                .build();
    }

    private FieldSpec generateChamberIdSpec() {
        ColumnData columnData = new ColumnData.Builder(CHAMBER_ID_VARIABLE_NAME, CHAMBER_ID_COLUMN_VARIABLE_NAME, LongColumn.class, TypeName.get(Long.class))
                .setColumnName(CHAMBER_ID_COLUMN_NAME)
                .setNotNull(true)
                .setUnique(true)
                .build();

        return generateFieldSpec("", columnData);
    }

    private FieldSpec generateParentChamberIdSpec() {
        ColumnData columnData = new ColumnData.Builder(PARENT_CHAMBER_ID_VARIABLE_NAME, PARENT_CHAMBER_ID_COLUMN_VARIABLE_NAME, LongColumn.class, TypeName.get(Long.class))
                .setColumnName(PARENT_CHAMBER_ID_COLUMN_NAME)
                .setNotNull(true)
                .build();

        return generateFieldSpec("", columnData);
    }

    private FieldSpec generateFieldSpec(String tableName, ColumnData columnData) {
        FieldSpec.Builder builder = FieldSpec.builder(columnData.columnType, columnData.variableName, Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL);

        builder.initializer("$N", buildColumnInitializer(tableName, columnData));

        return builder.build();
    }

    private String buildColumnInitializer(String tableName, ColumnData columnData) {
        return String.format("new %s(\"%s%s\", %b, %b)", ((Class) columnData.columnType).getSimpleName(), tableName, columnData.columnName, columnData.notNull, columnData.unique);
    }

    private FieldSpec generateColumnsArraySpec(TableData tableData) {
        List<ColumnData> columns = tableData.columns;
        FieldSpec.Builder builder = FieldSpec.builder(Column[].class, COLUMNS, Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL);

        StringBuilder columnsBuilder = new StringBuilder("new Column[] {");

        columnsBuilder.append(CHAMBER_ID_COLUMN_VARIABLE_NAME);

        if (tableData.isChild()) {
            columnsBuilder.append(", ");
            columnsBuilder.append(PARENT_CHAMBER_ID_COLUMN_VARIABLE_NAME);
        }

        for (int i = 0; i < columns.size(); i++) {
            if (i == 0)
                columnsBuilder.append(", ");

            columnsBuilder.append(columns.get(i).variableName);

            if (i < columns.size() - 1)
                columnsBuilder.append(", ");
            else
                columnsBuilder.append("}");
        }

        return builder.initializer("$N", columnsBuilder.toString()).build();
    }

    private MethodSpec generateDataStoreMethodSpec(TableData tableData) {
        String className = DataStoreIn.class.getSimpleName();
        String dataStore = Util.toLowerFistLetter(className);

        MethodSpec.Builder builder = MethodSpec.methodBuilder("getDataStoreFor")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(DataStoreIn.class)
                .addParameter(TypeName.get(tableData.typeMirror), tableData.fieldTableName)
                .addStatement("$N $N = $T.createDataStore()", className, dataStore, TypeName.get(DataConnection.Companion.class));

        if (tableData.isChild())
            builder.addStatement("$N.put($N, $N.get$N())", dataStore, PARENT_CHAMBER_ID_COLUMN_VARIABLE_NAME, tableData.fieldTableName, Util.toUpperFirstLetter(PARENT_CHAMBER_ID_VARIABLE_NAME));

        for (ColumnData columnData : tableData.columns) {
            if (columnData.variableElementName.startsWith("is"))
                builder.addStatement("$N.put($N, $N.$N())", dataStore, columnData.variableName, tableData.fieldTableName, columnData.variableElementName);
            else if (columnData.isList)
                builder.addStatement("$N.put($N, $T.listToString($N.get$N()))", dataStore, columnData.variableName, TypeName.get(UtilsKt.class), tableData.fieldTableName, Util.toUpperFirstLetter(columnData.variableElementName));
            else
                builder.addStatement("$N.put($N, $N.get$N())", dataStore, columnData.variableName, tableData.fieldTableName, Util.toUpperFirstLetter(columnData.variableElementName));
        }

        builder.addStatement("return $N", dataStore);

        return builder.build();
    }

    private MethodSpec generateChildrenMethodSpec(TableData tableData) {
        ParameterizedTypeName returnType = ParameterizedTypeName.get(List.class, ChildDataTable.class);
        ParameterizedTypeName listType = ParameterizedTypeName.get(ArrayList.class, ChildDataTable.class);

        MethodSpec.Builder build = MethodSpec.methodBuilder("getChildrenFor")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(returnType)
                .addParameter(TypeName.get(tableData.typeMirror), tableData.fieldTableName)
                .addStatement("$T children = new $T()", returnType, listType)
                .addCode("\n");

        for (ChildData childData : tableData.childrenData) {
            if (childData.isList) {
                build.addStatement("List<$T> $NItems = $N.get$N()", childData.parameterTypeName, childData.variableName, tableData.fieldTableName, Util.toUpperFirstLetter(childData.variableName));
                build.addCode("if ($NItems != null)\n", childData.variableName);
                build.addStatement("\tchildren.addAll($NItems)", childData.variableName);
            } else
                build.addStatement("children.add($N.get$N())", tableData.fieldTableName, Util.toUpperFirstLetter(childData.variableName));
        }

        build.addCode("\n");
        build.addStatement("return children");

        return build.build();
    }
}
