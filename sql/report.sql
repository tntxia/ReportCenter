create table report(
	id varchar(32) not null,
	name varchar(50),
	report_type varchar(20),
	file_path varchar(500),
	progress int,
	status varchar(50),
	create_time datetime,
	last_update datetime
	
)

create table report_template(
	id varchar(32) not null,
	name varchar(150),
	display_name varchar(200),
	service_url varchar(300),
	create_time datetime,
	last_update datetime
)

create table report_template_col(
	id varchar(32) not null,
	name varchar(200),
	field varchar(100),
	template_id varchar(32) not null,
	data_type varchar(20),
	create_time datetime,
	last_update datetime
)