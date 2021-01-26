package jetbrains.coverage.report;

import org.jetbrains.annotations.NotNull;

/**
 * Java specific representation of {@link ClassInfo} interface
 */
public abstract class JavaClassInfo implements ClassInfo {
  private CharSequence myName;
  private CharSequence myNamespace;
  private String myFQClassName;

  private final static String EMPTY = "";

  /**
   * Accepts fully qualified class name as a parameter.
   * @param fqClassName FQ class name
   */
  public JavaClassInfo(@NotNull String fqClassName) {
    myFQClassName = fqClassName;
    int lastDot = fqClassName.lastIndexOf(".");
    if (lastDot == -1) {
      myName = fqClassName;
      myNamespace = EMPTY;
    } else {
      myName = fqClassName.substring(lastDot+1);
      myNamespace = fqClassName.substring(0, lastDot);
    }
  }

  public String getModule() {
    return null;
  }

  @NotNull
  public String getName() {
    return myName.toString();
  }

  @NotNull
  public String getNamespace() {
    return myNamespace.toString();
  }

  @NotNull
  public String getFQName() {
    return myFQClassName;
  }
}
