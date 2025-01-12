package com.stock.api.util.retry;

import com.google.common.base.Preconditions;
import com.google.common.base.Stopwatch;
import com.stock.api.util.ApiUtils;
import com.stock.api.util.DataTypeUtils;
import com.stock.api.util.Op;
import org.springframework.retry.RetryException;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.backoff.BackOffPolicy;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

public class RetryerBuilder<T> {
   public static final int DEFAULT_FIXED_WAIT_INTERVAL_MILLION_SECONDS = 100;
   public static final int DEFAULT_MAX_ATTEMPTS = 3;
   private RetryPolicy retryPolicy;
   private BackOffPolicy backOffPolicy;
   private Predicate<T> resultPredicate;
   private int maxAttempts = 5;
   private long mostWaitingTime;
   private boolean timeoutSinceCreated;

   public static <T> RetryerBuilder<T> of() {
      return new RetryerBuilder();
   }

   public RetryerBuilder<T> withRetryPolicy(RetryPolicy retryPolicy) {
      this.retryPolicy = retryPolicy;
      return this;
   }

   public RetryerBuilder<T> withWaitStrategy(int maxAttempts, long fixedWaitIntervalMillionSeconds) {
      this.setRetryPolicy(maxAttempts);
      FixedBackOffPolicy policy = new FixedBackOffPolicy();
      policy.setBackOffPeriod(fixedWaitIntervalMillionSeconds);
      this.backOffPolicy = policy;
      return this;
   }

   public RetryerBuilder<T> withWaitStrategy(int maxAttempts, long initialInterval, double multiplier) {
      this.setRetryPolicy(maxAttempts);
      ExponentialBackOffPolicy policy = new ExponentialBackOffPolicy();
      policy.setInitialInterval(initialInterval);
      policy.setMultiplier(multiplier);
      this.backOffPolicy = policy;
      return this;
   }

   public RetryerBuilder<T> withFibonacciWaitStrategy(int maxAttempts, long maxInterval, long initialInterval) {
      Preconditions.checkArgument(maxInterval > initialInterval, "the max interval has to great than initial interval.");
      this.setRetryPolicy(maxAttempts);
      FibonacciBackOffPolicy policy = new FibonacciBackOffPolicy();
      policy.setMaxInterval(maxInterval);
      policy.setInitialInterval(initialInterval);
      this.backOffPolicy = policy;
      return this;
   }

   private void setRetryPolicy(int maxAttempts) {
      this.maxAttempts = maxAttempts;
      if (this.retryPolicy instanceof SimpleRetryPolicy) {
         ((SimpleRetryPolicy)this.retryPolicy).setMaxAttempts(maxAttempts);
      }

      if (this.retryPolicy == null) {
         this.retryPolicy = new SimpleRetryPolicy(maxAttempts, Op.map(RetryException.class, true), true);
      }

   }

   public RetryerBuilder<T> retryIfException(Class<? extends Exception>... exceptionClass) {
      Map<Class<? extends Throwable>, Boolean> registers = Op.map(exceptionClass, (e) -> {
         return e;
      }, (e) -> {
         return true;
      });
      registers.put(RetryException.class, true);
      this.retryPolicy = new SimpleRetryPolicy(this.maxAttempts, registers, true);
      return this;
   }

   public RetryerBuilder<T> retryIfResult(Predicate<T> predicate) {
      this.resultPredicate = predicate;
      return this;
   }

   public RetryerBuilder<T> retryIfResultNotTrue() {
      this.resultPredicate = (result) -> {
         return !DataTypeUtils.toBoolean(result, false);
      };
      return this;
   }

   public RetryerBuilder<T> atMost(long most, TimeUnit timeUnit) {
      this.mostWaitingTime = timeUnit.toMillis(most);
      return this;
   }

   public RetryerBuilder<T> atMostSinceNow(int most, TimeUnit timeUnit) {
      this.timeoutSinceCreated = true;
      this.mostWaitingTime = timeUnit.toMillis((long)most);
      return this;
   }

   public Retryer<T> build() {
      if (this.backOffPolicy == null) {
         this.withWaitStrategy(3, 100L);
      }

      Retryer<T> retryer = Retryer.of(this.retryPolicy, this.backOffPolicy, this.resultPredicate);
      retryer.setMostWaitingTime(this.mostWaitingTime);
      if (this.timeoutSinceCreated) {
         retryer.setTotalStopwatch(Stopwatch.createStarted());
      }

      return retryer;
   }
}
