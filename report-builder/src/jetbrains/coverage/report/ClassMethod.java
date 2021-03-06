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
 * Represents class method
 */
public class ClassMethod {
  private final CoverageStatus myCoverage;
  private final String mySignature;

  public ClassMethod(@NotNull String signature, @Nullable CoverageStatus status) {
    mySignature = signature;
    myCoverage = status;
  }

  /**
   * Returns method signature
   * @return method signature
   */
  public String getSignature() {
    return mySignature;
  }

  /**
   * Returns method coverage, or null. If null is returned this method will not affect coverage statistics.
   * @return method coverage
   */
  @Nullable
  public CoverageStatus getCoverage() {
    return myCoverage;
  }
}
