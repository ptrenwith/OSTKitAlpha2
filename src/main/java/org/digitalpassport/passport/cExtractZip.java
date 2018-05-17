/*
 * Copyright (c) 2017 Grintek Ewation. All rights reserved.
 */
package org.digitalpassport.passport;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 *
 * @author Philip M. Trenwith
 */
public class cExtractZip
{
  public void unzip(String zipFilePath, String destDirectory) throws IOException
  {
    File destDir = new File(destDirectory);
    if (!destDir.exists())
    {
      destDir.mkdir();
    }
    
    ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
    ZipEntry entry = zipIn.getNextEntry();
    // iterates over entries in the zip file
    while (entry != null)
    {
      String filePath = destDirectory + File.separator + entry.getName();
      if (!entry.isDirectory())
      {
        // if the entry is a file, extracts it
        extractFile(zipIn, filePath);
      }
      else
      {
        // if the entry is a directory, make the directory
        File dir = new File(filePath);
        dir.mkdir();
      }
      zipIn.closeEntry();
      entry = zipIn.getNextEntry();
    }
    zipIn.close();
  }

  /**
   * Extracts a zip entry (file entry)
   *
   * @param zipIn
   * @param filePath
   * @throws IOException
   */
  private void extractFile(ZipInputStream zipIn, String filePath) throws IOException
  {
    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
    byte[] bytesIn = new byte[4096];
    int read = 0;
    while ((read = zipIn.read(bytesIn)) != -1)
    {
      bos.write(bytesIn, 0, read);
    }
    bos.close();
  }
}
