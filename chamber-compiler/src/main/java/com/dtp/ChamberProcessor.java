package com.dtp;

import com.google.auto.service.AutoService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

@AutoService(Processor.class)
public class ChamberProcessor extends AbstractProcessor {

    private Messager messager;
    private DataCollector dataCollector;
    private FileGenerator fileGenerator;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);

        messager = processingEnvironment.getMessager();
        dataCollector = new DataCollector(messager);
        fileGenerator = new FileGenerator(processingEnvironment.getFiler());
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(ChamberTable.class.getCanonicalName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        List<TableData> tablesData = new ArrayList<>();

        for (Element element : roundEnvironment.getElementsAnnotatedWith(ChamberTable.class)) {
            if (element instanceof TypeElement) {
                TypeElement typeElement = (TypeElement) element;

                if (isValidClass(typeElement)) {
                    tablesData.add(dataCollector.getTableData(typeElement));
                }
            }
        }

        fileGenerator.generateFiles(tablesData);

        return true;
    }

    private boolean isValidClass(TypeElement typeElement) {
        return typeElement != null;
    }
}
