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

package jetbrains.coverage;

import com.intellij.openapi.util.io.FileUtil;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Pavel.Sher
 *         Date: 05.03.2008
 */
public class TempFiles {
  private static final File ourCurrentTempDir = new File(FileUtil.getTempDirectory());
  private final File myCurrentTempDir;

  private static Random ourRandom;

  static {
    //Enforce java.io.File to cache system property value java.io.tmpdir in order
    //to workaround build agent atempt to clean agent's agentTmp dir that is
    //set to java.io.tmpdir system property while build agent is runing.
    try {
      //noinspection ResultOfMethodCallIgnored
      File.createTempFile("magic", "enforce to cache").delete();
    } catch (IOException e) {
      e.printStackTrace();
    }
    ourRandom = new Random();
    ourRandom.setSeed(System.currentTimeMillis());
  }

  private final List<File> myFilesToDelete = new ArrayList<File>();

  public TempFiles() {
    myCurrentTempDir = ourCurrentTempDir;
    if (!myCurrentTempDir.isDirectory()) {

      throw new IllegalStateException("Temp directory is not a directory, was deleted by some process: " + myCurrentTempDir.getAbsolutePath() +
                                      "\njava.io.tmpdir: " + FileUtil.getTempDirectory());
    }
  }

  private File doCreateTempDir(String prefix, String suffix) throws IOException {
    prefix = prefix == null ? "" : prefix;
    suffix = suffix == null ? ".tmp" : suffix;

    do {
      int count = ourRandom.nextInt();
      final File f = new File(myCurrentTempDir, prefix + count + suffix);
      if (!f.exists() && f.mkdirs()) {
        return f.getCanonicalFile();
      }
    } while (true);

  }
  private File doCreateTempFile(String prefix, String suffix) throws IOException {
    final File file = doCreateTempDir(prefix, suffix);
    file.delete();
    file.createNewFile();
    return file;
  }

  public final File createTempFile(String content) throws IOException {
    File tempFile = createTempFile();
    FileUtil.writeToFile(tempFile, content.getBytes());
    return tempFile;
  }

  public final File createTempFile() throws IOException {
    File tempFile = doCreateTempFile("test", null);
    myFilesToDelete.add(tempFile);
    return tempFile;
  }

  /**
   * Returns a File object for created temp directory.
   * Also stores the value into this object accessed with {@link #getCurrentTempDir()}
   *
   * @return a File object for created temp directory
   * @throws IOException if directory creation fails.
   */
  public final File createTempDir() throws IOException {
    File f = doCreateTempDir("test", "");
    myFilesToDelete.add(f);
    return f;
  }

  /**
   * Returns the current directory used by the test or null if no test is running or no directory is created yet.
   *
   * @return see above
   */
  @Nullable
  public File getCurrentTempDir() {
    return myCurrentTempDir;
  }

  public void cleanup() {
    for (File file : myFilesToDelete) {
      if (file.exists()) {
        FileUtil.delete(file);
      }
    }
    myFilesToDelete.clear();
  }
}
