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
public class ExceptionBench {

    public static void main(String[] args) throws RunnerException {
        System.setProperty("io.netty.handler.ssl.noOpenSsl", "true");
        Options options = new OptionsBuilder()
                .verbosity(VerboseMode.NORMAL)
                .include(".*" + ExceptionBench.class.getSimpleName() + ".*")
                .build();

        new Runner(options).run();
    }

    @Benchmark
    public void useException() {
        try {
            SampleUseException.find("Sha256");
        } catch (RuntimeException re) {
            // blank line, ignore the exception.
        }
    }

    @Benchmark
    public void useErrorCode() {
        if (SampleUseErrorCode.find("Sha256") != 0) {
            // blank line, ignore the failure.
        }
    }

    @Benchmark
    public void useNullPointer() {
        if (SampleUseNullPointer.find("Sha256") == null) {
            // blank line, ignore the failure.
        }
    }

    private static class SampleUseException {
        private static void find(String algorithm) {
            throw new RuntimeException("No such algorithm: " + algorithm);
        }
    }

    private static class SampleUseErrorCode {
        private static int find(String algorithm) {
            return -1;
        }
    }

    private static class SampleUseNullPointer {
        private static SampleUseNullPointer find(String algorithm) {
            return null;
        }
    }
}
