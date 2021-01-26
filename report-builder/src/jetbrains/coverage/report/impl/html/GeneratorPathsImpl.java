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
import jetbrains.coverage.report.impl.IOUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 *         14.10.10 15:42
 */
public class GeneratorPathsImpl implements GeneratorPaths {
  private final LocalGeneratorPaths myPaths;
  private final File myGeneratingFile;
  private final File myGeneratingDir;

  public GeneratorPathsImpl(@NotNull LocalGeneratorPaths paths, @NotNull File generatingFile) {
    myGeneratingFile = generatingFile;
    myPaths = paths;
    myGeneratingDir = myGeneratingFile.getParentFile();
  }

  @NotNull
  private String makeRelative(@NotNull GeneratorPaths file) {
    return makeRelative(file.getReportFileName());
  }

  @NotNull
  private String makeRelative(@NotNull File file) {
    return IOUtil.makeRelative(myGeneratingDir.getAbsoluteFile(), file.getAbsoluteFile()).replace('\\', '/');
  }

  @NotNull
  public File getReportFileName() {
    return myGeneratingFile;
  }

  @NotNull
  public String getOrder(@NotNull SortOption order) {
    //TODO: this is a hack. Right code is to call myPaths here
    return order.getIndexFileName();
  }

  @NotNull
  public String getResourcesPath() {
    return makeRelative(myPaths.getResourcesPath());
  }

  @NotNull
  public String getModulesIndexPath(@NotNull SortOption order) {
    return makeRelative(myPaths.getModulesIndexPath(order));
  }

  @NotNull
  public String getNamespacesIndexPath(@NotNull ModuleInfo module, @NotNull SortOption order) {
    return makeRelative(myPaths.getNamespacesIndexPath(module, order));
  }

  @NotNull
  public String getClassesIndexPath(@NotNull ModuleInfo module, @Nullable String namespace, @NotNull SortOption order) {
    return makeRelative(myPaths.getClassesIndexPath(module, namespace, order));
  }

  @NotNull
  public String getClassCoveragePath(@NotNull ModuleInfo module, @Nullable String namespace, @NotNull ClassInfo clazz) {
    return makeRelative(myPaths.getClassCoveragePath(module, namespace, clazz));
  }
}
