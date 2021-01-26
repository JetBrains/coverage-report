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

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Map;

/**
* @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
*         14.10.10 12:48
*/
public interface TemplateProcessor {
  void renderTemplate(Map<String, Object> params, @NotNull GeneratorPaths outputFile) throws IOException;
}
