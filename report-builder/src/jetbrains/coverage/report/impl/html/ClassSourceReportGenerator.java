package jetbrains.coverage.report.impl.html;

import jetbrains.coverage.report.ClassInfo;
import jetbrains.coverage.report.CoverageSourceData;
import jetbrains.coverage.report.StatisticsCalculator;
import jetbrains.coverage.report.impl.ClassDataBean;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
* @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
*         14.10.10 13:06
*/
public class ClassSourceReportGenerator extends BaseGenerator {
  private final CoverageSourceData myData;

  public ClassSourceReportGenerator(@NotNull TemplateProcessor templateFactory,
                                    @NotNull LocalGeneratorPaths paths,
                                    @Nullable CoverageSourceData data) {
    super(templateFactory, paths);
    myData = data;
  }

  public void generateClassCoverage(final ModuleInfo module, final String namespace, final ClassInfo cd, final StatisticsCalculator covStatsCalculator) throws IOException {
    Map<String, Object> templateModel = new HashMap<String, Object>();
    templateModel.put("module", module);
    templateModel.put("namespace", namespace);
    templateModel.put("classDataBean", new ClassDataBean(cd, myData));
    templateModel.put("statsCalculator", covStatsCalculator);
    templateModel.put("sortOption", SortOption.NONE);

    myTemplateFactory.renderTemplate(templateModel, myPaths.getClassCoveragePath(module, namespace, cd));
  }

}
