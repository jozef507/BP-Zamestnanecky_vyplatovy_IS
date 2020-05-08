package application.pdf;

import application.models.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PaymnetTable
{
    PaymentD paymentD;

    PdfPTable mainTable;

    PdfPTable tableHeader;
    PdfPTable table;

    PdfPTable tableEmpty;
    PdfPTable table1;
    PdfPTable table2;
    PdfPTable table3;
    PdfPTable table4;

    public PaymnetTable(PaymentD paymentD) throws Exception
    {
        this.paymentD = paymentD;

        setTableHeader();
        setTableEmpty();
        setTable1();
        setTable2();
        setTable3();
        setTable4();
        foo();
        setTable();
        setMainTable();


    }

    public PdfPTable getMainTable() {
        return mainTable;
    }

    private void setTableHeader() throws Exception
    {
        Font fontH1 = new Font(BaseFont.createFont("src/fonts/FreeSans.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED), 9);
        fontH1.setStyle(Font.BOLD);


        tableHeader = new PdfPTable(3);
        tableHeader.setWidthPercentage(100); //Width 100%
        tableHeader.setSpacingBefore(0f); //Space before table
        tableHeader.setSpacingAfter(0f); //Space after table

        //Set Column widths
        float[] columnWidths = {1f, 1f, 1f};
        tableHeader.setWidths(columnWidths);

        PdfPCell headerCell1 = new PdfPCell(new Paragraph(getEmptyString(paymentD.getMonthNumber()) + "  " + getEmptyString(paymentD.getEmployeeLastnameName()), fontH1));
        headerCell1.setBorder(PdfPCell.NO_BORDER);
        headerCell1.setHorizontalAlignment(Element.ALIGN_LEFT);
        headerCell1.setVerticalAlignment(Element.ALIGN_MIDDLE);

        PdfPCell headerCell2 = new PdfPCell(new Paragraph(getEmptyString(paymentD.getPlaceName()) + "/" + getEmptyString(paymentD.getPositionName()), fontH1));
        headerCell2.setBorder(PdfPCell.NO_BORDER);
        headerCell2.setHorizontalAlignment(Element.ALIGN_CENTER);
        headerCell2.setVerticalAlignment(Element.ALIGN_MIDDLE);


        PdfPCell headerCell3 = new PdfPCell(new Paragraph("Andre Topic, s.r.o.", fontH1));
        headerCell3.setBorder(PdfPCell.NO_BORDER);
        headerCell3.setHorizontalAlignment(Element.ALIGN_RIGHT);
        headerCell3.setVerticalAlignment(Element.ALIGN_MIDDLE);

        tableHeader.addCell(headerCell1);
        tableHeader.addCell(headerCell2);
        tableHeader.addCell(headerCell3);


    }

    private void setTableEmpty() throws Exception
    {

        tableEmpty = new PdfPTable(1); // 3 columns.
        //tableEmpty.setWidthPercentage(100); //Width 100%
        tableEmpty.setSpacingBefore(0f); //Space before table
        tableEmpty.setSpacingAfter(0f); //Space after table

        //Set Column widths
        float[] columnWidths = {1f};
        tableEmpty.setWidths(columnWidths);

        PdfPCell cell1 = new PdfPCell(new Paragraph());
        cell1.setBorder(Rectangle.NO_BORDER);
        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);

        tableEmpty.addCell(cell1);

    }

    private void setTable1() throws Exception
    {
        Font fontH1 = new Font(BaseFont.createFont("src/fonts/FreeSans.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED), 7);

        table1 = new PdfPTable(4); // 3 columns.
        //table1.setWidthPercentage(30); //Width 100%
        table1.setSpacingBefore(0f); //Space before table
        table1.setSpacingAfter(0f); //Space after table

        //Set Column widths
        float[] columnWidths = {31f, 19f, 25f, 25f};
        table1.setWidths(columnWidths);

        PdfPCell cell1 = new PdfPCell(new Paragraph("Polož.", fontH1));
        cell1.setBorder(Rectangle.BOTTOM);
        cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);

        PdfPCell cell2 = new PdfPCell(new Paragraph("Dní", fontH1));
        cell2.setBorder(Rectangle.BOTTOM);
        cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);

        PdfPCell cell3 = new PdfPCell(new Paragraph("Hod/Jed", fontH1));
        cell3.setBorder(Rectangle.BOTTOM);
        cell3.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);

        PdfPCell cell4 = new PdfPCell(new Paragraph("Čiast.", fontH1));
        cell4.setBorder(Rectangle.BOTTOM);
        cell4.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell4.setVerticalAlignment(Element.ALIGN_MIDDLE);

        table1.addCell(cell1);
        table1.addCell(cell2);
        table1.addCell(cell3);
        table1.addCell(cell4);



        cell1 = new PdfPCell(new Paragraph("Fond prac. c.", fontH1));
        cell1.setBorder(Rectangle.NO_BORDER);
        cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);

        cell2 = new PdfPCell(new Paragraph(new BigDecimal(paymentD.getDaysFund()).setScale(1, RoundingMode.HALF_UP).toPlainString(), fontH1));
        cell2.setBorder(Rectangle.NO_BORDER);
        cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);

        cell3 = new PdfPCell(new Paragraph(getWageString(paymentD.getHoursFund()), fontH1));
        cell3.setBorder(Rectangle.NO_BORDER);
        cell3.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);

        cell4 = new PdfPCell(new Paragraph("", fontH1));
        cell4.setBorder(Rectangle.NO_BORDER);
        cell4.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell4.setVerticalAlignment(Element.ALIGN_MIDDLE);

        table1.addCell(cell1);
        table1.addCell(cell2);
        table1.addCell(cell3);
        table1.addCell(cell4);

        cell1 = new PdfPCell(new Paragraph("Odpracovane", fontH1));
        cell1.setBorder(Rectangle.NO_BORDER);
        cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);

        cell2 = new PdfPCell(new Paragraph(new BigDecimal(paymentD.getDaysWorked()).setScale(1, RoundingMode.HALF_UP).toPlainString(), fontH1));
        cell2.setBorder(Rectangle.NO_BORDER);
        cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);

        cell3 = new PdfPCell(new Paragraph(getWageString(paymentD.getHoursWorked()), fontH1));
        cell3.setBorder(Rectangle.NO_BORDER);
        cell3.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);

        cell4 = new PdfPCell(new Paragraph("", fontH1));
        cell4.setBorder(Rectangle.NO_BORDER);
        cell4.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell4.setVerticalAlignment(Element.ALIGN_MIDDLE);

        table1.addCell(cell1);
        table1.addCell(cell2);
        table1.addCell(cell3);
        table1.addCell(cell4);

        for(PaymentBasicComponentD p : paymentD.getBasicComponentDS())
        {
            if(!isWageNull(p.getWageForUnits()))
            {
                cell1 = new PdfPCell(new Paragraph(p.getWageForm(), fontH1));
                cell1.setBorder(Rectangle.NO_BORDER);
                cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);

                cell2 = new PdfPCell(new Paragraph("", fontH1));
                cell2.setBorder(Rectangle.NO_BORDER);
                cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);

                cell3 = new PdfPCell(new Paragraph(getWageString(p.getWorkedUnits()), fontH1));
                cell3.setBorder(Rectangle.NO_BORDER);
                cell3.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);

                cell4 = new PdfPCell(new Paragraph(getWageString(p.getWageForUnits()), fontH1));
                cell4.setBorder(Rectangle.NO_BORDER);
                cell4.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell4.setVerticalAlignment(Element.ALIGN_MIDDLE);

                table1.addCell(cell1);
                table1.addCell(cell2);
                table1.addCell(cell3);
                table1.addCell(cell4);
            }
        }

        for(PaymentDynamicComponentD p : paymentD.getDynamicComponentDS())
        {
            if(!isWageNull(p.getWage()))
            {
                cell1 = new PdfPCell(new Paragraph(p.getCharacteristic(), fontH1));
                cell1.setBorder(Rectangle.NO_BORDER);
                cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);

                cell2 = new PdfPCell(new Paragraph("", fontH1));
                cell2.setBorder(Rectangle.NO_BORDER);
                cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);

                cell3 = new PdfPCell(new Paragraph("", fontH1));
                cell3.setBorder(Rectangle.NO_BORDER);
                cell3.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);

                cell4 = new PdfPCell(new Paragraph(getWageString(p.getWage()), fontH1));
                cell4.setBorder(Rectangle.NO_BORDER);
                cell4.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell4.setVerticalAlignment(Element.ALIGN_MIDDLE);

                table1.addCell(cell1);
                table1.addCell(cell2);
                table1.addCell(cell3);
                table1.addCell(cell4);
            }
        }

        for(PaymentSurchargeD p : paymentD.getSurchargeDS())
        {
            if(!isWageNull(p.getHours()))
            {
                cell1 = new PdfPCell(new Paragraph(p.getSurchargeName(), fontH1));
                cell1.setBorder(Rectangle.NO_BORDER);
                cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);

                cell2 = new PdfPCell(new Paragraph("", fontH1));
                cell2.setBorder(Rectangle.NO_BORDER);
                cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);

                cell3 = new PdfPCell(new Paragraph(getWageString(p.getHours()), fontH1));
                cell3.setBorder(Rectangle.NO_BORDER);
                cell3.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);

                cell4 = new PdfPCell(new Paragraph(getWageString(p.getWage()), fontH1));
                cell4.setBorder(Rectangle.NO_BORDER);
                cell4.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell4.setVerticalAlignment(Element.ALIGN_MIDDLE);

                table1.addCell(cell1);
                table1.addCell(cell2);
                table1.addCell(cell3);
                table1.addCell(cell4);
            }
        }

        for(PaymentWageCompensationD p : paymentD.getWageCompensationDS())
        {
            if(!isWageNull(p.getHours()) && !isWageNull(p.getDays()))
            {
                cell1 = new PdfPCell(new Paragraph(p.getReason(), fontH1));
                cell1.setBorder(Rectangle.NO_BORDER);
                cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);

                cell2 = new PdfPCell(new Paragraph(new BigDecimal(p.getDays()).setScale(1, RoundingMode.HALF_UP).toPlainString(), fontH1));
                cell2.setBorder(Rectangle.NO_BORDER);
                cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);

                cell3 = new PdfPCell(new Paragraph(getWageString(p.getHours()), fontH1));
                cell3.setBorder(Rectangle.NO_BORDER);
                cell3.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);

                cell4 = new PdfPCell(new Paragraph(getWageString(p.getWage()), fontH1));
                cell4.setBorder(Rectangle.NO_BORDER);
                cell4.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell4.setVerticalAlignment(Element.ALIGN_MIDDLE);

                table1.addCell(cell1);
                table1.addCell(cell2);
                table1.addCell(cell3);
                table1.addCell(cell4);
            }
        }

        for(PaymentOtherComponentD p : paymentD.getOtherComponentDS())
        {
            if(!isWageNull(p.getWage()))
            {
                cell1 = new PdfPCell(new Paragraph(p.getName(), fontH1));
                cell1.setBorder(Rectangle.NO_BORDER);
                cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);

                cell2 = new PdfPCell(new Paragraph("", fontH1));
                cell2.setBorder(Rectangle.NO_BORDER);
                cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);

                cell3 = new PdfPCell(new Paragraph("", fontH1));
                cell3.setBorder(Rectangle.NO_BORDER);
                cell3.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);

                cell4 = new PdfPCell(new Paragraph(getWageString(p.getWage()), fontH1));
                cell4.setBorder(Rectangle.NO_BORDER);
                cell4.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell4.setVerticalAlignment(Element.ALIGN_MIDDLE);

                table1.addCell(cell1);
                table1.addCell(cell2);
                table1.addCell(cell3);
                table1.addCell(cell4);
            }
        }
    }

    private void setTable2() throws Exception
    {
        Font fontH1 = new Font(BaseFont.createFont("src/fonts/FreeSans.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED), 7);

        table2 = new PdfPTable(2); // 3 columns.
        //table2.setWidthPercentage(20); //Width 100%
        table2.setSpacingBefore(0f); //Space before table
        table2.setSpacingAfter(0f); //Space after table


        //Set Column widths
        float[] columnWidths = {6f, 4f};
        table2.setWidths(columnWidths);

        PdfPCell cell1 = new PdfPCell(new Paragraph("Polož.", fontH1));
        cell1.setBorder(Rectangle.BOTTOM);
        cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);

        PdfPCell cell2 = new PdfPCell(new Paragraph("Čiast.", fontH1));
        cell2.setBorder(Rectangle.BOTTOM);
        cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);

        table2.addCell(cell1);
        table2.addCell(cell2);


        cell1 = new PdfPCell(new Paragraph("Hrubá mzda", fontH1));
        cell1.setBorder(Rectangle.NO_BORDER);
        cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);

        cell2 = new PdfPCell(new Paragraph(getWageString(paymentD.getGrossWage()), fontH1));
        cell2.setBorder(Rectangle.NO_BORDER);
        cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);

        table2.addCell(cell1);
        table2.addCell(cell2);

        cell1 = new PdfPCell(new Paragraph("Poistné", fontH1));
        cell1.setBorder(Rectangle.NO_BORDER);
        cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);

        cell2 = new PdfPCell(new Paragraph(getWageString(paymentD.getEmployeeEnsurence()), fontH1));
        cell2.setBorder(Rectangle.NO_BORDER);
        cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);

        table2.addCell(cell1);
        table2.addCell(cell2);

        cell1 = new PdfPCell(new Paragraph("Nezdanitel. mzda", fontH1));
        cell1.setBorder(Rectangle.NO_BORDER);
        cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);

        cell2 = new PdfPCell(new Paragraph(getWageString(paymentD.getNonTaxableWage()), fontH1));
        cell2.setBorder(Rectangle.NO_BORDER);
        cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);

        table2.addCell(cell1);
        table2.addCell(cell2);

        cell1 = new PdfPCell(new Paragraph("Zdanitel. mzda", fontH1));
        cell1.setBorder(Rectangle.NO_BORDER);
        cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);

        cell2 = new PdfPCell(new Paragraph(getWageString(paymentD.getTaxableWage()), fontH1));
        cell2.setBorder(Rectangle.NO_BORDER);
        cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);

        table2.addCell(cell1);
        table2.addCell(cell2);

        cell1 = new PdfPCell(new Paragraph("Preddav. na dan", fontH1));
        cell1.setBorder(Rectangle.NO_BORDER);
        cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);

        cell2 = new PdfPCell(new Paragraph(getWageString(paymentD.getTaxAdvances()), fontH1));
        cell2.setBorder(Rectangle.NO_BORDER);
        cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);

        table2.addCell(cell1);
        table2.addCell(cell2);

        if(!isWageNull(paymentD.getTaxBonus()))
        {
            cell1 = new PdfPCell(new Paragraph("Danov. bonus", fontH1));
            cell1.setBorder(Rectangle.NO_BORDER);
            cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);

            cell2 = new PdfPCell(new Paragraph(getWageString(paymentD.getTaxBonus()), fontH1));
            cell2.setBorder(Rectangle.NO_BORDER);
            cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);

            table2.addCell(cell1);
            table2.addCell(cell2);
        }

        cell1 = new PdfPCell(new Paragraph("Cistá mzda", fontH1));
        cell1.setBorder(Rectangle.NO_BORDER);
        cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);

        cell2 = new PdfPCell(new Paragraph(getWageString(paymentD.getNetWage()), fontH1));
        cell2.setBorder(Rectangle.NO_BORDER);
        cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);

        table2.addCell(cell1);
        table2.addCell(cell2);

        for(PaymentDeductionD p : paymentD.getDeductionDS())
        {
            if(!isWageNull(p.getSum()))
            {
                cell1 = new PdfPCell(new Paragraph(p.getName(), fontH1));
                cell1.setBorder(Rectangle.NO_BORDER);
                cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);

                cell2 = new PdfPCell(new Paragraph(getWageString(p.getSum()), fontH1));
                cell2.setBorder(Rectangle.NO_BORDER);
                cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);

                table2.addCell(cell1);
                table2.addCell(cell2);
            }
        }

        cell1 = new PdfPCell(new Paragraph("K vyplate", fontH1));
        cell1.setBorder(Rectangle.NO_BORDER);
        cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);

        cell2 = new PdfPCell(new Paragraph(getWageString(paymentD.getTotal()), fontH1));
        cell2.setBorder(Rectangle.NO_BORDER);
        cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);

        table2.addCell(cell1);
        table2.addCell(cell2);

        cell1 = new PdfPCell(new Paragraph("Na ucet", fontH1));
        cell1.setBorder(Rectangle.NO_BORDER);
        cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);

        cell2 = new PdfPCell(new Paragraph(getWageString(paymentD.getOnAccount()), fontH1));
        cell2.setBorder(Rectangle.NO_BORDER);
        cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);

        table2.addCell(cell1);
        table2.addCell(cell2);

        cell1 = new PdfPCell(new Paragraph("V hotovosti", fontH1));
        cell1.setBorder(Rectangle.NO_BORDER);
        cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);

        cell2 = new PdfPCell(new Paragraph(getWageString(paymentD.getCash()), fontH1));
        cell2.setBorder(Rectangle.NO_BORDER);
        cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);

        table2.addCell(cell1);
        table2.addCell(cell2);

    }

    private void setTable3() throws Exception
    {
        Font fontH1 = new Font(BaseFont.createFont("src/fonts/FreeSans.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED), 7);


        table3 = new PdfPTable(4); // 3 columns.
        //table1.setWidthPercentage(30); //Width 100%
        table3.setSpacingBefore(0f); //Space before table
        table3.setSpacingAfter(0f); //Space after table

        //Set Column widths
        float[] columnWidths = {33f, 25f, 21f, 21f};
        table3.setWidths(columnWidths);


        PdfPCell cell1 = new PdfPCell(new Paragraph("Odvod", fontH1));
        cell1.setBorder(Rectangle.BOTTOM);
        cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);

        PdfPCell cell2 = new PdfPCell(new Paragraph("VZ", fontH1));
        cell2.setBorder(Rectangle.BOTTOM);
        cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);

        PdfPCell cell3 = new PdfPCell(new Paragraph("Z-ec", fontH1));
        cell3.setBorder(Rectangle.BOTTOM);
        cell3.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);

        PdfPCell cell4 = new PdfPCell(new Paragraph("Z-el", fontH1));
        cell4.setBorder(Rectangle.BOTTOM);
        cell4.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell4.setVerticalAlignment(Element.ALIGN_MIDDLE);

        table3.addCell(cell1);
        table3.addCell(cell2);
        table3.addCell(cell3);
        table3.addCell(cell4);

        for(LevyD l : paymentD.getLevyDS())
        {
            cell1 = new PdfPCell(new Paragraph(l.getName(), fontH1));
            cell1.setBorder(Rectangle.NO_BORDER);
            cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);

            cell2 = new PdfPCell(new Paragraph(getWageString(l.getAssessmentBasis()), fontH1));
            cell2.setBorder(Rectangle.NO_BORDER);
            cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);

            cell3 = new PdfPCell(new Paragraph(getWageString(l.getEmployeeSum()), fontH1));
            cell3.setBorder(Rectangle.NO_BORDER);
            cell3.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);

            cell4 = new PdfPCell(new Paragraph(getWageString(l.getEmployerSum()), fontH1));
            cell4.setBorder(Rectangle.NO_BORDER);
            cell4.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell4.setVerticalAlignment(Element.ALIGN_MIDDLE);

            table3.addCell(cell1);
            table3.addCell(cell2);
            table3.addCell(cell3);
            table3.addCell(cell4);
        }
    }

    private void setTable4() throws Exception
    {
        Font fontH = new Font(BaseFont.createFont("src/fonts/FreeSans.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED), 7);
        Font fontH1 = new Font(BaseFont.createFont("src/fonts/FreeSans.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED), 6);

        table4 = new PdfPTable(2); // 3 columns.
        //table1.setWidthPercentage(30); //Width 100%
        table4.setSpacingBefore(0f); //Space before table
        table4.setSpacingAfter(0f); //Space after table

        //Set Column widths
        float[] columnWidths = {65f, 35f};
        table4.setWidths(columnWidths);

        PdfPCell cell1 = new PdfPCell(new Paragraph("Polož.", fontH));
        cell1.setBorder(Rectangle.BOTTOM);
        cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);

        PdfPCell cell2 = new PdfPCell(new Paragraph("Čiast.", fontH));
        cell2.setBorder(Rectangle.BOTTOM);
        cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);

        table4.addCell(cell1);
        table4.addCell(cell2);

        String type;
        if(paymentD.getRelationType().contains("D:"))
            type = "Dohoda";
        else
            type = "PP";

        cell1 = new PdfPCell(new Paragraph("Vzťah", fontH1));
        cell1.setBorder(Rectangle.NO_BORDER);
        cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);

        cell2 = new PdfPCell(new Paragraph(type, fontH1));
        cell2.setBorder(Rectangle.NO_BORDER);
        cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);

        table4.addCell(cell1);
        table4.addCell(cell2);

        cell1 = new PdfPCell(new Paragraph("Tyzd. prac. cas", fontH1));
        cell1.setBorder(Rectangle.NO_BORDER);
        cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);

        cell2 = new PdfPCell(new Paragraph(getEmptyString(paymentD.getWeekTime()), fontH1));
        cell2.setBorder(Rectangle.NO_BORDER);
        cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);

        table4.addCell(cell1);
        table4.addCell(cell2);

        cell1 = new PdfPCell(new Paragraph("Tarifa:", fontH1));
        cell1.setBorder(Rectangle.NO_BORDER);
        cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);

        cell2 = new PdfPCell(new Paragraph());
        cell2.setBorder(Rectangle.NO_BORDER);
        cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);

        table4.addCell(cell1);
        table4.addCell(cell2);

        for(PaymentBasicComponentD p : paymentD.getBasicComponentDS())
        {
            cell1 = new PdfPCell(new Paragraph(p.getWageForm(), fontH1));
            cell1.setBorder(Rectangle.NO_BORDER);
            cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);

            cell2 = new PdfPCell(new Paragraph(getWageString(p.getWageTarif())+" / " + p.getWageFormUnitShort(), fontH1));
            cell2.setBorder(Rectangle.NO_BORDER);
            cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);

            table4.addCell(cell1);
            table4.addCell(cell2);
        }


        cell1 = new PdfPCell(new Paragraph("", fontH1));
        cell1.setBorder(Rectangle.NO_BORDER);
        cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell1.setMinimumHeight(5f);

        cell2 = new PdfPCell(new Paragraph());
        cell2.setBorder(Rectangle.NO_BORDER);
        cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell2.setMinimumHeight(5f);

        table4.addCell(cell1);
        table4.addCell(cell2);


        if(paymentD.getRelationType().contains("PP:"))
        {
            cell1 = new PdfPCell(new Paragraph("Dovolenka:", fontH1));
            cell1.setBorder(Rectangle.NO_BORDER);
            cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);

            cell2 = new PdfPCell(new Paragraph());
            cell2.setBorder(Rectangle.NO_BORDER);
            cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);

            table4.addCell(cell1);
            table4.addCell(cell2);

            cell1 = new PdfPCell(new Paragraph("vycer./narok", fontH1));
            cell1.setBorder(Rectangle.NO_BORDER);
            cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);

            BigDecimal bigDecimal = new BigDecimal(paymentD.getYearHoliday()).add(new BigDecimal(paymentD.getYearHolidayLastYear()));
            cell2 = new PdfPCell(new Paragraph(getWageString(paymentD.getYearHolideyTaken())+"/"+bigDecimal.toPlainString(), fontH1));
            cell2.setBorder(Rectangle.NO_BORDER);
            cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);

            table4.addCell(cell1);
            table4.addCell(cell2);

            cell1 = new PdfPCell(new Paragraph("", fontH1));
            cell1.setBorder(Rectangle.NO_BORDER);
            cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell1.setMinimumHeight(5f);


            cell2 = new PdfPCell(new Paragraph("", fontH1));
            cell2.setBorder(Rectangle.NO_BORDER);
            cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell2.setMinimumHeight(5f);

            table4.addCell(cell1);
            table4.addCell(cell2);
        }

        cell1 = new PdfPCell(new Paragraph("Cen. práce", fontH1));
        cell1.setBorder(Rectangle.NO_BORDER);
        cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);

        cell2 = new PdfPCell(new Paragraph(getWageString(paymentD.getWorkPrice()), fontH1));
        cell2.setBorder(Rectangle.NO_BORDER);
        cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);

        table4.addCell(cell1);
        table4.addCell(cell2);

        cell1 = new PdfPCell(new Paragraph("Odv. zam-el", fontH1));
        cell1.setBorder(Rectangle.NO_BORDER);
        cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);

        cell2 = new PdfPCell(new Paragraph(getWageString(paymentD.getEmployerLevies()), fontH1));
        cell2.setBorder(Rectangle.NO_BORDER);
        cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);

        table4.addCell(cell1);
        table4.addCell(cell2);

        cell1 = new PdfPCell(new Paragraph("Odv. dan. zam-ec", fontH1));
        cell1.setBorder(Rectangle.NO_BORDER);
        cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);

        cell2 = new PdfPCell(new Paragraph(getWageString(paymentD.getEmployeeLevies()), fontH1));
        cell2.setBorder(Rectangle.NO_BORDER);
        cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);

        table4.addCell(cell1);
        table4.addCell(cell2);

        cell1 = new PdfPCell(new Paragraph("Odv. dan spolu", fontH1));
        cell1.setBorder(Rectangle.NO_BORDER);
        cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);

        cell2 = new PdfPCell(new Paragraph(getWageString(paymentD.getLeviesSum()), fontH1));
        cell2.setBorder(Rectangle.NO_BORDER);
        cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);

        table4.addCell(cell1);
        table4.addCell(cell2);

    }

    private void setTable() throws Exception
    {
        table = new PdfPTable(7); // 3 columns.
        table.setWidthPercentage(100); //Width 100%
        table.setSpacingBefore(0f); //Space before table
        table.setSpacingAfter(0f); //Space after table
        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);


        //Set Column widths
        float[] columnWidths =  {55f, 5f, 36f, 5f, 53f, 5f, 36f};
        table.setWidths(columnWidths);

        table.addCell(table1);
        table.addCell(tableEmpty);//
        table.addCell(table2);
        table.addCell(tableEmpty);//
        table.addCell(table3);
        table.addCell(tableEmpty);//
        table.addCell(table4);
    }

    private void setMainTable() throws Exception
    {
        mainTable = new PdfPTable(1); // 3 columns.
        mainTable.setWidthPercentage(100); //Width 100%
        mainTable.setSpacingBefore(10f); //Space before table
        mainTable.setSpacingAfter(10f); //Space after table
        mainTable.setKeepTogether(true);
        mainTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);


        //Set Column widths
        float[] columnWidths =  {1f};
        mainTable.setWidths(columnWidths);

        mainTable.addCell(tableHeader);
        mainTable.addCell(table);//

    }

    private void foo()
    {
        int size = 0;

        if(table1.size() > size)
            size = table1.size();
        if(table2.size() > size)
            size = table2.size();
        if(table3.size() > size)
            size = table3.size();
        if(table4.size() > size)
            size = table4.size();

        if(table1.size() < size)
            addRowsToTable1(size-table1.size());
        if(table2.size() < size)
            addRowsToTable2(size-table2.size());
        if(table3.size() < size)
            addRowsToTable3(size-table3.size());
        if(table4.size() < size)
            addRowsToTable4(size-table4.size());

    }

    private void addRowsToTable1(int rows)
    {
        PdfPCell cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        for(int i = 0; i<rows*4; i++)
        {
            table1.addCell(cell);
        }
    }

    private void addRowsToTable2(int rows)
    {
        PdfPCell cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        for(int i = 0; i<rows*2; i++)
        {
            table2.addCell(cell);
        }
    }

    private void addRowsToTable3(int rows)
    {
        PdfPCell cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        for(int i = 0; i<rows*4; i++)
        {
            table3.addCell(cell);
        }
    }

    private void addRowsToTable4(int rows)
    {
        PdfPCell cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        for(int i = 0; i<rows*2; i++)
        {
            table4.addCell(cell);
        }
    }

    private String getEmptyString(String s)
    {
        if(s==null)
            return "";
        else
            return s;
    }

    private String getEmptyWage(String s)
    {
        if((new BigDecimal(s)).compareTo(new BigDecimal("0"))==0)
            return "";
        else
            return s;
    }

    private String getWageString(String s)
    {
        BigDecimal c = new BigDecimal(s);
        return c.setScale(2, RoundingMode.HALF_UP).toPlainString();
    }

    private boolean isWageNull(String s)
    {
        BigDecimal c = new BigDecimal(s);
        if(c.compareTo(new BigDecimal("0"))==0)
            return true;
        else
            return false;
    }


}
