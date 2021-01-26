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
