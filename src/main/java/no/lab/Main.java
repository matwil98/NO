package no.lab;

import java.io.*;
import java.util.*;
import java.util.List;
import java.util.function.Function;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Main {
    private static String testContent;
    private static List<String> testOperators = Arrays.asList("int", "unsigned", "float", "long", "if", "else", "do",
                                                                "while", "for", "return", "break", "char", "bool");
    private static String filepath = "PrzykładoweProgramy/Program 2.c";
    public static void main(String[] args) {
        Map<String, Long> operators = mapOperators();
        Map<String, Long> operands = operands();
        showMapElements(operators, "Operator");
        showMapElements(operands, "Operand");
        System.out.println();
        halstedMeasure(operators, operands);

    }

    public static String readCode(String filepath) {
        StringBuilder output = new StringBuilder();
        String cleanedCode = "";
        try {
            File file = new File(filepath);
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String out;
            while ((out = bufferedReader.readLine()) != null) {
                output.append(out).append("\n");
            }

            //czyszczenie z komentarzy oznaczonych //
            Pattern pattern = Pattern.compile("\\/\\/.+");
            Matcher matcher = pattern.matcher(output);
            cleanedCode = matcher.replaceAll("");
            //czyszczenie z plików nagłówkowych
            pattern = Pattern.compile("\\#+.+");
            matcher = pattern.matcher(cleanedCode);
            cleanedCode = matcher.replaceAll("");
            bufferedReader.close();
            FileWriter fileWriter = new FileWriter("kod.txt");
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.print(cleanedCode);
            printWriter.close();
        } catch (FileNotFoundException exception) {
            System.out.println("Plik o zadanej ścieżce nie istnieje");
            System.out.println(exception.getMessage());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return cleanedCode;
    }

    public static void showMapElements(Map<String, Long> map, String type) {
        System.out.println("Wykaz ilościowy " + type + "ów w programie");
        for (var entry : map.entrySet()) {
            System.out.println(type + ": " + entry.getKey() + " Liczba wystąpień: " + entry.getValue());
        }
        System.out.println("--------------------------------------------------------------------------------------");
    }

    public static Map<String, Long> operands() {
        Map<String, Long> operandsMap = new HashMap<>();
        Pattern pattern = Pattern.compile("printf\".+\"");
        Matcher matcher = pattern.matcher(testContent);
        operandsMap.putAll(matcher.results().map(MatchResult::group).collect(Collectors.groupingBy(Function.identity(), Collectors.counting())));
        testContent = matcher.replaceAll("");
        //zliczenie wskaźników
        pattern = Pattern.compile("\\*\\w+");
        matcher = pattern.matcher(testContent);
        operandsMap.putAll(matcher.results().map(MatchResult::group).collect(Collectors.groupingBy(Function.identity(), Collectors.counting())));
        testContent = matcher.replaceAll("");
        pattern = Pattern.compile("\\w+");
        matcher = pattern.matcher(testContent);
        operandsMap.putAll(matcher.results().map(MatchResult::group).filter(o -> !testOperators.contains(o)).collect(Collectors.groupingBy(Function.identity(), Collectors.counting())));
        testContent = matcher.replaceAll("");
        pattern = Pattern.compile("\"\\%\"");
        matcher = pattern.matcher(testContent);
        operandsMap.putAll(matcher.results().map(MatchResult::group).collect(Collectors.groupingBy(Function.identity(), Collectors.counting())));
        testContent = matcher.replaceAll("");
        return operandsMap;
    }

    public static Map<String, Long> mapOperators() {
        Map<String, Long> codeOperators = new HashMap<>();
        testContent = readCode(filepath);
        //usuniecie komentarzy pisanych w nastepujacy sposób /* komentarz /*
        Pattern pattern = Pattern.compile("\\/\\*\\s.+\\s\\*\\/");
        Matcher matcher = pattern.matcher(testContent);
        testContent = matcher.replaceAll("");
        //zliczenie średników
        pattern = Pattern.compile("\\;");
        matcher = pattern.matcher(testContent);
        long numOfOccurences = matcher.results().count();
        codeOperators.put(pattern.pattern().replace("\\", ""), numOfOccurences);
        testContent = matcher.replaceAll("");
        //zliczenie przecników
        pattern = Pattern.compile("\\,");
        matcher = pattern.matcher(testContent);
        numOfOccurences = matcher.results().count();
        codeOperators.put(pattern.pattern().replace("\\", ""), numOfOccurences);
        testContent = matcher.replaceAll("");
        //zliczenie plusów i podwójnych plusów
        pattern = Pattern.compile("\\+\\+|\\+");
        matcher = pattern.matcher(testContent);
        codeOperators.putAll(matcher.results().map(MatchResult::group).collect(Collectors.groupingBy(Function.identity(), Collectors.counting())));
        testContent = matcher.replaceAll("");
        //zliczenie minusów i podwójnych minusów
        pattern = Pattern.compile("\\-\\-|\\-");
        matcher = pattern.matcher(testContent);
        codeOperators.putAll(matcher.results().map(MatchResult::group).collect(Collectors.groupingBy(Function.identity(), Collectors.counting())));
        testContent = matcher.replaceAll("");
        //zliczenie znaków = i ==
        pattern = Pattern.compile("\\=\\=|\\=");
        matcher = pattern.matcher(testContent);
        codeOperators.putAll(matcher.results().map(MatchResult::group).collect(Collectors.groupingBy(Function.identity(), Collectors.counting())));
        testContent = matcher.replaceAll("");
        //zliczenie { i }
        pattern = Pattern.compile("\\{|\\}");
        matcher = pattern.matcher(testContent);
        codeOperators.putAll(matcher.results().map(MatchResult::group).collect(Collectors.groupingBy(Function.identity(), Collectors.counting())));
        testContent = matcher.replaceAll("");
        //zliczenie ( i ), ||, &, &&
        pattern = Pattern.compile("\\&\\&|\\&|\\(|\\)|\\|\\|");
        matcher = pattern.matcher(testContent);
        codeOperators.putAll(matcher.results().map(MatchResult::group).collect(Collectors.groupingBy(Function.identity(), Collectors.counting())));
        testContent = matcher.replaceAll("");
        pattern = Pattern.compile("\\[\\w+\\]");
        matcher = pattern.matcher(testContent);
        codeOperators.putAll(matcher.results().map(MatchResult::group).collect(Collectors.groupingBy(Function.identity(), Collectors.counting())));
        testContent = matcher.replaceAll("");
        // operatory int, float itp.
        pattern = Pattern.compile("[a-z]+");
        matcher = pattern.matcher(testContent);
        codeOperators.putAll(matcher.results().map(MatchResult::group).filter(o -> testOperators.contains(o)).collect(Collectors.groupingBy(Function.identity(), Collectors.counting())));
        return codeOperators;
    }

    public static void halstedMeasure(Map<String, Long> operators, Map<String, Long> operands) {
        System.out.println("-------- WYZNACZANIE MIAR HALSTEDA --------");
        long differentOpertors = operators.keySet().size();
        long differentOperands = operands.keySet().size();
        long numOfOperators = operators.values().stream().reduce(0L, Long::sum);
        long numOfOperands = operands.values().stream().reduce(0L, Long::sum);
        long dictionary = differentOpertors + differentOperands;
        long programLength = numOfOperators + numOfOperands;
        double programVolume = programLength * (Math.log(dictionary) / Math.log(2));
        long programDifficulty = (differentOpertors * numOfOperands) / (2 * differentOperands);
        long effortRequired = (long) (programVolume * programDifficulty);
        double predictNumOfError = Math.pow(effortRequired, 0.66667) / 3000;
        System.out.println("Liczba różnych operatorów: " + differentOpertors);
        System.out.println("Liczba różnych operandów: " + differentOperands);
        System.out.println("Łączna liczba operatorów w programie: " + numOfOperators);
        System.out.println("Łączna liczba operandów w programie: " + numOfOperands);
        System.out.println("Słownik programu: " + dictionary);
        System.out.println("Długość programu: " + programLength);
        System.out.println("Objętość programu: " + programVolume);
        System.out.println("Trudność programu: " + programDifficulty);
        System.out.println("Wymagany nakład pracy: " + effortRequired);
        System.out.println("Przewidywana liczba błędów: " + predictNumOfError);
    }

}
