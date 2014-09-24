package com.nicta.metrics.config;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.hibernate.cache.HashtableCacheProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.jolbox.bonecp.BoneCPDataSource;

@Configuration
@PropertySource("classpath:config.properties")
@EnableJpaRepositories(basePackages = {"com.nicta.metrics.dao"})
@EnableTransactionManagement
@ComponentScan(basePackages = {"com.nicta.metrics.dao", "com.nicta.metrics.service"})
public class JpaConfiguration {

	@Autowired
	Environment env;
	
	@Bean
    public DataSource dataSource() {
		
		String user = env.getProperty("db.username"),
                pw = env.getProperty("db.password"),
                url = env.getProperty("db.url"),
                driverClassName = env.getProperty("db.driver");

		BoneCPDataSource boneCPDataSource = new BoneCPDataSource();
		
        boneCPDataSource.setDriverClass(driverClassName);
        boneCPDataSource.setJdbcUrl(url);
        boneCPDataSource.setUsername(user);
        boneCPDataSource.setPassword(pw);
        
        boneCPDataSource.setIdleConnectionTestPeriodInMinutes(60);
        boneCPDataSource.setIdleMaxAgeInMinutes(420);
        boneCPDataSource.setMaxConnectionsPerPartition(30);
        boneCPDataSource.setMinConnectionsPerPartition(10);
        boneCPDataSource.setPartitionCount(3);
        boneCPDataSource.setAcquireIncrement(5);
        boneCPDataSource.setStatementsCacheSize(100);
 
        return boneCPDataSource;
    }

	@Bean
	public Map<String, Object> jpaProperties() {
		Map<String, Object> props = new HashMap<String, Object>();
		props.put("hibernate.dialect", env.getProperty("hibernate.dialect"));
		props.put("hibernate.cache.provider_class", HashtableCacheProvider.class.getName());
		return props;
	}

	@Bean
	public JpaVendorAdapter jpaVendorAdapter() {
		HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
		hibernateJpaVendorAdapter.setShowSql(false);
		hibernateJpaVendorAdapter.setGenerateDdl(true);
		hibernateJpaVendorAdapter.setDatabase(Database.MYSQL);
		return hibernateJpaVendorAdapter;
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean() {
		LocalContainerEntityManagerFactoryBean lef = new LocalContainerEntityManagerFactoryBean();
		lef.setDataSource(dataSource());
		lef.setJpaPropertyMap(this.jpaProperties());
		lef.setJpaVendorAdapter(this.jpaVendorAdapter());
		lef.setPackagesToScan("com.nicta.metrics.domain");
		return lef;
	}
	
	@Bean
    public EntityManagerFactory entityManagerFactory() {
		return localContainerEntityManagerFactoryBean().getObject();
	}
	
	@Bean
	public PlatformTransactionManager transactionManager() {
		return new JpaTransactionManager( entityManagerFactory() );
	}
	
}
