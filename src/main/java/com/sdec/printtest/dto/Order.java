package com.sdec.printtest.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Order {
    private String OrderID;
    private String FileName;
    private int Number;
    private double Price;
    private byte[] Data;
    private String FileType;
    public void setOrderID(String orderID) {
        OrderID = orderID;
    }
    public void setFileName(String fileName) {
        FileName = fileName;
    }
    public void setFileType(String fileType) {
        FileType = fileType;
    }

    public void setData(byte[] data) {
        Data = data;
    }
    public byte[] getData() {
        return Data;
    }

}
