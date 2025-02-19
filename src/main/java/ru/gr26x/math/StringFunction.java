package ru.gr26x.math;

import java.util.regex.Pattern;

public class StringFunction implements Function{

    private String function = null;
    private String independentVariable = "x";

    public String getFunction() {
        return function;
    }

    private void setFunction(String function){
        this.function = function;
        if (function != null) {
            try {
                var f = setArgument(0);
                Expression.evaluate(f);
            } catch (IllegalArgumentException e) {
                throw e;
            } catch (Exception ignored) {
            }
        }
    }

    public String getIndependentVariable(){
        return independentVariable;
    }

    private void setIndependentVariable(String variableName){
        this.independentVariable = variableName;
        if (function != null && !function.isBlank())
            setFunction(this.function);
    }

    public StringFunction(String function, String independentVariableName) throws IllegalArgumentException {
        setIndependentVariable(independentVariableName);
        setFunction(function);
    }

    private String setArgument(double i) {
        if (function != null) {
            Pattern p = Pattern.compile("(^|\\b|\\d)" + independentVariable + "($|\\b)");
            var m = p.matcher(function);
            return m.replaceAll("(" + i + ")");
        }
        return null;
    }

    @Override
    public double evaluate(double x) {
        var expr = setArgument(x);
        if (function == null) return 0.0;
        return Expression.evaluate(expr);
    }
}
