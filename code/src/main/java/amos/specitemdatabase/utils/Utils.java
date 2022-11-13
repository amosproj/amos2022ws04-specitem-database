package amos.specitemdatabase.utils;

import java.util.Random;

public class Utils {

    private Utils() {
        throw new UnsupportedOperationException("This should not be instantiated");
    }

    private static class RandomEnumOfType<E extends Enum<E>> {

        private static final Random RANDOM = new Random();
        private final E[] values;

        public RandomEnumOfType(Class<E> enumType) {
            this.values = enumType.getEnumConstants();
        }

        public E getRandomEnum() {
            return this.values[RANDOM.nextInt(this.values.length)];
        }
    }
}
