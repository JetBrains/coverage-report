/*
 * Copyright 2000-2021 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jetbrains.coverage.report.idea;

import com.intellij.rt.coverage.data.ClassData;
import com.intellij.rt.coverage.data.LineCoverage;
import com.intellij.rt.coverage.data.LineData;
import com.intellij.rt.coverage.data.ProjectData;
import com.intellij.rt.coverage.util.ProjectDataLoader;
import jetbrains.coverage.report.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * Provides implementation of CoverageData for IDEA coverage runner
 */
public class IDEACoverageData implements CoverageData, CoverageSourceData {
  private final List<ClassInfo> myClasses = new ArrayList<ClassInfo>();
  private final SourceCodeProvider mySourceCodeProvider;

  public IDEACoverageData(final ProjectData projectData, @NotNull final SourceCodeProvider sourceCodeProvider) throws IOException {
    mySourceCodeProvider = sourceCodeProvider;
    init(sourceCodeProvider, projectData);
  }

  public IDEACoverageData(@NotNull final String coverageIc, @NotNull final SourceCodeProvider sourceCodeProvider) throws IOException {
    mySourceCodeProvider = sourceCodeProvider;
    File coverageIcFile = new File(coverageIc);
    if (!coverageIcFile.isFile()) throw new FileNotFoundException("File " + coverageIc + " does not exist");

    init(sourceCodeProvider, ProjectDataLoader.load(coverageIcFile));
  }

  private void init(SourceCodeProvider sourceCodeProvider, ProjectData projectData) throws IOException {
    Map<String, Collection<String>> classAndRelatedClassesMap = collectClasses(projectData);

    for (Map.Entry<String, Collection<String>> classEntry: classAndRelatedClassesMap.entrySet()) {
      String className = classEntry.getKey();
      if (className == null || className.length() == 0) continue;
      if (className.startsWith("com.intellij.rt.coverage")) continue; // ignore coverage implementation classes
      if (isInnerClass(className)) continue;

      ClassData classData = projectData.getClassData(className);
      myClasses.add(new IDEACoverageClassInfo(projectData, className, classData, findRelatedClassData(projectData, classEntry.getValue())));
    }
  }

  @Nullable
  private CharSequence obtainSourceCode(IDEACoverageClassInfo ci) {
    try {
      return mySourceCodeProvider.getSourceCode(ci.getFQName());
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  public void renderSourceCodeFor(@NotNull ClassInfo aClazz, @NotNull CoverageCodeRenderer renderer) {
    IDEACoverageClassInfo idea = (IDEACoverageClassInfo) aClazz;

    final CharSequence source = obtainSourceCode(idea);
    if (source == null) return;

    Collection<ClassData> data = idea.getClassDataWithInternal();
    Map<Integer, CoverageStatus> lines = new TreeMap<Integer, CoverageStatus>();

    for (ClassData clazz : data) {
      final LineData[] linesArray = (LineData[]) clazz.getLines();
      if (linesArray == null) continue;
      for (LineData o : linesArray) {
        if (o != null) {
          lines.put(o.getLineNumber(), CoverageStatus.merge(lines.get(o.getLineNumber()), convertToLineCoverage(o)));
        }
      }
    }

    int lineNum = 0;
    for (CharSequence line : jetbrains.coverage.report.impl.StringUtil.getLines(source)) {
      lineNum++;

      renderer.writeCodeLine(lineNum, line, lines.get(lineNum));
    }
    renderer.codeWriteFinished();
  }

  private CoverageStatus convertToLineCoverage(final LineData status) {
    switch ((byte)status.getStatus()) {
      case LineCoverage.NONE:
        return CoverageStatus.NONE;
      case LineCoverage.PARTIAL:
        return CoverageStatus.PARTIAL;
      case LineCoverage.FULL:
        return CoverageStatus.FULL;
    }
    return CoverageStatus.NONE;
  }

  @NotNull
  public Collection<ClassInfo> getClasses() {
    return myClasses;
  }

  public CoverageSourceData getSourceData() {
    return this;
  }

  private static Map<String, Collection<String>> collectClasses(final ProjectData projectData) throws IOException {
    Set<String> allClasses = new HashSet<String>();
    //noinspection unchecked
    allClasses.addAll(projectData.getClasses().keySet());

    Map<String, Collection<String>> classMap = new HashMap<String, Collection<String>>();

    for (final String className: allClasses) {
      String outerClassName = className;
      if (isInnerClass(className)) {
        int first$ = className.indexOf('$');
        outerClassName = className.substring(0, first$);
      }

      Collection<String> relatedClasses = classMap.get(outerClassName);
      if (relatedClasses == null) {
        relatedClasses = new HashSet<String>();
        classMap.put(outerClassName, relatedClasses);
      }

      if (outerClassName.length() != className.length()) {
        relatedClasses.add(className);
      }
    }

    return classMap;
  }

  private static Collection<ClassData> findRelatedClassData(final ProjectData projectData, final Collection<String> classNames) {
    List<ClassData> result = new ArrayList<ClassData>();
    for (String clName: classNames) {
      ClassData cd = projectData.getClassData(clName);
      if (cd != null) {
        result.add(cd);
      }
    }
    return result;
  }

  private static boolean isInnerClass(String className) {
    return className.indexOf('$') != -1;
  }
}
