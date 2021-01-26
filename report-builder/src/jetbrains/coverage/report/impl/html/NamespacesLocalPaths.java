package jetbrains.coverage.report.impl.html;

import jetbrains.coverage.report.impl.html.paths.ModulesDirGenerator;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 *         14.10.10 19:14
 */
public class NamespacesLocalPaths extends LocalGeneratorPathsBase {
  public NamespacesLocalPaths(@NotNull File reportsDir) {
    super(new ModulesDirGenerator(reportsDir) {
      @NotNull
      @Override
      protected File createHome(long id) {
        return getBase();
      }
    });
  }
}
