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

package jetbrains.coverage.html;

import com.intellij.openapi.util.io.FileUtil;
import jetbrains.coverage.BaseTestCase;
import jetbrains.coverage.report.ClassInfo;
import jetbrains.coverage.report.CoverageData;
import jetbrains.coverage.report.Entry;
import jetbrains.coverage.report.ReportBuilderFactory;
import jetbrains.coverage.report.html.HTMLReportBuilder;
import jetbrains.coverage.report.impl.html.HTMLReportBuilderImpl;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * @author Pavel.Sher
 */
public class HTMLReportBuilderTest extends BaseTestCase {
  private File myReportDir;

  @Before
  public void setUp() throws Exception {
    super.setUp();
    myReportDir = myTempFiles.createTempDir();
    System.out.println("myReportDir = " + myReportDir);
  }


  @Test
  public void test_generage_index_html_for_empty() throws IOException {
    MockCoverageData data = new MockCoverageData();
    System.out.println("myReportDir = " + myReportDir);
    HTMLReportBuilder buider = ReportBuilderFactory.createHTMLReportBuilder();
    buider.setReportDir(myReportDir);
    buider.generateReport(data);

    Assert.assertTrue(new File(myReportDir, "index.html").isFile());
  }

  @Test
  public void test_generage_index_html_for_empty2() throws IOException {
    MockCoverageData data = new MockCoverageData();

    HTMLReportBuilder buider = ReportBuilderFactory.createHTMLReportBuilder();
    final File zip = new File(myReportDir, "report.zip");
    buider.setReportZip(zip);
    buider.generateReport(data);


    Assert.assertTrue(zip.isFile());
    ZipInputStream zis = new ZipInputStream(new BufferedInputStream(new FileInputStream(zip)));
    try {

      for(ZipEntry ze = zis.getNextEntry(); ze != null; ze = zis.getNextEntry()) {
        System.out.println(ze.getName());
        if (ze.getName().equals("index.html")) return;
      }
      Assert.fail("index.html should be generated");
    } finally {
      zis.close();
    }
  }


  @Test
  public void test_generate_report() throws IOException {
    MockCoverageData covData = createMockCoverage("");

    HTMLReportBuilder builder = ReportBuilderFactory.createHTMLReportBuilder();
    builder.setReportDir(myReportDir);
    builder.generateReport(covData);

    checkReport(covData);
  }

  @Test
  public void test_generate_repor_with_title() throws IOException {
    MockCoverageData covData = createMockCoverage("");

    HTMLReportBuilder builder = ReportBuilderFactory.createHTMLReportBuilder();
    ((HTMLReportBuilderImpl)builder).setReportTitle("Title");
    builder.setReportDir(myReportDir);
    builder.generateReport(covData);

    checkReport(covData);
  }

  @Test
  public void test_generate_report_zip() throws IOException {
    MockCoverageData covData = createMockCoverage("");

    final File zip = new File(myReportDir, "zzz.zip");

    HTMLReportBuilder builder = ReportBuilderFactory.createHTMLReportBuilder();
    builder.setReportZip(zip);

    builder.generateReport(covData);

    Assert.assertTrue(zip.isFile());
  }

  @Test
  public void test_generate_report_with_modules() throws IOException {
    MockCoverageData covData = createHugeReport(5, 5, 100);

    generateDir(covData);

    System.out.println("myReportDir = " + myReportDir);
  }

  @Test
  public void test_generate_report_with_modules_zip() throws IOException {
    MockCoverageData covData = createHugeReport(5, 5, 100);

    generateZip(covData);

    System.out.println("myReportDir = " + myReportDir);
  }

