package com.concurrency.futuretask;

public class Starter {
    public static void main(String[] args) throws DataLoadException, InterruptedException {
        ProductPreLoader productPreLoader = new ProductPreLoader();
        productPreLoader.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Product product = productPreLoader.getProduct();

        System.out.println(product.getName());
    }
}
