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
