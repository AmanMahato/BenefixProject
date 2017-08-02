package com.interview.test;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * Author: Aman K Mahato
 *This is the main control of the App
 *To run this app, put all the pdf file in Resources folder
 * The output is generated as an Excel file named BenefixResult in root directory
 */

public class App {

    public static void main(String[] args) {
        List<String> fileNames=new App().allFileNames();
        XSSFWorkbook workbook=new XSSFWorkbook();
        XSSFSheet spreadsheet=workbook.createSheet(" Benefix Small Group Plans Upload ");
        String[] headers = new String[] { "start_date","end_date","product_name","states","group_rating_areas","zero_eighteen","nineteen_twenty","twenty_one","twenty_two","twenty_three","twenty_four","twenty_five","twenty_six","twenty_seven","twenty_eight","twenty_nine","thirty","thirty_one","thirty_two","thirty_three","thirty_four","thirty_five","thirty_six","thirty_seven","thirty_eight"
                ,"thirty_nine","forty","forty_one","forty_two","forty_three","forty_four","forty_five","forty_six","forty_seven","forty_eight","forty_nine","fifty","fifty_one","fifty_two","fifty_three","fifty_four",	"fifty_five","fifty_six","fifty_seven","fifty_eight","fifty_nine","sixty","sixty_one","sixty_two","sixty_three","sixty_four","sixty_five_plus" };
        int rowid=1;
        XSSFRow row;
        PdfReader reader = null;
        int numOfPage = 0;
        Integer startingRow = 2;
        for(String fileName:fileNames) {
            try {
                reader = new PdfReader("src/Resources/" +fileName);
                numOfPage = reader.getNumberOfPages();

            } catch (IOException e) {
                e.printStackTrace();
            }
            for (int p = 1; p <= numOfPage; p++) {
                List<String> pageIndividualLines = new ArrayList<String>();
                String page = null;
                try {
                    page = PdfTextExtractor.getTextFromPage(reader, p);//check here
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Scanner scan = new Scanner(page);
                while (scan.hasNextLine()) {
                    pageIndividualLines.add(scan.nextLine());
                }
                int pageLineSize = pageIndividualLines.size();
                String[] dates = pageIndividualLines.get(0).split(" ");
                String startDate = dates[4];
                String endDate = dates[6];
                String planName = HelperClass.getPlanName(pageIndividualLines.get(3));
                String stateAbbreviated = planName.substring(0, 2);
                String groupRatingAreas = HelperClass.getNumbers(pageIndividualLines.get(2));
                TreeMap<String, Object[]> treeMap = new TreeMap<String, Object[]>();
                TreeMap<Integer, String> tempMap = new TreeMap<Integer, String>();
                List<String> objList = new ArrayList<String>();
                objList.add(startDate);
                objList.add(endDate);
                objList.add(planName);
                objList.add(stateAbbreviated);
                objList.add(groupRatingAreas);
                for (int i = 5; i < pageLineSize - 4; i++) {//remove last 4 lines from each page
                    try {
                        String currentLine = pageIndividualLines.get(i);
                        String[] ageAndRate = currentLine.split(" ");
                        tempMap.put(Integer.valueOf(HelperClass.checkValid(ageAndRate[0])), ageAndRate[1]);
                        tempMap.put(Integer.valueOf(HelperClass.checkValid(ageAndRate[2])), ageAndRate[3]);
                        tempMap.put(Integer.valueOf(HelperClass.checkValid(ageAndRate[4])), ageAndRate[5]);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                for (Map.Entry<Integer, String> map : tempMap.entrySet()) {
                    objList.add(map.getValue());
                }
                Object[] object = objList.toArray(new Object[objList.size()]);
                treeMap.put(startingRow.toString(), object);
                startingRow++;
                for (Map.Entry<String, Object[]> entry : treeMap.entrySet()) {
                    String key = entry.getKey();
                }
                try {
                    Set<String> keyid = treeMap.keySet();
                    int headerIndex=0;
                    for (String key : keyid) {
                        while(headerIndex<headers.length){
                            row = spreadsheet.createRow(0);
                            Cell cell = row.createCell(headerIndex);
                            cell.setCellValue((String) headers[headerIndex++]);
                        }
                        int cellid = 0;
                        row = spreadsheet.createRow(rowid++);
                        Object[] objectArr = treeMap.get(key);
                        for (Object obj : objectArr) {
                            Cell cell = row.createCell(cellid++);
                            cell.setCellValue((String) obj);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        try{
            FileOutputStream outputStream = new FileOutputStream("BenefixResult.xlsx");
            workbook.write(outputStream);
        }catch (Exception e){
            e.printStackTrace();

        }
    }

    public List<String> allFileNames(){
        List<String> results = new ArrayList<String>();
        File[] files = new File("src/Resources").listFiles();
        for (File file : files) {
            if (file.isFile()) {
                results.add(file.getName());
            }
        }
        return results;
    }
}