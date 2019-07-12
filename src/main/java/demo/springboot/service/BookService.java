package demo.springboot.service;

import demo.springboot.domain.Book;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Book 业务接口层
 *
 * Created by bysocket on 30/09/2017.
 */
public interface BookService {
    /**
     * 获取所有 Book
     */
    List<Book> findAll();

    /**
     * 新增 Book
     *
     * @param book {@link Book}
     */
    Book insertByBook(Book book);

    /**
     * 更新 Book
     *
     * @param book {@link Book}
     */
    Book update(Book book);

    /**
     * 删除 Book
     *
     * @param id 编号
     */
    Book delete(Long id);

    /**
     * 获取 Book
     *
     * @param id 编号
     */
    Book findById(Long id);

    /**
     * 浏览器导出word
     * @param id
     * @param response
     */
    void downloadBook(Long id, HttpServletResponse response);

    /**
     * 浏览器导出excel
     * @param id
     * @param response
     * @param i
     */
    void downloadBook2(Long id, HttpServletResponse response, int i);

    /**
     * 浏览器导出Excel压缩包
     * @param id
     * @param response
     * @param i
     * @throws IOException
     */
    void downloadBook3(Long id, HttpServletResponse response, int i) throws IOException;

    /**
     * 下载zip
     * @param id
     * @param response
     * @param i
     */
    void downloadBookZip(Long id, HttpServletResponse response, int i) throws IOException;
}
