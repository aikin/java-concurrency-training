package com.concurrency.futuretask;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

class ProductPreLoader {

    private final FutureTask<Product> productLoadedFutureTask = new FutureTask<Product>(new Callable<Product>() {
        @Override
        public Product call() throws Exception {
            return loadProduct();
        }

        private Product loadProduct() {
            return new Product(1, "product");
        }
    });

    private final Thread preLoaderThread = new Thread(productLoadedFutureTask);


    public void start() {
        preLoaderThread.start();
    }

    public Product getProduct() throws InterruptedException, DataLoadException {
        try {
            return productLoadedFutureTask.get();
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            if (cause instanceof DataLoadException) {
                throw (DataLoadException) cause;
            }
            throw launderThrowable(cause);
        }
    }

    private static RuntimeException launderThrowable(Throwable cause) {
        if (cause instanceof RuntimeException) {
            return (RuntimeException) cause;
        }
        if (cause instanceof Error) {
            throw  (Error) cause;
        }
        throw new IllegalStateException("Not unchecked", cause);
    }
}
