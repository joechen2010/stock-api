package com.stock.api.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class RandomData {
   private String random;
   private String code;

   public String getRandom() {
      return this.random;
   }

   public void setRandom(String random) {
      this.random = random;
   }

   public String getCode() {
      return this.code;
   }

   public void setCode(String code) {
      this.code = code;
   }

   public String toString() {
      return (new ToStringBuilder(this)).append("random", this.random).append("code", this.code).toString();
   }
}
