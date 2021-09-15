package code.bench;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Thread)
@Fork(3)
@Warmup(iterations = 5)
@Measurement(iterations = 5)
public class FailedSha256Bench {
    private static final byte[] message = "Hello, world!".getBytes();
    private static final MessageDigest sha256;

    static {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (Exception ex) {
            md = null;
        }

        sha256 = md;
    }

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
                .include(FailedSha256Bench.class.getSimpleName())
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

    private static byte[] digest() {
        return sha256.digest(message);
    }

    private static class SampleUseUncheckedException {
        private static void find(String algorithm) {
            digest();
            throw new RuntimeException("No such algorithm: " + algorithm);
        }
    }

    private static class SampleUseCheckedException {
        private static void find(String algorithm) throws NoSuchAlgorithmException {
            digest();
            throw new NoSuchAlgorithmException("No such algorithm: " + algorithm);
        }
    }

    private static class SampleUseErrorCode {
        private static int find(String algorithm) {
            digest();
            return -1;
        }
    }

    private static class SampleReturnedException {
        private static Exception find(String algorithm) {
            digest();
            return new NoSuchAlgorithmException("No such algorithm: " + algorithm);
        }
    }

    private static class SampleUseNullPointer {
        private static SampleUseNullPointer find(String algorithm) {
            digest();
            return null;
        }
    }

    private static class SampleUseOptional {
        private static Optional<SampleUseOptional> find(String algorithm) {
            digest();
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
