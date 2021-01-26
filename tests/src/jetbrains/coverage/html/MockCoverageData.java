package jetbrains.coverage.html;

import jetbrains.coverage.Strings;
import jetbrains.coverage.report.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Pavel.Sher
 */
public class MockCoverageData implements CoverageData, CoverageSourceData {
  private List<ClassInfo> myClasses = new ArrayList<ClassInfo>();
  private final Map<ClassInfo, String> mySources = new HashMap<ClassInfo, String>();

  private int myCodeSize = 0;

  public void setCodeSize(int codeSize) {
    myCodeSize = codeSize;
  }

  public void addClass(ClassInfo cd) {
    myClasses.add(cd);
  }

  @NotNull
  public Collection<ClassInfo> getClasses() {
    return myClasses;
  }

  public CoverageSourceData getSourceData() {
    return this;
  }

  public void setSource(ClassInfo clazz, String source) {
    mySources.put(clazz, source);
  }

  public void renderSourceCodeFor(@NotNull ClassInfo clazz, @NotNull CoverageCodeRenderer renderer) {
    renderer.writeCodeLine(1, "public class " + clazz.getName() + " {", null);
    final String src = mySources.get(clazz);
    if (src != null) {
      renderer.writeCodeLine(7, src, CoverageStatus.FULL);
    }
    renderer.writeCodeLine(10, "Test line 10: null", null);
    renderer.writeCodeLine(20, "Test line 20: partial", CoverageStatus.PARTIAL);
    renderer.writeCodeLine(30, "Test line 30: full", CoverageStatus.FULL);
    renderer.writeCodeLine(40, "Test line 40: none", CoverageStatus.NONE);
    renderer.writeCodeLine(50, "} //", null);
    renderer.writeCodeLine(60, "", null);

    renderer.writeSectionHeader("Another code:");
    for(int i = 0; i < myCodeSize; i++) {
      final int id = 60 + 10 + 10 * i;
      renderer.writeCodeLine(id, "Test line " + id + " " + Strings.ofSize( 100 + (int)Math.abs(1000 * Math.sin( i*i ))), i % 3 == 0 ? CoverageStatus.FULL : i % 3 == 1 ?  CoverageStatus.NONE : CoverageStatus.PARTIAL);
    }
  }
}
