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
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 *         19.10.10 16:40
 */
public interface CoverageSourceData {
  /**
   * This method is called for class to procude sources.
   * If no methods is called, it's decided class contains no sources
   * @param clazz class to render sources for
   * @param renderer renderer object given for class to render it's contents
   */
  void renderSourceCodeFor(@NotNull ClassInfo clazz, @NotNull CoverageCodeRenderer renderer);
}
