package com.hw.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description 自定义 DispatcherServlet 用来处理请求实现Spring MVC底层逻辑
 * @Author hw
 * @Date 2018/12/3 15:54
 * @Version 1.0
 */
public class MyDispatcherServlet extends HttpServlet {

    /**
     * 用来存储扫描到的类文件名
     */
    private List<String> clazzNames = new ArrayList<>();

    @Override
    public void init(ServletConfig config) throws ServletException {
        System.out.println("=============   init MyDispatcherServlet ...");
        // step0. 读取配置文件
        String basePackage = config.getInitParameter("base-package");
        System.out.println("=============   Prepare to scan package " + basePackage);
        // step1. 扫描指定包路径下的类
        scanPackages(basePackage);
        for (String clazzName : clazzNames) {
            System.out.println(clazzName);
        }
        // step2. 类实例化

        // step3. 依赖注入

        // step4. 映射处理


    }


    /**
     * step1. 扫描指定包路径下的类
     *
     * @param basePackage 指定的路径
     */
    private void scanPackages(String basePackage) {
        // step 1. 把路径的. 替换成 系统的文件分隔符
        String path = basePackage.replaceAll("\\.", "/");
        // step 2. 初始化当前路径的资源URL
        URL url = getClass().getClassLoader().getResource("/" + path);
        // step 3. 目录递归扫描所有的类文件
        String filePath = url.getFile();
        System.out.println("=============   filePath: " + filePath);
        File[] files = new File(filePath).listFiles();
        for (File file : files) {
            String filePackage = basePackage + "." + file.getName();
            // step 3.1 判断是不是目录，是目录，继续递归扫描；不是添加进去
            if (file.isDirectory()) {
                scanPackages(filePackage);
            } else {
                clazzNames.add(filePackage);
            }
        }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

}
