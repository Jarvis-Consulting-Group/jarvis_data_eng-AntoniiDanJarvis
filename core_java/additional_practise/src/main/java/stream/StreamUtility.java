package stream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class StreamUtility implements StreamPractise {

    @Override
    public Stream<String> createStrStream(String... strings) {
        return Arrays.stream(strings);
    }
    @Override
    public Stream<String> toUpperCase(String... strings) {
       return createStrStream(strings)
                .map(String::toUpperCase);
    }
    @Override
    public Stream<String> filter(Stream<String> stringStream, String pattern) {
        return stringStream.filter(s -> s.matches(pattern));
    }
    @Override
    public IntStream createIntStream(int[] arr) {
        return IntStream.of(arr);
    }
    @Override
    public <E> List<E> toList(Stream<E> stream) {
        return stream.collect(Collectors.toList());
    }
    @Override
    public List<Integer> toList(IntStream intStream) {
        return intStream.collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
    @Override
    public IntStream createIntStream(int start, int end) {
        return IntStream.range(start, end + 1);
    }
    @Override
    public DoubleStream squareRootIntStream(IntStream intStream) {
        return intStream.asDoubleStream()
                .map(Math::sqrt);
    }
    @Override
    public IntStream getOdd(IntStream intStream) {
        return intStream.filter(value -> value % 2 != 0);
    }
    @Override
    public Consumer<String> getLambdaPrinter(String prefix, String suffix) {
        return (val) -> {
            System.out.println(prefix + val + suffix);
        };
    }
    @Override
    public void printMessages(String[] messages, Consumer<String> printer) {
        Arrays.stream(messages)
                .forEach(printer);
    }
    @Override
    public void printOdd(Consumer<String> printer) {
        createIntStream(1, 7)
                .filter(val -> val % 2 != 0)
                .mapToObj(String::valueOf)
                .forEach(printer);
    }
    @Override
    public Stream<Integer> flatNestedInt(Stream<List<Integer>> ints) {
        return ints.flatMap(Collection::parallelStream)
                .map(val -> val * val);
    }
}