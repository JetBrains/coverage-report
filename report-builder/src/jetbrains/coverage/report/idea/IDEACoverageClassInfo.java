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

import com.intellij.rt.coverage.data.BranchData;
import com.intellij.rt.coverage.data.ClassData;
import com.intellij.rt.coverage.data.LineCoverage;
import com.intellij.rt.coverage.data.LineData;
import com.intellij.rt.coverage.data.ProjectData;
import com.intellij.rt.coverage.data.instructions.ClassInstructions;
import com.intellij.rt.coverage.data.instructions.LineInstructions;
import jetbrains.coverage.report.ClassInfo;
import jetbrains.coverage.report.Entry;
import jetbrains.coverage.report.JavaClassInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Pavel.Sher
 */
public class IDEACoverageClassInfo extends JavaClassInfo {
  private final ProjectData myProjectData;
  private final ClassData myClassData;
  private final Collection<ClassData> myInnerClasses;

  public IDEACoverageClassInfo(@NotNull ProjectData projectData,
                               @NotNull final String className,
                               @Nullable final ClassData classData,
                               @NotNull final Collection<ClassData> innerClasses) {
    super(className);
    myProjectData = projectData;
    myClassData = classData;
    myInnerClasses = innerClasses;
  }

  @NotNull
  public Collection<ClassData> getClassDataWithInternal() {
    Collection<ClassData> list = new ArrayList<ClassData>();
    if (myClassData != null) {
      list.add(myClassData);
    }
    list.addAll(myInnerClasses);
    return list;
  }

  public Entry getLineStats() {
    int total = 0;
    int covered = 0;
    if (myClassData == null) return null;

    LineData[] lines = (LineData[]) myClassData.getLines();
    if (lines == null) return null;

    for (LineData ld: lines) {
      if (ld == null) continue; // if ld is null the line is not executable
      total++;
      if (isCovered(ld.getStatus())) {
        covered++;
      }
    }
    return new Entry(total, covered);
  }

  @Nullable
  public Entry getMethodStats() {
    int total = 0;
    int covered = 0;

    if (myClassData == null) return null;
    for (String m: myClassData.getMethodSigs()) {
      total++;
      Integer status = myClassData.getStatus(m);
      if (isCovered(status)) {
        covered++;
      }
    }
    return new Entry(total, covered);
  }

  public Entry getBlockStats() {
    int total = 0;
    int covered = 0;
    if (myClassData == null) return null;
    LineData[] lines = (LineData[]) myClassData.getLines();
    if (lines == null) return null;
    for (LineData line: lines) {
      if (line == null) continue;
      BranchData branches = line.getBranchData();
      if (branches == null) continue;
      total += branches.getTotalBranches();
      covered += branches.getCoveredBranches();
    }
    return new Entry(total, covered);
  }

  public Entry getStatementStats() {
    if (!myProjectData.isInstructionsCoverageEnabled()) return null;
    final ClassInstructions classInstructions = myProjectData.getInstructions().get(getFQName());
    if (classInstructions == null) return null;
    if (myClassData == null) return null;
    final LineData[] lines = (LineData[])myClassData.getLines();
    if (lines == null) return null;
    int total = 0;
    int covered = 0;
    final LineInstructions[] instructions = classInstructions.getlines();
    for (LineData lineData : lines) {
      if (lineData == null) continue;
      if (lineData.getLineNumber() >= instructions.length) break;
      final LineInstructions lineInstructions = instructions[lineData.getLineNumber()];
      if (lineInstructions == null) continue;
      final BranchData summary = lineInstructions.getInstructionsData(lineData);
      total += summary.getTotalBranches();
      covered += summary.getCoveredBranches();
    }
    return new Entry(total, covered);
  }

  public List<ClassInfo> getInnerClasses() {
    List<ClassInfo> result = new ArrayList<ClassInfo>();
    for (ClassData inner: myInnerClasses) {
      result.add(new IDEACoverageClassInfo(myProjectData, inner.getName(), inner, Collections.<ClassData>emptyList()));
    }
    return result;
  }

  private boolean isCovered(final Integer status) {
    if (status == null) return false;

    switch ((byte)status.intValue()) {
      case LineCoverage.NONE:
        return false;
      case LineCoverage.PARTIAL:
        return true;
      case LineCoverage.FULL:
        return true;
    }
    return false;
  }
}
