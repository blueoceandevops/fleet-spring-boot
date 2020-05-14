package com.fleet.excel.util;

import com.fleet.excel.annotation.ExcelColumn;
import com.fleet.excel.annotation.ExcelSheet;
import com.fleet.excel.value.Values;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Excel工具类
 */
public class ExcelUtil<T> {

    public Class<T> clazz;

    public ExcelUtil(Class<T> clazz) {
        this.clazz = clazz;
    }

    private FormulaEvaluator formulaEvaluator;

    /**
     * 导入EXCEL
     */
    public List<T> read(MultipartFile file) throws Exception {
        InputStream in = file.getInputStream();
        return read(in);
    }

    /**
     * 导入EXCEL
     */
    public List<T> read(File file) throws Exception {
        FileInputStream in = new FileInputStream(file);
        return read(in);
    }

    /**
     * 导入EXCEL
     */
    public List<T> read(File file, String sheetName) throws Exception {
        FileInputStream in = new FileInputStream(file);
        return read(in, sheetName);
    }

    /**
     * 导入EXCEL
     */
    public List<T> read(InputStream in) throws Exception {
        int sheetAt = 0;
        int startWith = 1;
        if (clazz.isAnnotationPresent(ExcelSheet.class)) {
            ExcelSheet excelSheet = clazz.getAnnotation(ExcelSheet.class);
            sheetAt = excelSheet.sheetAt();
            startWith = excelSheet.startWith();
        }

        Workbook workbook = getWorkbook(in);
        Sheet sheet = getSheet(workbook, sheetAt);
        formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
        Map<Integer, Field> fieldMap = getFieldMap();
        List<T> list = getList(sheet, fieldMap, startWith);
        workbook.close();
        in.close();
        return list;
    }

    /**
     * 导入EXCEL
     */
    public List<T> read(InputStream in, String sheetName) throws Exception {
        int startWith = 1;
        if (clazz.isAnnotationPresent(ExcelSheet.class)) {
            ExcelSheet excelSheet = clazz.getAnnotation(ExcelSheet.class);
            startWith = excelSheet.startWith();
        }

        Workbook workbook = getWorkbook(in);
        Sheet sheet = getSheet(workbook, sheetName);

        formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
        Map<Integer, Field> fieldMap = getFieldMap();
        List<T> list = getList(sheet, fieldMap, startWith);
        workbook.close();
        in.close();
        return list;
    }

    private Workbook getWorkbook(InputStream in) throws Exception {
        return WorkbookFactory.create(in);
    }

