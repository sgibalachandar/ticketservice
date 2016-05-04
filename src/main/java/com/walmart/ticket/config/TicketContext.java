package com.walmart.ticket.config;

import com.walmart.ticket.dao.ITicketDao;
import com.walmart.ticket.dao.TicketDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.sql.DataSource;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class TicketContext {

	@Bean(name="dataSource")
	public DataSource dataSource() {

		EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
		EmbeddedDatabase db = builder
			.setType(EmbeddedDatabaseType.DERBY) //.H2 or .DERBY
			.addScript("db/sql/create-db.sql")
			.addScript("db/sql/insert-data.sql")
			.build();
		return db;
	}
	@Bean(name="jdbcTemplate")
	public NamedParameterJdbcTemplate getJdbcTemplate() {

		return new NamedParameterJdbcTemplate(dataSource());
	}


}
