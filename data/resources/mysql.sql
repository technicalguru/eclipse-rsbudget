DROP TABLE IF EXISTS budget_recognitions;
DROP TABLE IF EXISTS category_recognitions;
DROP TABLE IF EXISTS historical_item_status;
DROP TABLE IF EXISTS historical_items;
DROP TABLE IF EXISTS settings;
DROP TABLE IF EXISTS transactions;
DROP TABLE IF EXISTS planned_transactions;
DROP TABLE IF EXISTS periodical_transaction;
DROP TABLE IF EXISTS periodical_budgets;
DROP TABLE IF EXISTS budgets;
DROP TABLE IF EXISTS transaction_categories;
DROP TABLE IF EXISTS plans;
DROP TABLE IF EXISTS account_status;
DROP TABLE IF EXISTS accounts;
DROP TABLE IF EXISTS banks;

CREATE TABLE banks (
	id INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
	name VARCHAR(200) NOT NULL,
	blz VARCHAR(8) NOT NULL,
	url VARCHAR(200) NOT NULL,
	creation_date TIMESTAMP NOT NULL DEFAULT 0,
	change_date TIMESTAMP NOT NULL DEFAULT 0,

	PRIMARY KEY(id)
) ENGINE=InnoDB;

CREATE TABLE accounts (
	id INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
	name VARCHAR(50) NOT NULL,
	owner VARCHAR(50) NOT NULL,
	bank INT(11) UNSIGNED NOT NULL,
	account_number VARCHAR(15) NOT NULL,
	login VARCHAR(20) NOT NULL,
	password VARCHAR(200) NOT NULL,
	active int(1) NOT NULL DEFAULT 1,
	planning_relevant int(1) NOT NULL DEFAULT 1,
	creation_date TIMESTAMP NOT NULL DEFAULT 0,
	change_date TIMESTAMP NOT NULL DEFAULT 0,

	PRIMARY KEY(id),
	FOREIGN KEY (bank) REFERENCES banks(id)
) ENGINE=InnoDB;

CREATE TABLE account_status (
	id INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
	account INT(11) UNSIGNED NOT NULL,
	date_of_status TIMESTAMP NOT NULL DEFAULT 0,
	balance DECIMAL(10,2) NOT NULL,
	creation_date TIMESTAMP NOT NULL DEFAULT 0,
	change_date TIMESTAMP NOT NULL DEFAULT 0,
	
	PRIMARY KEY(id),
	FOREIGN KEY(account) REFERENCES accounts(id)
) ENGINE=InnoDB;

CREATE TABLE plans (
	id INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
	month VARCHAR(10) NOT NULL,
	balance_start DECIMAL(10,2),
	balance_end DECIMAL(10,2),
	comment VARCHAR(256),
	creation_date TIMESTAMP NOT NULL DEFAULT 0,
	change_date TIMESTAMP NOT NULL DEFAULT 0,
	
	PRIMARY KEY(id),
	UNIQUE INDEX month_index (month)
) ENGINE=InnoDB;

CREATE TABLE transaction_categories (
	id INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
	name VARCHAR(50) NOT NULL,
	is_default INT(1) NOT NULL DEFAULT 0,
	creation_date TIMESTAMP NOT NULL DEFAULT 0,
	change_date TIMESTAMP NOT NULL DEFAULT 0,
	
	PRIMARY KEY(id)
) ENGINE=InnoDB;

CREATE TABLE budgets (
	id INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
	name VARCHAR(100) NOT NULL,
	category INT(11) UNSIGNED NOT NULL,
	plan INT(11) UNSIGNED NOT NULL,
	amount DECIMAL(10,2) NOT NULL,
	display_order INT(5) UNSIGNED NOT NULL,
	creation_date TIMESTAMP NOT NULL DEFAULT 0,
	change_date TIMESTAMP NOT NULL DEFAULT 0,
	
	PRIMARY KEY(id),
	FOREIGN KEY(plan) REFERENCES plans(id),
	FOREIGN KEY(category) REFERENCES transaction_categories(id)
) ENGINE=InnoDB;

CREATE TABLE periodical_budgets (
	id INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
	name VARCHAR(100) NOT NULL,
	category INT(11) UNSIGNED NOT NULL,
	amount DECIMAL(10,2) NOT NULL,
	planned_period VARCHAR(15) NOT NULL,
	display_order INT(5) UNSIGNED NOT NULL,
	month_sequence_number INT(3) NOT NULL DEFAULT 1,
	creation_date TIMESTAMP NOT NULL DEFAULT 0,
	change_date TIMESTAMP NOT NULL DEFAULT 0,
	
	PRIMARY KEY(id),
	FOREIGN KEY(category) REFERENCES transaction_categories(id)
) ENGINE=InnoDB;

CREATE TABLE periodical_transaction (
	id INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
	name VARCHAR(100) NOT NULL,
	account INT(11) UNSIGNED NOT NULL,
	amount DECIMAL(10,2) NOT NULL,
	category INT(11) UNSIGNED NOT NULL,
	annotation VARCHAR(200),
	periodical_budget INT(11) UNSIGNED,
	planned_period VARCHAR (15) NOT NULL,
	month_sequence_number INT(3) NOT NULL DEFAULT 1,
	display_order INT(5) UNSIGNED NOT NULL,
	creation_date TIMESTAMP NOT NULL DEFAULT 0,
	change_date TIMESTAMP NOT NULL DEFAULT 0,
	
	PRIMARY KEY(id),
	FOREIGN KEY(account) REFERENCES accounts(id),
	FOREIGN KEY(category) REFERENCES transaction_categories(id),
	FOREIGN KEY(periodical_budget) REFERENCES periodical_budgets(id)
) ENGINE=InnoDB;

