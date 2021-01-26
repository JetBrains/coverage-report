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

/**
 * Given the coverage data calculates coverage statistics.
 * Not thread safe.
 */
public interface StatisticsCalculator {
  /**
   * Computes statistics.
   * @param coverageData coverage data
   */
  void compute(@NotNull CoverageData coverageData);

  /**
   * Computes statistics with difference information between supplied coverage datas.
   * @param before older coverage
   * @param after newer coverage
   */
  void compute(@NotNull CoverageData before, @NotNull CoverageData after);

  /**
   * Returns coverage statistics for single class
   * @param classInfo class
   * @return see above
   */
  @NotNull
  CoverageStatistics getForClass(@NotNull ClassInfo classInfo);

  /**
   * Returns coverage statistics for class including inner classes (inner classes statistics added to the class statistics).
   * @param classInfo class
   * @return see above
   */
  @NotNull
  CoverageStatistics getForClassWithInnerClasses(@NotNull ClassInfo classInfo);

  /**
   * Returns coverage statistics for namespace (e.g. package in Java)
   * @param module module
   * @param namespace namespace
   * @return see above
   */
  @NotNull
  CoverageStatistics getForNamespace(@Nullable String module, String namespace);


  /**
   * Returns coverage statistics for module (e.g. .NET assembly)
   * @param module module name
   * @return see above
   */
  @NotNull
  CoverageStatistics getForModule(@Nullable String module);

  /**
   * Returns total statistics for all classes.
   * @return see above
   */
  @NotNull
  CoverageStatistics getOverallStats();
}
