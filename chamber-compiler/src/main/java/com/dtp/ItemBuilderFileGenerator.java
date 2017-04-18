package com.dtp;

import com.dtp.columns.StringListColumn;
import com.dtp.data_table.TableType;
import com.dtp.query.QueryBuilder;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.List;

import javax.lang.model.element.Modifier;

import static com.dtp.Const.CHAMBER_ID_COLUMN_VARIABLE_NAME;
import static com.dtp.Const.CHAMBER_ID_VARIABLE_NAME;
import static com.dtp.Const.DATABASE_VARIABLE_NAME;
import static com.dtp.Const.DATA_STORE_OUT_VARIABLE_NAME;
import static com.dtp.Const.PARENT_CHAMBER_ID_COLUMN_VARIABLE_NAME;
import static com.dtp.Const.PARENT_CHAMBER_ID_VARIABLE_NAME;
import static com.dtp.Const.TABLE_NAME;

/**
 * Created by ner on 4/10/17.
 */

class ItemBuilderFileGenerator {

    static TypeSpec generateBuilder(TableData tableData) {
        TypeSpec.Builder builder = TypeSpec.classBuilder("Builder")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addSuperinterface(ParameterizedTypeName.get(ClassName.get(ItemBuilder.class), ClassName.get(tableData.typeMirror)));

        builder.addMethod(generateBuildItemMethodSpec(tableData));

        return builder.build();
    }

    private static MethodSpec generateBuildItemMethodSpec(TableData tableData) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("buildItem")
                .addModifiers(Modifier.PUBLIC)
                .returns(TypeName.get(tableData.typeMirror))
                .addParameter(DataStoreOut.class, DATA_STORE_OUT_VARIABLE_NAME)
                .addParameter(Database.class, DATABASE_VARIABLE_NAME);

        builder.addStatement("$T $N = $N.get($N)", TypeName.get(Long.class), CHAMBER_ID_VARIABLE_NAME, DATA_STORE_OUT_VARIABLE_NAME, CHAMBER_ID_COLUMN_VARIABLE_NAME);

        if (tableData.isChild())
            builder.addStatement("$T $N = $N.get($N)", TypeName.get(Long.class), PARENT_CHAMBER_ID_VARIABLE_NAME, DATA_STORE_OUT_VARIABLE_NAME, PARENT_CHAMBER_ID_COLUMN_VARIABLE_NAME);

        for (ColumnData columnData : tableData.columns) {
            if (columnData.columnType == StringListColumn.class) {
                builder.addStatement("String $NString = $N.get($N)", columnData.variableElementName, DATA_STORE_OUT_VARIABLE_NAME, columnData.variableName);
                builder.addStatement("$T $N = $T.split($NString)", columnData.dataType, columnData.variableElementName, UtilsKt.class, columnData.variableElementName);
            }
            else
                builder.addStatement("$T $N = $N.get($N)", columnData.dataType, columnData.variableElementName, DATA_STORE_OUT_VARIABLE_NAME, columnData.variableName);
        }

        builder.addCode("\n");

        for (ChildData childData : tableData.childrenData) {
            if (childData.isList) {
                builder.addStatement("$T $N = $N.findAll(new $TTable.Builder(), $T.Companion.with($TTable.$N).whereEquals($TTable.$N, $N).build())",
                        childData.type, childData.variableName, DATABASE_VARIABLE_NAME, childData.parameterTypeName, TypeName.get(QueryBuilder.class), childData.parameterTypeName, TABLE_NAME,
                        childData.parameterTypeName, PARENT_CHAMBER_ID_COLUMN_VARIABLE_NAME, CHAMBER_ID_VARIABLE_NAME);
            } else {
                builder.addStatement("$T $N = $N.findFirst(new $TTable.Builder(), $T.Companion.with($TTable.$N).whereEquals($TTable.$N, $N).build())",
                        childData.type, childData.variableName, DATABASE_VARIABLE_NAME, childData.type, TypeName.get(QueryBuilder.class),
                        childData.type, TABLE_NAME, childData.type, CHAMBER_ID_COLUMN_VARIABLE_NAME, CHAMBER_ID_VARIABLE_NAME);
            }
        }

        builder.addCode("$T $NObject = new $T(", TypeName.get(tableData.typeMirror), tableData.fieldTableName, TypeName.get(tableData.typeMirror));

        List<VariableData> variables = tableData.variables;

        for (int i = 0; i < variables.size(); i++) {
            VariableData variableData = variables.get(i);

            if (variableData instanceof ColumnData) {
                ColumnData columnData = (ColumnData) variableData;

                if (i < variables.size() - 1)
                    builder.addCode("$N, ", columnData.variableElementName);
                else
                    builder.addCode("$N", columnData.variableElementName);
            } else if (variableData instanceof ChildData) {
                ChildData child = (ChildData) variableData;

                if (i < variables.size() - 1)
                    builder.addCode("$N, ", child.variableName);
                else
                    builder.addCode("$N", child.variableName);
            }
        }

        builder.addCode(");\n\n");

        builder.addStatement("$NObject.setChamberId($N)", tableData.fieldTableName, CHAMBER_ID_VARIABLE_NAME);

        if (tableData.isChild())
            builder.addStatement("$NObject.setParentChamberId($N)", tableData.fieldTableName, PARENT_CHAMBER_ID_VARIABLE_NAME);

        builder.addCode("\n");

        builder.addStatement("return $NObject", tableData.fieldTableName);

        return builder.build();
    }
}
