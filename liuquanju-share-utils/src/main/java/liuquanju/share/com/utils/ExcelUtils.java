package liuquanju.share.com.utils;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.*;

/**
 * @Description:
 * @Author: yingjie.liu
 * @Date: 2020/08/22/17:24
 */
public class ExcelUtils {
    //总行数
    private int totalRows = 0;
    //总列数
    private int totalCells = 0;
    //错误信息
    private String errorInfo;

    public ExcelUtils() {
    }

    //得到总行数
    public int getTotalRows() {
        return totalRows;
    }

    //得到总行数
    public int getTotalCells() {
        return totalCells;
    }

    //得到错误信息
    public String getErrorInfo() {
        return errorInfo;
    }

    /**
     * 检查文件名是否为空或者是否是Excel格式的文件
     *
     * @param filePath
     * @return
     */
    public boolean validateExcel(String filePath) {
        if (filePath == null || !(isExcel2003(filePath) || isExcel2007(filePath))) {
            errorInfo = "文件名不是excel格式";
            return false;
        }
        /** 检查文件是否存在 */
        File file = new File(filePath);
        if (file == null || !file.exists()) {
            errorInfo = "文件不存在";
            return false;
        }
        return true;
    }

    /**
     * 根据文件名读取excel文件
     *
     * @param filePath
     * @return
     */
    public List<List<String>> read(String filePath) {
        List<List<String>> dataLst = new ArrayList<List<String>>();
        InputStream is = null;
        try {
            /** 验证文件是否合法 */
            if (!validateExcel(filePath)) {
                System.out.println(errorInfo);
                return null;
            }
            /** 判断文件的类型，是2003还是2007 */
            boolean isExcel2003 = true;
            if (isExcel2007(filePath)) {
                isExcel2003 = false;
            }
            /** 调用本类提供的根据流读取的方法 */
            File file = new File(filePath);
            is = new FileInputStream(file);
            dataLst = read(is, isExcel2003);
            is.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    is = null;
                    e.printStackTrace();
                }
            }
        }
        /** 返回最后读取的结果 */
        return dataLst;
    }


    /**
     * 根据流读取Excel文件
     *
     * @param inputStream
     * @param isExcel2003
     * @return
     */
    public List<List<String>> read(InputStream inputStream, boolean isExcel2003) {

        List<List<String>> dataLst = null;
        try {
            /** 根据版本选择创建Workbook的方式 */
            Workbook wb = null;
            if (isExcel2003) {
                wb = new HSSFWorkbook(inputStream);
            } else {
                wb = new XSSFWorkbook(inputStream);
            }
            dataLst = read(wb);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataLst;

    }

    /**
     * 读取数据，以行读取
     *
     * @param wb Workbook
     * @return
     */
    private List<List<String>> read(Workbook wb) {
        List<List<String>> dataLst = new ArrayList<List<String>>();
        /** 得到第一个shell */
        Sheet sheet = wb.getSheetAt(0);
        /** 得到Excel的行数 */
        this.totalRows = sheet.getPhysicalNumberOfRows();
        /** 得到Excel的列数 */
        if (this.totalRows >= 1 && sheet.getRow(0) != null) {
            this.totalCells = sheet.getRow(0).getPhysicalNumberOfCells();
        }

        /** 循环Excel的行 */

        for (int r = 0; r < this.totalRows; r++) {
            Row row = sheet.getRow(r);
            if (row == null) {
                continue;
            }
            List<String> rowLst = new ArrayList<String>();

            /** 循环Excel的列 */
            for (int c = 0; c < this.getTotalCells(); c++) {
                String cellValue = getCellValue(row, c);
                rowLst.add(cellValue);
            }
            /** 保存第r行的第c列 */
            dataLst.add(rowLst);
        }
        return dataLst;
    }

    private String getCellValue(Row row, int c) {
        Cell cell = row.getCell(c);
        String cellValue = "";
        if (null != cell) {
            // 以下是判断数据的类型
            switch (cell.getCellType()) {
                // 数字
                case HSSFCell.CELL_TYPE_NUMERIC:
                    cellValue = cell.getNumericCellValue() + "";
                    break;
                // 字符串
                case HSSFCell.CELL_TYPE_STRING:
                    cellValue = cell.getStringCellValue();
                    break;
                // Boolean
                case HSSFCell.CELL_TYPE_BOOLEAN:
                    cellValue = cell.getBooleanCellValue() + "";
                    break;
                // 公式
                case HSSFCell.CELL_TYPE_FORMULA:
                    cellValue = cell.getCellFormula() + "";
                    break;
                // 空值
                case HSSFCell.CELL_TYPE_BLANK:
                    cellValue = "";
                    break;
                // 非法字符
                case HSSFCell.CELL_TYPE_ERROR:
                    cellValue = "非法字符";
                    break;
                default:
                    cellValue = "未知类型";
                    break;
            }
        }
        return cellValue;
    }

    /**
     * 删除列
     *
     * @param sheet
     * @param columnToDelete
     */
    public void deleteColumn(Sheet sheet, int columnToDelete) {
        for (int rId = 0; rId <= sheet.getLastRowNum(); rId++) {
            Row row = sheet.getRow(rId);
            for (int cID = columnToDelete; cID <= row.getLastCellNum(); cID++) {
                Cell cOld = row.getCell(cID);
                if (cOld != null) {
                    row.removeCell(cOld);
                }
                Cell cNext = row.getCell(cID + 1);
                if (cNext != null) {
                    Cell cNew = row.createCell(cID, cNext.getCellType());
                    cloneCell(cNew, cNext);
                    if (rId == 0) {
                        sheet.setColumnWidth(cID, sheet.getColumnWidth(cID + 1));
                    }
                }
            }
        }
    }

    /**
     * 右边列左移
     *
     * @param cNew
     * @param cOld
     */
    private void cloneCell(Cell cNew, Cell cOld) {
        cNew.setCellComment(cOld.getCellComment());
        cNew.setCellStyle(cOld.getCellStyle());

        if (Cell.CELL_TYPE_BOOLEAN == cNew.getCellType()) {
            cNew.setCellValue(cOld.getBooleanCellValue());
        } else if (Cell.CELL_TYPE_NUMERIC == cNew.getCellType()) {
            cNew.setCellValue(cOld.getNumericCellValue());
        } else if (Cell.CELL_TYPE_STRING == cNew.getCellType()) {
            cNew.setCellValue(cOld.getStringCellValue());
        } else if (Cell.CELL_TYPE_ERROR == cNew.getCellType()) {
            cNew.setCellValue(cOld.getErrorCellValue());
        } else if (Cell.CELL_TYPE_FORMULA == cNew.getCellType()) {
            cNew.setCellValue(cOld.getCellFormula());
        }
    }

    /**
     * 是否是2003的excel，返回true是2003
     *
     * @param filePath 文件完整路径
     * @return boolean
     */
    private boolean isExcel2003(String filePath) {
        return filePath.matches("^.+\\.(?i)(xls)$");

    }

    /**
     * 是否是2007的excel，返回true是2007
     *
     * @param filePath 文件完整路径
     * @return boolean
     */
    private boolean isExcel2007(String filePath) {
        return filePath.matches("^.+\\.(?i)(xlsx)$");

    }

    /**
     * 删除某列，采用复制法先将要删除的列的后一列复制到要删除的列，最后删除最后一列
     *
     * @param sheet
     * @param startCellIndex
     */
    public void deleteCellByIndex(Sheet sheet, int startCellIndex) {
        /** 得到Excel的行数 */
        this.totalRows = sheet.getPhysicalNumberOfRows();
        /** 得到Excel的列数 */
        if (this.totalRows >= 1 && sheet.getRow(0) != null) {
            this.totalCells = sheet.getRow(0).getPhysicalNumberOfCells();
        }

        //该sheet的所有行
        for (int i = 0; i < totalRows; i++) {
            // 列，下标从要删除的列开始
            for (int j = startCellIndex; j < totalCells; j++) {
                Row row = sheet.getRow(i);
                Cell distCell = row.getCell(j);
                Cell srcCell = row.getCell(j + 1);
                if (distCell == null && srcCell != null) {
                    distCell = row.createCell(j);
                } else if (distCell == null && srcCell == null) {
                    continue;
                } else if (distCell != null && srcCell == null) {
                    row.removeCell(distCell);
                    continue;
                }

                if (srcCell == null && (j + 1) == totalCells) {
                    row.removeCell(distCell);
                    continue;
                }
                copyCell(srcCell, distCell);
            }
        }
    }
    public void deleteCellByName(Sheet sheet, String cellName) {
        /** 得到Excel的行数 */
        this.totalRows = sheet.getPhysicalNumberOfRows();
        /** 得到Excel的列数 */
        int startCellIndex = -1;
        if (this.totalRows >= 1 && sheet.getRow(0) != null) {
            this.totalCells = sheet.getRow(0).getPhysicalNumberOfCells();
            for (int i = 0; i < totalCells; i++) {
                Row row = sheet.getRow(0);
                String cellValue = getCellValue(row, i);
                if (cellName.equals(cellValue)){
                    startCellIndex = i;
                    break;
                }
            }
        }

        if (startCellIndex == -1) {
            return;
        }

        //该sheet的所有行
        for (int i = 0; i < totalRows; i++) {
            // 列，下标从要删除的列开始
            for (int j = startCellIndex; j < totalCells; j++) {
                Row row = sheet.getRow(i);
                Cell distCell = row.getCell(j);
                Cell srcCell = row.getCell(j + 1);
                if (distCell == null && srcCell != null) {
                    distCell = row.createCell(j);
                } else if (distCell == null && srcCell == null) {
                    continue;
                } else if (distCell != null && srcCell == null) {
                    row.removeCell(distCell);
                    continue;
                }
                if (srcCell == null && (j + 1) == totalCells) {
                    row.removeCell(distCell);
                    continue;
                }
                copyCell(srcCell, distCell);
            }
        }
    }

    private void copyCell(Cell srcCell, Cell distCell) {
        distCell.setCellStyle(srcCell.getCellStyle());
        if (srcCell.getCellComment() != null) {
            distCell.setCellComment(srcCell.getCellComment());
        }
        int srcCellType = srcCell.getCellType();
        distCell.setCellType(srcCellType);
        if (srcCellType == HSSFCell.CELL_TYPE_NUMERIC) {
            if (HSSFDateUtil.isCellDateFormatted(srcCell)) {
                distCell.setCellValue(srcCell.getDateCellValue());
            } else {
                distCell.setCellValue(srcCell.getNumericCellValue());
            }
        } else if (srcCellType == HSSFCell.CELL_TYPE_STRING) {
            distCell.setCellValue(srcCell.getRichStringCellValue());
        } else if (srcCellType == HSSFCell.CELL_TYPE_BLANK) {
            // nothing21
        } else if (srcCellType == HSSFCell.CELL_TYPE_BOOLEAN) {
            distCell.setCellValue(srcCell.getBooleanCellValue());
        } else if (srcCellType == HSSFCell.CELL_TYPE_ERROR) {
            distCell.setCellErrorValue(srcCell.getErrorCellValue());
        } else if (srcCellType == HSSFCell.CELL_TYPE_FORMULA) {
            distCell.setCellFormula(srcCell.getCellFormula());
        } else { // nothing29

        }
    }

    public static void main(String[] args) {
        File file = new File("C:\\Users\\yingj\\Desktop\\新建 Microsoft Excel 工作表.xlsx");
        ExcelUtils excel = new ExcelUtils();
        try {
            FileInputStream is = new FileInputStream(file);
            Workbook wb = new XSSFWorkbook(is);
//            excel.deleteCellByIndex(wb.getSheetAt(0), 7);
            excel.deleteCellByName(wb.getSheetAt(0),"序列21");
            excel.deleteCellByName(wb.getSheetAt(0),"序列22");
            FileOutputStream fileOut = new FileOutputStream(file);
            wb.write(fileOut);
            fileOut.close();
            System.out.println("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
