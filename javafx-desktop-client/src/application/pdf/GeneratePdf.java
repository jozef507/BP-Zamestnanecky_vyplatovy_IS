package application.pdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;

public class GeneratePdf
{
    private Document document;
    private PdfWriter writer;

    private String name;

    public GeneratePdf(String name)
    {
        this.name = name;
        document = new Document();
        try {
            writer = PdfWriter.getInstance(document, new FileOutputStream(name));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setPageAndOpen(Rectangle pageSize, int marginTop, int marginRight, int marginBottom, int marginLeft)
    {
        document.setPageSize(pageSize);
        document.setMargins(marginLeft, marginRight, marginTop, marginBottom);
        document.open();
    }



    public void setAttributes(String author, String title, String subject)
    {
        document.addAuthor(author);
        document.addCreationDate();
        document.addTitle(title);
        document.addSubject(subject);
    }

    public Document getDocument() {
        return document;
    }

    public PdfWriter getWriter() {
        return writer;
    }

    public void close()
    {
        this.document.close();
        this.writer.close();
    }
}
