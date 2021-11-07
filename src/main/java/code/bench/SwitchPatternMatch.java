package code.bench;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Thread)
@Fork(3)
@Warmup(iterations = 5)
@Measurement(iterations = 5)
public class SwitchPatternMatch {
    private static final Shape shape = new Shape.Rectangle(10, 10);

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
                .include(SwitchPatternMatch.class.getSimpleName())
                .forks(3)
                .warmupIterations(5)
                .measurementIterations(5)
                .build();

        new Runner(options).run();
    }

    @Benchmark
    public Shape.Circle useIfElsePattern() {
        if (shape instanceof Shape.Square square) {
           return null;
        } else if (shape instanceof Shape.Circle circle) {
            return circle;
        }

        return null;
    }

    @Benchmark
    public Shape.Circle useIfElseCast() {
        if (shape instanceof Shape.Square square) {
            return null;
        } else if (shape instanceof Shape.Circle) {
            return (Shape.Circle)shape;
        }

        return null;
    }

    @Benchmark
    public Shape.Circle useSwitchPattern() {
        return switch (shape) {
            case Shape.Circle circle -> circle;
            case Shape.Square square -> null;
            case Shape.Rectangle rect -> null;
        };
    }

    sealed interface Shape permits Shape.Circle, Shape.Square, Shape.Rectangle {
        record Circle(double radius) implements Shape {
            // blank
        }

        record Square(double side) implements Shape {
            // blank
        }

        record Rectangle(double length, double width) implements Shape {
            // blank
        }
    }
}
