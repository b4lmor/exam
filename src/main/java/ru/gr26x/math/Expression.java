package ru.gr26x.math;

import net.objecthunter.exp4j.ExpressionBuilder;

public class Expression {

    /**
     * Метод для вычисления математического выражения
     * @param expression выражение, значение которого следует вычислить
     * @return результат вычисления выражения
     * @throws IllegalArgumentException Исключение выбрасывается в случае некорректной записи математического выражения
     */
    public static double evaluate(String expression) {
        try {
            // Создаем объект Expression с помощью ExpressionBuilder
            var exp = new ExpressionBuilder(expression).build();
            // Вычисляем выражение и возвращаем результат
            return exp.evaluate();
        } catch (IllegalArgumentException e) {
            // В случае ошибки выбрасываем исключение
            throw new IllegalArgumentException("Неверное математическое выражение: \n\"" + expression + "\"", e);
        }
    }
}
