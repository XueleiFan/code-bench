package crypto.bench;

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
public class PatternBench {
    private static final Shape shape = new Shape.Circle(10);

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
                .include(PatternBench.class.getSimpleName())
                .forks(3)
                .warmupIterations(5)
                .measurementIterations(5)
                .build();

        new Runner(options).run();
    }

    @Benchmark
    public Shape.Circle usePattern() {
        if (shape instanceof Shape.Circle circle) {
            return circle;
        }

        return null;
    }

    @Benchmark
    public Shape.Circle useCast() {
        if (shape instanceof Shape.Circle) {
            return (Shape.Circle)shape;
        }

        return null;
    }

    sealed interface Shape permits Shape.Circle, Shape.Square {
        record Circle(double radius) implements Shape {
            // blank
        }

        record Square(double side) implements Shape {
            // blank
        }
    }
}
