/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AkashApplications.src;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageMar;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcBorders;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STBorder;

import java.math.BigInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.PageRanges;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STJc;

/**
 *
 * @author akash
 */
public class GenerateInvoice {
    String buyerAddress, invoiceNote, date, totalSum,paymentType;
    JTable table;
    DefaultTableModel dtm;

    public GenerateInvoice(String buyerAddress, String invoiceNote, String date, String totalSum, JTable table, String paymentType) {
        this.buyerAddress = buyerAddress;
        this.invoiceNote = invoiceNote;
        this.date = date;

        this.totalSum = totalSum;
        this.paymentType = paymentType;
        this.table = table;
        dtm = (DefaultTableModel) table.getModel();
    }
    
    
    public boolean printInvoice() throws FileNotFoundException, InvalidFormatException, IOException
    {
        
        XWPFDocument document = new XWPFDocument();
        CTSectPr sectPr = document.getDocument().getBody().addNewSectPr();
        CTPageMar pageMar = sectPr.addNewPgMar();
        pageMar.setLeft(BigInteger.valueOf(720L));
        pageMar.setTop(BigInteger.valueOf(460L));
        pageMar.setRight(BigInteger.valueOf(720L));
        pageMar.setBottom(BigInteger.valueOf(460L));
        
        XWPFParagraph dateP = document.createParagraph();
        XWPFRun dateRun = dateP.createRun();
        dateRun.addBreak();
        dateRun.setText(date);
        dateRun.setFontSize(8);
        dateRun.setItalic(true);
        dateP.setAlignment(ParagraphAlignment.RIGHT);
        dateRun.addBreak();
        
        XWPFParagraph title = document.createParagraph();
        XWPFRun titleRun = title.createRun();
        titleRun.addBreak();
        titleRun.setText("Challan Delivery ");
        titleRun.setBold(true);
        title.setAlignment(ParagraphAlignment.CENTER);
        titleRun.addBreak();
        
        
        //header table
        XWPFTable productTable = document.createTable();
        productTable.setCellMargins(50, 50, 50, 50);
        productTable.getCTTbl().getTblPr().unsetTblBorders();
        productTable.getCTTbl().addNewTblPr().addNewTblW().setW(BigInteger.valueOf(11000));
        
        XWPFTableRow row1 = productTable.getRow(0);
        
        XWPFTableCell cellSellerBuyer = row1.getCell(0);
        cellSellerBuyer.getCTTc().addNewTcPr().addNewTcW().setW(BigInteger.valueOf(5000));
        
        CTTc ctTc = cellSellerBuyer.getCTTc();
        CTTcPr tcPr = ctTc.getTcPr();
        CTTcBorders border = tcPr.addNewTcBorders();
        border.addNewBottom().setVal(STBorder.SINGLE);
//        border.addNewRight().setVal(STBorder.SINGLE);
        border.addNewLeft().setVal(STBorder.SINGLE);
        border.addNewTop().setVal(STBorder.SINGLE); 
        
        XWPFParagraph seller = cellSellerBuyer.addParagraph();
        XWPFRun sellerRun = seller.createRun();
        seller.setAlignment(ParagraphAlignment.LEFT);
        sellerRun.setText("Buyer:");
        sellerRun.addBreak();
        String[] ar = buyerAddress.split("\n");
        for(String s : ar)
        {
            sellerRun.setText(s);
            sellerRun.addBreak();
        }

        
        XWPFTableCell cellProductDesc = row1.createCell();
        cellProductDesc.getCTTc().addNewTcPr().addNewTcW().setW(BigInteger.valueOf(6000));

        ctTc = cellProductDesc.getCTTc();
        tcPr = ctTc.getTcPr();
        border = tcPr.addNewTcBorders();
        border.addNewBottom().setVal(STBorder.SINGLE);
        border.addNewRight().setVal(STBorder.SINGLE);
        //border.addNewLeft().setVal(STBorder.SINGLE);
        border.addNewTop().setVal(STBorder.SINGLE); 

        XWPFParagraph productDesc = cellProductDesc.addParagraph();
        productDesc.setAlignment(ParagraphAlignment.LEFT);
        XWPFRun runInvoice = productDesc.createRun();
        
        
//        if(table.getRowCount() == 1)
//        {
//            runInvoice.addTab();
//            runInvoice.addTab();
//            runInvoice.addTab();
//            String imgLoc = "Barcodes/"+ dtm.getValueAt(0, 6) +".jpg";
//            FileInputStream image = new FileInputStream(imgLoc);
//            runInvoice.addPicture(image, XWPFDocument.PICTURE_TYPE_JPEG,imgLoc, Units.toEMU(200), Units.toEMU(50));
//            runInvoice.addBreak();
//            runInvoice.addBreak();
//        }
        
        runInvoice.setText("Delivery Note");
        runInvoice.addTab();
        runInvoice.addTab();
        runInvoice.setText(invoiceNote);
        runInvoice.addBreak();
        runInvoice.addBreak();
        
        //document.createParagraph().createRun().addBreak();
        
        XWPFTable productDetails = document.createTable();
        productDetails.setCellMargins(50,50,50,50);
        //productDetails.getCTTbl().getTblPr().addNewJc().setVal(STJc.RIGHT);
        productDetails.getCTTbl().getTblPr().unsetTblBorders();
        productDetails.getCTTbl().addNewTblPr().addNewTblW().setW(BigInteger.valueOf(11000));
        
        XWPFTableRow pDetailHeader = productDetails.getRow(0);
        
        XWPFTableCell header0 = pDetailHeader.getCell(0);
        header0.getCTTc().addNewTcPr().addNewTcW().setW(BigInteger.valueOf(1000));
        XWPFParagraph headerText = header0.addParagraph();
        XWPFRun run = headerText.createRun();
        run.setText("Sl. No.");
        run.setBold(true);
        ctTc = header0.getCTTc();
        tcPr = ctTc.addNewTcPr();
        border = tcPr.addNewTcBorders();
        border.addNewBottom().setVal(STBorder.SINGLE);
        border.addNewRight().setVal(STBorder.SINGLE);
        border.addNewLeft().setVal(STBorder.SINGLE);
        border.addNewTop().setVal(STBorder.SINGLE); 
        
        
        XWPFTableCell header1 = pDetailHeader.createCell();
        header1.getCTTc().addNewTcPr().addNewTcW().setW(BigInteger.valueOf(4700));
        XWPFParagraph headerText1 = header1.addParagraph();
        XWPFRun run1 = headerText1.createRun();
        run1.setText("Description of Goods");
        run1.setBold(true);
        ctTc = header1.getCTTc();
        tcPr = ctTc.addNewTcPr();
        border = tcPr.addNewTcBorders();
        border.addNewBottom().setVal(STBorder.SINGLE);
        border.addNewRight().setVal(STBorder.SINGLE);
        border.addNewLeft().setVal(STBorder.SINGLE);
        border.addNewTop().setVal(STBorder.SINGLE); 
        
        XWPFTableCell header3 = pDetailHeader.createCell();
        header3.getCTTc().addNewTcPr().addNewTcW().setW(BigInteger.valueOf(2000));
        XWPFParagraph headerText3 = header3.addParagraph();
        XWPFRun run3 = headerText3.createRun();
        run3.setText("Rate per sheet");
        run3.setBold(true);
        ctTc = header3.getCTTc();
        tcPr = ctTc.addNewTcPr();
        border = tcPr.addNewTcBorders();
        border.addNewBottom().setVal(STBorder.SINGLE);
        border.addNewRight().setVal(STBorder.SINGLE);
        border.addNewLeft().setVal(STBorder.SINGLE);
        border.addNewTop().setVal(STBorder.SINGLE); 
        
        XWPFTableCell header4 = pDetailHeader.createCell();
        header4.getCTTc().addNewTcPr().addNewTcW().setW(BigInteger.valueOf(1300));
        XWPFParagraph headerText4 = header4.addParagraph();
        XWPFRun run4 = headerText4.createRun();
        run4.setText("Qty");
        run4.setBold(true);
        ctTc = header4.getCTTc();
        tcPr = ctTc.addNewTcPr();
        border = tcPr.addNewTcBorders();
        border.addNewBottom().setVal(STBorder.SINGLE);
        border.addNewRight().setVal(STBorder.SINGLE);
        border.addNewLeft().setVal(STBorder.SINGLE);
        border.addNewTop().setVal(STBorder.SINGLE); 
        
        XWPFTableCell header5 = pDetailHeader.createCell();
        header5.getCTTc().addNewTcPr().addNewTcW().setW(BigInteger.valueOf(2000));
        XWPFParagraph headerText5 = header5.addParagraph();
        XWPFRun run5 = headerText5.createRun();
        run5.setText("Amount");
        run5.setBold(true);
        ctTc = header5.getCTTc();
        tcPr = ctTc.addNewTcPr();
        border = tcPr.addNewTcBorders();
        border.addNewBottom().setVal(STBorder.SINGLE);
        border.addNewRight().setVal(STBorder.SINGLE);
        border.addNewLeft().setVal(STBorder.SINGLE);
        border.addNewTop().setVal(STBorder.SINGLE); 
  
       
        
        for(int i=0; i<1; i++)
        {
            XWPFTableRow fTableRow = productDetails.createRow();
            for(int j=0; j<6; j++)
            {
                XWPFTableCell cell = fTableRow.getCell(j);
                try{
                    ctTc = cell.getCTTc();
                    tcPr = ctTc.addNewTcPr();
                    border = tcPr.addNewTcBorders();
                    //border.addNewBottom().setVal(STBorder.SINGLE);
                    border.addNewRight().setVal(STBorder.SINGLE);
                    border.addNewLeft().setVal(STBorder.SINGLE);
                    if(i==0)
                        border.addNewTop().setVal(STBorder.SINGLE); 
                }
                catch(Exception e)
                {
                    System.err.println(e.getMessage());
                }
                
                switch(j)
                {
                    case 0:
                        cell.getCTTc().addNewTcPr().addNewTcW().setW(BigInteger.valueOf(1000));
                        break;
                    case 1:
                        cell.getCTTc().addNewTcPr().addNewTcW().setW(BigInteger.valueOf(4700));
                        break;
                    case 2:
                        cell.getCTTc().addNewTcPr().addNewTcW().setW(BigInteger.valueOf(2000));
                        break;
                    case 3:
                        cell.getCTTc().addNewTcPr().addNewTcW().setW(BigInteger.valueOf(1300));
                        break;
                    case 4:
                        cell.getCTTc().addNewTcPr().addNewTcW().setW(BigInteger.valueOf(2000));
                        break;
                    
                }
                
            }
        }
        
        
        int totalQty = 0;
        
        for(int i=0; i<table.getRowCount(); i++)
        {
            XWPFTableRow fTableRow = productDetails.createRow();
            for(int j=0; j<table.getColumnCount()-1; j++)
            {
                XWPFTableCell cell = fTableRow.getCell(j);
                try{
                    ctTc = cell.getCTTc();
                    tcPr = ctTc.addNewTcPr();
                    border = tcPr.addNewTcBorders();
                    if(i == table.getRowCount()-1)
                    border.addNewBottom().setVal(STBorder.SINGLE);
                    border.addNewRight().setVal(STBorder.SINGLE);
                    border.addNewLeft().setVal(STBorder.SINGLE);
//                    border.addNewTop().setVal(STBorder.SINGLE); 
                }
                catch(Exception e)
                {
                    System.err.println(e.getMessage());
                }
                
                switch(j)
                {
                    case 0:
                        cell.getCTTc().addNewTcPr().addNewTcW().setW(BigInteger.valueOf(1000));
                        cell.setText(String.valueOf(dtm.getValueAt(i, j)));
                        break;
                    case 1:
                        cell.getCTTc().addNewTcPr().addNewTcW().setW(BigInteger.valueOf(4700));
                        cell.setText(String.valueOf(dtm.getValueAt(i, j)));
                        break;
                    case 2:
                        cell.getCTTc().addNewTcPr().addNewTcW().setW(BigInteger.valueOf(2000));
                        cell.setText(String.valueOf(dtm.getValueAt(i, j)));
                        break;
                    case 3:
                        totalQty += Integer.parseInt(String.valueOf(dtm.getValueAt(i, j)));
                        cell.setText(String.valueOf(dtm.getValueAt(i, j)));
                        cell.getCTTc().addNewTcPr().addNewTcW().setW(BigInteger.valueOf(1300));
                        break;
                    case 4:
                        cell.getCTTc().addNewTcPr().addNewTcW().setW(BigInteger.valueOf(2000));
                        cell.setText("Rs. "+String.valueOf(dtm.getValueAt(i, j)));
                        break;
                    
                }
                
            }
        }
        
        XWPFTableRow finalRow = productDetails.createRow();
        XWPFTableCell fc0 = finalRow.getCell(0);
        fc0.getCTTc().addNewTcPr().addNewTcW().setW(BigInteger.valueOf(1000));
        
        XWPFTableCell fc1 = finalRow.getCell(1);
        fc1.getCTTc().addNewTcPr().addNewTcW().setW(BigInteger.valueOf(4700));
        
        
        
        XWPFTableCell fc3 = finalRow.getCell(2);
        fc3.getCTTc().addNewTcPr().addNewTcW().setW(BigInteger.valueOf(2000));
        XWPFParagraph fp1= fc3.addParagraph();
        XWPFRun fRun1 = fp1.createRun();
        fRun1.setBold(true);
        fRun1.setText("TOTAL");
        
        XWPFTableCell fc4 = finalRow.getCell(3);
        fc4.getCTTc().addNewTcPr().addNewTcW().setW(BigInteger.valueOf(1300));
        XWPFParagraph fp2= fc4.addParagraph();
        XWPFRun fRun2 = fp2.createRun();
        fRun2.setBold(true);
        fRun2.setText(String.valueOf(totalQty));
        
        XWPFTableCell fc5 = finalRow.getCell(4);
        fc5.getCTTc().addNewTcPr().addNewTcW().setW(BigInteger.valueOf(2000));
        XWPFParagraph fp3= fc5.addParagraph();
        XWPFRun fRun3 = fp3.createRun();
        fRun3.setBold(true);
        fRun3.setText("Rs. "+totalSum);
        
        ctTc = fc0.getCTTc();
        tcPr = ctTc.getTcPr();
        border = tcPr.addNewTcBorders();
        //border.addNewBottom().setVal(STBorder.SINGLE);
        //border.addNewRight().setVal(STBorder.SINGLE);
//        border.addNewLeft().setVal(STBorder.SINGLE);
         border.addNewTop().setVal(STBorder.SINGLE);
        
        ctTc = fc1.getCTTc();
        tcPr = ctTc.getTcPr();
        border = tcPr.addNewTcBorders();
        //border.addNewBottom().setVal(STBorder.SINGLE);
        border.addNewRight().setVal(STBorder.SINGLE);
//        border.addNewLeft().setVal(STBorder.SINGLE);
        border.addNewTop().setVal(STBorder.SINGLE);
       
        
        
        ctTc = fc3.getCTTc();
        tcPr = ctTc.getTcPr();
        border = tcPr.addNewTcBorders();
        border.addNewBottom().setVal(STBorder.SINGLE);
        border.addNewRight().setVal(STBorder.SINGLE);
        border.addNewLeft().setVal(STBorder.SINGLE);
        border.addNewTop().setVal(STBorder.SINGLE); 
        
        ctTc = fc4.getCTTc();
        tcPr = ctTc.getTcPr();
        border = tcPr.addNewTcBorders();
        border.addNewBottom().setVal(STBorder.SINGLE);
        border.addNewRight().setVal(STBorder.SINGLE);
        border.addNewLeft().setVal(STBorder.SINGLE);
        border.addNewTop().setVal(STBorder.SINGLE); 
        
        ctTc = fc5.getCTTc();
        tcPr = ctTc.getTcPr();
        border = tcPr.addNewTcBorders();
        border.addNewBottom().setVal(STBorder.SINGLE);
        border.addNewRight().setVal(STBorder.SINGLE);
        border.addNewLeft().setVal(STBorder.SINGLE);
        border.addNewTop().setVal(STBorder.SINGLE); 
        
        XWPFParagraph wordP = document.createParagraph();
        XWPFRun wpRun = wordP.createRun();
        wpRun.addBreak();
        wpRun.addBreak();
        wpRun.addBreak();
        wpRun.setText("Amount in words");
        
        XWPFParagraph netAMT = document.createParagraph();
        XWPFRun netAmtRun = netAMT.createRun();
        netAmtRun.setBold(true);
        netAmtRun.setText(ConvertMoneyToNumberMain.convert(totalSum.replace(",", "")));
        
        XWPFParagraph netPMethod = document.createParagraph();
        XWPFRun pMethodRun = netPMethod.createRun();
        pMethodRun.addBreak();
        pMethodRun.addBreak();
        pMethodRun.setItalic(true);
        pMethodRun.setFontSize(10);
        pMethodRun.setText("NB. - Good sold to the above buyer is in "+paymentType+".");
        
        try{
            FileOutputStream outputStream = new FileOutputStream("/home/akash/Desktop/Challan"+invoiceNote.replace("/","_")+".docx");
            document.write(outputStream);
            outputStream.close();
            new InvoiceNoteManager().setProperty();
            printDocument();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        return true;
    }

    private void printDocument() {
       
        
    }
}
