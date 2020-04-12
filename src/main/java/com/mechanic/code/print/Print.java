package com.mechanic.code.print;
import javafx.print.*;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;

public class Print {
    public Print(GridPane node) {
        PrinterJob printerJob = PrinterJob.createPrinterJob();
        if (printerJob != null) {
            boolean correctSettings = printerJob.showPrintDialog(null);
            PageLayout pageLayout= printerJob.getPrinter().createPageLayout(Paper.A4,PageOrientation.PORTRAIT, Printer.MarginType.DEFAULT);
            double scaleX = pageLayout.getPrintableWidth() / node.getBoundsInParent().getWidth();
            double scaleY = pageLayout.getPrintableHeight() / node.getBoundsInParent().getHeight();
            Scale scale = new Scale(scaleX,scaleY);
            node.getTransforms().add(scale);
            if (correctSettings) {
                boolean printing = printerJob.printPage(pageLayout,node);
                if (printing) {
                    printerJob.endJob();
                } else {
                    System.out.println("Printing failed");
                }
            }else{
                System.out.println("Error with printer settings");
            }
            node.getTransforms().remove(scale);
        } else {
            System.out.println("Error with creating printer job");
        }
    }

}