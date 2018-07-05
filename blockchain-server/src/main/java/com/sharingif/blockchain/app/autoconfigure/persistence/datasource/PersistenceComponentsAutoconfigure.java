package com.sharingif.blockchain.app.autoconfigure.persistence.datasource;

import com.sharingif.cube.persistence.database.DataSourcePoolConfig;
import com.sharingif.cube.security.confidentiality.encrypt.TextEncryptor;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
public class PersistenceComponentsAutoconfigure {


}