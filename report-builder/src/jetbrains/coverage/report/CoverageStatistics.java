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

import org.jetbrains.annotations.Nullable;

/**
 * Represents coverage statistics
 */
public interface CoverageStatistics {
  /**
   * Returns class statistics
   * @return see above
   */
  @Nullable
  StatEntry getClassStats();

  /**
   * Returns methods statistics
   * @return see above
   */
  @Nullable
  StatEntry getMethodStats();

  /**
   * Returns blocks (conditions {@literal &} loops) statistics
   * @return see above
   */
  @Nullable
  StatEntry getBlockStats();

  /**
   * Returns lines statistics
   * @return see above
   */
  @Nullable
  StatEntry getLineStats();

  /**
   * Returns statements statistics
   * @return see above
   */
  @Nullable
  StatEntry getStatementStats();
}
