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
public class SwitchBench {
    private static final String[] ops =
            new String[] {"SHA-1", "SHA-256", "SHA-384", "SHA-521"};

    @Param({"SHA-1", "SHA-256", "SHA-384", "SHA-521"})
    private String op;

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
                .include(SwitchBench.class.getSimpleName())
                .forks(3)
                .warmupIterations(5)
                .measurementIterations(5)
                .build();

        new Runner(options).run();
    }

    @Benchmark
    public String useIfElseExpression() {
        if (op.equals("SHA-1")) {
            return ops[0];
        } else if (op.equals("SHA-256")) {
            return ops[1];
        } else if (op.equals("SHA-384")) {
            return ops[2];
        } else if (op.equals("SHA-512")) {
            return ops[3];
        } else {
            return "";
        }
    }

    @Benchmark
    public String useSwitchExpression() {
        return switch (op) {
            case "SHA-1" -> ops[0];
            case "SHA-256" -> ops[1];
            case "SHA-384" -> ops[2];
            case "SHA-512" -> ops[3];
            default -> "";
        };
    }
}
