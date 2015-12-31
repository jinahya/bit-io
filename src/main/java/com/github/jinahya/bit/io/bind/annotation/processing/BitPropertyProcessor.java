/*
 * Copyright 2015 Jin Kwon &lt;jinahya_at_gmail.com&gt;.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.jinahya.bit.io.bind.annotation.processing;


import com.github.jinahya.bit.io.bind.annotation.BitProperty;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement;


/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@IgnoreJRERequirement
//@SupportedSourceVersion(SourceVersion.RELEASE_6)
//@SupportedAnnotationTypes({"com.github.jinahya.bit.io.bind.annotation.BitProperty"})
public class BitPropertyProcessor extends AbstractProcessor {


    private static final Set<String> SUPPORTED_ANNOTATION_TYPES;


    static {
        final Set<String> s = new HashSet<String>(1);
        s.add(BitProperty.class.getName());
        SUPPORTED_ANNOTATION_TYPES = Collections.unmodifiableSet(s);
    }


    @Override
    public SourceVersion getSupportedSourceVersion() {

        return SourceVersion.RELEASE_6;
    }


    @Override
    public Set<String> getSupportedAnnotationTypes() {

        return SUPPORTED_ANNOTATION_TYPES;
    }


    @Override
    public synchronized void init(final ProcessingEnvironment processingEnv) {

        System.out.println("init(" + processingEnv + ")");

        super.init(processingEnv);

        messager = processingEnv.getMessager();
    }


    @Override
    public boolean process(final Set<? extends TypeElement> annotations,
                           final RoundEnvironment roundEnv) {

        System.out.println("process(" + annotations + ", " + roundEnv + ")");

        for (final TypeElement annotation : annotations) {
            messager.printMessage(Diagnostic.Kind.NOTE,
                                  "annotation: " + annotation);
            for (final Element element
                 : roundEnv.getElementsAnnotatedWith(annotation)) {
                messager.printMessage(Diagnostic.Kind.NOTE,
                                      "element: " + element.toString());
            }
        }

        return true;
    }


    private transient Messager messager;

}

