package jetbrains.coverage.html;

import jetbrains.coverage.report.ClassInfo;
import jetbrains.coverage.report.Entry;
import jetbrains.coverage.report.JavaClassInfo;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

/**
 * @author Pavel.Sher
 */
public class MockClassInfo extends JavaClassInfo {
  private List<ClassInfo> myInnerClasses;
  private String myModuleName;
  private Entry myLines;
  private Entry myMethods;
  private Entry myBlocks;

  public MockClassInfo(final String fqname) {
    this(fqname, null);
  }

  public MockClassInfo(@NotNull String fqClassName, String myModuleName) {
    super(fqClassName);
    this.myModuleName = myModuleName;
  }

  @Override
  public String getModule() {
    return myModuleName;
  }

  public void setModule(String myModuleName) {
    this.myModuleName = myModuleName;
  }

  public void setInnerClasses(final List<ClassInfo> innerClasses) {
    myInnerClasses = innerClasses;
  }

  public Collection<ClassInfo> getInnerClasses() {
    return myInnerClasses;
  }

  public Entry getMethodStats() {
    return myMethods;
  }

  public Entry getBlockStats() {
    return myBlocks;
  }

  public Entry getLineStats() {
    return myLines;
  }

  public void setLines(Entry lines) {
    myLines = lines;
  }

  public void setMethods(Entry methods) {
    myMethods = methods;
  }

  public void setBlocks(Entry blocks) {
    myBlocks = blocks;
  }

  public Entry getStatementStats() {
    return null;
  }
}
