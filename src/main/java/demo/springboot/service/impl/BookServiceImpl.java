package demo.springboot.service.impl;

import com.deepoove.poi.XWPFTemplate;
import demo.springboot.domain.Book;
import demo.springboot.domain.BookRepository;
import demo.springboot.service.BookService;
import demo.springboot.utils.Excel2TemplateUtils;
import demo.springboot.utils.ExcelTemplateUtils;
import demo.springboot.utils.UncloseableZipOutputStream;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.zip.ZipEntry;

/**
 * Book 业务层实现
 *
 *
 */
@Service
public class BookServiceImpl implements BookService {

    @Autowired
    BookRepository bookRepository;
    //文件名
//    private String tempName ="4个人借款格式申请书.xls";
    private String tempName ="备忘录.docx|1贷款封面(抵押贷，怀2）.docx|2信贷档案目录2.xls|" +
            "4个人借款格式申请书(2)怀安城(1).xls|5贷前查询申请表.xls|6面谈记录及授信额度测算表 抵押贷.xlsx|" +
            "7.调查报告 抵押贷 打工者 怀安城(2)(1).docx|7.调查报告 抵押贷 营业执照者 怀安城.docx|8信贷业务调查审查审批表  抵押贷.xls|" +
            "9借款人支付委托书.docx|10受托支付审批表怀安城(1).docx|12个人单身证明承诺书.docx|" +
            "15同意抵押意见书 （抵押贷款用）单签.docx|" +
            "16二手车鉴定评估作业表.xlsx|19车抵贷  车辆贷款委托协议书(2)(1).docx|20个人汽车消费贷款推荐承诺书.docx|21担保承诺书(1)(1).docx|" +
            "22到期未偿还贷款法律责任告知书.docx|23债务人违约失信惩戒承诺函模板 2.docx|24上会  会议纪要.docx|25上会  送审报告.docx|" +
            "26上会  协议合同.docx|德鑫慧源汽车分期贷款担保合同怀安城(1).docx|照片格式北京(1).docx|客户声明(1)(1).docx";
    private String tempName2 ="备忘录.docx|1贷款封面(抵押贷，怀2）.docx|2信贷档案目录2.xls|" +
            "4个人借款格式申请书(2)怀安城(1).xls|5贷前查询申请表 双签.xls|6面谈记录及授信额度测算表 抵押贷.xlsx|" +
            "7.调查报告 抵押贷 打工者 怀安城(2)(1).docx|7.调查报告 抵押贷 营业执照者 怀安城.docx|8信贷业务调查审查审批表  抵押贷 双签.xls|" +
            "9借款人支付委托书.docx|10受托支付审批表怀安城(1).docx|12个人单身证明承诺书.docx|" +
            "15同意抵押意见书 （抵押贷款用）.docx|" +
            "16二手车鉴定评估作业表.xlsx|19车抵贷  车辆贷款委托协议书(2)(1).docx|20个人汽车消费贷款推荐承诺书.docx|21担保承诺书(1)(1).docx|" +
            "22到期未偿还贷款法律责任告知书.docx|23债务人违约失信惩戒承诺函模板 2.docx|24上会  会议纪要.docx|25上会  送审报告.docx|" +
            "26上会  协议合同.docx|德鑫慧源汽车分期贷款担保合同怀安城(1).docx|照片格式北京(1).docx|客户声明(1)(1).docx";

    @Override
    public Page<Book> findAll(Integer page , Integer size) {
        @SuppressWarnings("deprecation")
        Pageable pageable = new PageRequest(page, size, Sort.Direction.DESC, "id");
        return bookRepository.findAll(pageable);
    }

