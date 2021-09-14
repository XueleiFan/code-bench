package crypto.bench;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.VerboseMode;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Thread)
@Fork(3)
@Warmup(iterations = 5)
@Measurement(iterations = 5)
public class RecordBench {
    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
                .include(RecordBench.class.getSimpleName())
                .forks(3)
                .warmupIterations(5)
                .measurementIterations(5)
                .build();

        new Runner(options).run();
    }

    @Benchmark
    public int useRecord() {
        RecordPoint p = new RecordPoint(10, 20);
        return p.x;
    }

    @Benchmark
    public int useFinal() {
        FinalPoint p = new FinalPoint(10, 20);
        return p.x;
    }

    @Benchmark
    public int useMethod() {
        MethodPoint p = new MethodPoint(10, 20);
        return p.getX();
    }

    private static final class FinalPoint {
        final int x;
        final int y;

        FinalPoint(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private static final class MethodPoint {
        final int x;
        final int y;

        MethodPoint(int x, int y) {
            this.x = x;
            this.y = y;
        }

        int getX() {
            return x;
        }
    }

    private record RecordPoint(int x, int y) {
        // blank
    }
}
