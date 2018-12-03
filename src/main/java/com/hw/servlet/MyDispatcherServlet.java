package com.hw.servlet;

import com.hw.annotation.Autowired;
import com.hw.annotation.Controller;
import com.hw.annotation.Qualifier;
import com.hw.annotation.Repository;
import com.hw.annotation.RequestMapping;
import com.hw.annotation.Service;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

    /**
     * 需要实例化的类集合 IOC容器
     */
    private Map<String, Object> ioc = new HashMap<>();

    /**
     * 映射处理
     */
    private Map<String, Object> handleMethod = new HashMap<>();
    /**
     * 保存所有Controller方法的容器
     */
    private Map<String, Object> controllerMapper = new HashMap<>();

    @Override
    public void init(ServletConfig config) {
        System.out.println("=============   init MyDispatcherServlet ...");
        // step0. 读取配置文件
        String basePackage = config.getInitParameter("base-package");
        System.out.println("=============   Prepare to scan package " + basePackage);

        // step1. 扫描指定包路径下的类
        scanPackages(basePackage);
//        for (String clazzName : clazzNames) {
//            System.out.println(clazzName);
//        }

        // step2. 类实例化
        classInstance();

        // step3. 依赖注入 / 完成装配类
        injectClass();

        // step4. 映射处理
        handleMapping();

    }

    private void handleMapping() {

        if (ioc.isEmpty()) {
            System.out.println("没有实例化的对象！");
            return;
        }
        for (Map.Entry<String, Object> entry : ioc.entrySet()) {
            // 从集合中获取实例
            Object instance = entry.getValue();
            if (instance.getClass().isAnnotationPresent(Controller.class)) {
                RequestMapping requestMapping = instance.getClass().getAnnotation(RequestMapping.class);
                // 获取Controller 层的 RequestMapping中定义的路径值
                String path = requestMapping.value();
                Method[] methods = instance.getClass().getDeclaredMethods();
                for (Method method : methods) {
                    RequestMapping methodMapping = method.getAnnotation(RequestMapping.class);
                    // 获取方法上的 路径值
                    String methodPath = methodMapping.value();
                    handleMethod.put(path + methodPath, method);
                    controllerMapper.put(path + methodPath, instance);
                }

            }
        }
    }

    /**
     * step3. 依赖注入 / 完成装配类
     */
    private void injectClass() {

        if (ioc.isEmpty()) {
            System.out.println("没有实例化的对象！");
            return;
        }
        for (Map.Entry<String, Object> entry : ioc.entrySet()) {
            // 从集合中获取实例
            Object instance = entry.getValue();
            // 获取类中的所有成员属性
            Field[] fields = instance.getClass().getDeclaredFields();
            for (Field field : fields) {
                // 判断成员属性上有无相应的批注Autowired注解
                if (field.isAnnotationPresent(Autowired.class)) {
                    Autowired autowired = field.getAnnotation(Autowired.class);
                    boolean required = autowired.required();

                    String value = "";
                    if (field.isAnnotationPresent(Qualifier.class)) {
                        Qualifier qualifier = field.getAnnotation(Qualifier.class);
                        if ("".equals(qualifier.value().trim())) {
                            value = toLowerFirstWord(field.getName());
                        } else {
                            value = toLowerFirstWord(qualifier.value());
                        }
                    } else {
                        value = toLowerFirstWord(field.getName());
                    }

                    // 获取私有的方法
                    field.setAccessible(true);
                    try {
                        // 最关键的地方， 依赖注入
                        Object obj = null;
                        if (required) {
                            obj = ioc.get(value);
                            if (obj == null) {
                                throw new RuntimeException("无实例可注入！value=" + value);
                            }
                        }
                        field.set(instance, obj);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    /**
     * step2. 类实例化
     */
    private void classInstance() {
        // step1. 判断是否为空
        if (clazzNames.isEmpty()) {
            System.out.println("没有扫描到任何类！");
            return;
        }
        // step2.
        for (String name : clazzNames) {
            // 去掉 .class 后缀
            String realName = name.replaceAll(".class", "");
            try {
                Class clazz = Class.forName(realName);
                // 如果是包含Controller注解
                if (clazz.isAnnotationPresent(Controller.class)) {
                    Controller controller = (Controller) clazz.getAnnotation(Controller.class);
                    // 完成controller类的实例化
                    Object instance = clazz.newInstance();

                    String value = controller.value();
                    if ("".equals(value.trim())) {
                        value = toLowerFirstWord(clazz.getSimpleName());
                    }
                    // 把类的映射路径和类的实例化进行绑定
                    ioc.put(value, instance);
                }
                // 如果包含Service注解
                if (clazz.isAnnotationPresent(Service.class)) {
                    Service service = (Service) clazz.getAnnotation(Service.class);
                    String value = service.value();
                    if ("".equals(value.trim())) {
                        value = toLowerFirstWord(clazz.getSimpleName());
                    }
                    Object instance = clazz.newInstance();
                    ioc.put(value, instance);

                }
                // 如果包含Repository注解
                if (clazz.isAnnotationPresent(Repository.class)) {
                    Repository repository = (Repository) clazz.getAnnotation(Repository.class);
                    String value = repository.value();
                    if ("".equals(value.trim())) {
                        value = toLowerFirstWord(clazz.getSimpleName());
                    }
                    Object instance = clazz.newInstance();
                    ioc.put(value, instance);
                }

            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 将字符串中的首字母小写
     */
    private String toLowerFirstWord(String name) {
        char[] charArray = name.toCharArray();
        if (Character.isUpperCase(charArray[0])) {
            charArray[0] += 32;
        }
        return String.valueOf(charArray);
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
        String uri = req.getRequestURI();
        String contextPath = req.getContextPath();
        String path = uri.replaceAll(contextPath, "");

        if (!this.handleMethod.containsKey(path)) {
            resp.getWriter().write("404 NOT FOUND!");
            return;
        }
        Method method = (Method) handleMethod.get(path);


        Object instance = controllerMapper.get(path);
        try {
            // 获取方法的参数列表
            Class<?>[] parameterTypes = method.getParameterTypes();
            // 获取请求的参数
            Map<String, String[]> requestParamMap = req.getParameterMap();
            // 组装反射方法所需要的参数列表
            Object[] parameterMap = new Object[parameterTypes.length];
            // 遍历方法的参数列表
            for (int i = 0; i < parameterTypes.length; i++) {
                // 根据参数的类型做某些处理
                String simpleName = parameterTypes[i].getSimpleName();
                if (simpleName.equals("HttpServletRequest")) {
                    parameterMap[i] = req;
                    continue;
                }
                if (simpleName.equals("HttpServletResponse")) {
                    parameterMap[i] = req;
                    continue;
                }
                if (simpleName.equals("String")) {
                    for (Map.Entry<String, String[]> entry : requestParamMap.entrySet()) {
                        String value = Arrays.toString(entry.getValue()).replaceAll("\\[|\\]", "").replaceAll(",\\s", ",");
                        parameterMap[i] = value;
                    }
                }

            }
            // 利用反射机制来调用方法
            Object obj = method.invoke(instance, parameterMap);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

    }

}
