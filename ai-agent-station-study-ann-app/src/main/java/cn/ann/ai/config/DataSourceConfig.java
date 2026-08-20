package cn.ann.ai.config;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.Objects;

/**
 * @author zhang san
 * @description
 * @create 2026/1/15 8:36
 */
@Configuration
public class DataSourceConfig {

    @Bean("mysqlDataSource")
    @Primary
    public DataSource mysqlDataSource(
            @Value("${spring.datasource.mysql.driver-class-name}") String driverClassName,
            @Value("${spring.datasource.mysql.url}") String url,
            @Value("${spring.datasource.mysql.username}") String username,
            @Value("${spring.datasource.mysql.password}") String password,
            @Value("${spring.datasource.mysql.hikari.maximum-pool-size:10}") int maximumPoolSize,
            @Value("${spring.datasource.mysql.hikari.minimum-idle:5}") int minimumIdle,
            @Value("${spring.datasource.mysql.hikari.idle-timeout:30000}") long idleTimeout,
            @Value("${spring.datasource.mysql.hikari.connection-timeout:30000}") long
                    connectionTimeout,
            @Value("${spring.datasource.mysql.hikari.max-lifetime:1800000}") long maxLifetime) {

        //HikariCP是目前性能最优的Java连接池，Spring Boot 2.x默认使用
        //maximumPoolSize(10) ：业务场景下设置较大连接数，支持高并发访问
        //minimumIdle(5) ：保持一定数量的空闲连接，提升响应速度
        //idleTimeout(30秒) ：空闲连接超时释放，避免资源浪费
        //connectionTimeout(30秒) ：获取连接的最大等待时间
        //maxLifetime(30分钟) ：连接的最大存活时间，防止长连接问题
        //省去了握手和验证的时间，复用连接
        // 创建HikariCP连接池

        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setJdbcUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        // 连接池参数配置
        dataSource.setMaximumPoolSize(maximumPoolSize);     // 最大连接数：10
        dataSource.setMinimumIdle(minimumIdle);             // 最小空闲连接数：5
        dataSource.setIdleTimeout(idleTimeout);             // 空闲超时时间：30秒
        dataSource.setConnectionTimeout(connectionTimeout); // 连接超时时间：30秒
        dataSource.setMaxLifetime(maxLifetime);             // 连接最大生命周期：30分钟
        dataSource.setPoolName("MainHikariPool");           // 连接池名称

        return dataSource;
    }

    //@Qualifier("mysqlDataSource") ：明确指定注入名为"mysqlDataSource"的Bean
    //SqlSessionFactoryBean ：MyBatis与Spring集成的核心工厂类
    @Bean("sqlSessionFactory")
    public SqlSessionFactoryBean sqlSessionFactory(@Qualifier("mysqlDataSource")DataSource mysqlDataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(mysqlDataSource);

        //设置mybatis配置文件位置和xml文件位置
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();//解析路径
        sqlSessionFactoryBean.setConfigLocation(resolver.getResource("classpath:mybatis/config/mybatis-config.xml"));
        sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:mybatis/mapper/*.xml"));
        return sqlSessionFactoryBean;
    }

    @Bean("sqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("sqlSessionFactory") SqlSessionFactoryBean sqlSessionFactoryBean)throws Exception {
        return new SqlSessionTemplate(Objects.requireNonNull(sqlSessionFactoryBean.getObject()));
    }


//    连接池优化策略：
//
//    连接数设置较小（最大5个），因为向量查询通常是计算密集型，不需要大量并发连接
//
//    针对AI查询场景的特点进行优化，避免资源浪费
//
//    快速失败机制：
//
//    setInitializationFailTimeout(1) ：设置1毫秒快速失败
//
//    避免在向量库不可用时长时间等待，快速发现问题
//
//    setConnectionTestQuery("SELECT 1") ：简单的连接健康检查
    @Bean("pgVectorDataSource")
    public DataSource pgVectorDataSource(
            @Value("${spring.datasource.pgvector.driver-class-name}") String driverClassName,
            @Value("${spring.datasource.pgvector.url}") String url,
            @Value("${spring.datasource.pgvector.username}") String username,
            @Value("${spring.datasource.pgvector.password}") String password,
            @Value("${spring.datasource.pgvector.hikari.maximum-pool-size:5}") int maximumPoolSize,
            @Value("${spring.datasource.pgvector.hikari.minimum-idle:2}") int minimumIdle,
            @Value("${spring.datasource.pgvector.hikari.idle-timeout:30000}") long idleTimeout,
            @Value("${spring.datasource.pgvector.hikari.connection-timeout:30000}") long connectionTimeout) {

        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setJdbcUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        // 向量库专用连接池配置
        dataSource.setMaximumPoolSize(maximumPoolSize);     // 较小连接数：5
        dataSource.setMinimumIdle(minimumIdle);             // 较少空闲连接：2
        dataSource.setIdleTimeout(idleTimeout);
        dataSource.setConnectionTimeout(connectionTimeout);

        // 向量库特殊配置
        dataSource.setInitializationFailTimeout(1);        // 1ms快速失败
        dataSource.setConnectionTestQuery("SELECT 1");      // 连接测试查询
        dataSource.setAutoCommit(true);                     // 自动提交事务
        dataSource.setPoolName("PgVectorHikariPool");       // 连接池名称

        return dataSource;
    }

    @Bean("pgVectorJdbcTemplate")
    public JdbcTemplate pgVectorJdbcTemplate(@Qualifier("pgVectorDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
