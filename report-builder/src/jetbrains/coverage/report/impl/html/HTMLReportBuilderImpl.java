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

import jetbrains.coverage.report.*;
import jetbrains.coverage.report.html.HTMLReportBuilder;
import jetbrains.coverage.report.impl.FooterInfos;
import jetbrains.coverage.report.impl.IOUtil;
import jetbrains.coverage.report.impl.html.fs.FileSystem;
import jetbrains.coverage.report.impl.html.fs.RealFSImpl;
import jetbrains.coverage.report.impl.html.fs.ZipFileSystem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

/**
 * @author Pavel.Sher
 */
public class HTMLReportBuilderImpl implements HTMLReportBuilder {
  private File myReportDir;
  private File myReportZip;
  private String myResourceBundleName = "javaCoverage";
  private String myReportTitle = "";

  private static final String CSS_DIR = "css";
  private static final String IMG_DIR = "img";
  private static final String JS_DIR = "js";
  private String myFooterText;
  private String myFooterSourceText;

  public File getReportZip() {
    return myReportZip;
  }

  public void setReportZip(@NotNull File reportZip) {
    myReportZip = reportZip;
    setReportDir(myReportZip.getParentFile());
  }

  public void setReportDir(@NotNull final File reportDir) {
    myReportDir = reportDir;
  }

  public void setResourceBundleName(@NotNull final String resourceBundleName) {
    myResourceBundleName = resourceBundleName;
  }

  public void setFooterHTML(@NotNull String html) {
    myFooterText = html;
  }

  public void setSourceCodeFooterHTML(@NotNull String html) {
    myFooterSourceText = html;
  }

  public void setReportTitle(@NotNull String reportTitle) {
    myReportTitle = reportTitle;
  }

  public void generateReport(@NotNull CoverageData coverageData) throws ReportGenerationFailedException {
    StatisticsCalculator covStatsCalculator = ReportBuilderFactory.createStatisticsCalculator();
    generateReport(coverageData, covStatsCalculator);
  }

  public void generateReport(@NotNull CoverageData data, @NotNull StatisticsCalculator calculator) throws ReportGenerationFailedException {
    calculator.compute(data);
    doGenerateReport(data, calculator);
  }

  private void doGenerateReport(final CoverageData data, final StatisticsCalculator covStatsCalculator) {
    FileSystem fs = null;
    try {
      fs = createFS();
      createReportDir(fs);
      MapToSet<ModuleInfo, ClassInfo> moduleToClassesMap = groupByModules(data.getClasses());

      final TemplateProcessorFactory fac;
      final LocalGeneratorPaths paths;
      final TemplateFactory templateFactory = new TemplateFactory();
      if (moduleToClassesMap.keySet().size() > 1) {
        paths = new ModulesLocalPaths(myReportDir);
        fac = new TemplateProcessorFactory(templateFactory, myResourceBundleName, true, getFooterInfos(), fs, myReportTitle);
        new ModulesIndexGenerator(fac.createModulesIndexProcessor(), paths).generateModulesIndex(moduleToClassesMap.keySet(), covStatsCalculator);
      } else {
        fac = new TemplateProcessorFactory(templateFactory, myResourceBundleName, false, getFooterInfos(), fs, myReportTitle);
        paths = new NamespacesLocalPaths(myReportDir);
      }


      if (moduleToClassesMap.isEmpty()) {
        fac.createEmptyTemplate().renderTemplate(Collections.<String, Object>emptyMap(), paths.getModulesIndexPath(SortOption.NONE));
      } else {
        for (ModuleInfo info : moduleToClassesMap.keySet()) {
          generateReportForModule(data.getSourceData(), covStatsCalculator, fac, paths, moduleToClassesMap, info);
        }
      }
    } catch (IOException e) {
      throw new ReportGenerationFailedException("Failed to generate report", e);
    } finally {
      if (fs instanceof Closeable) {
        IOUtil.close((Closeable) fs);
      }
    }
  }

  private FileSystem createFS() throws IOException {
    if (myReportZip != null) {
      return new ZipFileSystem(myReportZip);
    }

    if (myReportDir != null) {
      return new RealFSImpl();
    }

    throw new IllegalStateException("Report directory must be specified");
  }

