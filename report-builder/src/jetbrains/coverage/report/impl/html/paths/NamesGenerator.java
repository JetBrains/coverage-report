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

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Eugene Petrenko (eugene.petrenko@gmail.com)
 *         Date: 25.02.11 19:55
 */
public abstract class NamesGenerator<K, V> {
  private final Map<Object, V> myData = new HashMap<Object, V>();
  private final AtomicLong myCounter = new AtomicLong();

  @NotNull
  protected abstract V createV(long id);

  @NotNull
  protected abstract Object makeKey(@NotNull K k);

  public V get(@NotNull K k) {
    final Object o = makeKey(k);
    V vHolder = myData.get(o);
    if (vHolder == null) {
      long id = myCounter.incrementAndGet();
      vHolder = createV(id);
      myData.put(o, vHolder);
    }
    return vHolder;
  }
}
