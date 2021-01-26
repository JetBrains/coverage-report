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
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 *         19.10.10 13:46
 */
public interface CoverageCodeRenderer {
  /**
   * Creates new source file section with header before
   * @param caption section header text
   */
  void writeSectionHeader(@NotNull String caption);

  /**
   * Attached a source code line to a source code section.
   * @param lineNumber number of line to show
   * @param source text of a line
   * @param status covered status
   */
  void writeCodeLine(int lineNumber, @NotNull CharSequence source, @Nullable CoverageStatus status);

  /**
   * Should be called at the end of source file rendering.
   */
  void codeWriteFinished();
}
