package demo.springboot.service.impl;

import com.deepoove.poi.XWPFTemplate;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
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
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${filePath}")
    private String filePath;
    @Value("${singleFile}")
    private String singleFile;
    @Value("${doubleFile}")
    private String doubleFile;
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
    public void downloadBook3(Long id, HttpServletResponse resp, int type){
    }

    @Override
    public void downloadBookZip(Long id, HttpServletResponse resp, int type) throws IOException {
        Book book = findById(id);
        //保证金算法
        if (StringUtils.isNotBlank(book.getCSumM())) {
            if (Double.valueOf(book.getCSumM()) > 0) {
                Double db = Double.valueOf(book.getCSumM()) * 0.05;
                DecimalFormat df = new DecimalFormat("#.0000000");
                String result = df.format(db);
                while (result.endsWith("0")) {
                    result = result.substring(0, result.length() - 1);
                }
                if (result.endsWith(".")) {
                    result = result.substring(0, result.length() - 1);
                }
                book.setBond(result);
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
        //服务费0000渠道费0000
        if (StringUtils.isNotBlank(book.getCSumM()) && StringUtils.isNotBlank(book.getCSumML())) {
            book.setSCML(String.valueOf(Math.round(Double.valueOf(book.getCSumML()) * Double.valueOf(book.getNop())*0.005)));
            book.setCFML(String.valueOf(Math.round(Double.valueOf(book.getCSumML()) *0.03)));
        }
        //设置年月日（yyyy/MM/dd）
        if (StringUtils.isNotBlank(book.getSYear()) && StringUtils.isNotBlank(book.getSMonth()) && StringUtils.isNotBlank(book.getSDay())) {
            book.setSYMD(book.getSYear()+"/"+book.getSMonth()+"/"+book.getSDay());
        }
        //上扣算法
        if (StringUtils.isNotBlank(book.getCSumML())) {
            if (Double.valueOf(book.getCSumML()) > 0) {
                Double db  = Double.valueOf(book.getCSumML()) * 0.005;
                DecimalFormat df = new DecimalFormat("#.00000000");
                String result = df.format(db);
                while (result.endsWith("0")) {
                    result = result.substring(0, result.length() - 1);
                }
                if (result.endsWith(".")) {
                    result = result.substring(0, result.length() - 1);
                }
                book.setCSumMR(result);
            }
        }
        //月还款算法
        if (StringUtils.isNotBlank(book.getCSumML())) {
            if (Double.valueOf(book.getCSumML()) > 0) {
                Double db = Double.valueOf(book.getCSumML()) * 0.013;
                DecimalFormat df = new DecimalFormat("#.00000000");
                String result = df.format(db);
                while (result.endsWith("0")) {
                    result = result.substring(0, result.length() - 1);
                }
                if (result.endsWith(".")) {
                    result = result.substring(0, result.length() - 1);
                }
                book.setCSumMRL(result);
            }
        }
        Map<String, Object> data = new HashMap<>();
        data.put("cls", book);
        String zipName = "";
        if (type > 0) {
            zipName = book.getName() + " " + book.getCBrand() + " " + book.getSYear() + "." + book.getSMonth() + "." + book.getSDay()+"等额本息";
        }else {
            zipName=book.getName() + " " + book.getCBrand() + " " + book.getSYear() + "." + book.getSMonth() + "." + book.getSDay()+"先息后本";
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
            String newFilePath = "";
            if (type > 0){
                newFilePath = filePath+"\\temp1";
            }else {
                newFilePath = filePath+"\\temp2";;
            }
            List<String> ighoneFileList = new ArrayList<>();
            if (book.getSType().equals("单签")) {
                ighoneFileList = Arrays.asList(singleFile.split("\\|", -1));
            }else if (book.getSType().equals("双签")){
                ighoneFileList = Arrays.asList(doubleFile.split("\\|", -1));
            }
            //获取文件列表
            File file = new File(newFilePath);
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length;i++) {
                if (ighoneFileList.contains(fileList[i].getName())) {
                    continue;
                }
                InputStream stream = new FileInputStream(fileList[i]);
                String zipFileName = fileList[i].getName();
                if (zipFileName.endsWith(".docx")&& !zipFileName.startsWith("19") && !zipFileName.startsWith("26")&& !zipFileName.startsWith("德鑫慧源")
                        && !zipFileName.startsWith("先息德鑫慧源") && !zipFileName.startsWith("先息服务合同")){
                    zipFileName = zipFileName.substring(0, zipFileName.length() - 1);
                }
                ZipEntry z = new ZipEntry(zipFileName);
                zipOutputStream.putNextEntry(z);
                //重点开始,创建压缩文件
                //todo:原因为XSSFWorkbook.write 会自动关闭流，导致后续执行时报stream closed。
                //todo:创建一个ByteArrayOutputStream，先将workbook写入ByteArrayOutputStream中，然后在写入zipOutputStream，即使在写入ByteArrayOutputStream后将流关闭，也不会影响zipOutputStream。
                if (fileList[i].getName().endsWith(".xlsx")||fileList[i].getName().endsWith(".xlsm")){
                    exportExcel(data,stream,zipOutputStream);
                }else if(fileList[i].getName().endsWith(".xls")) {
                    exportExcel2(data,stream,zipOutputStream);
                }else if(fileList[i].getName().endsWith(".jpg")) {
                    exportJpg(stream,zipOutputStream);
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
     * 读取jpg的输出流
     */
    public void exportJpg(InputStream stream,UncloseableZipOutputStream zipOutputStream ) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        int i = stream.available();
        byte[] buff = new byte[i];
        stream.read(buff);
//        stream.close();
        bos.write(buff);
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
