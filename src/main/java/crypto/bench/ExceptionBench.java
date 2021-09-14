package crypto.bench;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Thread)
@Fork(3)
@Warmup(iterations = 5)
@Measurement(iterations = 5)
public class ExceptionBench {

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
                .include(ExceptionBench.class.getSimpleName())
                .forks(3)
                .warmupIterations(5)
                .measurementIterations(5)
                .build();

        new Runner(options).run();
    }

    @Benchmark
    public void useUncheckedException() {
        try {
            SampleUseUncheckedException.find("Sha256");
        } catch (RuntimeException re) {
            // blank line, ignore the exception.
        }
    }

    @Benchmark
    public void useReturnedException() {
        if (SampleReturnedException.find("Sha256") != null) {
            // blank line, ignore the failure.
        }
    }

    @Benchmark
    public void useCheckedException() {
        try {
            SampleUseCheckedException.find("Sha256");
        } catch (NoSuchAlgorithmException nsae) {
            // blank line, ignore the exception.
        }
    }

    @Benchmark
    public void useTwoStackException() {
        try {
            SampleUseTwoStacks.find("Sha256");
        } catch (RuntimeException re) {
            // blank line, ignore the exception.
        }
    }

    @Benchmark
    public void useThreeStackException() {
        try {
            SampleUseThreeStacks.find("Sha256");
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

    @Benchmark
    public void useOptional() {
        if (!SampleUseOptional.find("Sha256").isPresent()) {
            // blank line, ignore the failure.
        }
    }

    private static class SampleUseUncheckedException {
        private static void find(String algorithm) {
            throw new RuntimeException("No such algorithm: " + algorithm);
        }
    }

    private static class SampleUseCheckedException {
        private static void find(String algorithm) throws NoSuchAlgorithmException {
            throw new NoSuchAlgorithmException("No such algorithm: " + algorithm);
        }
    }

    private static class SampleUseErrorCode {
        private static int find(String algorithm) {
            return -1;
        }
    }

    private static class SampleReturnedException {
        private static Exception find(String algorithm) {
            return new NoSuchAlgorithmException("No such algorithm: " + algorithm);
        }
    }

    private static class SampleUseNullPointer {
        private static SampleUseNullPointer find(String algorithm) {
            return null;
        }
    }

    private static class SampleUseOptional {
        private static Optional<SampleUseOptional> find(String algorithm) {
            return Optional.empty();
        }
    }

    private static class SampleUseTwoStacks {
        private static void find(String algorithm) {
            SampleUseUncheckedException.find(algorithm);
        }
    }

    private static class SampleUseThreeStacks {
        private static void find(String algorithm) {
            SampleUseTwoStacks.find(algorithm);
        }
    }
}
