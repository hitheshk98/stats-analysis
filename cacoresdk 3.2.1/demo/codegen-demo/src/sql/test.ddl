DROP TABLE IF EXISTS J
;

DROP TABLE IF EXISTS J_K
;

DROP TABLE IF EXISTS K
;

DROP TABLE IF EXISTS O
;

DROP TABLE IF EXISTS ONT_CLOS
;

DROP TABLE IF EXISTS ONT_NODE
;

DROP TABLE IF EXISTS ONT_REL
;


CREATE TABLE J
(
	prop1 VARCHAR(255),
	id NUMERIC(38) NOT NULL,
	ont_node_id NUMERIC(38),
	PRIMARY KEY (id),
	KEY (ont_node_id)
) 
;
CREATE TABLE J_K
(
	j_id NUMERIC(38) NOT NULL,
	k_id NUMERIC(38) NOT NULL,
	KEY (j_id),
	KEY (k_id)
) 
;
CREATE TABLE K
(
	prop1 INTEGER,
	prop2 VARCHAR(50),
	id NUMERIC(38) NOT NULL,
	prop3 DATE,
	prop4 FLOAT(0),
	prop5 VARCHAR(50),
	parent_id NUMERIC(38),
	discriminator VARCHAR(255) NOT NULL,
	javatype VARCHAR(255) NOT NULL,
	PRIMARY KEY (id),
	KEY (parent_id)
) 
;
CREATE TABLE O
(
	id NUMERIC(38) NOT NULL,
	n_id NUMERIC(38),
	prop1 VARCHAR(50),
	PRIMARY KEY (id),
	KEY (n_id)
) 
;
CREATE TABLE ONT_CLOS
(
	id NUMERIC(38) NOT NULL,
	ancestor_id NUMERIC(38),
	descendant_id NUMERIC(38),
	PRIMARY KEY (id),
	KEY (ancestor_id),
	KEY (descendant_id)
) 
;
CREATE TABLE ONT_NODE
(
	id NUMERIC(38) NOT NULL,
	name VARCHAR(50),
	PRIMARY KEY (id)
) 
;
CREATE TABLE ONT_REL
(
	id NUMERIC(38) NOT NULL,
	parent_id NUMERIC(38),
	child_id NUMERIC(38),
	type_id NUMERIC(38),
	PRIMARY KEY (id),
	KEY (parent_id),
	KEY (child_id),
	KEY (type_id)
) 
;




ALTER TABLE J ADD CONSTRAINT FK_J_ONT_NODE 
	FOREIGN KEY (ont_node_id) REFERENCES ONT_NODE (id)
;

ALTER TABLE J_K ADD CONSTRAINT FK_J_K_J 
	FOREIGN KEY (j_id) REFERENCES J (id)
;

ALTER TABLE J_K ADD CONSTRAINT FK_J_K_K 
	FOREIGN KEY (k_id) REFERENCES K (id)
;

ALTER TABLE K ADD CONSTRAINT FK_K_K 
	FOREIGN KEY (parent_id) REFERENCES K (id)
;

ALTER TABLE O ADD CONSTRAINT FK_O_K 
	FOREIGN KEY (n_id) REFERENCES K (id)
;

ALTER TABLE ONT_CLOS ADD CONSTRAINT FK_ONT_CLOS_ONT_NODE 
	FOREIGN KEY (ancestor_id) REFERENCES ONT_NODE (id)
;

ALTER TABLE ONT_CLOS ADD CONSTRAINT FK_ONT_CLOS_ONT_NODE_1 
	FOREIGN KEY (descendant_id) REFERENCES ONT_NODE (id)
;

ALTER TABLE ONT_REL ADD CONSTRAINT FK_ONT_REL_ONT_NODE 
	FOREIGN KEY (parent_id) REFERENCES ONT_NODE (id)
;

ALTER TABLE ONT_REL ADD CONSTRAINT FK_ONT_REL_ONT_NODE_1 
	FOREIGN KEY (child_id) REFERENCES ONT_NODE (id)
;

ALTER TABLE ONT_REL ADD CONSTRAINT FK_ONT_REL_ONT_NODE_2 
	FOREIGN KEY (type_id) REFERENCES ONT_NODE (id)
;
