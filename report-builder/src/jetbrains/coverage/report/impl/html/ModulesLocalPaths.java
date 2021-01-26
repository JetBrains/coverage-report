package jetbrains.coverage.report.impl.html;

import jetbrains.coverage.report.impl.html.paths.ModulesDirGenerator;

import java.io.File;

/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 *         14.10.10 13:17
 */
public class ModulesLocalPaths extends LocalGeneratorPathsBase {
  public ModulesLocalPaths(File reportsDir) {
    super(new ModulesDirGenerator(reportsDir));
  }
}
