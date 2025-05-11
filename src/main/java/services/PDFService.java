package services;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import entities.Event;
import entities.Location;

import java.io.FileOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PDFService {
    private static final Font TITLE_FONT = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
    private static final Font HEADER_FONT = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
    private static final Font NORMAL_FONT = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static void generateEventsPDF(List<Event> events, String filePath) {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            // Add title
            Paragraph title = new Paragraph("Events Report", TITLE_FONT);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            // Add date
            Paragraph date = new Paragraph("Generated on: " + java.time.LocalDate.now(), NORMAL_FONT);
            date.setAlignment(Element.ALIGN_RIGHT);
            date.setSpacingAfter(20);
            document.add(date);

            // Create table for events summary
            PdfPTable summaryTable = new PdfPTable(6);
            summaryTable.setWidthPercentage(100);
            summaryTable.setSpacingBefore(10f);
            summaryTable.setSpacingAfter(10f);

            // Add table headers
            String[] headers = {"Name", "Type", "Category", "Dates", "Price", "Status"};
            for (String header : headers) {
                summaryTable.addCell(new Phrase(header, HEADER_FONT));
            }

            // Add event data to summary table
            for (Event event : events) {
                summaryTable.addCell(new Phrase(event.getName(), NORMAL_FONT));
                summaryTable.addCell(new Phrase(event.getTypeEvent().toString(), NORMAL_FONT));
                summaryTable.addCell(new Phrase(event.getGenreEvent().toString(), NORMAL_FONT));
                summaryTable.addCell(new Phrase(
                    event.getStartDate().format(DATE_FORMATTER) + " to " + 
                    event.getEndDate().format(DATE_FORMATTER), 
                    NORMAL_FONT
                ));
                summaryTable.addCell(new Phrase(String.format("$%.2f", event.getFinalPrice()), NORMAL_FONT));
                summaryTable.addCell(new Phrase(event.getStatusEvent().toString(), NORMAL_FONT));
            }
            document.add(summaryTable);

            // Add detailed information for each event
            for (Event event : events) {
                document.add(new Paragraph("\n"));
                
                // Create a table for the event details with border
                PdfPTable eventTable = new PdfPTable(1);
                eventTable.setWidthPercentage(100);
                eventTable.setSpacingBefore(10f);
                eventTable.setSpacingAfter(10f);
                
                // Add event name as header
                PdfPCell headerCell = new PdfPCell(new Phrase(event.getName(), HEADER_FONT));
                headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                headerCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                headerCell.setBorder(Rectangle.BOTTOM);
                headerCell.setPadding(5f);
                eventTable.addCell(headerCell);

                // Create content cell
                PdfPCell contentCell = new PdfPCell();
                contentCell.setPadding(10f);
                contentCell.setBorder(Rectangle.NO_BORDER);

                // Create a table for the content
                PdfPTable contentTable = new PdfPTable(2);
                contentTable.setWidthPercentage(100);

                // Add event details
                addDetailRow(contentTable, "Description:", event.getDescription());
                addDetailRow(contentTable, "Start Date:", event.getStartDate().format(DATE_FORMATTER));
                addDetailRow(contentTable, "End Date:", event.getEndDate().format(DATE_FORMATTER));
                addDetailRow(contentTable, "Price:", String.format("$%.2f", event.getPrice()));
                addDetailRow(contentTable, "Final Price:", String.format("$%.2f", event.getFinalPrice()));
                addDetailRow(contentTable, "Type:", event.getTypeEvent().toString());
                addDetailRow(contentTable, "Category:", event.getGenreEvent().toString());
                addDetailRow(contentTable, "Status:", event.getStatusEvent().toString());
                addDetailRow(contentTable, "Participants:", event.getNbParticipant() + "/" + event.getMaxParticipants());
                addDetailRow(contentTable, "Views:", String.valueOf(event.getVuesNb()));
                addDetailRow(contentTable, "Fidelity Points:", String.valueOf(event.getFidelityPoints()));
                
                if (event.getPromotionRate() > 0) {
                    addDetailRow(contentTable, "Promotion Rate:", String.format("%.1f%%", event.getPromotionRate()));
                }

                // Add location information if available
                if (event.getLocation() != null) {
                    Location location = event.getLocation();
                    contentTable.addCell(new PdfPCell(new Phrase("Location Information:", HEADER_FONT)));
                    contentTable.addCell(new PdfPCell(new Phrase("")));
                    
                    addDetailRow(contentTable, "Country:", location.getCountry());
                    addDetailRow(contentTable, "Description:", location.getDescription());
                    addDetailRow(contentTable, "Visa Required:", location.isVisa() ? "Yes" : "No");

                    // Add location image if available
                    if (location.getImages() != null && !location.getImages().isEmpty()) {
                        try {
                            Image image = Image.getInstance(location.getImages());
                            image.scaleToFit(200, 200);
                            PdfPCell imageCell = new PdfPCell(image, true);
                            imageCell.setColspan(2);
                            imageCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                            imageCell.setPadding(10f);
                            contentTable.addCell(imageCell);
                        } catch (Exception e) {
                            System.err.println("Error loading image: " + e.getMessage());
                        }
                    }
                }

                contentCell.addElement(contentTable);
                eventTable.addCell(contentCell);
                document.add(eventTable);
            }

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error generating PDF: " + e.getMessage());
        }
    }

    private static void addDetailRow(PdfPTable table, String label, String value) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label, NORMAL_FONT));
        labelCell.setBorder(Rectangle.NO_BORDER);
        labelCell.setPadding(5f);
        table.addCell(labelCell);

        PdfPCell valueCell = new PdfPCell(new Phrase(value, NORMAL_FONT));
        valueCell.setBorder(Rectangle.NO_BORDER);
        valueCell.setPadding(5f);
        table.addCell(valueCell);
    }
} 