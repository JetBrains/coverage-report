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
