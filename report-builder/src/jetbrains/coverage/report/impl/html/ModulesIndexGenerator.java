package jetbrains.coverage.report.impl.html;

import jetbrains.coverage.report.CoverageStatistics;
import jetbrains.coverage.report.StatisticsCalculator;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.*;

/**
* @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
*         14.10.10 12:48
*/
public class ModulesIndexGenerator extends BaseGenerator {
  public ModulesIndexGenerator(@NotNull TemplateProcessor templateFactory, @NotNull LocalGeneratorPaths paths) {
    super(templateFactory, paths);
  }

  public void generateModulesIndex(Collection<ModuleInfo> modules, final StatisticsCalculator covStatsCalculator) throws IOException {
    for (SortOption sortOption: getGenerateSortOptions(covStatsCalculator)) {
      Map<String, Object> templateModel = new HashMap<String, Object>();
      templateModel.put("modules", prepareModules(modules, covStatsCalculator, sortOption));
      templateModel.put("statsCalculator", covStatsCalculator);
      templateModel.put("sortOption", sortOption);

      myTemplateFactory.renderTemplate(templateModel, myPaths.getModulesIndexPath(sortOption));
    }
  }

  private List<ModuleInfo> prepareModules(final Collection<ModuleInfo> modules, final StatisticsCalculator covStatsCalculator, final SortOption sortOption) {
    List<ModuleInfo> result = removeModulesWithoutStatistics(modules, covStatsCalculator);
    Collections.sort(result, sortOption.createModulesComparator(covStatsCalculator));
    return result;
  }

  private List<ModuleInfo> removeModulesWithoutStatistics(final Collection<ModuleInfo> modules, final StatisticsCalculator covStatsCalculator) {
    return filterCovered(modules, new Converter<ModuleInfo>() {
      @NotNull
      public CoverageStatistics convert(ModuleInfo moduleInfo) {
        return covStatsCalculator.getForModule(moduleInfo.getName());
      }
    });
  }
}
