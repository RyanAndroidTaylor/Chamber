package com.dtp;

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

        List<ColumnData> columns = tableData.columns;

        builder.addStatement("$T $N = dataStoreOut.get($N)", TypeName.get(Long.class), "chamberId", "CHAMBER_ID");

        for (ColumnData columnData : columns) {
            builder.addStatement("$T $N = dataStoreOut.get($N)", columnData.dataType, columnData.variableElementName, columnData.variableName);
        }

        builder.addCode("\n$T $N = new $T(", TypeName.get(tableData.typeMirror), tableData.fieldTableName, TypeName.get(tableData.typeMirror));

        for (int i = 0; i < columns.size(); i++) {
            ColumnData columnData = columns.get(i);

            if (i < columns.size() - 1)
                builder.addCode("$N, ", columnData.variableElementName);
            else
                builder.addCode("$N", columnData.variableElementName);
        }

        builder.addCode(");\n\n");

        builder.addStatement("$N.setChamberId($N)", tableData.fieldTableName, "chamberId");

        builder.addCode("\n");

        builder.addStatement("return $N", tableData.fieldTableName);

        return builder.build();
    }
}
