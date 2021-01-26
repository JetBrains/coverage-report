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
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 * Represents a class
 */
public interface ClassInfo {
  /**
   * Name of the class (short name, without namespace)
   * @return see above
   */
  @NotNull
  String getName();

  /**
   * Module, a.k.a. .jar or .NET Assembly
   * @return module or null
   */
  @Nullable
  String getModule();

  /**
   * Class namespace (e.g. package in Java)
   * @return see above
   */
  @NotNull
  String getNamespace();

  /**
   * Returns class fully qualified name
   * @return see above
   */
  @NotNull
  String getFQName();

  /**
   * Inner classes.
   * @return see above
   */
  @Nullable
  Collection<ClassInfo> getInnerClasses();

  /**
   * Returns methods statistics
   * @return see above
   */
  @Nullable
  Entry getMethodStats();

  /**
   * Returns blocks (conditions {@literal &} loops) statistics
   * @return see above
   */
  @Nullable
  Entry getBlockStats();

  /**
   * Returns lines statistics
   * @return see above
   */
  @Nullable
  Entry getLineStats();

  /**
   * Returnes statement coverage
   * @return see above
   */
  @Nullable
  Entry getStatementStats();
}
