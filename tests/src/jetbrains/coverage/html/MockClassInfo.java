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

package jetbrains.coverage.html;

import jetbrains.coverage.report.ClassInfo;
import jetbrains.coverage.report.Entry;
import jetbrains.coverage.report.JavaClassInfo;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

/**
 * @author Pavel.Sher
 */
public class MockClassInfo extends JavaClassInfo {
  private List<ClassInfo> myInnerClasses;
  private String myModuleName;
  private Entry myLines;
  private Entry myMethods;
  private Entry myBlocks;

  public MockClassInfo(final String fqname) {
    this(fqname, null);
  }

  public MockClassInfo(@NotNull String fqClassName, String myModuleName) {
    super(fqClassName);
    this.myModuleName = myModuleName;
  }

  @Override
  public String getModule() {
    return myModuleName;
  }

  public void setModule(String myModuleName) {
    this.myModuleName = myModuleName;
  }

  public void setInnerClasses(final List<ClassInfo> innerClasses) {
    myInnerClasses = innerClasses;
  }

  public Collection<ClassInfo> getInnerClasses() {
    return myInnerClasses;
  }

  public Entry getMethodStats() {
    return myMethods;
  }

  public Entry getBlockStats() {
    return myBlocks;
  }

  public Entry getLineStats() {
    return myLines;
  }

  public void setLines(Entry lines) {
    myLines = lines;
  }

  public void setMethods(Entry methods) {
    myMethods = methods;
  }

  public void setBlocks(Entry blocks) {
    myBlocks = blocks;
  }

  public Entry getStatementStats() {
    return null;
  }
}