    private Sheet getSheet(Workbook workbook, String sheetName) {
        Sheet sheet;
        if (StringUtils.isNotEmpty(sheetName)) {
            sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                sheet = getSheet(workbook, 0);
            }
        } else {
            sheet = getSheet(workbook, 0);
        }
        return sheet;
    }

    private Sheet getSheet(Workbook workbook, Integer sheetAt) {
        return workbook.getSheetAt(sheetAt);
    }

    private List<T> getList(Sheet sheet, Map<Integer, Field> fieldMap, Integer StartWith) throws Exception {
        List<T> list = new ArrayList<>();
        for (Row row : sheet) {
            // 从第 StartWith 行开始取数据
            if (row.getRowNum() < StartWith) {
                continue;
            }
            T t = clazz.newInstance();

            //检查所有字段是否都为空
            Boolean isNull = true;
            for (Integer col : fieldMap.keySet()) {
                Field field = fieldMap.get(col);
                // 设置实体类私有属性可访问
                field.setAccessible(true);
                ExcelColumn excelColumn = field.getAnnotation(ExcelColumn.class);
                Cell cell = row.getCell(col);
                if (cell == null) {
                    continue;
                }
                String value = getValue(cell);// 单元格中的内容
                if (StringUtils.isEmpty(value)) {
                    continue;
                } else {
                    if (isNull) {
                        isNull = false;
                    }
                }

                if (Values.class.isAssignableFrom(excelColumn.values()) && !excelColumn.values().isAssignableFrom(Values.class)) {
                    Class<? extends Values> valueClazz = excelColumn.values();
                    value = valueClazz.newInstance().getKey(value);
                }

                Class<?> fieldType = field.getType();
                if (fieldType == Integer.TYPE || fieldType == Integer.class) {
                    if (StringUtils.isNotEmpty(value)) {
                        Integer intVal = Integer.parseInt(value);
                        field.set(t, intVal);
                    }
                } else if (fieldType == String.class) {
                    if (StringUtils.isNotEmpty(value)) {
                        field.set(t, value);
                    }
                } else if (fieldType == Short.TYPE || fieldType == Short.class) {
                    if (StringUtils.isNotEmpty(value)) {
                        Short shortVal = Short.parseShort(value);
                        field.set(t, shortVal);
                    }
                } else if (fieldType == Long.TYPE || fieldType == Long.class) {
                    if (StringUtils.isNotEmpty(value)) {
                        Long longVal = Long.parseLong(value);
                        field.set(t, longVal);
                    }
                } else if (fieldType == Float.TYPE || fieldType == Float.class) {
                    if (StringUtils.isNotEmpty(value)) {
                        Float floatVal = Float.parseFloat(value);
                        field.set(t, floatVal);
                    }
                } else if (fieldType == Double.TYPE || fieldType == Double.class) {
                    if (StringUtils.isNotEmpty(value)) {
                        Double doubleVal = Double.parseDouble(value);
                        field.set(t, doubleVal);
                    }
                } else if (fieldType == BigDecimal.class) {
                    if (StringUtils.isNotEmpty(value)) {
                        BigDecimal bigDecimalVal = new BigDecimal(value);
                        field.set(t, bigDecimalVal);
                    }
                } else if (fieldType == Boolean.TYPE || fieldType == Boolean.class) {
                    if (StringUtils.isNotEmpty(value)) {
                        Boolean booleanVal = Boolean.parseBoolean(value);
                        field.set(t, booleanVal);
                    }
                } else if (fieldType == Date.class) {
                    if (StringUtils.isNotEmpty(value)) {
                        SimpleDateFormat sdf = new SimpleDateFormat();
                        if (StringUtils.isNotEmpty(excelColumn.dateFormat().trim())) {
                            sdf = new SimpleDateFormat(excelColumn.dateFormat());
                        }
                        Date dateVal = sdf.parse(value);
                        field.set(t, dateVal);
                    }
                } else {
                    if (StringUtils.isNotEmpty(value)) {
                        field.set(t, value);
                    }
                }
            }
            if (!isNull) {
                list.add(t);
            }
        }
        return list;
    }

    /**
     * 导出EXCEL模板（表头）
     */
    public void exportTemplate(String sheetName, OutputStream out) throws Exception {
        export(null, sheetName, out);
    }

    /**
     * 导出EXCEL模板（表头与示例数据）
     */
    public void exportTemplate(List<T> list, String sheetName, OutputStream out) throws Exception {
        export(list, sheetName, out);
    }

    /**
     * 导出EXCEL
     */
    public void export(List<T> list, OutputStream out) throws Exception {
        int startWith = 1;
        if (clazz.isAnnotationPresent(ExcelSheet.class)) {
            ExcelSheet excelSheet = clazz.getAnnotation(ExcelSheet.class);
            startWith = excelSheet.startWith();
        }

        // 产生工作薄对象
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 产生工作表对象
        HSSFSheet sheet = createSheet(workbook);

        Map<Integer, Field> fieldMap = getFieldMap();

        createHead(workbook, sheet, fieldMap);
        createBody(workbook, sheet, fieldMap, list, startWith);

        workbook.write(out);
        workbook.close();
        out.flush();
        out.close();
    }

    /**
     * 导出EXCEL
     */
    public void export(List<T> list, String sheetName, OutputStream out) throws Exception {
        int startWith = 1;
        if (clazz.isAnnotationPresent(ExcelSheet.class)) {
            ExcelSheet excelSheet = clazz.getAnnotation(ExcelSheet.class);
            startWith = excelSheet.startWith();
        }

        // 产生工作薄对象
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 产生工作表对象
        HSSFSheet sheet = createSheet(workbook, sheetName);

        Map<Integer, Field> fieldMap = getFieldMap();

        createHead(workbook, sheet, fieldMap);
        createBody(workbook, sheet, fieldMap, list, startWith);

        workbook.write(out);
        workbook.close();
        out.flush();
        out.close();
    }

    private HSSFSheet createSheet(HSSFWorkbook workbook) {
        // 产生工作表对象
        HSSFSheet sheet = workbook.createSheet();
        // 设置默认列宽
        sheet.setDefaultColumnWidth(15);
        // 设置默认行高
        sheet.setDefaultRowHeight((short) (2 * 256));
        return sheet;
    }

    private HSSFSheet createSheet(HSSFWorkbook workbook, String sheetName) {
        // 产生工作表对象
        HSSFSheet sheet = workbook.createSheet(sheetName);
        // 设置默认列宽
        sheet.setDefaultColumnWidth(15);
        // 设置默认行高
        sheet.setDefaultRowHeight((short) (2 * 256));
        return sheet;
    }

    private void createHead(HSSFWorkbook workbook, HSSFSheet sheet, Map<Integer, Field> fieldMap) {
        // 创建行
        HSSFRow row = sheet.createRow(0);
        // 生成一个样式
        HSSFCellStyle headCellStyle = workbook.createCellStyle();
        // 设置标题样式
        headCellStyle = setHeadStyle(workbook, headCellStyle);
        for (Integer col : fieldMap.keySet()) {
            Field field = fieldMap.get(col);
            // 设置实体类私有属性可访问
            field.setAccessible(true);
            ExcelColumn excelColumn = field.getAnnotation(ExcelColumn.class);
            // 创建列
            HSSFCell cell = row.createCell(col);
            cell.setCellStyle(headCellStyle);
            cell.setCellValue(excelColumn.name());
        }
    }

    private void createBody(HSSFWorkbook workbook, HSSFSheet sheet, Map<Integer, Field> fieldMap, List<T> list, Integer StartWith) throws Exception {
        // 写入各条记录，每条记录对应excel表中的一行
        if (list != null) {
            // 生成一个样式
            HSSFCellStyle bodyCellStyle = workbook.createCellStyle();
            // 设置内容样式
            bodyCellStyle = setBodyStyle(workbook, bodyCellStyle);
            for (int i = 0; i < list.size(); i++) {
                // 创建行
                HSSFRow row = sheet.createRow(i + StartWith);
                T t = (T) list.get(i);
                for (Integer col : fieldMap.keySet()) {
                    Field field = fieldMap.get(col);
                    // 设置实体类私有属性可访问
                    field.setAccessible(true);
                    ExcelColumn excelColumn = field.getAnnotation(ExcelColumn.class);

                    // 创建列
                    HSSFCell cell = row.createCell(col);
                    cell.setCellStyle(bodyCellStyle);

                    // 根据ExcelVOAttribute中设置情况决定是否导出，有些情况需要保持为空
                    if (excelColumn.isExport()) {
                        Object o = field.get(t);
                        if (o == null) {
                            continue;
                        }

                        Class<?> fieldType = field.getType();

                        // 时间格式转换
                        if (StringUtils.isNotEmpty(excelColumn.dateFormat().trim())) {
                            String dateVal;
                            if (fieldType == Date.class) {
                                SimpleDateFormat sdf = new SimpleDateFormat(excelColumn.dateFormat());
                                dateVal = sdf.format((Date) o);
                            } else {
                                dateVal = String.valueOf(o);
                            }
                            // 生成一个data样式（转换结果可能显示错误，需要手动转换成需要的格式）
                            // HSSFCellStyle dataFormatCellStyle = workbook.createCellStyle();
                            // dataFormatCellStyle.cloneStyleFrom(bodyCellStyle);
                            // CreationHelper createHelper = workbook.getCreationHelper();
                            // short dateFormat = createHelper.createDataFormat().getFormat(excelColumn.dateFormat());
                            // dataFormatCellStyle.setDataFormat(dateFormat);
                            // cell.setCellStyle(dataFormatCellStyle);
                            cell.setCellValue(dateVal);
                            continue;
                        }

                        if (Values.class.isAssignableFrom(excelColumn.values()) && !excelColumn.values().isAssignableFrom(Values.class)) {
                            Class<? extends Values> valueClazz = excelColumn.values();
                            String value = valueClazz.newInstance().get(String.valueOf(o));
                            cell.setCellValue(value);
                            continue;
                        }

                        if (fieldType == Integer.TYPE || fieldType == Integer.class) {
                            Integer intVal = (Integer) o;
                            cell.setCellValue(intVal);
                        } else if (fieldType == String.class) {
                            String stringVal = (String) o;
                            cell.setCellValue(stringVal);
                        } else if (fieldType == Short.TYPE || fieldType == Short.class) {
                            Short shortVal = (Short) o;
                            cell.setCellValue(shortVal);
                        } else if (fieldType == Long.TYPE || fieldType == Long.class) {
                            Long longVal = (Long) o;
                            cell.setCellValue(longVal);
                        } else if (fieldType == Float.TYPE || fieldType == Float.class) {
                            Float floatVal = (Float) o;
                            cell.setCellValue(floatVal);
                        } else if (fieldType == Double.TYPE || fieldType == Double.class) {
                            Double doubleVal = (Double) o;
                            cell.setCellValue(doubleVal);
                        } else if (fieldType == BigDecimal.class) {
                            BigDecimal bigDecimalVal = (BigDecimal) o;
                            double doubleVal = bigDecimalVal.doubleValue();
                            // 生成一个data样式（转换结果可能显示错误，需要手动转换成需要的格式）
                            // HSSFCellStyle dataFormatCellStyle = workbook.createCellStyle();
                            // dataFormatCellStyle.cloneStyleFrom(bodyCellStyle);
                            // CreationHelper createHelper = workbook.getCreationHelper();
                            // short dateFormat = createHelper.createDataFormat().getFormat("￥#,##0.00");
                            // dataFormatCellStyle.setDataFormat(dateFormat);
                            // cell.setCellStyle(dataFormatCellStyle);
                            cell.setCellValue(doubleVal);
                        } else if (fieldType == Boolean.TYPE || fieldType == Boolean.class) {
                            Boolean booleanVal = (Boolean) o;
                            cell.setCellValue(booleanVal);
                        } else if (fieldType == Date.class) {
                            Date dateVal = (Date) o;
                            cell.setCellValue(dateVal);
                        } else {
                            String stringVal = String.valueOf(o);
                            cell.setCellValue(stringVal);
                        }
                    }
                }
            }
        }
    }

    private HSSFCellStyle setHeadStyle(HSSFWorkbook workbook, HSSFCellStyle style) {
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        // 生成字体
        HSSFFont font = workbook.createFont();
        font.setColor(IndexedColors.BLACK.getIndex());
        font.setFontHeightInPoints((short) 12);
        font.setBold(true);
        // 把字体应用到当前的样样式
        style.setFont(font);
        return style;
    }

    private HSSFCellStyle setBodyStyle(HSSFWorkbook workbook, HSSFCellStyle style) {
        style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        // style.setAlignment(HorizontalAlignment.RIGHT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        // 生成字体
        HSSFFont font = workbook.createFont();
        font.setColor(IndexedColors.BLACK.getIndex());
        font.setFontHeightInPoints((short) 10);
        // 把字体应用到当前的样样式
        style.setFont(font);
        return style;
    }

    /**
     * 导出EXCEL(按照模板导出)
     */
    public void exportByTemplate(List<T> list, OutputStream out) throws Exception {
        String template = "";
        int sheetAt = 0;
        int headAt = 0;
        int startWith = 1;
        if (clazz.isAnnotationPresent(ExcelSheet.class)) {
            ExcelSheet excelSheet = clazz.getAnnotation(ExcelSheet.class);
            template = excelSheet.template();
            sheetAt = excelSheet.sheetAt();
            headAt = excelSheet.headAt();
            startWith = excelSheet.startWith();
        }
        if (StringUtils.isEmpty(template.trim())) {
            throw new Exception("模板文件未找到");
        }

        InputStream in = this.getClass().getResourceAsStream(template);
        if (in == null) {
            throw new Exception("模板文件未找到");
        }

        Workbook workbook = getWorkbook(in);
        Sheet sheet = getSheet(workbook, sheetAt);
        Map<Integer, Field> fieldMap = getFieldMap();

        createHead(sheet, fieldMap, headAt);
        createBody(sheet, fieldMap, list, startWith);

        workbook.write(out);
        workbook.close();
        out.flush();
        out.close();
    }

    private void createHead(Sheet sheet, Map<Integer, Field> fieldMap, int headAt) {
        Row row = sheet.getRow(headAt);
        for (Integer col : fieldMap.keySet()) {
            Field field = fieldMap.get(col);
            // 设置实体类私有属性可访问
            field.setAccessible(true);
            ExcelColumn excelColumn = field.getAnnotation(ExcelColumn.class);
            // 创建列
            Cell cell = row.getCell(col);
            if (cell == null) {
                cell = row.createCell(col);
            }
            cell.setCellValue(excelColumn.name());
        }
    }

    private void createBody(Sheet sheet, Map<Integer, Field> fieldMap, List<T> list, Integer StartWith) throws Exception {
        // 写入各条记录，每条记录对应excel表中的一行
        if (list != null) {
            Short rowHeight = null;
            Map<Integer, CellStyle> cellStyleMap = new HashMap<>();
            for (int i = 0; i < list.size(); i++) {
                Row row = sheet.getRow(i + StartWith);
                if (row == null) {
                    row = sheet.createRow(i + StartWith);
                    if (rowHeight != null) {
                        row.setHeight(rowHeight);
                    }
                } else {
                    if (i == 0) {
                        rowHeight = row.getHeight();
                    }
                }

                T t = (T) list.get(i);
                for (Integer col : fieldMap.keySet()) {
                    Field field = fieldMap.get(col);
                    // 设置实体类私有属性可访问
                    field.setAccessible(true);
                    ExcelColumn excelColumn = field.getAnnotation(ExcelColumn.class);

                    // 创建列
                    Cell cell = row.getCell(col);
                    if (cell == null) {
                        cell = row.createCell(col);
                    }

                    if (i == 0) {
                        CellStyle cellStyle = cell.getCellStyle();
                        cellStyleMap.put(col, cellStyle);
                    } else {
                        CellStyle cellStyle = cellStyleMap.get(col);
                        cell.setCellStyle(cellStyle);
                    }

                    // 根据ExcelVOAttribute中设置情况决定是否导出，有些情况需要保持为空
                    if (excelColumn.isExport()) {
                        Object o = field.get(t);
                        if (o == null) {
                            continue;
                        }

                        Class<?> fieldType = field.getType();

                        // 时间格式转换
                        if (StringUtils.isNotEmpty(excelColumn.dateFormat().trim())) {
                            String dateVal;
                            if (fieldType == Date.class) {
                                SimpleDateFormat sdf = new SimpleDateFormat(excelColumn.dateFormat());
                                dateVal = sdf.format((Date) o);
                            } else {
                                dateVal = String.valueOf(o);
                            }
                            cell.setCellValue(dateVal);
                            continue;
                        }

                        if (Values.class.isAssignableFrom(excelColumn.values()) && !excelColumn.values().isAssignableFrom(Values.class)) {
                            Class<? extends Values> valueClazz = excelColumn.values();
                            String value = valueClazz.newInstance().get(String.valueOf(o));
                            cell.setCellValue(value);
                            continue;
                        }

                        if (fieldType == Integer.TYPE || fieldType == Integer.class) {
                            Integer intVal = (Integer) o;
                            cell.setCellValue(intVal);
                        } else if (fieldType == String.class) {
                            String stringVal = (String) o;
                            cell.setCellValue(stringVal);
                        } else if (fieldType == Short.TYPE || fieldType == Short.class) {
                            Short shortVal = (Short) o;
                            cell.setCellValue(shortVal);
                        } else if (fieldType == Long.TYPE || fieldType == Long.class) {
                            Long longVal = (Long) o;
                            cell.setCellValue(longVal);
                        } else if (fieldType == Float.TYPE || fieldType == Float.class) {
                            Float floatVal = (Float) o;
                            cell.setCellValue(floatVal);
                        } else if (fieldType == Double.TYPE || fieldType == Double.class) {
                            Double doubleVal = (Double) o;
                            cell.setCellValue(doubleVal);
                        } else if (fieldType == BigDecimal.class) {
                            BigDecimal bigDecimalVal = (BigDecimal) o;
                            double doubleVal = bigDecimalVal.doubleValue();
                            cell.setCellValue(doubleVal);
                        } else if (fieldType == Boolean.TYPE || fieldType == Boolean.class) {
                            Boolean booleanVal = (Boolean) o;
                            cell.setCellValue(booleanVal);
                        } else if (fieldType == Date.class) {
                            Date dateVal = (Date) o;
                            cell.setCellValue(dateVal);
                        } else {
                            String stringVal = String.valueOf(o);
                            cell.setCellValue(stringVal);
                        }
                    }
                }
            }
        }
    }

    private Map<Integer, Field> getFieldMap() {
        // 得到所有定义字段
        Field[] fields = clazz.getDeclaredFields();
        // field集合
        Map<Integer, Field> fieldMap = new HashMap<>();
        // 得到所有field并存放到一个Map中，重复项会被覆盖
        for (Field field : fields) {
            // 设置实体类私有属性可访问
            field.setAccessible(true);
            if (field.isAnnotationPresent(ExcelColumn.class)) {
                ExcelColumn excelColumn = field.getAnnotation(ExcelColumn.class);
                int col = getExcelCol(excelColumn.column());
                fieldMap.put(col, field);
            }
        }
        return fieldMap;
    }

    /**
     * 将EXCEL中A,B,C,D,E列映射成0,1,2,3
     */
    private int getExcelCol(String column) {
        column = column.toUpperCase();
        // 从-1开始计算，字母重1开始运算
        int count = -1;
        char[] cs = column.toCharArray();
        for (int i = 0; i < cs.length; i++) {
            count += (cs[i] - 64) * Math.pow(26, cs.length - 1 - i);
        }
        return count;
    }

    /**
     * 设置单元格上提示
     */
    private HSSFSheet setHSSFPrompt(HSSFSheet sheet, String promptTitle, String promptContent, int firstRow, int endRow, int firstCol, int endCol) {
        // 构造constraint对象
        DVConstraint dvConstraint = DVConstraint.createCustomFormulaConstraint("DD1");
        // 设置数据有效性加载在哪个单元格上，四个参数分别是：起始行、终止行、起始列、终止列
        CellRangeAddressList cellRangeAddressList = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);
        // 数据有效性对象
        HSSFDataValidation hssfDataValidation = new HSSFDataValidation(cellRangeAddressList, dvConstraint);
        hssfDataValidation.createPromptBox(promptTitle, promptContent);
        sheet.addValidationData(hssfDataValidation);
        return sheet;
    }

    /**
     * 设置某些列的值只能输入预制的数据，显示下拉框
     */
    private HSSFSheet setHSSFValidation(HSSFSheet sheet, String[] explicitListValues, int firstRow, int endRow, int firstCol, int endCol) {
        // 加载下拉列表内容
        DVConstraint dvConstraint = DVConstraint.createExplicitListConstraint(explicitListValues);
        // 设置数据有效性加载在哪个单元格上，四个参数分别是：起始行、终止行、起始列、终止列
        CellRangeAddressList cellRangeAddressList = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);
        // 数据有效性对象
        HSSFDataValidation hssfDataValidation = new HSSFDataValidation(cellRangeAddressList, dvConstraint);
        sheet.addValidationData(hssfDataValidation);
        return sheet;
    }

    private String getValue(Cell cell) {
        String value = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        switch (cell.getCellType()) {
            case NUMERIC: // 数值
                if (DateUtil.isCellDateFormatted(cell)) {
                    Date d = DateUtil.getJavaDate(cell.getNumericCellValue());
                    value = sdf.format(d);
                } else {
                    value = BigDecimal.valueOf(cell.getNumericCellValue()).stripTrailingZeros().toPlainString();
                }
                break;
            case STRING: // 字符串
                value = cell.getStringCellValue();
                break;
            case BOOLEAN: // Boolean
                value = cell.getBooleanCellValue() + "";
                break;
            case FORMULA: // 公式
                value = getCellValue(formulaEvaluator.evaluate(cell));
                break;
            case BLANK: // 空值
                break;
            case ERROR: // 错误
                break;
            default:
                break;
        }
        return value;
    }

    private String getCellValue(CellValue cellValue) {
        String value = "";
        switch (cellValue.getCellType()) {
            case NUMERIC:
                value = BigDecimal.valueOf(cellValue.getNumberValue()).stripTrailingZeros().toPlainString();
                break;
            case STRING:
                value = cellValue.getStringValue();
                break;
            case BOOLEAN:
                value = cellValue.getBooleanValue() + "";
                break;
            default:
                break;
        }
        return value;
    }
}