CREATE TABLE planned_transactions (
	id INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
	name VARCHAR(100) NOT NULL,
	account INT(11) UNSIGNED NOT NULL,
	amount DECIMAL(10,2) NOT NULL,
	category INT(11) UNSIGNED NOT NULL,
	annotation VARCHAR(200),
	budget INT(11) UNSIGNED,
	plan INT(11) UNSIGNED NOT NULL,
	display_order INT(5) UNSIGNED NOT NULL,
	creation_date TIMESTAMP NOT NULL DEFAULT 0,
	change_date TIMESTAMP NOT NULL DEFAULT 0,
	
	PRIMARY KEY(id),
	FOREIGN KEY(account) REFERENCES accounts(id),
	FOREIGN KEY(category) REFERENCES transaction_categories(id),
	FOREIGN KEY(plan) REFERENCES plans(id),
	FOREIGN KEY(budget) REFERENCES budgets(id)
) ENGINE=InnoDB;

CREATE TABLE transactions (
	id INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
	tx_hash VARCHAR(255) NOT NULL,
	transaction_text VARCHAR(100) NOT NULL,
	account INT(11) UNSIGNED NOT NULL,
	amount DECIMAL(10,2) NOT NULL,
	category INT(11) UNSIGNED NOT NULL,
	annotation VARCHAR(200),
	budget INT(11) UNSIGNED,
	planned_transaction INT(11) UNSIGNED,
	transaction_date TIMESTAMP NOT NULL DEFAULT 0,
	value_date TIMESTAMP NOT NULL DEFAULT 0,
	partner_name VARCHAR(50) NOT NULL,
	partner_bank VARCHAR(50) NOT NULL,
	partner_account VARCHAR(20) NOT NULL,
	plan INT(11) UNSIGNED NOT NULL,
	display_order INT(5) UNSIGNED NOT NULL,
	creation_date TIMESTAMP NOT NULL DEFAULT 0,
	change_date TIMESTAMP NOT NULL DEFAULT 0,

	PRIMARY KEY(id),
	FOREIGN KEY(account) REFERENCES accounts(id),
	FOREIGN KEY(category) REFERENCES transaction_categories(id),
	FOREIGN KEY(budget) REFERENCES budgets(id),
	FOREIGN KEY(plan) REFERENCES plans(id),
	FOREIGN KEY(planned_transaction) REFERENCES planned_transactions(id)
) ENGINE=InnoDB;

CREATE TABLE settings (
	id INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
	setting_key VARCHAR(255) NOT NULL,
	setting_value VARCHAR(255) NOT NULL,
	creation_date TIMESTAMP NOT NULL DEFAULT 0,
	change_date TIMESTAMP NOT NULL DEFAULT 0,
	
	PRIMARY KEY(id),
	UNIQUE INDEX key_index (setting_key)
) ENGINE=InnoDB;

CREATE TABLE historical_items (
	id INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
	name VARCHAR(50) NOT NULL,
	unit VARCHAR(50) NOT NULL,
	show_history int(1) NOT NULL DEFAULT 1,
	show_changes int(1) NOT NULL DEFAULT 1,
	is_float_value int(1) NOT NULL DEFAULT 1,
	creation_date TIMESTAMP NOT NULL DEFAULT 0,
	change_date TIMESTAMP NOT NULL DEFAULT 0,

	PRIMARY KEY(id)
) ENGINE=InnoDB;

CREATE TABLE historical_item_status (
	id INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
	item INT(11) UNSIGNED NOT NULL,
	date_of_status TIMESTAMP NOT NULL DEFAULT 0,
	value DECIMAL(10,2) NOT NULL,
	creation_date TIMESTAMP NOT NULL DEFAULT 0,
	change_date TIMESTAMP NOT NULL DEFAULT 0,
	
	PRIMARY KEY(id),
	FOREIGN KEY(item) REFERENCES historical_items(id)
) ENGINE=InnoDB;

CREATE TABLE budget_recognitions (
	id INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
	budget INT(11) UNSIGNED,
	expression VARCHAR(200) NOT NULL,
	rank INT(5) UNSIGNED NOT NULL,
	creation_date TIMESTAMP NOT NULL DEFAULT 0,
	change_date TIMESTAMP NOT NULL DEFAULT 0,

	PRIMARY KEY(id),
	FOREIGN KEY(budget) REFERENCES periodical_budgets(id)
) ENGINE=InnoDB;

CREATE TABLE category_recognitions (
	id INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
	category INT(11) UNSIGNED,
	expression VARCHAR(200) NOT NULL,
	rank INT(5) UNSIGNED NOT NULL,
	creation_date TIMESTAMP NOT NULL DEFAULT 0,
	change_date TIMESTAMP NOT NULL DEFAULT 0,

	PRIMARY KEY(id),
	FOREIGN KEY(category) REFERENCES transaction_categories(id)
) ENGINE=InnoDB;