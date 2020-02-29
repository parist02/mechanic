package com.mechanic.code;

import javafx.print.*;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Window;

public class Print {

    private GridPane nodePrint;
    private Rectangle rect;
    private WritableImage writableImage;
    private double width;
    private double height;


    public Print(GridPane node,Window window){
        width=node.getWidth();
        height=node.getHeight();
        PrinterJob printerJob=PrinterJob.createPrinterJob();
        if (printerJob!=null){
            boolean correctSettings=printerJob.showPrintDialog(window);
            //PageLayout pageLayout=printerJob.getPrinter().createPageLayout(Paper.A4,)
            boolean correctPageSettings=printerJob.showPageSetupDialog(window);
            if (correctPageSettings&&correctSettings){
                for (int i=1; i<node.getRowCount();i++){

                    printerJob.printPage(node);
                }
                printerJob.endJob();
            }else{
                System.out.println("Error with settings");
            }

        }else{
            System.out.println("Error with creating printer job");
        }


    }



}