  private long generateDir(MockCoverageData covData) throws IOException {
    HTMLReportBuilder builder = ReportBuilderFactory.createHTMLReportBuilder();
    builder.setFooterHTML("Powered by <a href=\"ddd\">dotCover</a>");
    FileUtil.delete(myReportDir);
    myReportDir.mkdirs();
    builder.setReportDir(myReportDir);
    final File zip = myTempFiles.createTempFile();


    final long start = new Date().getTime();
    builder.generateReport(covData);

    final long zipStart = new Date().getTime();

    zip(zip, myReportDir);
    System.out.println("  packing-zip= " + (new Date().getTime() - zipStart));

    final long time = new Date().getTime() - start;
    dumpZipSize(zip);
    return time;
  }

  private void dumpZipSize(@NotNull final File zip) throws IOException {
/*
    long sz = 0;
    long cnt = 0;

    ZipInputStream zis = new ZipInputStream(new BufferedInputStream(new FileInputStream(zip)));
    try {
      for (ZipEntry ze = zis.getNextEntry(); ze != null; ze = zis.getNextEntry()) {
        cnt++;
        while (zis.read() >= 0) {
          sz++;
        }
      }
    } finally {
      zis.close();
    }

*/

    System.out.println("ziped-size = " + (zip.length())/1024/1024 + "mb");
//    System.out.println("unzipped-size = " + (sz)/1024/1024 + "mb");
//    System.out.println("unzipped-count = " + cnt);
  }

