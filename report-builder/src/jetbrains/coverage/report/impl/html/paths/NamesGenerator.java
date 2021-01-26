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
