package com.stock.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Online {
   @JsonProperty("Errcode")
   private int errcode;
   @JsonProperty("Message")
   private String message;
   @JsonProperty("Status")
   private int status;
   @JsonProperty("Data")
   private OnlineData data;

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

   public OnlineData getData() {
      return this.data;
   }

   public void setData(OnlineData data) {
      this.data = data;
   }

   public static class OnlineData {
      @JsonProperty("Sfyx")
      private String online;
      @JsonProperty("SystemStatus")
      private String systemStatus;

      public String getOnline() {
         return this.online;
      }

      public void setOnline(String online) {
         this.online = online;
      }

      public String getSystemStatus() {
         return this.systemStatus;
      }

      public void setSystemStatus(String systemStatus) {
         this.systemStatus = systemStatus;
      }
   }
}
