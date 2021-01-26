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
