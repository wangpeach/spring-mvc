package com.saiy.config;

import org.logicalcobwebs.proxool.ProxoolDataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by rjora on 2016/12/29 0029.
 */
@Configuration
@EnableWebMvc
@PropertySource("classpath:config.properties")
@ComponentScan(basePackages = "com.saiy")
public class WebConfig extends WebMvcConfigurerAdapter implements EnvironmentAware {

    private Environment env;

    public void setEnvironment(Environment environment) {
        this.env = environment;
    }

    @Bean
    public ViewResolver viewResolver() {
        InternalResourceViewResolver view = new InternalResourceViewResolver();
        view.setViewClass(JstlView.class);
        view.setPrefix("/WEB-INF/views/");
        view.setSuffix(".jsp");
        return view;
    }

    @Bean(name = "dataSource")
    public DataSource dataSource() {
        ProxoolDataSource proxool = new ProxoolDataSource();
        proxool.setDriver(env.getProperty("jdbc.driver"));
        proxool.setDriverUrl(env.getProperty("jdbc.url"));
        proxool.setUser(env.getProperty("jdbc.username"));
        proxool.setPassword(env.getProperty("jdbc.password"));
        proxool.setDelegateProperties(env.getProperty("jdbc.properties"));
        proxool.setMaximumConnectionCount(Integer.parseInt(env.getProperty("jdbc.maximumConnectionCount")));
        proxool.setMinimumConnectionCount(Integer.parseInt(env.getProperty("jdbc.minimumConnectionCount")));
        return proxool;
    }

    public String srcPath() {
        String path = WebConfig.class.getResource("").getPath();
        path = path.substring(1, path.indexOf("com"));
        path = path.replace("%20", " ");
        return path;
    }

    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactoryBean sqlSessionFactory() {
        SqlSessionFactoryBean sqlSession = new SqlSessionFactoryBean();
        sqlSession.setDataSource(this.dataSource());

        InputStream stream = null;
        try {
            stream = new FileInputStream(this.srcPath() + "mybatis-config.xml");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Resource resource = new InputStreamResource(stream);
        sqlSession.setConfigLocation(resource);
        return sqlSession;
    }

    @Bean
    public MapperScannerConfigurer mapperScanner() {
        MapperScannerConfigurer scanner = new MapperScannerConfigurer();
        scanner.setBasePackage("com.saiy.mapper");
        scanner.setSqlSessionFactoryBeanName("sqlSessionFactory");
        return scanner;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("*.js").addResourceLocations("/assets/**").setCachePeriod(31536000);
        super.addResourceHandlers(registry);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        super.addInterceptors(registry);
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry)  {
        super.configureViewResolvers(registry);
    }


}
