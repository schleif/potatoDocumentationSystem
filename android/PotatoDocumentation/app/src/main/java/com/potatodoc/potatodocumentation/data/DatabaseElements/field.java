package com.potatodoc.potatodocumentation.data.DatabaseElements;

/**
 * Created by Marcel W on 09.01.2016.
 */
public class field {

    private int fieldNo;
    private int rowNo;
    private int columnNo;

    public field(int fieldNo, int rowNo, int columnNo) {

        this.fieldNo = fieldNo;
        this.rowNo = rowNo;
        this.columnNo = columnNo;
    }

    public Integer getFieldNo() {
        return fieldNo;
    }

    public void setFieldNo() {
        this.fieldNo = fieldNo;
    }

    public Integer getRowNo() {
        return rowNo;
    }

    public void setRowNo() {
        this.rowNo = rowNo;
    }

    public Integer getColumnNo() {
        return rowNo;
    }

    public void setColumnNo() {
        this.columnNo = columnNo;
    }

    public String toString() {
        return "Das Feld Nummer " + fieldNo + " besitzt " + rowNo*columnNo + "Parzellen";
    }
}
