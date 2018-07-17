package com.prostate.order.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * 读操作数据源
 */
@Configuration
@MapperScan(basePackages = "com.prostate.order.mapper.pra.read", sqlSessionTemplateRef  = "readPraSqlSessionTemplate")
public class ReadPraDataSourceConfiguration {

    @Value("${spring.datasource.readPra.driver-class-name}")
    private String driverClassName;

    @Value("${spring.datasource.readPra.url}")
    private String url;

    @Value("${spring.datasource.readPra.username}")
    private String username;

    @Value("${spring.datasource.readPra.password}")
    private String password;


    @Bean(name = "readPraDataSource")
    public DataSource dataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(this.driverClassName);
        dataSource.setUrl(this.url);
        dataSource.setUsername(this.username);
        dataSource.setPassword(this.password);
        return dataSource;
    }

    @Bean(name = "readPraSqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("readPraDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:mapping/pra/read/*.xml"));
        return bean.getObject();
    }

    @Bean(name = "readPraTransactionManager")
    public DataSourceTransactionManager transactionManager(@Qualifier("readPraDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "readPraSqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("readPraSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}