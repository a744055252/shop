package utils;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public abstract class ExcelUtils {

    /**
     * excel表的导入功能
     *
     * @param is excel表的储存路径
     * @return List<List < String>> 行(列(,,,,,))
     * @作者：我
     */
    public static List<List<String>> importExecl(InputStream is) throws IOException {
        List<List<String>> data = new ArrayList<>();
        // 得到Excel工作簿对象
        Workbook wb;
        wb = new HSSFWorkbook(is);
        // 得到Excel工作表对象
        // 得到第一个shell
        Sheet sheet = wb.getSheetAt(0);
        // 得到所有行数
        int totalRows = sheet.getPhysicalNumberOfRows();
        // 得到Excel的列数
        int totalCells = 0;
        if (totalRows >= 1 && sheet.getRow(0) != null) {
            totalCells = sheet.getRow(0).getPhysicalNumberOfCells();
        }
        // 循环行
        for (int rowNum = 0; rowNum < totalRows; rowNum++) {
            Row row = sheet.getRow(rowNum);
            if (row == null) {
                continue;
            }
            // 存放本行中的所列的数据
            List<String> rowLst = new ArrayList<>();
            // 循环列
            for (int column = 0; column < totalCells; column++) {
                Cell cell = row.getCell(column);
                String cellValue = "";
                if (null != cell) {
                    // 以下是判断数据的类型
                    switch (cell.getCellType()) {
                        case _NONE:
                            cellValue = "未知类型";
                            break;
                        case NUMERIC:
                            if (HSSFDateUtil.isCellDateFormatted(cell)) {
                                // 处理日期格式、时间格式
                                SimpleDateFormat sdf;
                                if (cell.getCellStyle().getDataFormat() == HSSFDataFormat
                                        .getBuiltinFormat("h:mm")) {
                                    sdf = new SimpleDateFormat("HH:mm");
                                } else {// 日期
                                    sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                }
                                java.util.Date date = cell.getDateCellValue();
                                cellValue = sdf.format(date);
                            } else if (cell.getCellStyle().getDataFormat() == 58) {
                                // 处理自定义日期格式：m月d日(通过判断单元格的格式id解决，id的值是58)
                                SimpleDateFormat sdf = new SimpleDateFormat(
                                        "yyyy-MM-dd");
                                double value = cell.getNumericCellValue();
                                java.util.Date date = org.apache.poi.ss.usermodel.DateUtil
                                        .getJavaDate(value);
                                cellValue = sdf.format(date);
                            } else {
                                // 纯数字格式
                                double value = cell.getNumericCellValue();
                                CellStyle style = cell.getCellStyle();
                                DecimalFormat format = new DecimalFormat("0.00");
                                String temp = style.getDataFormatString();
                                // 单元格设置成常规
                                if ("General".equals(temp)) {
                                    format.applyPattern("#");
                                }
                                cellValue = format.format(value);
                            }
                            break;
                        case STRING:
                            cellValue = cell.getStringCellValue();
                            break;
                        case FORMULA:
                            cellValue = cell.getCellFormula() + "";
                            break;
                        case BLANK:
                            cellValue = "";
                            break;
                        case BOOLEAN:
                            cellValue = cell.getBooleanCellValue() + "";
                            break;
                        case ERROR:
                            cellValue = "非法字符";
                            break;
                        default:
                            cellValue = "未知类型";
                    }
                }
                rowLst.add(cellValue);
            }
            data.add(rowLst);
        }
        is.close();
        return data;
    }

    /**
     * @param
     * @描述：是否是2003的excel，返回true是2003
     * @作者：我
     * @参数：@return
     * @返回值：boolean
     */

    public static boolean isExcel2003(String filePath) {

        return filePath.matches("^.+\\.(?i)(xls)$");

    }

    /**
     * @param
     * @描述：是否是2007的excel，返回true是2007
     * @作者：我
     * @参数：@return
     * @返回值：boolean
     */

    public static boolean isExcel2007(String filePath) {

        return filePath.matches("^.+\\.(?i)(xlsx)$");

    }

    public static String getBase64Content(String base64) {
        if (base64 != null) {
            String[] splitArray = base64.split(",");
            if (splitArray.length != 2) {
                return "error";
            } else {
                return splitArray[1];
            }
        } else {
            return "error";
        }
    }

    public static HSSFWorkbook write(HSSFWorkbook wb, String sheetName, List<List<String>> rowData, int startRow) {
        // 建立新的sheet对象（excel的表单）
        HSSFSheet sheet = wb.createSheet(sheetName);
        int dataSize = rowData.size();
        if (getRowCount(sheet) > 0) {// 如果小于等于0，则一行都不存在
            sheet.shiftRows(startRow, getRowCount(sheet), dataSize);
        }
        for (int i = 0; i < dataSize; i++) {
            Row row = sheet.createRow(i + startRow);
            for (int j = 0; j < rowData.get(i).size(); j++) {
                Cell cell = row.createCell(j);
                if (rowData.get(i).get(j) == null) {
                    cell.setCellValue("");
                } else {
                    cell.setCellValue(rowData.get(i).get(j) + "");
                }
            }
        }
        return wb;
    }

    /**
     * todo 改进celltype的生成
     * @param wb
     * @param sheetName
     * @param rowData
     * @param startRow
     * @return
     */
    public static HSSFWorkbook writeGrid(HSSFWorkbook wb, String sheetName, List<List<Grid>> rowData, int startRow) {
        // 建立新的sheet对象（excel的表单）
        HSSFSheet sheet = wb.createSheet(sheetName);
        int dataSize = rowData.size();
        if (getRowCount(sheet) > 0) {// 如果小于等于0，则一行都不存在
            sheet.shiftRows(startRow, getRowCount(sheet), dataSize);
        }
        for (int i = 0; i < dataSize; i++) {
            Row row = sheet.createRow(i + startRow);
            for (int j = 0; j < rowData.get(i).size(); j++) {
                Cell cell = row.createCell(j);
                if (rowData.get(i).get(j) == null) {
                    cell.setCellValue("");
                } else {
                    Grid grid = rowData.get(i).get(j);
                    HSSFCellStyle cellStyle = wb.createCellStyle();

                    // 表格类型
                    HSSFDataFormat dataFormat = wb.createDataFormat();
                    GridType gridType = grid.getType();
                    short type = gridType.convert(dataFormat);
                    cellStyle.setDataFormat(type);

                    // 字体
                    HSSFFont fontStyle2 = wb.createFont();
                    fontStyle2.setColor(grid.getColer());

                    cellStyle.setFont(fontStyle2);
                    cell.setCellStyle(cellStyle);

                    // 设置值
                    gridType.setValue2Cell(cell, grid.getO());
                }
            }
        }
        return wb;
    }

    public static class Grid {

        /**
         * 表格内的数据
         */
        private Object o;
        /**
         * 表格类型
         */
        private GridType type;
        /**
         * 颜色 {@link HSSFColor} eg: HSSFColor.BLACK.index
         */
        private short coler;

        public static Grid valueOf(Object o, GridType type, short coler) {
            return new Grid(o, type, coler);
        }

        public static Grid valueOfDef(Object o) {
            return new Grid(o, GridType.string, (short) 0);
        }


        public static Grid valueOfStr(Object o, short coler) {
            return new Grid(o, GridType.string, coler);
        }

        public static Grid valueOfNum(Object o, short coler) {
            return new Grid(o, GridType.number, coler);
        }

        private Grid(Object o, GridType type, short coler) {
            this.o = o;
            this.type = type;
            this.coler = coler;
        }

        public Object getO() {
            return o;
        }

        public void setO(Object o) {
            this.o = o;
        }

        public GridType getType() {
            return type;
        }

        public void setType(GridType type) {
            this.type = type;
        }

        public short getColer() {
            return coler;
        }

        public void setColer(short coler) {
            this.coler = coler;
        }
    }

    public enum GridType {
        string {
            @Override
            public short convert(HSSFDataFormat dataFormat) {
                return dataFormat.getFormat("@");
            }

            @Override
            public void setValue2Cell(Cell cell, Object value) {
                if (value == null) {
                    cell.setCellValue("");
                } else {
                    cell.setCellValue(value.toString());
                }
            }
        },
        number {
            @Override
            public short convert(HSSFDataFormat dataFormat) {
                return dataFormat.getFormat("0.00");
            }

            @Override
            public void setValue2Cell(Cell cell, Object value) {
                if (value == null) {
                    cell.setCellValue("");
                } else {
                    cell.setCellValue((Double) value);
                }
            }
        };


        public short convert(HSSFDataFormat dataFormat) {
            throw new RuntimeException("不支持的方法");
        }

        public void setValue2Cell(Cell cell, Object value) {
            throw new RuntimeException("不支持的方法");
        }

    }

    private static int getRowCount(HSSFSheet sheet) {
        if (sheet.getPhysicalNumberOfRows() == 0) {
            return 0;
        }
        return sheet.getLastRowNum() + 1;

    }



}