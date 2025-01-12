package com.stock.api.util.retry;

import com.stock.api.util.Op;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.RetryContext;
import org.springframework.retry.backoff.*;

import java.util.concurrent.atomic.AtomicInteger;

public class FibonacciBackOffPolicy extends ExponentialBackOffPolicy {
   private static final Logger log = LoggerFactory.getLogger(FibonacciBackOffPolicy.class);
   private Sleeper sleeper = new ThreadWaitSleeper();

   public BackOffContext start(RetryContext context) {
      return new FibonacciBackOffContext(this.getInitialInterval(), this.getMaxInterval());
   }

   protected FibonacciBackOffPolicy newInstance() {
      return new FibonacciBackOffPolicy();
   }

   public void backOff(BackOffContext backOffContext) throws BackOffInterruptedException {
      FibonacciBackOffContext context = (FibonacciBackOffContext)backOffContext;

      try {
         this.sleeper.sleep(context.getSleepAndIncrement());
      } catch (InterruptedException var4) {
         log.error(Op.str("Interrupted  {}", Op.str((Throwable)var4)));
         Thread.currentThread().interrupt();
      }

   }

   public static int getFibonacciSeq(int number) {
      return number <= 1 ? number : getFibonacciSeq(number - 1) + getFibonacciSeq(number - 2);
   }

   static class FibonacciBackOffContext implements BackOffContext {
      private long initialInterval;
      private long maxInterval;
      private AtomicInteger count = new AtomicInteger();

      public FibonacciBackOffContext(long initialInterval, long maxInterval) {
         this.initialInterval = initialInterval;
         this.maxInterval = maxInterval;
      }

      public synchronized long getSleepAndIncrement() {
         long sleep;
         if ((long)this.fibonacci(this.count.get()) * this.initialInterval > this.maxInterval) {
            sleep = this.maxInterval;
         } else {
            sleep = this.getNextInterval();
         }

         return sleep;
      }

      protected long getNextInterval() {
         this.count.incrementAndGet();
         return (long)this.fibonacci(this.count.get()) * this.initialInterval;
      }

      protected int fibonacci(int number) {
         return number <= 1 ? number : this.fibonacci(number - 1) + this.fibonacci(number - 2);
      }
   }
}
