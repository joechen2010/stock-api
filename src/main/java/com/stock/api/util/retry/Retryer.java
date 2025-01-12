package com.stock.api.util.retry;

import com.google.common.base.Stopwatch;
import com.stock.api.util.ApiUtils;
import com.stock.api.util.DataTypeUtils;
import com.stock.api.util.Op;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.*;
import org.springframework.retry.backoff.BackOffPolicy;
import org.springframework.retry.support.RetryTemplate;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

public class Retryer<T> {
   private static final Logger logger = LoggerFactory.getLogger(Retryer.class);
   protected static final String LAST_RESULT = "LastResult";
   private RetryTemplate template;
   private Predicate<T> resultPredicate;
   private long mostWaitingTime;
   protected ThreadLocal<RetryContext> contextInfo = new ThreadLocal();
   protected Stopwatch totalStopwatch;

   public void setTemplate(RetryTemplate template) {
      this.template = template;
   }

   public void setResultPredicate(Predicate<T> resultPredicate) {
      this.resultPredicate = resultPredicate;
   }

   public void setMostWaitingTime(long mostWaitingTime) {
      this.mostWaitingTime = mostWaitingTime;
   }

   public void setTotalStopwatch(Stopwatch totalStopwatch) {
      this.totalStopwatch = totalStopwatch;
   }

   public long getMostWaitingTime() {
      return this.mostWaitingTime;
   }

   public T call(Callable<T> action) {
      AtomicInteger count = new AtomicInteger(0);
      Stopwatch start = this.getStopwatch();

      try {
         return this.template.execute((context) -> {
            this.contextInfo.set(context);
            if (count.get() > 0) {
               logger.debug("Retry: count= {} and waiting time = {} ms.", count.get(), start.elapsed(TimeUnit.MILLISECONDS));
            }

            if (this.mostWaitingTime != 0L && start.elapsed(TimeUnit.MILLISECONDS) > this.mostWaitingTime) {
               this.throwLastException(context);
               return null;
            } else {
               count.incrementAndGet();
               context.setAttribute("retryCount", count.get() - 1);
               T t = action.call();
               if (this.resultPredicate != null && this.resultPredicate.test(t)) {
                  context.setAttribute("LastResult", t);
                  throw new RetryException("The result does not match the expect, retry it late");
               } else {
                  return t;
               }
            }
         });
      } catch (RetryException var5) {
         logger.debug("Retry failed last attempts: {} due to {{}}", this.getRetryCount(), Op.str((Throwable)var5));
      } catch (Exception var6) {
         this.throwRawException(var6);
      }

      return this.getLastResult();
   }

   protected Stopwatch getStopwatch() {
      return this.totalStopwatch == null ? Stopwatch.createStarted() : this.totalStopwatch;
   }

   private T getLastResult() {
      Object v = this.contextInfo.get() != null ? ((RetryContext)this.contextInfo.get()).getAttribute("LastResult") : null;
      return (T)v;
   }

   protected void throwRawException(Exception e) {
      if (e.getCause() instanceof RuntimeException) {
         throw (RuntimeException)e.getCause();
      } else if (e instanceof RuntimeException) {
         throw (RuntimeException)e;
      } else {
         String msg = Op.str("Retry failed last attempt: count= {}", this.getRetryCount());
         throw new TerminatedRetryException(msg, e);
      }
   }

   protected void throwLastException(RetryContext context) {
      Throwable lastError = context.getLastThrowable();
      if (!(lastError instanceof RetryException)) {
         if (lastError instanceof RuntimeException) {
            throw (RuntimeException)lastError;
         } else {
            throw new TerminatedRetryException("Abort the retry due to error", lastError);
         }
      }
   }

   public int getRetryCount() {
      return this.contextInfo.get() != null ? DataTypeUtils.toInt(((RetryContext)this.contextInfo.get()).getAttribute("retryCount")) : 0;
   }

   public static <T> Retryer<T> of(RetryPolicy retryPolicy, BackOffPolicy backOffPolicy, Predicate<T> resultPredicate) {
      return of(retryPolicy, backOffPolicy, resultPredicate, (RetryListener)null);
   }

   public static <T> Retryer<T> of(RetryPolicy retryPolicy, BackOffPolicy backOffPolicy, Predicate<T> resultPredicate, RetryListener listener) {
      Retryer<T> retryer = new Retryer();
      RetryTemplate template = new RetryTemplate();
      template.setRetryPolicy(retryPolicy);
      template.setBackOffPolicy(backOffPolicy);
      if (listener != null) {
         template.registerListener(listener);
      }

      retryer.setTemplate(template);
      retryer.setResultPredicate(resultPredicate);
      return retryer;
   }
}
