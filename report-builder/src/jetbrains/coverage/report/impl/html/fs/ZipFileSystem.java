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

package jetbrains.coverage.report.impl.html.fs;

import com.intellij.openapi.diagnostic.Logger;
import jetbrains.coverage.report.impl.IOUtil;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author Eugene Petrenko (eugene.petrenko@gmail.com)
 *         Date: 27.02.11 19:54
 */
public class ZipFileSystem implements FileSystem, Closeable {
  private static final Logger LOG = Logger.getInstance(ZipFileSystem.class.getName());

  private final File myBase;
  private final ZipOutputStream myZos;
  private final AtomicBoolean myIsWriting = new AtomicBoolean(false);
  private final Set<String> myFiles = new TreeSet<String>();

  public ZipFileSystem(@NotNull final File output) throws IOException {
    myBase = output.getParentFile();
    IOUtil.createDir(myBase);
    myZos = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(output)));
  }

  @NotNull
  public OutputStream openFile(@NotNull File path) throws IOException {
    String rel = IOUtil.makeRelative(myBase, path);
    if (!myFiles.add(rel)) {
      LOG.warn("Attepting to write to " + rel + " second time. Fake output stream will be returned to avoid error");
      return new OutputStream() {
        @Override
        public void write(int b) throws IOException {
        }
      };
    }

    if (!myIsWriting.compareAndSet(false, true)) {
      throw new IOException("Failed to open more than one writer into zip");
    }

    myZos.putNextEntry(new ZipEntry(rel));
    return new OutputStream() {
      @Override
      public void write(int b) throws IOException {
        myZos.write(b);
      }

      @Override
      public void write(byte[] b) throws IOException {
        myZos.write(b);
      }

      @Override
      public void write(byte[] b, int off, int len) throws IOException {
        myZos.write(b, off, len);
      }

      @Override
      public void close() throws IOException {
        try {
          myZos.closeEntry();
        } finally {
          myIsWriting.set(false);
        }
      }
    };
  }

  public void close() throws IOException {
    myZos.close();
  }
}
