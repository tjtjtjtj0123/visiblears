package com.xcally.ars.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

import java.util.List;

@Data
public class Commercekorea {

    @JsonProperty("auth_key")
    private String authKey;

    @JsonProperty("r_code")
    private String rCode;

    @JsonProperty("r_msg")
    private String rMsg;

    @JsonProperty("data")
    private List<Warehouse> data;
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ApiResponse{")
                .append("authKey='").append(authKey).append('\'')
                .append(", rCode='").append(rCode).append('\'')
                .append(", rMsg='").append(rMsg).append('\'')
                .append(", data=[");
        
        if (data != null) {
            for (Warehouse warehouse : data) {
                sb.append(warehouse.toString()).append(", ");
            }
            // 마지막 쉼표와 공백 제거
            if (!data.isEmpty()) {
                sb.setLength(sb.length() - 2);
            }
        }

        sb.append("]}");

        return sb.toString();
    }


    // Warehouse 클래스 정의

    public static class Warehouse {
        @JsonProperty("warehouse_id")
        private int warehouseId;

        @JsonProperty("warehouse_code")
        private String warehouseCode;

        @JsonProperty("warehouse_name")
        private String warehouseName;

        @JsonProperty("warehouse_type_id")
        private int warehouseTypeId;

        @JsonProperty("warehouse_type_name")
        private String warehouseTypeName;

        @JsonProperty("warehouse_type_code")
        private String warehouseTypeCode;

        @JsonProperty("priority_no")
        private int priorityNo;

        @JsonProperty("picking_target_flag")
        private boolean pickingTargetFlag;

        @JsonProperty("stock_qty_target_flag")
        private boolean stockQtyTargetFlag;

        @JsonProperty("working_qty_target_flag")
        private boolean workingQtyTargetFlag;

        // Getters and Setters
        // (생략)
        @Override
        public String toString() {
            return "Warehouse{" +
                    "warehouseId=" + warehouseId +
                    ", warehouseCode='" + warehouseCode + '\'' +
                    ", warehouseName='" + warehouseName + '\'' +
                    ", warehouseTypeId=" + warehouseTypeId +
                    ", warehouseTypeName='" + warehouseTypeName + '\'' +
                    ", warehouseTypeCode='" + warehouseTypeCode + '\'' +
                    ", priorityNo=" + priorityNo +
                    ", pickingTargetFlag=" + pickingTargetFlag +
                    ", stockQtyTargetFlag=" + stockQtyTargetFlag +
                    ", workingQtyTargetFlag=" + workingQtyTargetFlag +
                    '}';
        }
        
    }


    
}
