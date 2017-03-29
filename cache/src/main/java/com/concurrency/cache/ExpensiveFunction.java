package com.concurrency.cache;

import java.math.BigInteger;

class ExpensiveFunction implements Computable<String, BigInteger>  {

    @Override
    public BigInteger compute(String key) {
        return new BigInteger(key);
    }
}
