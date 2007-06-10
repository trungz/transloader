package com.googlecode.transloader.clone;

public interface CloningStrategy {
	CloningStrategy MINIMAL =
			new ReflectionCloningStrategy(new MinimalCloningDecisionStrategy(),
					new ObjenesisInstantiationStrategy(), new SerializationCloningStrategy());
	CloningStrategy MAXIMAL =
			new ReflectionCloningStrategy(new MaximalCloningDecisionStrategy(),
					new ObjenesisInstantiationStrategy(), new SerializationCloningStrategy());

	Object cloneObjectToClassLoader(Object original, ClassLoader targetClassLoader) throws Exception;
}
