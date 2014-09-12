--
-- Create DB and tabless
-- Author: MICKCHEN
-- Version: 1.0
-- History: 2013-10-18 Create by MICKCHEN
--

CREATE DATABASE arh_db DEFAULT charset=utf8;

USE arh_db;

--
-- Create business table
--
CREATE TABLE tb_busi(id INTEGER PRIMARY KEY AUTO_INCREMENT,
					 busi_name VARCHAR(32) NOT NULL,
					 busi_desc VARCHAR(256)) ENGINE = INNODB DEFAULT CHARSET=utf8;

--
-- Create module table
--
CREATE TABLE tb_module(id INTEGER PRIMARY KEY AUTO_INCREMENT,
					   module_name VARCHAR(128) NOT NULL,
					   module_desc VARCHAR(256),
					   parent_busi_id INTEGER NOT NULL) ENGINE = INNODB DEFAULT CHARSET=utf8;
					   
--
-- Create function node table
--					   
CREATE TABLE tb_fun(id INTEGER PRIMARY KEY AUTO_INCREMENT,
					fun_id INTEGER NOT NULL,
					fun_name VARCHAR(1024) NOT NULL,
					fun_desc VARCHAR(256),
                    module_id INTEGER DEFAULT 0) ENGINE = INNODB DEFAULT CHARSET=utf8;

--
-- Create function relation table
--
CREATE TABLE tb_fun_relation(id INTEGER PRIMARY KEY AUTO_INCREMENT,
							 child_fun_id INTEGER NOT NULL,
							 parent_fun_id INTEGER NOT NULL,
							 module_id INTEGER DEFAULT 0) ENGINE = INNODB DEFAULT CHARSET=utf8;

--
-- Create command table
--
CREATE TABLE tb_cmd_interface(id INTEGER PRIMARY KEY AUTO_INCREMENT,
							  command VARCHAR(32),
							  interface_name varchar(256),
							  busi_id INTEGER DEFAULT 0) ENGINE = INNODB DEFAULT CHARSET=utf8;

--
-- Create file functions
--
CREATE TABLE tb_file_fun(id INTEGER PRIMARY KEY AUTO_INCREMENT,
						 file_name VARCHAR(128),
						 fun_name VARCHAR(256),
						 fun_count INTEGER DEFAULT 0) ENGINE = INNODB DEFAULT CHARSET=utf8;
