package Framework;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileInputStream;
import java.util.*;

public class Data {

    protected static Map<String, ArrayList<Map<String, String>>> getTestData()    {

        Map<String, ArrayList<Map<String, String>>> excelData = new HashMap<>();
        try {
            ArrayList<Map<String, String>> tcData = new ArrayList<>();
            Map<String, String> dataMap = null;
            FileInputStream fis = new FileInputStream(Config.getProperty("datasheet"));
            Workbook workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheetAt(0);
            int lastRow = sheet.getLastRowNum();
            String key="", value = "";

            for(int i=1; i<=lastRow; i++) {
                dataMap = new HashMap<>();
                Row headerRow = sheet.getRow(0);
                Row row = sheet.getRow(i);
                int lastColumn = row.getLastCellNum();
                String tcName = row.getCell(0).getStringCellValue().trim();

                for (int j=1;j <= lastColumn;j++)   {
                    Cell headerCell = headerRow.getCell(j);
                    Cell valueCell = row.getCell(j);
                    key = headerCell.getStringCellValue().trim();
                    switch (valueCell.getCellType())    {
                        case BLANK:
                        case _NONE:
                            value= "";
                            break;
                        case STRING:
                        case FORMULA:
                            value = valueCell.getStringCellValue().trim();
                            break;
                        case BOOLEAN:
                            value = Boolean.toString(valueCell.getBooleanCellValue());
                            break;
                        case NUMERIC:
                            value = Double.toString(valueCell.getNumericCellValue());
                            break;
                        default:
                        case ERROR:
                            System.out.println("No data found for column: " + key);
                    }
                    dataMap.put(key, value);
                }
                tcData.add(dataMap);
                excelData.put(tcName, tcData);
                dataMap = null;
            }
        }
        catch (Exception e) {
            e.getMessage();
        }
        finally {
            return excelData;
        }
    }
}
