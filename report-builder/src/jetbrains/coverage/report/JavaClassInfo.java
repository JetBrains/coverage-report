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

package jetbrains.coverage.report;

import org.jetbrains.annotations.NotNull;

/**
 * Java specific representation of {@link ClassInfo} interface
 */
public abstract class JavaClassInfo implements ClassInfo {
  private CharSequence myName;
  private CharSequence myNamespace;
  private String myFQClassName;

  private final static String EMPTY = "";

  /**
   * Accepts fully qualified class name as a parameter.
   * @param fqClassName FQ class name
   */
  public JavaClassInfo(@NotNull String fqClassName) {
    myFQClassName = fqClassName;
    int lastDot = fqClassName.lastIndexOf(".");
    if (lastDot == -1) {
      myName = fqClassName;
      myNamespace = EMPTY;
    } else {
      myName = fqClassName.substring(lastDot+1);
      myNamespace = fqClassName.substring(0, lastDot);
    }
  }

  public String getModule() {
    return null;
  }

  @NotNull
  public String getName() {
    return myName.toString();
  }

  @NotNull
  public String getNamespace() {
    return myNamespace.toString();
  }

  @NotNull
  public String getFQName() {
    return myFQClassName;
  }
}
