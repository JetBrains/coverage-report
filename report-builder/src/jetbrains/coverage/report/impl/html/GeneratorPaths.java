package jetbrains.coverage.report.impl.html;

import jetbrains.coverage.report.ClassInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 *         14.10.10 13:13
 */
public interface GeneratorPaths {
  @NotNull
  File getReportFileName();

  @NotNull
  String getOrder(@NotNull SortOption order);

  @NotNull
  String getResourcesPath();

  @NotNull
  String getModulesIndexPath(@NotNull SortOption order);

  @NotNull
  String getNamespacesIndexPath(@NotNull ModuleInfo module, @NotNull SortOption order);

  @NotNull
  String getClassesIndexPath(@NotNull ModuleInfo module, @Nullable String namespace, @NotNull SortOption order);

  @NotNull
  String getClassCoveragePath(@NotNull ModuleInfo module, @Nullable String namespace, @NotNull ClassInfo clazz);
}
