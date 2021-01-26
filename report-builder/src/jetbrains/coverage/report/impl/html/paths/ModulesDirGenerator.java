package jetbrains.coverage.report.impl.html.paths;

import jetbrains.coverage.report.impl.html.ModuleInfo;
import jetbrains.coverage.report.impl.html.SortOption;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
* @author Eugene Petrenko (eugene.petrenko@gmail.com)
*         Date: 27.02.11 18:42
*/
public class ModulesDirGenerator extends NamesGenerator<ModuleInfo, NamespacesDirGenerator> {
  private final File myBase;

  public ModulesDirGenerator(@NotNull final File base) {
    myBase = base;
  }

  @NotNull
  public File getBase() {
    return myBase;
  }

  @NotNull
  @Override
  protected final NamespacesDirGenerator createV(long id) {
    return new NamespacesDirGenerator(createHome(id));
  }

  @NotNull
  protected File createHome(long id) {
    return new File(myBase, "mod-" + Long.toHexString(id));
  }

  @NotNull
  @Override
  protected Object makeKey(@NotNull ModuleInfo moduleInfo) {
    final String name = moduleInfo.getName();
    return name == null ? ")(*&^%$#@!___EMPTY_MODULE_NAME__)(*&^%$#@!" : name;
  }

  @NotNull
  public File getModuleIndexPath(@NotNull SortOption order) {
    return new File(myBase, order.getIndexFileName());
  }
}
