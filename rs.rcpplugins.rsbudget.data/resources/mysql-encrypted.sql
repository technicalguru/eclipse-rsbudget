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
	name VARBINARY(256) NOT NULL,
	blz VARBINARY(32) NOT NULL,
	url VARBINARY(256) NOT NULL,
	creation_date VARBINARY(32) NOT NULL,
	change_date VARBINARY(32) NOT NULL,
	
	PRIMARY KEY(id)
) ENGINE = InnoDB;

CREATE TABLE accounts (
	id INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
	name VARBINARY(64) NOT NULL,
	owner VARBINARY(64) NOT NULL,
	bank INT(11) UNSIGNED NOT NULL,
	account_number VARBINARY(32) NOT NULL,
	login VARBINARY(32) NOT NULL,
	password VARBINARY(32) NOT NULL,
	active VARBINARY(8) NOT NULL,
	planning_relevant VARBINARY(8) NOT NULL,
	creation_date VARBINARY(32) NOT NULL,
	change_date VARBINARY(32) NOT NULL,
	
	PRIMARY KEY(id),
	FOREIGN KEY (bank) REFERENCES banks(id)
) ENGINE = InnoDB;

CREATE TABLE account_status (
	id INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
	account INT(11) UNSIGNED NOT NULL,
	date_of_status VARBINARY(32) NOT NULL,
	balance VARBINARY(32) NOT NULL,
	creation_date VARBINARY(32) NOT NULL,
	change_date VARBINARY(32) NOT NULL,
	
	PRIMARY KEY(id),
	FOREIGN KEY(account) REFERENCES accounts(id)
) ENGINE = InnoDB;

CREATE TABLE plans (
	id INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
	month VARBINARY(8) NOT NULL,
	balance_start VARBINARY(32),
	balance_end VARBINARY(32),
	comment VARBINARY(256),
	creation_date VARBINARY(32) NOT NULL,
	change_date VARBINARY(32) NOT NULL,
	
	PRIMARY KEY(id),
	UNIQUE INDEX month_index (month)
) ENGINE = InnoDB;

CREATE TABLE transaction_categories (
	id INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
	name VARBINARY(64) NOT NULL,
	is_default VARBINARY(8) NOT NULL,
	creation_date VARBINARY(32) NOT NULL,
	change_date VARBINARY(32) NOT NULL,
	
	PRIMARY KEY(id)
) ENGINE = InnoDB;

CREATE TABLE budgets (
	id INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
	name VARBINARY(64) NOT NULL,
	category INT(11) UNSIGNED NOT NULL,
	plan INT(11) UNSIGNED NOT NULL,
	amount VARBINARY(32) NOT NULL,
	display_order VARBINARY(8) NOT NULL,
	creation_date VARBINARY(32) NOT NULL,
	change_date VARBINARY(32) NOT NULL,
	
	PRIMARY KEY(id),
	FOREIGN KEY(plan) REFERENCES plans(id),
	FOREIGN KEY(category) REFERENCES transaction_categories(id)
) ENGINE = InnoDB;

CREATE TABLE periodical_budgets (
	id INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
	name VARBINARY(64) NOT NULL,
	category INT(11) UNSIGNED NOT NULL,
	amount VARBINARY(32) NOT NULL,
	planned_period VARBINARY(16) NOT NULL,
	display_order VARBINARY(8) NOT NULL,
	month_sequence_number VARBINARY(8) NOT NULL,
	creation_date VARBINARY(32) NOT NULL,
	change_date VARBINARY(32) NOT NULL,
	
	PRIMARY KEY(id),
	FOREIGN KEY(category) REFERENCES transaction_categories(id)
) ENGINE = InnoDB;

