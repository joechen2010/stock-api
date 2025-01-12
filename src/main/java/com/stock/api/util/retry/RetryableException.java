package com.stock.api.util.retry;

public class RetryableException extends RuntimeException {
   public RetryableException(String message) {
      super(message);
   }
}