  @NotNull
  private FooterInfos getFooterInfos() {
    return new FooterInfos() {
      public String getModulesIndexFooterHTML() {
        return myFooterText;
      }

      public String getNamespacesIndexFooterHTML() {
        return myFooterText;
      }

      public String getClassesIndexFooterHTML() {
        return myFooterText;
      }

      public String getSourceFooterHTML() {
        return myFooterSourceText == null ? myFooterText : myFooterSourceText;
      }
    };
  }

  private void generateReportForModule(@Nullable final CoverageSourceData sourceInfo,
                                       @NotNull final StatisticsCalculator covStatsCalculator,
                                       @NotNull final TemplateProcessorFactory fac,
                                       @NotNull final LocalGeneratorPaths paths,
                                       @NotNull MapToSet<ModuleInfo, ClassInfo> moduleToClassesMap,
                                       @NotNull ModuleInfo info) throws IOException {
    MapToSet<String, ClassInfo> namespaceToClassMap = groupByNamespace(moduleToClassesMap.getValues(info));

    new NamespacesIndexGenerator(fac.createNamespacesIndexProcessor(), paths).generateNamespacesIndex(info, namespaceToClassMap.keySet(), covStatsCalculator);

    for (String namespace : namespaceToClassMap.keySet()) {
          final Collection<ClassInfo> nClasses = namespaceToClassMap.getValues(namespace);

          new ClassesIndexGenerator(fac.createClassesIndexProcessor(), paths).generateNamespaceIndex(info, namespace, nClasses, covStatsCalculator);
          for (ClassInfo clazz: nClasses) {
            new ClassSourceReportGenerator(fac.createClassSourceProcessor(), paths, sourceInfo).generateClassCoverage(info, namespace, clazz, covStatsCalculator);
          }
        }
  }

  private void createReportDir(@NotNull final FileSystem fs) {
    try {
      prepareReportDir(fs);
    } catch (IOException e) {
      throw new ReportGenerationFailedException("Failed to create report directory", e);
    }
  }

  public void generateReport(@NotNull final CoverageData before, @NotNull final CoverageData after) throws ReportGenerationFailedException {
    StatisticsCalculator covStatsCalculator = ReportBuilderFactory.createStatisticsCalculator();
    covStatsCalculator.compute(before, after);

    doGenerateReport(after, covStatsCalculator);
  }

  private void prepareReportDir(@NotNull final FileSystem fs) throws IOException {
    File cssDir = new File(myReportDir, CSS_DIR);
    IOUtil.copyResource(getClass(), "/htmlTemplates/css/coverage.css", fs.openFile(new File(cssDir, "coverage.css")));
    IOUtil.copyResource(getClass(), "/htmlTemplates/css/idea.min.css", fs.openFile(new File(cssDir, "idea.min.css")));

    File imgDir = new File(myReportDir, IMG_DIR);
    IOUtil.copyResource(getClass(), "/htmlTemplates/img/arrowUp.gif", fs.openFile(new File(imgDir, "arrowUp.gif")));
    IOUtil.copyResource(getClass(), "/htmlTemplates/img/arrowDown.gif", fs.openFile(new File(imgDir, "arrowDown.gif")));

    File jsDir = new File(myReportDir, JS_DIR);
    IOUtil.copyResource(getClass(), "/htmlTemplates/js/highlight.min.js", fs.openFile(new File(jsDir, "highlight.min.js")));
    IOUtil.copyResource(getClass(), "/htmlTemplates/js/highlightjs-line-numbers.min.js", fs.openFile(new File(jsDir, "highlightjs-line-numbers.min.js")));
  }

  private MapToSet<ModuleInfo, ClassInfo> groupByModules(final Collection<ClassInfo> coverageData) {
    MapToSet<ModuleInfo, ClassInfo> set = new MapToSet<ModuleInfo, ClassInfo>();
    for (ClassInfo info : coverageData) {
      set.addValue(ModuleInfo.fromClassInfo(info), info);
    }
    return set;
  }

  private MapToSet<String, ClassInfo> groupByNamespace(final Collection<ClassInfo> coverageData) {
    MapToSet<String, ClassInfo> set = new MapToSet<String, ClassInfo>();
    for (ClassInfo cd: coverageData) {
      String ns = cd.getNamespace();
      set.addValue(ns, cd);
    }

    return set;
  }
}
