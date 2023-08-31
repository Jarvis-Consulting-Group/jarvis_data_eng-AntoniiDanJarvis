package stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class StreamTest {
    StreamUtility streamUtility;

    @BeforeEach
    public void initializeStreamUtility() {
        streamUtility = new StreamUtility();
    }

    @Test
    public void shouldCreateStringStreamFromArray() {
        String[] strings = {"test1, test2"};
        assertArrayEquals(streamUtility.createStrStream(strings).toArray(), strings);
    }

    @Test
    public void shouldConvertAllStringsToUppercase() {
        String[] strings = {"test1, test2"};
        assertLinesMatch(streamUtility.toUpperCase(strings).collect(Collectors.toList()),
                Stream.of(strings)
                        .map(String::toUpperCase).collect(Collectors.toList()));
    }

    @Test
    public void shouldFilterStringsContainsThePattern() {
        String[] strings = {"test", "test2", "asdw"};
        String[] matchedStrings = {"test"};
        assertLinesMatch(streamUtility.filter(Stream.of(strings), "test").collect(Collectors.toList()),
                Stream.of(matchedStrings).collect(Collectors.toList()));
    }

    @Test
    public void shouldCreateIntStreamFromArray() {
        int[] array = {1, 3, 4};
        assertArrayEquals(streamUtility.createIntStream(array).toArray(), array);
    }

    @Test
    public void shouldConvertStreamToList() {
        String[] strings = {"test1, test2"};
        assertArrayEquals(streamUtility.toList(Stream.of(strings)).toArray(), strings);
    }

    @Test
    public void ConvertIntStreamToList() {
        int[] array = {1, 3, 4};
       assertTrue(streamUtility.toList(Stream.of(array)).contains(array));
    }

    @Test
    public void shouldCreateIntStreamRangeFromStartToEndInclusive() {
        int[] array = {1, 2, 3, 4, 5};
        assertArrayEquals(streamUtility.createIntStream(1, 5).toArray(), array);
    }

    @Test
    public void shouldConvertIntStreamToDoubleStream() {
        int[] array = {1, 2, 3};
        double[] expectedArray = {Math.sqrt(1.0), Math.sqrt(2.0), Math.sqrt(3.0)};
        assertArrayEquals(streamUtility.squareRootIntStream(Arrays.stream(array)).toArray(), expectedArray);
    }

    @Test
    public void shouldFilterAllEvenNumberAndReturnOddNumbersFromIntStream() {
        int[] array = {1, 2, 3, 4, 5};
        int[] expectedArray = {1, 3, 5};
        assertArrayEquals(streamUtility.getOdd(Arrays.stream(array)).toArray(), expectedArray);
    }

    @Test
    public void shouldReturnLambdaThatPrintMessageWithPrefixSuffix() {
        streamUtility.getLambdaPrinter("aaa", "ccc").accept("bbb");
    }

    @Test
    public void shouldPrintEachMessageWithGivenPrinter() {
        streamUtility.printMessages(new String[]{"test1, test2"}, streamUtility.getLambdaPrinter("a", "b"));
    }

    @Test
    public void shouldPrintAllOddNumberFromIntStream() {
        streamUtility.printOdd(streamUtility.getLambdaPrinter("a", "b"));
    }

    @Test
    public void shouldSquareEachNumberFromInput() {
        List<Integer> list = List.of(1, 2, 3);
        List<Integer> expectedList = List.of(1, 4, 9);

        assertEquals(streamUtility.flatNestedInt(Stream.of(list)).collect(Collectors.toList()), expectedList);
    }
}
