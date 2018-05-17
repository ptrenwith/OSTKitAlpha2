package org.digitalpassport.passport;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 *
 * @author Philip M. Trenwith
 */
public class cCreateZip 
{
  static final int BUFFER = 4096;
  public ZipOutputStream outputFile;
  
  public cCreateZip(String sPassportName)
  {
    try
    {
      System.out.println("Finalizing Passport: " + sPassportName + "");
      FileOutputStream fos = new FileOutputStream(sPassportName);
      outputFile = new ZipOutputStream(new BufferedOutputStream(fos));
    }
    catch (FileNotFoundException ex)
    {
      Logger.getLogger(cCreateZip.class.getName()).log(Level.SEVERE, null, ex);
    }
  } 
  
  public void addFile(String name, String filepath)
  {
    try 
    {
      //out.setMethod(ZipOutputStream.DEFLATED);
      byte data[] = new byte[BUFFER];
      // get a list of files from current directory
      File f = new File(filepath);
      FileInputStream fi = new FileInputStream(f);
      BufferedInputStream origin = new  BufferedInputStream(fi, BUFFER);
      ZipEntry entry = new ZipEntry(name);
      outputFile.putNextEntry(entry);
      int count;
      while((count = origin.read(data, 0, BUFFER)) != -1) 
      {
         outputFile.write(data, 0, count);
      }
      origin.close();
    } 
    catch(Exception e) 
    {
      e.printStackTrace();
    }
  }
  
  public void close() 
  {
    try 
    {
      outputFile.close();
    }
    catch (IOException ex)
    {
      Logger.getLogger(cCreateZip.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
}