  private void zip(@NotNull final File zip, @NotNull final File directory) throws IOException {
    ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zip)));
    zipFile(zos, directory, "");
    zos.close();
  }

  private void zipFile(@NotNull final ZipOutputStream zos, @NotNull final File file, @NotNull String relativePath) throws IOException {
    if (file.isDirectory()) {
      final File[] files = file.listFiles();
      if (files != null) {
        for (File ch : files) {
          zipFile(zos, ch, relativePath + file.getName() + "/");
        }
      }
    } else {
      zos.putNextEntry(new ZipEntry(relativePath + file.getName()));
      byte[] buff = new byte[65536*8];
      InputStream is = new BufferedInputStream(new FileInputStream(file));
      int i;
      while((i = is.read(buff)) > 0) {
        zos.write(buff, 0, i);
      }
      zos.closeEntry();
    }
  }

  private long generateZip(MockCoverageData covData) throws IOException {
    HTMLReportBuilder builder = ReportBuilderFactory.createHTMLReportBuilder();
    builder.setFooterHTML("Powered by <a href=\"ddd\">dotCover</a>");
    FileUtil.delete(myReportDir);
    myReportDir.mkdirs();
    final File zip = new File(myReportDir, "report.zip");
    builder.setReportZip(zip);


    final long start = new Date().getTime();
    builder.generateReport(covData);
    final long zipTime = new Date().getTime() - start;
    dumpZipSize(zip);
    return zipTime;
  }

  @Test
  public void test_generation_speed() throws IOException {
    MockCoverageData covData = createHugeReport(5, 5, 100);
    final int TIMES = 3;

    long dirTimes = 0;
    long zipTimes = 0;

    for(int i =0;  i < TIMES; i++) {
      System.gc();
      final long actualDir = generateDir(covData);
      System.out.println("dirTimes = " + actualDir);

      System.gc();
      final long actualZip = generateZip(covData);
      System.out.println("zipTimes = " + actualZip);

      dirTimes += actualDir;
      zipTimes += actualZip;
    }

    System.out.println("Total avg zipTimes = " + zipTimes/TIMES);
    System.out.println("Total avg dirTimes = " + dirTimes/TIMES);
  }

  @Test
  public void test_empty_package() throws IOException {
    MockCoverageData covData = createMockCoverage("");
    MockClassInfo mcl = new MockClassInfo("class_without_package");
    covData.addClass(mcl);
    mcl.setLines(new Entry(1,0));

    HTMLReportBuilder builder = ReportBuilderFactory.createHTMLReportBuilder();
    builder.setReportDir(myReportDir);
    builder.generateReport(covData);

    checkReport(covData);
  }

  @Test
  public void test_diff() throws IOException {
    MockCoverageData olderData = createMockCoverage("");
    MockCoverageData newerData = createMockCoverage("");
    MockClassInfo classInfo = new MockClassInfo("jetbrains.NewClass");
    classInfo.setLines(new Entry(1,1));
    newerData.addClass(classInfo);

    HTMLReportBuilder builder = ReportBuilderFactory.createHTMLReportBuilder();
    builder.setReportDir(myReportDir);
    builder.generateReport(olderData, newerData);
  }

  private MockCoverageData createMockCoverage(String add) {
    return createMockCoverage(add, 0);
  }

  private MockCoverageData createMockCoverage(String add, int totalLines) {
    MockCoverageData covData = new MockCoverageData();
    for(int j = 0; j <= totalLines; j++) {
      MockClassInfo cd1 = new MockClassInfo("jetbrains." + add + "buildServer.BaseTestCase" + j);
      MockClassInfo cd2 = new MockClassInfo("jetbrains." + add + "buildServer.util.FileUtil" + j);
      MockClassInfo cd3 = new MockClassInfo("jetbrains." + add + "MainClass" + j);
      covData.addClass(cd1);
      covData.addClass(cd2);
      covData.addClass(cd3);

      cd1.setLines(new Entry(10 + 5*j, 4 + (int)(3.1415*j)));
      cd2.setLines(new Entry(50  + 2*j, 37 + (int)(1.45*j)));
      cd3.setLines(new Entry(230 + 3*j, 137 + (int)(2.39*j)));
      cd1.setBlocks(new Entry(10 + 5*j, 4 + (int)(3.1415*j)));
      cd1.setStatements(new Entry(10 + 5*j, 4 + (int)(3.1415*j)));

      covData.setSource(cd1, "class BaseTestCase" + j + " {}");
      covData.setSource(cd2, "class FileUtil" + j + " {}");
      covData.setSource(cd3, "class MainClass" + j + " {}");
    }
    return covData;
  }

  private MockCoverageData createMockCoverageWithModules() {
    final MockCoverageData c1 = createMockCoverage("c.", 50);
    final MockCoverageData c2 = createMockCoverage("c.", 130);
    final MockCoverageData c3 = createMockCoverage("c.", 150);

    MockCoverageData cov = new MockCoverageData();
    int i = 1;
    for (MockCoverageData data : Arrays.asList(c1, c2, c3)) {
      for (ClassInfo info : data.getClasses()) {
        MockClassInfo ii = (MockClassInfo) info;
        if (i > 1) {
          ii.setModule("Module_" + i);
        }
        cov.addClass(ii);
      }
      i++;
    }
    return cov;
  }


  private MockCoverageData createHugeReport(int mod, int ns, int cl) {
    MockCoverageData data = new MockCoverageData();

    for(int m = 0; m < mod; m++) {
      for(int n = 0; n < ns; n++) {
        for(int c = 0; c < cl; c++) {
          data.addClass(new MockClassInfo("Namespace" + n +".class" +c, "module" + m));
        }
      }
    }
    data.setCodeSize(50);
    return data;
  }

  private void checkReport(CoverageData covData) throws IOException {
    Assert.assertTrue(myReportDir.isDirectory());
    Assert.assertTrue(new File(myReportDir, "css").isDirectory());
    Assert.assertTrue(new File(myReportDir, "img").isDirectory());
    Assert.assertTrue(new File(myReportDir, "js").isDirectory());
    checkIndex(covData);
  }

  private void checkIndex(final CoverageData covData) throws IOException {
    final File indexFile = new File(myReportDir, "index.html");
    Assert.assertTrue(indexFile.isFile());
    String indexContent = new String(FileUtil.loadFileText(indexFile));

    for (ClassInfo cd: covData.getClasses()) {
      Assert.assertTrue(indexContent, indexContent.contains(cd.getNamespace()));
    }
  }
}
