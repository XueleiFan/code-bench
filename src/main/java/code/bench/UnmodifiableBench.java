package code.bench;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Thread)
@Fork(3)
@Warmup(iterations = 5)
@Measurement(iterations = 5)
public class UnmodifiableBench {
    private static final String[] hellos = new String[] {
            "hello", "hola", "halo", "hai", "helo", "hey", "hei"};
    private static final List<String> helloList = List.of(hellos);

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
                .include(UnmodifiableBench.class.getSimpleName())
                .forks(3)
                .warmupIterations(5)
                .measurementIterations(5)
                .build();

        new Runner(options).run();
    }

    @Benchmark
    public void useCollections() {
        List<String> copy = helloList;
        for (int i = 0; i < 1000; i++) {
            copy = Collections.unmodifiableList(copy);
        }
    }

    @Benchmark
    public void useListOf() {
        List<String> copy = helloList;
        for (int i = 0; i < 1000; i++) {
            copy = List.of(hellos);
        }
    }
}
