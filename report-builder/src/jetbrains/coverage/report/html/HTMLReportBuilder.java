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

package jetbrains.coverage.report.html;

import jetbrains.coverage.report.CoverageData;
import jetbrains.coverage.report.ReportGenerationFailedException;
import jetbrains.coverage.report.StatisticsCalculator;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * HTML report builder.
 */
public interface HTMLReportBuilder {
  /**
   * Sets report directory, i.e. directory where report will be generated.
   * @param reportDir report dir
   */
  void setReportDir(@NotNull File reportDir);

  /**
   * Sets report zip file name, i.e. file to create zipped report.
   * @param reportZip report dir
   */
  void setReportZip(@NotNull File reportZip);

  /**
   * Generates report from the specified coverage data.
   * @param coverageData coverage data
   * @throws ReportGenerationFailedException if for some reason report cannot be generated
   */
  void generateReport(@NotNull CoverageData coverageData) throws ReportGenerationFailedException;

  /**
   * Generates report with coverage statistics difference according to previously gathered coverage data
   * @param older older coverage
   * @param newer newer coverage
   * @throws ReportGenerationFailedException if for some reason report cannot be generated
   */
  void generateReport(@NotNull CoverageData older, @NotNull CoverageData newer) throws ReportGenerationFailedException;

  /**
   * Generates report with given coverage statistics calculator.
   * It will call {@link StatisticsCalculator#compute(CoverageData)}
   * before generating stats.
   * @param data data
   * @param calculator calculator
   * @throws ReportGenerationFailedException on error
   */
  void generateReport(@NotNull CoverageData data, @NotNull StatisticsCalculator calculator) throws ReportGenerationFailedException;

  /**
   * Sets footer html text for generated source
   * @param html text to be included into generated pages as-is
   */
  void setFooterHTML(@NotNull String html);

  /**
   * Sets footer html text for generated pages showing source code
   * @param html text to be included into generated pages as-is
   */
  void setSourceCodeFooterHTML(@NotNull String html);
}
