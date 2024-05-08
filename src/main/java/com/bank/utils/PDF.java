package com.bank.utils;

import com.bank.BLL.CustomerBLL;
import com.bank.DAL.SQLServer;
import com.bank.DTO.Bank_Account;
import com.bank.DTO.Customer;
import com.bank.GUI.components.DataTable;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class PDF {
    public static void exportPDFStatement (Bank_Account bankAccount, String start_date, String end_date) {
        List<List<String>> statement = new SQLServer().getStatement_By_Date(bankAccount.getNumber(), start_date, end_date);
        DateTimeFormatter myFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        for (List<String> strings : statement) {
            strings.set(0, LocalDateTime.parse(strings.get(0), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S")).format(myFormat));
        }
        String[] columnNames = new String[]{"Thời Gian", "Số Tiền Ghi Nợ", "Số Tiền Ghi Có", "Số Dư", "Nội Dung Chi Tiết"};
        DataTable dataTable = new DataTable(new Object[0][0], columnNames);
        Object[][] data = new Object[statement.size()][5];
        double balance = Double.parseDouble(statement.get(0).get(3));
        double amount;
        if (statement.get(0).get(1).equals("0.0")) {
            amount = Double.parseDouble(statement.get(0).get(2));
            balance -= amount;
        }
        else {
            amount = Double.parseDouble(statement.get(0).get(1));
            balance += amount;
        }

        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
        model.setRowCount(0);
        for (int i = 0; i < statement.size(); i++) {
            data[i][0] = statement.get(i).get(0);
            if (!statement.get(i).get(1).equals("0.0"))
                data[i][1] = VNString.currency(Double.parseDouble(statement.get(i).get(1)));
            else
                data[i][1] = " ";

            if (!statement.get(i).get(2).equals("0.0"))
                data[i][2] = VNString.currency(Double.parseDouble(statement.get(i).get(2)));
            else
                data[i][2] = " ";

            data[i][3] = VNString.currency(Double.parseDouble(statement.get(i).get(3)));
            data[i][4] = statement.get(i).get(4);
        }

        for (Object[] object : data) {
            model.addRow(object);
        }

        //--------------------------------//

        try {
            // Create PDF document
            Document document = new Document(new RectangleReadOnly(PageSize.A4.getHeight(), PageSize.A4.getWidth()));

            // Initialize PDF writer
            PdfWriter.getInstance(document, new FileOutputStream(getFileNameDate("src/main/resources/ExportPDF", "workSchedule", java.sql.Date.valueOf(LocalDate.now()))));

            // Open document
            document.open();
            String fontPath = "font/Roboto/Roboto-Regular.ttf"; // adjust the path as needed
            Font regularFont = getCustomFont(fontPath);

            fontPath = "font/Roboto/Roboto-Bold.ttf";
            Font boldFont = getCustomFont(fontPath);

            fontPath = "font/Roboto/Roboto-Italic.ttf";
            Font italicFont = getCustomFont(fontPath);

            // Add title to the document
            boldFont.setSize(25);
            Paragraph titleParagraph = new Paragraph(30F, "Sao Kê Tài Khoản", boldFont);
            titleParagraph.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(titleParagraph);


            boldFont.setSize(20);
            Paragraph dateParagraph = new Paragraph(30f, new SimpleDateFormat("dd/MM/yyy").format(java.sql.Date.valueOf(start_date)) + " - " + new SimpleDateFormat("dd/MM/yyy").format(java.sql.Date.valueOf(end_date)), boldFont);
            dateParagraph.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(dateParagraph);
            document.add(new Paragraph(30f, " "));

            Customer customer = new CustomerBLL().searchCustomers("[no] = '" + bankAccount.getCustomer_no() + "'").get(0);

            regularFont.setSize(13F);
            Paragraph nameCustomerParagraph = new Paragraph(30f, "Chủ tài khoản:        " + customer.getName().toUpperCase() , regularFont);
            nameCustomerParagraph.setAlignment(Paragraph.ALIGN_LEFT);
            document.add(nameCustomerParagraph);

            Paragraph bank_account_numberParagraph = new Paragraph(30f, "Số tài khoản:          " + bankAccount.getNumber().toUpperCase() , regularFont);
            bank_account_numberParagraph.setAlignment(Paragraph.ALIGN_LEFT);
            document.add(bank_account_numberParagraph);

            Paragraph noCustomerParagraph = new Paragraph(30f, "Số CCCD:                " + customer.getCustomerNo().toUpperCase() , regularFont);
            noCustomerParagraph.setAlignment(Paragraph.ALIGN_LEFT);
            document.add(noCustomerParagraph);
            document.add(new Paragraph(30f, " "));

            boldFont.setSize(15F);
            Paragraph firstBalanceParagraph = new Paragraph(30f, "Số dư đầu kỳ: " + VNString.currency(balance) , boldFont);
            firstBalanceParagraph.setAlignment(Paragraph.ALIGN_LEFT);
            document.add(firstBalanceParagraph);
            document.add(new Paragraph(15F," "));

            regularFont.setSize(10F);
            // Convert JTable to PDF table
            PdfPTable pdfTable = new PdfPTable(dataTable.getColumnCount());
            pdfTable.setWidthPercentage(100); // Set dataTable width to 100% of page width
            pdfTable.setWidths(new float[]{50f, 40f, 40f, 40f, 100f});

            for (int i = 0; i < dataTable.getColumnCount(); i++) {
                pdfTable.addCell(new Phrase(dataTable.getColumnName(i), boldFont));
            }
            for (int i = 0; i < dataTable.getRowCount(); i++) {
                for (int j = 0; j < dataTable.getColumnCount(); j++) {
                    pdfTable.addCell(new Phrase(dataTable.getValueAt(i, j).toString(), regularFont));

                }
            }

            // Add PDF table to document
            document.add(pdfTable);

            // Close document
            document.close();
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }

    public static String getFileNameDate(String path, String name, Date date) {
        int max = 0;
        File[] files = new File(path).listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.getName().startsWith(name)) {
                    String fileName = file.getName();
                    int underscoreIndex = fileName.lastIndexOf('_');
                    if (underscoreIndex != -1) {
                        String numStr = fileName.substring(name.length(), underscoreIndex);
                        try {
                            int num = Integer.parseInt(numStr);
                            if (num > max) {
                                max = num;
                            }
                        } catch (NumberFormatException e) {
                            //ignore file
                        }
                    }
                }
            }
        }
        max++;
        return String.format("%s/%s%03d_%s.pdf", path, name, max, date.toString());
    }

    public static Font getCustomFont(String fontPath) throws IOException, DocumentException {
        BaseFont bf = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        return new Font(bf, 12);
    }
}
