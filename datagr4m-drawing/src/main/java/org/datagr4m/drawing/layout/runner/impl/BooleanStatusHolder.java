package org.datagr4m.drawing.layout.runner.impl;

import java.util.concurrent.Callable;

public class BooleanStatusHolder {
    public boolean status = false;
    
    public Callable<Boolean> getStatusTrueVerifier() {
        return new Callable<Boolean>() {
            @Override
			public Boolean call() throws Exception {
                return BooleanStatusHolder.this.status == true;
            }
        };
    }
}
