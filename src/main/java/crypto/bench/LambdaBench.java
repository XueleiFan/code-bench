package crypto.bench;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Thread)
@Fork(3)
@Warmup(iterations = 5)
@Measurement(iterations = 5)
public class LambdaBench {

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
                .include(LambdaBench.class.getSimpleName())
                .forks(3)
                .warmupIterations(5)
                .measurementIterations(5)
                .build();

        new Runner(options).run();
    }

    @Benchmark
    public void useLambdaExpression() {
        Consumer<Object> consumer = obj -> Objects.isNull(obj);
        consumer.accept("Hello, World!");
    }

    @Benchmark
    public void useMethodReference() {
        Consumer<Object> consumer = Objects::isNull;
        consumer.accept("Hello, World!");
    }
}
