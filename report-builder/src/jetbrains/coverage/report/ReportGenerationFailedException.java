package jetbrains.coverage.report;

/**
 * Can be thrown if report cannot be generated
 */
public class ReportGenerationFailedException extends RuntimeException {
  public ReportGenerationFailedException(final String message) {
    super(message);
  }

  public ReportGenerationFailedException(final String message, final Throwable cause) {
    super(message, cause);
  }
}
