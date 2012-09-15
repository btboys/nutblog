var ioc = {
	// 读取配置文件
	config : {
		type : "org.nutz.ioc.impl.PropertiesProxy",
		fields : {
			paths : [ "conf.properties" ]
		}
	},
	dataSource : {
		type : "org.apache.commons.dbcp.BasicDataSource",
		events : {
			depose : "close"
		},
		fields : {
			driverClassName:{
				java : "$config.get('db-driver')"
			},
			url : {
				java : "$config.get('db-url')"
			},
			username : {
				java : "$config.get('db-username')"
			},
			password : {
				java : "$config.get('db-password')"
			},
			initialSize : 2,
			maxActive : 50,
			minIdle : 2,
			maxWait : 1000,
			poolPreparedStatements : true,
			validationQuery : "select 1 from dual",
			timeBetweenEvictionRunsMillis : 60000,
			minEvictableIdleTimeMillis : 300000,
			testWhileIdle : true,
			testOnBorrow : false,
			testOnReturn : false
		}
	},
	dao : {
		type : "org.nutz.dao.impl.NutDao",
		fields : {
			dataSource : {
				refer : 'dataSource'
			}
		}
	}
};