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

package jetbrains.coverage.report.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.concurrent.Callable;

/**
 * @author Pavel.Sher
 */
public class IOUtil {
  /**
   * Creates directory if it does not exist. Throws runtime exception if directory creation was not successful.
   *
   * @param dir directory to be created
   * @return dir
   * @throws IOException if failed to create directory
   */
  public static File createDir(File dir) throws IOException {
    if (!dir.exists() && !dir.mkdirs()) {
      throw new IOException("Failed to create directory: " + dir.getAbsolutePath());
    }

    return dir;
  }

  /**
   * Copies contents of the specified resource into the specified file.
   * Uses {@link Class#getResourceAsStream(String)} in 'clazz' parameter to fetch the resource.
   *
   * @param clazz         class from the class loader that can access the resource
   * @param resourceName  Name of the classpath resource
   * @param outputStream  target stream for the resource data
   * @throws IOException if I/O error occurred
   */
  public static void copyResource(final @NotNull Class<?> clazz, final @NotNull String resourceName, final @NotNull OutputStream outputStream) throws IOException {
    Callable<Void> doCopy = new Callable<Void>() {
      public Void call() throws Exception {
        InputStream asStream = null;
        try {
          asStream = clazz.getResourceAsStream(resourceName);
          if (asStream != null) {
            copyStreamContent(asStream, outputStream);
          }
        } finally {
          close(asStream);
          close(outputStream);
        }
        return null;
      }
    };

    IOUtil.<Void, IOException>loop(doCopy);
  }

  @SuppressWarnings({"unchecked", "BusyWait"})
  public static <T, E extends Exception> T loop(Callable<T> action) throws E {
    int attempt = 0;
    while (true) {
      try {
        attempt++;
        return action.call();
      } catch (Exception e) {
        if (attempt < 3) {
          try {
            Thread.sleep(50);
          } catch (InterruptedException ex) {
            // no-op
          }
        } else {
          throw (E) e;
        }
      }
    }
  }

  public static void close(@Nullable Closeable e) {
    if (e != null) {
      try {
        e.close();
      } catch (IOException e1) {
        // ignore
      }
    }
  }

  /**
   * @param inputStream source stream
   * @param outputStream destination stream
   * @return bytes copied
   * @throws IOException
   */
  public static int copyStreamContent(InputStream inputStream, OutputStream outputStream) throws IOException {
    final byte[] buffer = new byte[10 * 1024];
    int count;
    int total = 0;
    while ((count = inputStream.read(buffer)) > 0) {
      outputStream.write(buffer, 0, count);
      total += count;
    }
    return total;
  }

  /**
   * Calculates relative path
   * @param base base
   * @param path path
   * @return returned path is a path to add to base to get path
   */
  @NotNull
  public static String makeRelative(@NotNull final File base, @NotNull final File path) {
    if (base.equals(path)) return ".";

    final String sFind = path.getPath();
    final String sBasePath = base.getPath();

    if (!path.getPath().startsWith(sBasePath + File.separatorChar)) {
      final String next = makeRelative(base.getParentFile(), path);
      if (".".equals(next)) return "..";
      return "../" + next;
    } else {
      return sFind.substring(sBasePath.length() + 1);
    }
  }

}
