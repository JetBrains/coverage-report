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
