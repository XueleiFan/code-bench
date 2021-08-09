package crypto.bench;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.VerboseMode;

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
    }

    @Benchmark
    public void useErrorCode() {
    }
}
