package org.digitalpassport.passport;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.digitalpassport.jdbc.cDatabaseHandler;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Philip M. Trenwith
 */
public class cProvenanceData
{

  private Document m_oDocument = null;
  public String m_sPRFile = "";
  private String m_sIndexTagName = "IndexRecord";
  private Node m_oIndex = null;
  private int m_iNumber = 1;
  private File m_oPAYLOAD = null;

  public cProvenanceData(File oPayload)
  {
    m_oPAYLOAD = oPayload;
    m_sPRFile = m_oPAYLOAD.getParentFile().getAbsolutePath() + File.separator + oPayload.getName() + "_Provenance.xml";
    init();
  }

  public cProvenanceData(File oPayload, String sProvenanceFile)
  {
    m_oPAYLOAD = oPayload;
    m_sPRFile = sProvenanceFile;
    init();
  }

  public void init()
  {
    getDocument();
  }

//  public void verifyFile(Object oClientPublicKey)
//  {
//    String sError = "";
//    boolean bFileValid=true;
//    Node oIndex = null;
//    Node oRecord = null;
//    Node oSignature = null;
//    
//    // get the index record
//    int iLastRecordNumber = 1000;
//    
//    Node oIndexNode = m_oDocument.getElementsByTagName(m_sIndexTagName).item(0);
//    if (oIndexNode != null)
//    {
//      NodeList childNodes = oIndexNode.getChildNodes();
//      for (int j = 0; j < childNodes.getLength(); j++)
//      {
//        Node oTemp = childNodes.item(j);
//        if (oTemp.getNodeName().equalsIgnoreCase("Index"))
//        {
//          oIndex = oTemp;
//          NodeList oIndexChildNodes = oTemp.getChildNodes();
//          for (int k = 0; k < oIndexChildNodes.getLength(); k++)
//          {
//            Node oTemp2 = oIndexChildNodes.item(k);
//            if (oTemp2.getNodeName().equalsIgnoreCase("LastRecordNumber"))
//            {
//              iLastRecordNumber=Integer.parseInt(oTemp2.getTextContent());
//            }
////            else if (oTemp2.getNodeName().equalsIgnoreCase("LastRecordHashCode"))
////            {
////              sLastRecordHashCode=oTemp2.getTextContent();
////            }
//          }
//        }
//        else if (oTemp.getNodeName().equalsIgnoreCase("DigitalSignature"))
//        {
//          oSignature = oTemp;
//        }
//      }
//      
//      // signature verification
//      String sRecord = nodeToString(oIndex);
//      String sSignature = oSignature.getTextContent();
//      if (m_oCrypto.verify(sRecord, sSignature, m_oCrypto.getPublicKey()))
//      {
//        Main.log("Integrity verified, Index Record");
//      }
//      else
//      {
//        if (m_oCrypto.verify(sRecord, sSignature, oClientPublicKey))
//        {
//          Main.log("Integrity verified, Index Record");
//        }
//        else
//        {
//          sError += "Index Record is invalid.\n";
//          Main.log("WARNING: Integrity COMPROMISED!!! Index Record");
//          bFileValid=false;
//        }
//      }
//    }
//    else
//    {
//      sError += "Index Record is missing!\n";
//      bFileValid=false;
//    }
//    
//    for (int i=1; i<=iLastRecordNumber; i++)
//    {
//      Node oProvenanceRecord = getRecordByNumber(i);
//      if (oProvenanceRecord != null)
//      {
//        NodeList childNodes = oProvenanceRecord.getChildNodes();
//        for (int j = 0; j < childNodes.getLength(); j++)
//        {
//          Node oTemp = childNodes.item(j);
//          if (oTemp.getNodeName().equalsIgnoreCase("Record"))
//          {
//            oRecord = oTemp;
//          }
//          else if (oTemp.getNodeName().equalsIgnoreCase("DigitalSignature"))
//          {
//            oSignature = oTemp;
//          }
//        }
//        
//        // signature verification
//        String sRecord = nodeToString(oRecord);
//        String sSignature = oSignature.getTextContent();
//        
//        if (m_oCrypto.verify(sRecord, sSignature, m_oCrypto.getPublicKey()))
//        {
//          Main.log("Integrity verified, Record: " + i);
//        }
//        else
//        {
//          if (m_oCrypto.verify(sRecord, sSignature, oClientPublicKey))
//          {
//            Main.log("Integrity verified, Record: " + i);
//          }
//          else
//          {
//            sError += "Record " + i + " is invalid.\n";
//            Main.log("WARNING: Integrity COMPROMISED!!! Record: " + i);
//            bFileValid=false;
//          }
//        }
//      }
//      else
//      {
//        if (m_oIndex == null)
//        {
//          break;
//        }
//        sError += "Record " + i + " is missing!\n";
//        Main.log("WARNING: Integrity COMPROMISED!!! Record: " + i + " is missing!");
//        bFileValid=false;
//      }
//    }
//    
//    if (bFileValid)
//    {
//      Main.log("\nProvidence Data Integrity valid! :-)");
//    }
//    else 
//    {
//      Main.log("\nWARNING: Providence Data HAS BEEN COMPROMISED!!!\n" + sError);
//    }
//  }
//  
  public void getDocument()
  {
    try
    {
      DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder;

      docBuilder = docFactory.newDocumentBuilder();
      Element rootElement;

      if (new File(m_sPRFile).exists())
      {
        m_oDocument = docBuilder.parse(m_sPRFile);
        rootElement = m_oDocument.getDocumentElement();
        m_oIndex = rootElement.getElementsByTagName(m_sIndexTagName).item(0);

        // get the next record number
        m_iNumber = rootElement.getElementsByTagName("ProvenanceRecord").getLength() + 1;
      }
      else
      {
        m_oDocument = docBuilder.newDocument();
        rootElement = m_oDocument.createElement("Provenance");
        m_oDocument.appendChild(rootElement);
      }
    }
    catch (Exception ex)
    {
      Logger.getLogger(cProvenanceData.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public Node getRecordByNumber(int iNumber)
  {
    Node oReturn = null;
    NodeList nodes = m_oDocument.getElementsByTagName("ProvenanceRecord");
    for (int i = 0; i < nodes.getLength(); i++)
    {
      oReturn = nodes.item(i);
      NodeList childNodes = oReturn.getChildNodes();
      for (int j = 0; j < childNodes.getLength(); j++)
      {
        Node oRecord = childNodes.item(j);
        if (oRecord.getNodeName().equalsIgnoreCase("Record"))
        {
          NodeList recordChildren = oRecord.getChildNodes();
          for (int k = 0; k < recordChildren.getLength(); k++)
          {
            Node oNumber = recordChildren.item(k);
            if (oNumber.getNodeName().equalsIgnoreCase("Number"))
            {
              if (Integer.parseInt(oNumber.getTextContent()) == iNumber)
              {
                return oReturn;
              }
            }
          }
        }
      }
    }
    return null;
  }

  private static String nodeToString(Node node)
  {
    StringWriter sw = new StringWriter();
    try
    {
      Transformer t = TransformerFactory.newInstance().newTransformer();
      t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
      //t.setOutputProperty(OutputKeys.INDENT, "no");
      t.transform(new DOMSource(node), new StreamResult(sw));
    }
    catch (TransformerException te)
    {
      Logger.getLogger(cProvenanceData.class.getName()).log(Level.SEVERE, "nodeToString Transformer Exception");
    }

    String sReturn = "";
    String sText = sw.toString();
    String[] sLines = sText.split("\n");
    for (String sLine : sLines)
    {
      sReturn += sLine.trim() + "";
    }
    return sReturn;
  }

  public static String getIPAddressAssignedByISP()
  {
    try
    {
      //"http://checkip.amazonaws.com"
      URL whatismyip = new URL("http://checkip.amazonaws.com");
      BufferedReader in = new BufferedReader(new InputStreamReader(
          whatismyip.openStream()));

      String ip = in.readLine(); //you get the IP as a String
      return ip;
    }
    catch (MalformedURLException ex)
    {
      Logger.getLogger(cProvenanceData.class.getName()).log(Level.SEVERE, null, ex);
    }
    catch (IOException ex)
    {
      Logger.getLogger(cProvenanceData.class.getName()).log(Level.SEVERE, null, ex);
    }
    return null;
  }

  public void generateModificationRecord()
  {
    modificationStub("Modification", getIPAddressAssignedByISP(), "ZA", "ptrenwith@gmail.com", "203n-PhilipMT3");
  }

  public void generateLocationRecord(boolean bServer)
  {
    if (bServer)
    {
      modificationStub("Location-Update", "178.62.89.26", "GB", "ptrenwith@gmail.com", "SERVER");
    }
    else
    {
      modificationStub("Location-Update", getIPAddressAssignedByISP(), "ZA", "ptrenwith@gmail.com", "203n-PhilipMT3");
    }
  }

  private void modificationStub(String sType, String sIPAddress, String sJurisdiction, String sAccount, String sMachine)
  {
    try
    {
      Element rootElement;
      rootElement = m_oDocument.getDocumentElement();
      if (m_oIndex == null)
      {
        indexStub();
      }

      Element oPR = m_oDocument.createElement("ProvenanceRecord");
      rootElement.appendChild(oPR);

      Element oRecord = m_oDocument.createElement("Record");
      oPR.appendChild(oRecord);

      Element oChain = m_oDocument.createElement("Chain");
      oRecord.appendChild(oChain);
      if (m_iNumber == 1)
      {
        oChain.setTextContent("");
      }
      else
      {
        Node node = getRecordByNumber(m_iNumber - 1);
        String sText = nodeToString(node);

        String SHA1 = "";//cBouncyCastle.SHA256_toString(sText);
        oChain.setTextContent(SHA1);
      }

      Element oNumber = m_oDocument.createElement("Number");
      oRecord.appendChild(oNumber);
      oNumber.appendChild(m_oDocument.createTextNode(m_iNumber + ""));

      Element oRecordType = m_oDocument.createElement("RecordType");
      oRecord.appendChild(oRecordType);
      oRecordType.appendChild(m_oDocument.createTextNode(sType));

      Element oWho = m_oDocument.createElement("Who");
      oRecord.appendChild(oWho);
      Element oUserAccount = m_oDocument.createElement("UserAccount");
      oWho.appendChild(oUserAccount);
      oUserAccount.appendChild(m_oDocument.createTextNode(sAccount));
      Element oMachine = m_oDocument.createElement("MachineName");
      oWho.appendChild(oMachine);
      oMachine.appendChild(m_oDocument.createTextNode(sMachine));

      Element oLocation = m_oDocument.createElement("Location");
      oRecord.appendChild(oLocation);
      Element oIPAddress = m_oDocument.createElement("IPAddress");
      oLocation.appendChild(oIPAddress);
      oIPAddress.appendChild(m_oDocument.createTextNode(sIPAddress));
      Element oJurisdiction = m_oDocument.createElement("Jurisdiction");
      oLocation.appendChild(oJurisdiction);
      oJurisdiction.appendChild(m_oDocument.createTextNode(sJurisdiction));

      long sTimestamp = new GregorianCalendar().getTimeInMillis();

      Element oTimestamp = m_oDocument.createElement("Timestamp");
      oRecord.appendChild(oTimestamp);
      oTimestamp.appendChild(m_oDocument.createTextNode(sTimestamp + ""));

      Element oHashCode = m_oDocument.createElement("HashCode");
      oRecord.appendChild(oHashCode);

      String sFileContent = readFileContent(m_oPAYLOAD);
      String sHash = "";//cBouncyCastle.SHA256_toString(sFileContent);

      oHashCode.appendChild(m_oDocument.createTextNode(sHash));

      String sRecordText = nodeToString(oHashCode.getParentNode());
      String signature = "";//m_oCrypto.sign(sRecordText);

      Element oDigitalSignature = m_oDocument.createElement("DigitalSignature");
      oPR.appendChild(oDigitalSignature);
      oDigitalSignature.setTextContent(signature);

      if (m_oIndex != null)
      {
        NodeList elements = m_oIndex.getChildNodes();
        for (int i = 0; i < elements.getLength(); i++)
        {
          Node oIndex = elements.item(i);
          if (oIndex.getNodeName().equalsIgnoreCase("Index"))
          {
            NodeList lsIndexChildren = oIndex.getChildNodes();
            for (int j = 0; j < lsIndexChildren.getLength(); j++)
            {
              Node oChild = lsIndexChildren.item(j);
              if (oChild.getNodeName().equalsIgnoreCase("LastRecordNumber"))
              {
                oChild.setTextContent(m_iNumber + "");
              }
              else if (oChild.getNodeName().equalsIgnoreCase("LastRecordHashCode"))
              {
                Node node = getRecordByNumber(m_iNumber);
                String sText = nodeToString(node);

                String SHA1 = "";//cBouncyCastle.SHA256_toString(sText);
                oChild.setTextContent(SHA1);

                String sIndexText = nodeToString(oIndex);
                String sIndexSigniture = "";//m_oCrypto.sign(sIndexText);

                for (int l = 0; l < elements.getLength(); l++)
                {
                  Node oIndexSign = elements.item(l);
                  if (oIndexSign.getNodeName().equalsIgnoreCase("DigitalSignature"))
                  {
                    oIndexSign.setTextContent(sIndexSigniture);
                  }
                }
              }
            }
          }
        }
      }
      saveToFile();
      m_iNumber++;
    }
    catch (Exception ex)
    {
      Logger.getLogger(cProvenanceData.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public void indexStub()
  {
    try
    {
      Element rootElement;
      rootElement = m_oDocument.getDocumentElement();

      Element oPR = m_oDocument.createElement("IndexRecord");
      rootElement.appendChild(oPR);

      Element oRecord = m_oDocument.createElement("Index");
      oPR.appendChild(oRecord);

      Element oChain = m_oDocument.createElement("LastRecordNumber");
      oRecord.appendChild(oChain);
      oChain.appendChild(m_oDocument.createTextNode("LastRecordNumber Value"));

      Element oNumber = m_oDocument.createElement("LastRecordHashCode");
      oRecord.appendChild(oNumber);
      oNumber.appendChild(m_oDocument.createTextNode("LastRecordHashCode Value"));

      Element oDigitalSignature = m_oDocument.createElement("DigitalSignature");
      oPR.appendChild(oDigitalSignature);
      oDigitalSignature.appendChild(m_oDocument.createTextNode("DigitalSignature Value"));

      m_oIndex = oRecord.getParentNode();
      saveToFile();
    }
    catch (Exception ex)
    {
      Logger.getLogger(cProvenanceData.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public void saveToFile()
  {
    Writer outxml = null;
    try
    {
      OutputFormat format = new OutputFormat(m_oDocument);
      format.setLineWidth(65);
      format.setIndenting(true);
      format.setIndent(2);
      outxml = new FileWriter(new File(m_sPRFile));
      XMLSerializer serializer = new XMLSerializer(outxml, format);
      serializer.serialize(m_oDocument);
    }
    catch (IOException ex)
    {
      Logger.getLogger(cProvenanceData.class.getName()).log(Level.SEVERE, null, ex);
    }
    finally
    {
      try
      {
        outxml.close();
      }
      catch (IOException ex)
      {
        Logger.getLogger(cProvenanceData.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }

  private String readFileContent(File oFile)
  {
    String sContent = "";
    FileInputStream fis = null;
    try
    {
      fis = new FileInputStream(oFile);
      byte[] data = new byte[(int) oFile.length()];
      fis.read(data);
      fis.close();
      sContent = new String(data, "UTF-8");
    }
    catch (FileNotFoundException ex)
    {
      Logger.getLogger(cProvenanceData.class.getName()).log(Level.SEVERE, null, ex);
    }
    catch (IOException ex)
    {
      Logger.getLogger(cProvenanceData.class.getName()).log(Level.SEVERE, null, ex);
    }
    finally
    {
      try
      {
        fis.close();
      }
      catch (IOException ex)
      {
        Logger.getLogger(cProvenanceData.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
    return sContent;
  }
}
