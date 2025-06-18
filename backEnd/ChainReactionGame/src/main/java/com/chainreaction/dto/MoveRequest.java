package com.chainreaction.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class MoveRequest {
    @NotNull(message = "Row is required")
    @Min(value = 0, message = "Row must be between 0 and 8")
    @Max(value = 8, message = "Row must be between 0 and 8")
    private Integer row;
    
    @NotNull(message = "Column is required")
    @Min(value = 0, message = "Column must be between 0 and 5")
    @Max(value = 5, message = "Column must be between 0 and 5")
    private Integer col;
    
    public MoveRequest() {}
    
    public MoveRequest(Integer row, Integer col) {
        this.row = row;
        this.col = col;
    }
    
    public Integer getRow() { return row; }
    public void setRow(Integer row) { this.row = row; }
    
    public Integer getCol() { return col; }
    public void setCol(Integer col) { this.col = col; }
}