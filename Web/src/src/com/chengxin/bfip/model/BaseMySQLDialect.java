package com.chengxin.bfip.model;

import java.sql.Types;

import org.hibernate.Hibernate;
import org.hibernate.dialect.MySQLDialect;

public class BaseMySQLDialect extends MySQLDialect {
	public BaseMySQLDialect() {
		super();
		registerHibernateType(Types.LONGVARCHAR, Hibernate.TEXT.getName());
	}
}
