package com.googlecode.transloader.clone;

public interface CloningStrategy {
	static final CloningStrategy MINIMAL =
			new NewObjectAndContentCloningStrategy(new MinimalCloningDecisionStrategy(),
					new ObjenesisInstantiationStrategy(), new SerializationCloningStrategy());
	static final CloningStrategy MAXIMAL =
			new NewObjectAndContentCloningStrategy(new MaximalCloningDecisionStrategy(),
					new ObjenesisInstantiationStrategy(), new SerializationCloningStrategy());

	Object cloneObjectToClassLoader(Object original, ClassLoader targetClassLoader) throws Exception;
}
