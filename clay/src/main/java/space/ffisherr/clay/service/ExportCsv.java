package space.ffisherr.clay.service;

import space.ffisherr.clay.entity.History;
import java.io.FileWriter;
import au.com.bytecode.opencsv.CSVWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//public class ExportCsv {
//
//    private static final String CSV_FILE_NAME = "history_csv";
//
//
//    public String convertToCSV(List<History> data) {
//        return Stream.of(data)
//                .map(this::escapeSpecialCharacters)
//                .collect(Collectors.joining(","));
//    }
//
//    public String escapeSpecialCharacters(String data) {
//        String escapedData = data.replaceAll("\\R", " ");
//        if (data.contains(",") || data.contains("\"") || data.contains("'")) {
//            data = data.replace("\"", "\"\"");
//            escapedData = "\"" + data + "\"";
//        }
//        return escapedData;
//    }
//
//
//
//    public void dataArray(List<History> histories) throws IOException {
//        File csvOutputFile = new File(CSV_FILE_NAME);
//        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
//            histories.stream()
//                    .map(this::convertToCSV)
//                    .forEach(pw::println);
//        }
//        assertTrue(csvOutputFile.exists());
//    }
//
//}

public class ExportCsv {
    public static void main(List<History> data) throws Exception
    {
        String csv = "data.csv";
        CSVWriter writer = new CSVWriter(new FileWriter(csv));
        //Create record
        final StringBuilder builder = new StringBuilder();
        data.forEach(history -> {
            builder.append(history.getCreatedAt()).append(",")
                    .append(history.getDirection()).append(",")
                    .append(history.getInstrument().getName()).append(",")
                    .append(history.getOneItemCost()).append(",")
                    .append(history.getPurchasedNumber()).append(",")
                    .append(history.getTotalAmount()).append(",")
                    .append(history.getLeftAmount()).append("\n");
        });
        //Write the record to file
        Arrays.stream(builder.toString().split("\n")).forEach(writer::writeNext);
        //close the writer
        writer.close();
    }

}