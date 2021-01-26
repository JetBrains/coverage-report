package jetbrains.coverage.report.impl.html;

import jetbrains.coverage.report.ClassInfo;
import jetbrains.coverage.report.impl.html.paths.ClassesDirGenerator;
import jetbrains.coverage.report.impl.html.paths.ModulesDirGenerator;
import jetbrains.coverage.report.impl.html.paths.NamespaceInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

/**
 * @author Eugene Petrenko (eugene.petrenko@gmail.com)
 *         Date: 27.02.11 18:48
 */
public class LocalGeneratorPathsBase implements LocalGeneratorPaths {
  protected final ModulesDirGenerator myModuleFiles;

  public LocalGeneratorPathsBase(ModulesDirGenerator moduleFiles) {
    myModuleFiles = moduleFiles;
  }

  @NotNull
  public File getResourcesPath() {
    return myModuleFiles.getBase();
  }

  private GeneratorPaths generate(@NotNull final File path) {
    return new GeneratorPathsImpl(this, path);
  }

  @NotNull
  public GeneratorPaths getModulesIndexPath(@NotNull SortOption order) {
    return generate(myModuleFiles.getModuleIndexPath(order));
  }

  @NotNull
  public GeneratorPaths getNamespacesIndexPath(@NotNull ModuleInfo module, @NotNull SortOption order) {
    return generate(myModuleFiles.get(module).getNamespaceIndexPath(order));
  }

  @NotNull
  protected ClassesDirGenerator getClassesIndexDir(@NotNull ModuleInfo module, @Nullable String namespace) {
    return myModuleFiles.get(module).get(new NamespaceInfo(namespace));
  }

  @NotNull
  public GeneratorPaths getClassesIndexPath(@NotNull ModuleInfo module, @Nullable String namespace, @NotNull SortOption order) {
    return generate(getClassesIndexDir(module, namespace).getClassesIndexPath(order));
  }

  @NotNull
  public GeneratorPaths getClassCoveragePath(@NotNull ModuleInfo module, @Nullable String namespace, @NotNull ClassInfo clazz) {
    return generate(getClassesIndexDir(module, namespace).get(clazz));
  }
}
