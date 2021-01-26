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

package jetbrains.coverage.report.impl.html;

import jetbrains.coverage.report.ClassInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 *         13.10.10 18:25
 */
public class ModuleInfo {
  private final String myName;

  public ModuleInfo(String name) {
    myName = name;
  }

  @Nullable
  public String getName() {
    return myName;
  }

  public boolean isEmpty() {
    return myName == null || myName.length() == 0;
  }

  public boolean isNullModule() {
    return myName == null;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    ModuleInfo that = (ModuleInfo) o;
    return !(myName != null ? !myName.equals(that.myName) : that.myName != null);

  }

  @Override
  public int hashCode() {
    return myName != null ? myName.hashCode() : 0;
  }

  @Override
  public String toString() {
    return "ModuleInfo{" +
            "myName='" + myName + '\'' +
            '}';
  }

  @NotNull
  public static ModuleInfo fromClassInfo(@NotNull ClassInfo ci) {
    return new ModuleInfo(ci.getModule());
  }

  public static ModuleInfo newEmpty() {
    return new ModuleInfo(null);
  }

  @NotNull
  public String getPathNamePrefix() {
    return myName == null ? "" : myName;
  }
}
