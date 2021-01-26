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

import jetbrains.coverage.report.html.HTMLReportBuilder;
import jetbrains.coverage.report.impl.StatisticsCalculatorImpl;
import jetbrains.coverage.report.impl.html.HTMLReportBuilderImpl;

/**
 * Factory for report builder classes
 */
public class ReportBuilderFactory {
  /**
   * Creates new HTML report builder
   * @return new HTML report builder
   */
  public static HTMLReportBuilder createHTMLReportBuilder() {
    return new HTMLReportBuilderImpl();
  }

  /**
   * Creates new HTML report builder
   * @return new HTML report builder
   */
  public static HTMLReportBuilder createHTMLReportBuilderForDotNet() {
    return new HTMLReportBuilderImpl() {{setResourceBundleName("dotNetCoverage");}};
  }

  /**
   * Creates new statistics calculator
   * @return new statistics calculator
   */
  public static StatisticsCalculator createStatisticsCalculator() {
    return new StatisticsCalculatorImpl();
  }
}