CREATE TABLE periodical_transaction (
	id INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
	name VARBINARY(64) NOT NULL,
	account INT(11) UNSIGNED NOT NULL,
	amount VARBINARY(32) NOT NULL,
	category INT(11) UNSIGNED NOT NULL,
	annotation VARBINARY(128),
	periodical_budget INT(11) UNSIGNED,
	planned_period VARBINARY(16) NOT NULL,
	month_sequence_number VARBINARY(8) NOT NULL,
	display_order VARBINARY(8) NOT NULL,
	creation_date VARBINARY(32) NOT NULL,
	change_date VARBINARY(32) NOT NULL,
	
	PRIMARY KEY(id),
	FOREIGN KEY(account) REFERENCES accounts(id),
	FOREIGN KEY(category) REFERENCES transaction_categories(id),
	FOREIGN KEY(periodical_budget) REFERENCES periodical_budgets(id)
) ENGINE = InnoDB;

CREATE TABLE planned_transactions (
	id INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
	name VARBINARY(64) NOT NULL,
	account INT(11) UNSIGNED NOT NULL,
	amount VARBINARY(32) NOT NULL,
	category INT(11) UNSIGNED NOT NULL,
	annotation VARBINARY(128),
	budget INT(11) UNSIGNED,
	plan INT(11) UNSIGNED NOT NULL,
	display_order VARBINARY(8) NOT NULL,
	creation_date VARBINARY(32) NOT NULL,
	change_date VARBINARY(32) NOT NULL,
	
	PRIMARY KEY(id),
	FOREIGN KEY(account) REFERENCES accounts(id),
	FOREIGN KEY(category) REFERENCES transaction_categories(id),
	FOREIGN KEY(plan) REFERENCES plans(id),
	FOREIGN KEY(budget) REFERENCES budgets(id)
) ENGINE = InnoDB;

CREATE TABLE transactions (
	id INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
	tx_hash VARBINARY(256) NOT NULL,
	transaction_text VARBINARY(128) NOT NULL,
	account INT(11) UNSIGNED NOT NULL,
	amount VARBINARY(32) NOT NULL,
	category INT(11) UNSIGNED NOT NULL,
	annotation VARBINARY(128),
	budget INT(11) UNSIGNED,
	planned_transaction INT(11) UNSIGNED,
	transaction_date VARBINARY(32) NOT NULL,
	value_date VARBINARY(32) NOT NULL,
	partner_name VARBINARY(64) NOT NULL,
	partner_bank VARBINARY(64) NOT NULL,
	partner_account VARBINARY(32) NOT NULL,
	plan INT(11) UNSIGNED NOT NULL,
	display_order VARBINARY(8) NOT NULL,
	creation_date VARBINARY(32) NOT NULL,
	change_date VARBINARY(32) NOT NULL,

	PRIMARY KEY(id),
	FOREIGN KEY(account) REFERENCES accounts(id),
	FOREIGN KEY(category) REFERENCES transaction_categories(id),
	FOREIGN KEY(budget) REFERENCES budgets(id),
	FOREIGN KEY(plan) REFERENCES plans(id),
	FOREIGN KEY(planned_transaction) REFERENCES planned_transactions(id)
) ENGINE = InnoDB;

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
	name VARBINARY(64) NOT NULL,
	unit VARBINARY(64) NOT NULL,
	show_history VARBINARY(32) NOT NULL,
	show_changes VARBINARY(32) NOT NULL,
	is_float_value VARBINARY(32) NOT NULL,
	creation_date VARBINARY(32) NOT NULL,
	change_date VARBINARY(32) NOT NULL,

	PRIMARY KEY(id)
) ENGINE=InnoDB;

CREATE TABLE historical_item_status (
	id INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
	item INT(11) UNSIGNED NOT NULL,
	date_of_status VARBINARY(32) NOT NULL,
	value VARBINARY(32) NOT NULL,
	creation_date VARBINARY(32) NOT NULL,
	change_date VARBINARY(32) NOT NULL,
	
	PRIMARY KEY(id),
	FOREIGN KEY(item) REFERENCES historical_items(id)
) ENGINE = InnoDB;

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
