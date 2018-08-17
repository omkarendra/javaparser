/*
 * Copyright 2016 Federico Tomassetti
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.javaparser.symbolsolver;

import com.github.javaparser.*;
import com.github.javaparser.ast.Node;
import com.github.javaparser.utils.CodeGenerationUtils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.github.javaparser.ParserConfiguration.LanguageLevel.BLEEDING_EDGE;

public abstract class AbstractTest implements JavaParserSugar {
    protected ParserConfiguration configuration = new ParserConfiguration().setLanguageLevel(BLEEDING_EDGE);
    @Override
    public <N extends Node> ParseResult<N> parse(ParseStart<N> start, Provider provider) {
        return new JavaParser(configuration).parse(start, provider);
    }

    protected static Path adaptPath(Path path) {
        if (Files.exists(path)) {
            return path.toAbsolutePath();
        }
        Path underSymbolSolver = CodeGenerationUtils.mavenModuleRoot(AbstractTest.class).resolve("javaparser-symbol-solver-testing").resolve(path);
        if (Files.exists(underSymbolSolver)) {
            return underSymbolSolver;
        } else {
            throw new IllegalArgumentException("I cannot adapt the path " + path);
        }
    }

    protected static Path adaptPath(String path) {
        return adaptPath(Paths.get(path));
    }

    protected boolean isJava9() {
        return System.getProperty("java.version").startsWith("9.");
    }

    protected boolean isJava10() {
        return System.getProperty("java.version").startsWith("10.");
    }

    protected boolean isJavaVersion9OrAbove() {
        String jdkVersion = System.getProperty("java.version");
        return Integer.parseInt(jdkVersion.substring(0, jdkVersion.indexOf('.'))) >= 9;
    }

    protected boolean isJava8() {
        return System.getProperty("java.version").startsWith("1.8.");
    }
}
