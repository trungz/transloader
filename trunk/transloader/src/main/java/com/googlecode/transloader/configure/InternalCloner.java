package com.googlecode.transloader.configure;

import com.googlecode.transloader.clone.reflect.internal.DefaultCloner;
import com.googlecode.transloader.clone.reflect.decide.MaximalCloningDecisionStrategy;
import com.googlecode.transloader.clone.reflect.decide.MinimalCloningDecisionStrategy;

/**
 * @author jeremywales
 */
public class InternalCloner {
    public static final com.googlecode.transloader.clone.reflect.internal.InternalCloner MINIMAL =
            new DefaultCloner(
                    new MinimalCloningDecisionStrategy(),
                    CloneInstantiater.DEFAULT,
                    ReferenceReflecter.DEFAULT
            );

    public static final com.googlecode.transloader.clone.reflect.internal.InternalCloner MAXIMAL =
            new DefaultCloner(
                    new MaximalCloningDecisionStrategy(),
                    CloneInstantiater.DEFAULT,
                    ReferenceReflecter.DEFAULT
            );

    private InternalCloner() {}
}
