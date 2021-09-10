package crypto.bench;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.VerboseMode;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Thread)
@Fork(3)
@Warmup(iterations = 5)
@Measurement(iterations = 5)
public class LockBench {
    private static NoLockCircle noLock = new NoLockCircle(10.00);
    private static LockedCircle locked = new LockedCircle(10.00);


    public static void main(String[] args) throws RunnerException {
        System.setProperty("io.netty.handler.ssl.noOpenSsl", "true");
        Options options = new OptionsBuilder()
                .verbosity(VerboseMode.NORMAL)
                .include(".*" + LockBench.class.getSimpleName() + ".*")
                .build();

        new Runner(options).run();
    }

    @Benchmark
    public double useNoLockMethod() {
        return noLock.getRadius();
    }

    @Benchmark
    public double useLockedMethod() {
        return locked.getRadius();
    }

    private static class NoLockCircle {
        final double radius;

        NoLockCircle(double radius) {
            this.radius = radius;
        }

        double getRadius() {
            return radius;
        }
    }

    private static class LockedCircle {
        final double radius;

        LockedCircle(double radius) {
            this.radius = radius;
        }

        synchronized double getRadius() {
            return radius;
        }
    }
}
