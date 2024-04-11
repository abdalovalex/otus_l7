package org.example.orderservice.saga;

public enum Events {
    ORDER_CREATE,
    ORDER_COMPLETE,
    ORDER_FAILED,

    STOCK_UPDATE,
    STOCK_ROLLBACK,

    PAYMENT_CREATE,
    PAYMENT_CANCEL,

    DELIVERY_CREATE
}

/*


order_create (event_order_create) (event_order_complete) (event_order_failed)
-> stock_update (event_stock_update) (event_stock_failed)
-> payment_create (ePAYMENT_FAILEDvent_payment_complete) (event_payment_failed)
-> delivery_create (event_delivery_complete) (event_delivery_failed)
->











 */