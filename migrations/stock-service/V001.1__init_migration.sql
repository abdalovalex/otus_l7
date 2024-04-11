CREATE TABLE IF NOT EXISTS public."stock"
(
    id         serial NOT NULL,
    product_id  int NOT NULL,
    count      int NOT NULL,
    price      numeric(15,2) NOT NULL,
    CONSTRAINT pk_stock PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public."order"
(
    id         serial NOT NULL,
    order_id   int NOT NULL,
    stock_id   int NOT NULL,
    count      int NOT NULL,
    price      numeric(15,2) NOT NULL,
    result     varchar(50) NULL,
    CONSTRAINT pk_order PRIMARY KEY (id),
    CONSTRAINT fk_customer FOREIGN KEY(stock_id) REFERENCES stock(id)
);

INSERT INTO "stock" (count, price, product_id)
VALUES(1000, 1.0, 1);