    @Override
    public Book insertByBook(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public Book update(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public Book delete(Long id) {
        Book book = bookRepository.findById(id).get();
        bookRepository.delete(book);
        return book;
    }

    @Override
    public Book findById(Long id) {
        return bookRepository.findById(id).get();
    }


    @Override
    public void downloadBook(Long id, HttpServletResponse resp) {
        //1,获取数据
        Book book = findById(id);
        try {
            String filepath = "";
            filepath="wordTemp/机动车抵押登记.docx";
          InputStream stream = getClass().getClassLoader().getResourceAsStream(filepath);
          XWPFTemplate template = XWPFTemplate.compile(stream).render(book);
        //todo：浏览器下载
        String fileName = "机动车抵押登记.docx";// 设置文件名，根据业务需要替换成要下载的文件名
            try {
                /*根据request的locale 得出可能的编码，中文操作系统通常是gb2312*/
                fileName = new String(fileName.getBytes("GB2312"), "ISO_8859_1");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        resp.setContentType("application/octet-stream");
        resp.setCharacterEncoding("utf-8");
        resp.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        OutputStream os =  resp.getOutputStream();
        template.write(os);
        template.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void downloadBook2(Long id, HttpServletResponse resp, int type) {
        //1,获取数据
        Book book = findById(id);
        Map<String, Object> data = new HashMap<>();
        data.put("cls", book);
        try {
            String filepath = "";

            if (type > 0) {
                filepath="wordTemp/4个人借款格式申请书.xls";
            }else {
                filepath="wordTemp/4个人借款格式申请书.xls";
            }
            InputStream stream = getClass().getClassLoader().getResourceAsStream(filepath);
            //todo：浏览器下载
            String fileName = "4个人借款格式申请书.xls";// 设置文件名，根据业务需要替换成要下载的文件名
            try {
                /*根据request的locale 得出可能的编码，中文操作系统通常是gb2312*/
                fileName = new String(fileName.getBytes("GB2312"), "ISO_8859_1");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            resp.setContentType("application/octet-stream");
            resp.setCharacterEncoding("utf-8");
            resp.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            OutputStream os =  resp.getOutputStream();
            //根据模板 templatePath 和数据 data 生成 excel 文件，写入 fos 流
            Excel2TemplateUtils.process(data, stream, os);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void downloadBook3(Long id, HttpServletResponse resp, int type) throws IOException {
//        Book book = findById(id);
//        Map<String, Object> data = new HashMap<>();
//        data.put("cls", book);
//        String zipName = book.getName();
//        try {
//            /*根据request的locale 得出可能的编码，中文操作系统通常是gb2312*/
//            zipName = new String(zipName.getBytes("GB2312"), "ISO_8859_1");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        resp.setContentType("application/octet-stream;charset=UTF-8");
//        resp.setHeader("Content-Disposition", "attachment;filename="+zipName+".zip");
//        OutputStream os =  resp.getOutputStream();
//        //压缩输出流
//        UncloseableZipOutputStream zipOutputStream = new UncloseableZipOutputStream(os);
//        try {
//
//            String fileName = "4个人借款格式申请书";// 设置文件名，根据业务需要替换成要下载的文件名
//            for (int i = 0; i < 6; i++) {
//                String filepath = "";
//                if (type > 0) {
//                    filepath="wordTemp/4个人借款格式申请书.xls";
//                }else {
//                    filepath="wordTemp/4个人借款格式申请书.xlsx";
//                }
//                InputStream stream = getClass().getClassLoader().getResourceAsStream(filepath);
//                //todo：浏览器下载
//                XSSFWorkbook wb= ExcelTemplateUtils.process2(data, stream);
//                //重点开始,创建压缩文件
//                ZipEntry z = new ZipEntry(fileName+i+".xlsx");
//                zipOutputStream.putNextEntry(z);
//                //todo:原因为XSSFWorkbook.write 会自动关闭流，导致后续执行时报stream closed。
//                //todo:创建一个ByteArrayOutputStream，先将workbook写入ByteArrayOutputStream中，然后在写入zipOutputStream，即使在写入ByteArrayOutputStream后将流关闭，也不会影响zipOutputStream。
//                ByteArrayOutputStream bos = new ByteArrayOutputStream();
//                wb.write(bos);
//                bos.writeTo(zipOutputStream);
//            }
//
//            zipOutputStream.flush();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }finally {
//            //注意关闭顺序，否则可能文件错误
//            if (zipOutputStream != null) {
//                zipOutputStream.reallyClose();
//            }
//            if (os != null) {
//                os.close();
//            }
//        }

    }

    @Override
    public void downloadBookZip(Long id, HttpServletResponse resp, int type) throws IOException {
        Book book = findById(id);
        //保证金算法
        if (StringUtils.isNotBlank(book.getCSumM())) {
            if (Double.valueOf(book.getCSumM()) > 0) {
                Double db = Double.valueOf(book.getCSumM()) * 0.05;
                DecimalFormat df = new DecimalFormat("#.00");
                book.setBond(df.format(db));
            }
        }
        //车价小写0000
        if (StringUtils.isNotBlank(book.getVPriceM())) {

            book.setVPriceML(String.valueOf(Math.round(Double.valueOf(book.getVPriceM()) *10000)));
        }
        //自筹小写0000
        if (StringUtils.isNotBlank(book.getSMoney())) {
            book.setSMoneyL(String.valueOf(Math.round(Double.valueOf(book.getSMoney()) *10000)));
        }
        //合同额小写0000
        if (StringUtils.isNotBlank(book.getCSumM())) {
            book.setCSumML(String.valueOf(Math.round(Double.valueOf(book.getCSumM()) *10000)));
        }
        //月收入0000
        if (StringUtils.isNotBlank(book.getMI())) {
            book.setMIL(String.valueOf(Math.round(Double.valueOf(book.getMI()) *10000)));
        }
        //年龄
        if (StringUtils.isNotBlank(book.getBYear())) {
            Calendar cal = Calendar.getInstance();
            int yearNow = cal.get(Calendar.YEAR);  //当前年份
            book.setAge(String.valueOf(yearNow-Integer.valueOf(book.getBYear())));
        }

        if (StringUtils.isNotBlank(book.getBYearP())) {
            Calendar cal = Calendar.getInstance();
            int yearNow = cal.get(Calendar.YEAR);  //当前年份
            book.setAgeP(String.valueOf(yearNow-Integer.valueOf(book.getBYearP())));
        }
        Map<String, Object> data = new HashMap<>();
        data.put("cls", book);
        String zipName = "";
        if (type > 0) {
            zipName = book.getName() + " " + book.getCBrand() + " " + book.getSYear() + "." + book.getSMonth() + "." + book.getSDay();
        }else {
            zipName=book.getName() + " " + book.getCBrand() + " " + book.getSYear() + "." + book.getSMonth() + "." + book.getSDay()+"黑版";
        }
        try {
            /*根据request的locale 得出可能的编码，中文操作系统通常是gb2312*/
            zipName = new String(zipName.getBytes("GB2312"), "ISO_8859_1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //todo：浏览器下载
        resp.setContentType("application/octet-stream;charset=UTF-8");
        resp.setHeader("Content-Disposition", "attachment;filename="+zipName+".zip");
        OutputStream os =  resp.getOutputStream();
        //压缩输出流
        UncloseableZipOutputStream zipOutputStream = new UncloseableZipOutputStream(os);
        try {
            String tempstr = "";
            if (book.getSType().equals("单签")) {
                tempstr = tempName;
            }else if (book.getSType().equals("双签")){
                tempstr = tempName2;
            }
            List<String> tempNames = Arrays.asList(tempstr.split("\\|",-1));
            //模板地址
            String filepath = "";
            for (String fileName: tempNames) {
                if (type > 0) {
                    filepath="wordTemp/temp1/"+fileName;
                }else {
                    filepath="wordTemp/temp2/"+fileName;
                }
                InputStream stream = getClass().getClassLoader().getResourceAsStream(filepath);
                String zipFileName = fileName;
                if (zipFileName.endsWith(".docx")&& !zipFileName.startsWith("19") && !zipFileName.startsWith("26")&& !zipFileName.startsWith("德鑫慧源")){
                    zipFileName = zipFileName.substring(0, zipFileName.length() - 1);
                }
                ZipEntry z = new ZipEntry(zipFileName);
                zipOutputStream.putNextEntry(z);
                //重点开始,创建压缩文件
                //todo:原因为XSSFWorkbook.write 会自动关闭流，导致后续执行时报stream closed。
                //todo:创建一个ByteArrayOutputStream，先将workbook写入ByteArrayOutputStream中，然后在写入zipOutputStream，即使在写入ByteArrayOutputStream后将流关闭，也不会影响zipOutputStream。
                if (filepath.endsWith(".xlsx")){
                    exportExcel(data,stream,zipOutputStream);
                }else if(filepath.endsWith(".xls")) {
                    exportExcel2(data,stream,zipOutputStream);
                }else {
                    exportDoc(book,stream,zipOutputStream);
                }
            }
            //刷出缓存
            zipOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            //注意关闭顺序，否则可能文件错误
            if (zipOutputStream != null) {
                zipOutputStream.reallyClose();
            }
            if (os != null) {
                os.close();
            }
        }
    }

    /**
     * 导入Excel模板流，输出生成的输出流
     * @param data
     * @param stream
     * @param zipOutputStream
     * @return
     * @throws IOException
     */
    public void exportExcel(Map<String, Object> data,InputStream stream,UncloseableZipOutputStream zipOutputStream ) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        XSSFWorkbook wb= ExcelTemplateUtils.process2(data, stream);
        wb.write(bos);
        wb.close();
        bos.writeTo(zipOutputStream);
    }

    /**
     * 导入Excel模板流，输出生成的输出流
     * @param data
     * @param stream
     * @param zipOutputStream
     * @return
     * @throws IOException
     */
    public void exportExcel2(Map<String, Object> data,InputStream stream,UncloseableZipOutputStream zipOutputStream ) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        HSSFWorkbook wb= Excel2TemplateUtils.process2(data, stream);
        wb.write(bos);
        wb.close();
        bos.writeTo(zipOutputStream);
    }
    /**
     * 导入Doc模板流，输出生成的输出流
     * @param book
     * @param stream
     * @param zipOutputStream
     * @return
     * @throws IOException
     */
    public void exportDoc(Book book,InputStream stream,UncloseableZipOutputStream zipOutputStream ) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        XWPFTemplate template = XWPFTemplate.compile(stream).render(book);
        template.write(bos);
        template.close();
        bos.writeTo(zipOutputStream);
    }
}
