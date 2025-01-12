package com.stock.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class RevokeResponse {
   @JsonProperty("Errcode")
   private int errcode;
   @JsonProperty("Message")
   private String message;
   @JsonProperty("Status")
   private int status;
   @JsonProperty("Data")
   private List<RevokeData> datas;

   public int getErrcode() {
      return this.errcode;
   }

   public void setErrcode(int errcode) {
      this.errcode = errcode;
   }

   public String getMessage() {
      return this.message;
   }

   public void setMessage(String message) {
      this.message = message;
   }

   public int getStatus() {
      return this.status;
   }

   public void setStatus(int status) {
      this.status = status;
   }

   public List<RevokeData> getDatas() {
      return this.datas;
   }

   public void setDatas(List<RevokeData> datas) {
      this.datas = datas;
   }

   public static class RevokeData {
      @JsonProperty("Cdsm")
      private String message;

      public String getMessage() {
         return this.message;
      }

      public void setMessage(String message) {
         this.message = message;
      }
   }
}
