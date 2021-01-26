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

package jetbrains.coverage.report.impl;

import jetbrains.coverage.report.ClassInfo;
import jetbrains.coverage.report.CoverageCodeRenderer;
import jetbrains.coverage.report.CoverageSourceData;
import jetbrains.coverage.report.CoverageStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * @author Pavel.Sher
 */
public class ClassDataBean {
  private ClassInfo myClassInfo;
  @Nullable
  private final CoverageSourceData mySourceData;

  public ClassDataBean(@NotNull final ClassInfo classInfo, @Nullable final CoverageSourceData sourceData) {
    myClassInfo = classInfo;
    mySourceData = sourceData;
  }

  public String getName() {
    return myClassInfo.getName();
  }

  public String getNamespace() {
    return myClassInfo.getNamespace();
  }

  public ClassInfo getClassData() {
    return myClassInfo;
  }

  public List<ClassDataBean> getInnerClasses() {
    List<ClassDataBean> result = new ArrayList<ClassDataBean>();
    collectInnerClasses(myClassInfo, result);
    Collections.sort(result, new Comparator<ClassDataBean>() {
      public int compare(final ClassDataBean o1, final ClassDataBean o2) {
        return o1.getName().compareToIgnoreCase(o2.getName());
      }
    });
    return result;
  }

  private void collectInnerClasses(final ClassInfo classInfo, final List<ClassDataBean> result) {
    final Collection<ClassInfo> innerClasses = classInfo.getInnerClasses();
    if (innerClasses != null) {
      for (ClassInfo cl: innerClasses) {
        result.add(new ClassDataBean(cl, mySourceData));
      }

      for (ClassInfo cl: innerClasses) {
        collectInnerClasses(cl, result);
      }
    }
  }

  public Collection<FileDataBean> getFiles() {
    if (mySourceData == null) return Collections.emptyList();

    final Collection<FileDataBean> data = new ArrayList<FileDataBean>(1);

    final CoverageCodeRenderer renderer = new CoverageCodeRenderer() {
      private Collection<LineDataBean> myLines = new ArrayList<LineDataBean>();
      private String myCaption;

      public void writeSectionHeader(@NotNull String caption) {
        myCaption = caption;
      }

      public void writeCodeLine(int lineNumber, @NotNull CharSequence source, @Nullable CoverageStatus status) {
        myLines.add(new LineDataBean(lineNumber, source, status));
      }

      public void codeWriteFinished() {
        if (myLines.size() > 0 || myCaption != null) {
          data.add(new FileDataBean(myCaption, myLines));
        }
        myLines = new ArrayList<LineDataBean>();
        myCaption = null;
      }
    };

    mySourceData.renderSourceCodeFor(myClassInfo, renderer);
    renderer.codeWriteFinished();

    return data;
  }

  public final static class FileDataBean {
    private final String myCaption;
    private final Collection<LineDataBean> myLines;

    public FileDataBean(@Nullable String caption, @NotNull Collection<LineDataBean> lines) {
      myCaption = caption;
      myLines = lines;
    }

    @Nullable
    public String getCaption() {
      return myCaption;
    }

    @NotNull
    public Collection<LineDataBean> getLines() {
      return myLines;
    }
  }

  public final static class LineDataBean {
    private int myLineNum;
    private CharSequence mySourceCode;
    private CoverageStatus myCoverageStatus;

    public LineDataBean(final int lineNum, final CharSequence sourceCode, @Nullable final CoverageStatus coverageStatus) {
      myLineNum = lineNum;
      mySourceCode = sourceCode;
      myCoverageStatus = coverageStatus;
    }

    public int getLineNum() {
      return myLineNum;
    }

    public CharSequence getSourceCode() {
      return mySourceCode;
    }

    public String getCoverage() {
      return myCoverageStatus.name();
    }

    public boolean isExecutable() {
      return myCoverageStatus != null;
    }
  }
}
