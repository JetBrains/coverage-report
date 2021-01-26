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

import java.util.*;

/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 *         13.10.10 17:44
 */
public class MapToSet<K, V> {
  private final Map<K, Collection<V>> myMap = new HashMap<K, Collection<V>>();

  public void addValue(K k, @NotNull V v) {
    Collection<V> vs = myMap.get(k);
    if (vs == null) {
      myMap.put(k, vs = new HashSet<V>());
    }

    vs.add(v);
  }

  public boolean isEmpty() {
    return myMap.isEmpty();
  }

  @NotNull
  public Collection<V> getValues(K k) {
    final Collection<V> vs = myMap.get(k);
    return vs == null ? Collections.<V>emptyList() : Collections.unmodifiableCollection(vs);
  }

    @NotNull
  public Collection<K> keySet() {
    return Collections.unmodifiableCollection(myMap.keySet());
  }
}
