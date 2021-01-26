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

import jetbrains.coverage.report.ClassInfo;
import jetbrains.coverage.report.CoverageStatistics;
import jetbrains.coverage.report.StatisticsCalculator;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.*;

/**
* @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
*         14.10.10 13:03
*/
public class ClassesIndexGenerator extends BaseGenerator {
  public ClassesIndexGenerator(@NotNull TemplateProcessor templateFactory, @NotNull LocalGeneratorPaths paths) {
    super(templateFactory, paths);
  }

  public void generateNamespaceIndex(ModuleInfo module, final String ns, final Collection<ClassInfo> nsClasses, final StatisticsCalculator covStatsCalculator) throws IOException {
    for (SortOption sortOption: getGenerateSortOptions(covStatsCalculator)) {
      Map<String, Object> templateModel = new HashMap<String, Object>();
      templateModel.put("module", module);
      templateModel.put("namespace", ns);
      templateModel.put("classes", prepareClasses(nsClasses, covStatsCalculator, sortOption));
      templateModel.put("statsCalculator", covStatsCalculator);
      templateModel.put("sortOption", sortOption);

      myTemplateFactory.renderTemplate(templateModel, myPaths.getClassesIndexPath(module, ns, sortOption));
    }
  }

  private List<ClassInfo> prepareClasses(final Collection<ClassInfo> classes, final StatisticsCalculator covStatsCalculator, final SortOption sortOption) {
    List<ClassInfo> result = removeClassesWithoutStats(classes, covStatsCalculator);
    Collections.sort(result, sortOption.createClassComparator(covStatsCalculator));
    return result;
  }

  private List<ClassInfo> removeClassesWithoutStats(@NotNull final Collection<ClassInfo> classes,
                                                    @NotNull final StatisticsCalculator covStatsCalculator) {
    return filterCovered(classes, new Converter<ClassInfo>() {
      @NotNull
      public CoverageStatistics convert(ClassInfo classInfo) {
        return covStatsCalculator.getForClassWithInnerClasses(classInfo);
      }
    });
  }
}
