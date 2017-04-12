package com.dtp;

import com.dtp.data_table.TableType;
import com.dtp.query.QueryBuilder;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.List;

import javax.lang.model.element.Modifier;

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
                .addParameter(DataStoreOut.class, "dataStoreOut")
                .addParameter(Database.class, "database");

        builder.addStatement("$T $N = dataStoreOut.get($N)", TypeName.get(Long.class), "chamberId", "CHAMBER_ID");

        if (tableData.tableType == TableType.CHILD)
            builder.addStatement("$T $N = dataStoreOut.get($N)", TypeName.get(Long.class), "parentChamberId", "PARENT_CHAMBER_ID");

        for (ColumnData columnData : tableData.columns) {
            builder.addStatement("$T $N = dataStoreOut.get($N)", columnData.dataType, columnData.variableElementName, columnData.variableName);
        }

        for (ChildData childData : tableData.childrenData) {
            if (childData.isList) {
                builder.addStatement("$T $N = database.findAll(new $TTable.Builder(), $T.Companion.with($TTable.TABLE_NAME).whereEquals($TTable.PARENT_CHAMBER_ID, chamberId).build())",
                        childData.type, childData.variableName, childData.parameterTypeName, TypeName.get(QueryBuilder.class), childData.parameterTypeName, childData.parameterTypeName);
            } else {
                builder.addStatement("$T $N = database.findFirst(new $TTable.Builder(), $T.Companion.with($TTable.TABLE_NAME).whereEquals($TTable.CHAMBER_ID, chamberId).build())",
                        childData.type, childData.variableName, childData.type, TypeName.get(QueryBuilder.class), childData.type, childData.type);
            }
        }

        builder.addCode("\n$T $N = new $T(", TypeName.get(tableData.typeMirror), tableData.fieldTableName, TypeName.get(tableData.typeMirror));

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

        builder.addStatement("$N.setChamberId($N)", tableData.fieldTableName, "chamberId");

        if (tableData.tableType == TableType.CHILD)
            builder.addStatement("$N.setParentChamberId($N)", tableData.fieldTableName, "parentChamberId");

        builder.addCode("\n");

        builder.addStatement("return $N", tableData.fieldTableName);

        return builder.build();
    }
}
