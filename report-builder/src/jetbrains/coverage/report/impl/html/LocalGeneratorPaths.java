package jetbrains.coverage.report.impl.html;

import jetbrains.coverage.report.ClassInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 *         14.10.10 13:15
 */
public interface LocalGeneratorPaths {
  @NotNull
  GeneratorPaths getModulesIndexPath(@NotNull SortOption order);

  @NotNull
  GeneratorPaths getNamespacesIndexPath(@NotNull ModuleInfo module, @NotNull SortOption order);

  @NotNull
  GeneratorPaths getClassesIndexPath(@NotNull ModuleInfo module, @Nullable String namespace, @NotNull SortOption order);

  @NotNull
  GeneratorPaths getClassCoveragePath(@NotNull ModuleInfo module, @Nullable String namespace, @NotNull ClassInfo clazz);

  @NotNull
  File getResourcesPath();
}
