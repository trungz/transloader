package com.googlecode.transloader.clone;

public interface CloningStrategy {
	static final CloningStrategy MINIMAL =
			new InstantiationPlusFieldsCloningStrategy(new MinimalCloningDecisionStrategy(),
					new ObjenesisInstantiationStrategy());
	static final CloningStrategy MAXIMAL =
			new InstantiationPlusFieldsCloningStrategy(new MaximalCloningDecisionStrategy(),
					new ObjenesisInstantiationStrategy());

	Object cloneObjectToClassLoader(Object original, ClassLoader targetClassLoader) throws Exception;
}
