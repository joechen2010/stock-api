package com.stock.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.stock.api.util.JsonUtil;

import java.util.List;

public class LoginResponse {
   @JsonProperty("Errcode")
   private int errcode;
   @JsonProperty("Message")
   private String message;
   @JsonProperty("Status")
   private int status;
   @JsonProperty("Data")
   private List<LoginData> loginDatas;

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

   public List<LoginData> getLoginDatas() {
      return this.loginDatas;
   }

   public void setLoginDatas(List<LoginData> loginDatas) {
      this.loginDatas = loginDatas;
   }

   public String toString() {
      return JsonUtil.objectToJson(this);
   }

   public static class LoginData {
      @JsonProperty("Time")
      private String time;
      @JsonProperty("Syspm1")
      private String syspm1;
      @JsonProperty("Syspm2")
      private String syspm2;
      @JsonProperty("Syspm3")
      private String sessionId;
      @JsonProperty("Date")
      private String date;
      @JsonProperty("Syspm_ex")
      private String uid;

      public String getTime() {
         return this.time;
      }

      public void setTime(String time) {
         this.time = time;
      }

      public String getSyspm1() {
         return this.syspm1;
      }

      public void setSyspm1(String syspm1) {
         this.syspm1 = syspm1;
      }

      public String getSyspm2() {
         return this.syspm2;
      }

      public void setSyspm2(String syspm2) {
         this.syspm2 = syspm2;
      }

      public String getSessionId() {
         return this.sessionId;
      }

      public void setSessionId(String sessionId) {
         this.sessionId = sessionId;
      }

      public String getDate() {
         return this.date;
      }

      public void setDate(String date) {
         this.date = date;
      }

      public String getUid() {
         return this.uid;
      }

      public void setUid(String uid) {
         this.uid = uid;
      }
   }
}
