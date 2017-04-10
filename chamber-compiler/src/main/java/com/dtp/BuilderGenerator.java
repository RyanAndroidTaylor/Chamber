package com.dtp;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;

/**
 * Created by ner on 4/10/17.
 */

class BuilderGenerator {

    static TypeSpec generateBuilder(TableData tableData) {
        TypeSpec.Builder builder = TypeSpec.classBuilder("Builder")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addSuperinterface(ParameterizedTypeName.get(ClassName.get(ItemBuilder.class), ClassName.get(tableData.typeMirror)));



        return builder.build();
    }

//    private static FieldSpec generateChamberIdSpec() {
//        return FieldSpec.builder(Long.class, "chamberId")
//                .initializer("cursor");
//    }
//
//    private static MethodSpec generateBuildItemMethodSpec(TableData tableData) {
//
//    }
}
