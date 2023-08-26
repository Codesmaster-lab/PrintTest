package com.sdec.printtest.publisher;

import com.sdec.printtest.config.MessagingConfig;
import com.sdec.printtest.dto.Order;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.print.Doc;
import java.awt.*;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

@RestController
@RequestMapping("/order")
public class OrderPublisher {
    @Autowired
    private RabbitTemplate template;

    @PostMapping("")
    public  String bookOrder(@RequestParam("file") MultipartFile file){
       Order order =new Order();
        order.setOrderID(UUID.randomUUID().toString());

        String docname = file.getOriginalFilename();

        try {
            order.setFileName(file.getOriginalFilename());
            order.setFileType(file.getContentType());
            order.setData(file.getBytes());
            template.convertAndSend(MessagingConfig.EXCHANGE, MessagingConfig.ROUTING_KEY, order);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return "Success !!";
    }

    @GetMapping("/getmsg")
    public void consumeMessageFromQueue() {
        Order order = (Order)(template.receiveAndConvert(MessagingConfig.QUEUE));
        try {

            String FILEPATH = "consumed.pdf";
            File file = new File(FILEPATH);
            OutputStream os = new FileOutputStream(file);

            os.write(order.getData());
            System.out.println("Successfully" + " byte inserted");
            os.close();

            try (PDDocument document = PDDocument.load(file)) {
                PrinterJob job = PrinterJob.getPrinterJob();
                job.setPageable(new PDFPageable(document));

                if (job.printDialog()) {
                    job.print();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Catch block to handle the exceptions
        catch (Exception e) {

            // Display exception on console
            System.out.println("Exception: " + e);
        }
        System.out.println(order);
    }
}
