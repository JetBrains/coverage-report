package jetbrains.coverage.report.impl.html;

import jetbrains.coverage.report.ClassInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 *         13.10.10 18:25
 */
public class ModuleInfo {
  private final String myName;

  public ModuleInfo(String name) {
    myName = name;
  }

  @Nullable
  public String getName() {
    return myName;
  }

  public boolean isEmpty() {
    return myName == null || myName.length() == 0;
  }

  public boolean isNullModule() {
    return myName == null;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    ModuleInfo that = (ModuleInfo) o;
    return !(myName != null ? !myName.equals(that.myName) : that.myName != null);

  }

  @Override
  public int hashCode() {
    return myName != null ? myName.hashCode() : 0;
  }

  @Override
  public String toString() {
    return "ModuleInfo{" +
            "myName='" + myName + '\'' +
            '}';
  }

  @NotNull
  public static ModuleInfo fromClassInfo(@NotNull ClassInfo ci) {
    return new ModuleInfo(ci.getModule());
  }

  public static ModuleInfo newEmpty() {
    return new ModuleInfo(null);
  }

  @NotNull
  public String getPathNamePrefix() {
    return myName == null ? "" : myName;
  }
}
