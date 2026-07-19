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

        //HikariCP鏄洰鍓嶆€ц兘鏈€浼樼殑Java杩炴帴姹狅紝Spring Boot 2.x榛樿浣跨敤
        //maximumPoolSize(10) 锛氫笟鍔″満鏅笅璁剧疆杈冨ぇ杩炴帴鏁帮紝鏀寔楂樺苟鍙戣闂?
        //minimumIdle(5) 锛氫繚鎸佷竴瀹氭暟閲忕殑绌洪棽杩炴帴锛屾彁鍗囧搷搴旈€熷害
        //idleTimeout(30绉? 锛氱┖闂茶繛鎺ヨ秴鏃堕噴鏀撅紝閬垮厤璧勬簮娴垂
        //connectionTimeout(30绉? 锛氳幏鍙栬繛鎺ョ殑鏈€澶х瓑寰呮椂闂?
        //maxLifetime(30鍒嗛挓) 锛氳繛鎺ョ殑鏈€澶у瓨娲绘椂闂达紝闃叉闀胯繛鎺ラ棶棰?
        //鐪佸幓浜嗘彙鎵嬪拰楠岃瘉鐨勬椂闂达紝澶嶇敤杩炴帴
        // 鍒涘缓HikariCP杩炴帴姹?

        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setJdbcUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        // 杩炴帴姹犲弬鏁伴厤缃?
        dataSource.setMaximumPoolSize(maximumPoolSize);     // 鏈€澶ц繛鎺ユ暟锛?0
        dataSource.setMinimumIdle(minimumIdle);             // 鏈€灏忕┖闂茶繛鎺ユ暟锛?
        dataSource.setIdleTimeout(idleTimeout);             // 绌洪棽瓒呮椂鏃堕棿锛?0绉?
        dataSource.setConnectionTimeout(connectionTimeout); // 杩炴帴瓒呮椂鏃堕棿锛?0绉?
        dataSource.setMaxLifetime(maxLifetime);             // 杩炴帴鏈€澶х敓鍛藉懆鏈燂細30鍒嗛挓
        dataSource.setPoolName("MainHikariPool");           // 杩炴帴姹犲悕绉?

        return dataSource;
    }

    //@Qualifier("mysqlDataSource") 锛氭槑纭寚瀹氭敞鍏ュ悕涓?mysqlDataSource"鐨凚ean
    //SqlSessionFactoryBean 锛歁yBatis涓嶴pring闆嗘垚鐨勬牳蹇冨伐鍘傜被
    @Bean("sqlSessionFactory")
    public SqlSessionFactoryBean sqlSessionFactory(@Qualifier("mysqlDataSource")DataSource mysqlDataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(mysqlDataSource);

        //璁剧疆mybatis閰嶇疆鏂囦欢浣嶇疆鍜寈ml鏂囦欢浣嶇疆
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();//瑙ｆ瀽璺緞
        sqlSessionFactoryBean.setConfigLocation(resolver.getResource("classpath:mybatis/config/mybatis-config.xml"));
        sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:mybatis/mapper/*.xml"));
        return sqlSessionFactoryBean;
    }

    @Bean("sqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("sqlSessionFactory") SqlSessionFactoryBean sqlSessionFactoryBean)throws Exception {
        return new SqlSessionTemplate(Objects.requireNonNull(sqlSessionFactoryBean.getObject()));
    }


//    杩炴帴姹犱紭鍖栫瓥鐣ワ細
//
//    杩炴帴鏁拌缃緝灏忥紙鏈€澶?涓級锛屽洜涓哄悜閲忔煡璇㈤€氬父鏄绠楀瘑闆嗗瀷锛屼笉闇€瑕佸ぇ閲忓苟鍙戣繛鎺?
//
//    閽堝AI鏌ヨ鍦烘櫙鐨勭壒鐐硅繘琛屼紭鍖栵紝閬垮厤璧勬簮娴垂
//
//    蹇€熷け璐ユ満鍒讹細
//
//    setInitializationFailTimeout(1) 锛氳缃?姣蹇€熷け璐?
//
//    閬垮厤鍦ㄥ悜閲忓簱涓嶅彲鐢ㄦ椂闀挎椂闂寸瓑寰咃紝蹇€熷彂鐜伴棶棰?
//
//    setConnectionTestQuery("SELECT 1") 锛氱畝鍗曠殑杩炴帴鍋ュ悍妫€鏌?
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

        // 鍚戦噺搴撲笓鐢ㄨ繛鎺ユ睜閰嶇疆
        dataSource.setMaximumPoolSize(maximumPoolSize);     // 杈冨皬杩炴帴鏁帮細5
        dataSource.setMinimumIdle(minimumIdle);             // 杈冨皯绌洪棽杩炴帴锛?
        dataSource.setIdleTimeout(idleTimeout);
        dataSource.setConnectionTimeout(connectionTimeout);

        // 鍚戦噺搴撶壒娈婇厤缃?
        dataSource.setInitializationFailTimeout(1);        // 1ms蹇€熷け璐?
        dataSource.setConnectionTestQuery("SELECT 1");      // 杩炴帴娴嬭瘯鏌ヨ
        dataSource.setAutoCommit(true);                     // 鑷姩鎻愪氦浜嬪姟
        dataSource.setPoolName("PgVectorHikariPool");       // 杩炴帴姹犲悕绉?

        return dataSource;
    }

    @Bean("pgVectorJdbcTemplate")
    public JdbcTemplate pgVectorJdbcTemplate(@Qualifier("pgVectorDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}

