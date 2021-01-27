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
  private ClassData myClassData;
  private Collection<ClassData> myInnerClasses;

  public IDEACoverageClassInfo(@NotNull final String className,
                               @Nullable final ClassData classData,
                               @NotNull final Collection<ClassData> innerClasses) {
    super(className);
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

    Object[] lines = myClassData.getLines();
    if (lines == null) return null;

    for (Object obj: lines) {
      if (obj == null) continue; // if obj is null the line is not executable
      total++;
      LineData ld = (LineData) obj;
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
    for (Object m: myClassData.getMethodSigs()) {
      total++;
      Integer status = myClassData.getStatus((String) m);
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
    Object[] lines = myClassData.getLines();
    if (lines == null) return null;
    for (Object l: lines) {
      if (l == null) continue;
      LineData line = (LineData) l;
      BranchData branches = line.getBranchData();
      if (branches == null) continue;
      total += branches.getTotalBranches();
      covered += branches.getCoveredBranches();
    }
    return new Entry(total, covered);
  }

  public Entry getStatementStats() {
    return null;
  }

  public List<ClassInfo> getInnerClasses() {
    List<ClassInfo> result = new ArrayList<ClassInfo>();
    for (ClassData inner: myInnerClasses) {
      result.add(new IDEACoverageClassInfo(inner.getName(), inner, Collections.<ClassData>emptyList()));
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
