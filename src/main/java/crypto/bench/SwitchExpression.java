package crypto.bench;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.VerboseMode;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Thread)
@Fork(3)
@Warmup(iterations = 5)
@Measurement(iterations = 5)
public class SwitchExpression {
    private int month;
    private int year;

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
                .include(SwitchExpression.class.getSimpleName())
                .forks(3)
                .warmupIterations(5)
                .measurementIterations(5)
                .build();

        new Runner(options).run();
    }

    @Setup(Level.Trial)
    public void setup() {
        Calendar today = Calendar.getInstance();
        this.month = today.get(Calendar.MONTH);
        this.year = today.get(Calendar.YEAR);
    }

    @Benchmark
    public int useIfElseExpression() {
        if (month == Calendar.JANUARY ||
            month == Calendar.MARCH ||
            month == Calendar.MAY ||
            month == Calendar.JULY ||
            month == Calendar.AUGUST ||
            month == Calendar.OCTOBER ||
            month == Calendar.DECEMBER) {
            return 31;
        } else if (month == Calendar.APRIL ||
            month == Calendar.JUNE ||
            month == Calendar.SEPTEMBER ||
            month == Calendar.NOVEMBER) {
            return 30;
        } else if (month == Calendar.FEBRUARY) {
            if (((year % 4 == 0) && !(year % 100 == 0))
                    || (year % 400 == 0)) {
                return 29;
            } else {
                return 28;
            }
        }

        throw new RuntimeException("Calendar in JDK does not work");
    }

    @Benchmark
    public int useSwitchClause() {
        switch (month) {
            case Calendar.JANUARY:
            case Calendar.MARCH:
            case Calendar.MAY:
            case Calendar.JULY:
            case Calendar.AUGUST:
            case Calendar.OCTOBER:
            case Calendar.DECEMBER:
                return 31;
            case Calendar.APRIL:
            case Calendar.JUNE:
            case Calendar.SEPTEMBER:
            case Calendar.NOVEMBER:
                return 30;
            case Calendar.FEBRUARY:
                if (((year % 4 == 0) && !(year % 100 == 0))
                        || (year % 400 == 0)) {
                    return 29;
                } else {
                    return 28;
                }
            default:
                throw new RuntimeException("Calendar in JDK does not work");
        }
    }

    @Benchmark
    public int useSwitchExpression() {
        return switch (month) {
            case Calendar.JANUARY,
                    Calendar.MARCH,
                    Calendar.MAY,
                    Calendar.JULY,
                    Calendar.AUGUST,
                    Calendar.OCTOBER,
                    Calendar.DECEMBER -> 31;
            case Calendar.APRIL,
                    Calendar.JUNE,
                    Calendar.SEPTEMBER,
                    Calendar.NOVEMBER -> 30;
            case Calendar.FEBRUARY -> {
                if (((year % 4 == 0) && !(year % 100 == 0))
                        || (year % 400 == 0)) {
                    yield 29;
                } else {
                    yield 28;
                }
            }
            default -> throw new RuntimeException(
                    "Calendar in JDK does not work");
        };
    }
}
