package com.mechanic.code;

import javafx.print.*;
import javafx.scene.Node;
import javafx.scene.transform.Scale;

public class Print {
    private Node nodePrint;

    public Print(Node node) {
        nodePrint=node;
    }

    public void print(){
        PrinterJob printerJob = PrinterJob.createPrinterJob();
        if (printerJob != null) {
            boolean correctSettings = printerJob.showPrintDialog(null);
            PageLayout pageLayout= printerJob.getPrinter().createPageLayout(Paper.A4,PageOrientation.PORTRAIT, Printer.MarginType.DEFAULT);

            double scaleX = pageLayout.getPrintableWidth() / nodePrint.getBoundsInParent().getWidth();
            double scaleY = pageLayout.getPrintableHeight() / nodePrint.getBoundsInParent().getHeight();
            Scale scale = new Scale(scaleX,scaleY);
            nodePrint.getTransforms().add(scale);
            if (correctSettings) {
                boolean printing = printerJob.printPage(pageLayout,nodePrint);
                if (printing) {
                    printerJob.endJob();
                } else {
                    System.out.println("Printing failed");
                }
            }else{
                System.out.println("Error with printer settings");
            }
            nodePrint.getTransforms().remove(scale);
        } else {
            System.out.println("Error with creating printer job");
        }

    }
}
