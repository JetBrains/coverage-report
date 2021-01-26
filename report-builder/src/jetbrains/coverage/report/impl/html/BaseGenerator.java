package jetbrains.coverage.report.impl.html;

import jetbrains.coverage.report.CoverageStatistics;
import jetbrains.coverage.report.Entry;
import jetbrains.coverage.report.StatisticsCalculator;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 *         14.10.10 12:56
 */
public class BaseGenerator {
  protected final TemplateProcessor myTemplateFactory;
  protected final LocalGeneratorPaths myPaths;

  public BaseGenerator(@NotNull final TemplateProcessor templateFactory, @NotNull LocalGeneratorPaths paths) {
    myPaths = paths;
    myTemplateFactory = templateFactory;
  }

  private boolean isCovered(CoverageStatistics stat) {
    return !isEmpty(stat.getLineStats()) || !isEmpty(stat.getStatementStats());
  }

  private boolean isEmpty(Entry entry) {
    return entry == null || entry.getTotal() <= 0 || entry.getCovered() < 0;
  }

  @NotNull
  protected Collection<SortOption> getGenerateSortOptions(@NotNull final StatisticsCalculator covStatsCalculator) {
    final List<SortOption> sortOptions = new ArrayList<SortOption>(Arrays.asList(SortOption.values()));
    sortOptions.remove(SortOption.NONE);

    if (isEmpty(covStatsCalculator.getOverallStats().getLineStats())) {
      sortOptions.remove(SortOption.SORT_BY_LINE);
      sortOptions.remove(SortOption.SORT_BY_LINE_DESC);
    }

    if (isEmpty(covStatsCalculator.getOverallStats().getStatementStats())) {
      sortOptions.remove(SortOption.SORT_BY_STATEMENT);
      sortOptions.remove(SortOption.SORT_BY_STATEMENT_DESC);
    }

    return sortOptions;
  }

  protected interface Converter<T> {
    @NotNull
    CoverageStatistics convert(T t);
  }

  @NotNull
  protected <T> List<T> filterCovered(@NotNull Collection<T> collection, @NotNull Converter<T> conv) {
    List<T> list = new ArrayList<T>(collection.size());
    for (T t : collection) {
      if (isCovered(conv.convert(t))){
        list.add(t);
      }
    }
    return list;
  }
}
