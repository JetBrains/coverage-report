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

import jetbrains.coverage.report.impl.html.SortOption;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
* @author Eugene Petrenko (eugene.petrenko@gmail.com)
*         Date: 27.02.11 18:41
*/
public class NamespacesDirGenerator extends NamesGenerator<NamespaceInfo, ClassesDirGenerator> {
  private final File myBase;

  public NamespacesDirGenerator(@NotNull File base) {
    myBase = base;
  }

  @NotNull
  @Override
  protected ClassesDirGenerator createV(long id) {
    return new ClassesDirGenerator(new File(myBase, "ns-" + Long.toHexString(id)));
  }

  @NotNull
  @Override
  protected Object makeKey(@NotNull NamespaceInfo namespaceInfo) {
    final String namespace = namespaceInfo.getNamespace();
    return namespace == null ? ")(*&^%$#@!___EMPTY_NAMESPACE_NAME__)(*&^%$#@!" :  namespace;
  }

  @NotNull
  public File getBase() {
    return myBase;
  }

  @NotNull
  public File getNamespaceIndexPath(@NotNull SortOption order) {
    return new File(myBase, order.getIndexFileName());
  }

}
