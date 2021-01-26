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
