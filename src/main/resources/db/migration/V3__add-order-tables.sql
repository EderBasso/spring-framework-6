drop table if exists beer_order;

drop table if exists beer_order_line;

create table beer_order (
                            id varchar(36) not null,
                            version integer,
                            created_date datetime(6),
                            update_date datetime(6),
                            customer_ref varchar(255),
                            customer_id varchar(36),
                    PRIMARY KEY (id),
                    CONSTRAINT FOREIGN KEY (customer_id)
                        REFERENCES customer(id)
) engine=InnoDB;

create table beer_order_line (
                            id varchar(36) not null,
                            version integer,
                            created_date datetime(6),
                            update_date datetime(6),
                            beer_id varchar(36),
                            order_quantity integer,
                            quantity_allocated integer,
                            beer_order_id varchar(36),
                    PRIMARY KEY (id),
                    CONSTRAINT FOREIGN KEY (beer_id)
                        REFERENCES beer(id),
                    CONSTRAINT FOREIGN KEY (beer_order_id)
                        REFERENCES beer_order(id)
) engine=InnoDB;