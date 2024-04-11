package org.example.orderservice.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.orderservice.saga.Events;
import org.example.orderservice.saga.States;
import org.example.orderservice.saga.action.DeliveryCreateAction;
import org.example.orderservice.saga.action.OrderCompleteAction;
import org.example.orderservice.saga.action.OrderFailedAction;
import org.example.orderservice.saga.action.PaymentCancelAction;
import org.example.orderservice.saga.action.PaymentCreateAction;
import org.example.orderservice.saga.action.StockRollbackAction;
import org.example.orderservice.saga.action.StockUpdateAction;

import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.action.Actions;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.persist.StateMachineRuntimePersister;

import static org.example.orderservice.saga.States.DELIVERY_CREATE;
import static org.example.orderservice.saga.States.ORDER_COMPLETE;
import static org.example.orderservice.saga.States.ORDER_CREATE;
import static org.example.orderservice.saga.States.ORDER_FAILED;
import static org.example.orderservice.saga.States.PAYMENT_CANCEL;
import static org.example.orderservice.saga.States.PAYMENT_CREATE;
import static org.example.orderservice.saga.States.STOCK_ROLLBACK;
import static org.example.orderservice.saga.States.STOCK_UPDATE;

@Configuration
@EnableStateMachineFactory
@RequiredArgsConstructor
@Slf4j
public class SagaConfigure extends EnumStateMachineConfigurerAdapter<States, Events> {
    private final OrderCompleteAction orderCompleteAction;
    private final OrderFailedAction orderFailedAction;
    private final StockUpdateAction stockUpdateAction;
    private final StockRollbackAction stockRollbackAction;
    private final PaymentCreateAction paymentCreateAction;
    private final PaymentCancelAction paymentCancelAction;
    private final DeliveryCreateAction deliveryCreateAction;
    private final StateMachineRuntimePersister<States, Events, String> stateMachineRuntimePersister;

    @Override
    public void configure(StateMachineConfigurationConfigurer<States, Events> config)
            throws Exception {
        config
                .withPersistence()
                .runtimePersister(stateMachineRuntimePersister)
                .and()
                .withConfiguration()
                .autoStartup(true);
//                .listener(listener());
    }

    @Override
    public void configure(StateMachineStateConfigurer<States, Events> states)
            throws Exception {
        states
                .withStates()
                .initial(ORDER_CREATE)
                .state(ORDER_COMPLETE, Actions.errorCallingAction(orderCompleteAction, moveToFailedState()))
                .state(ORDER_FAILED, Actions.errorCallingAction(orderFailedAction, moveToFailedState()))
                .state(STOCK_UPDATE, Actions.errorCallingAction(stockUpdateAction, moveToFailedState()))
                .state(STOCK_ROLLBACK, Actions.errorCallingAction(stockRollbackAction, moveToFailedState()))
                .state(PAYMENT_CREATE, Actions.errorCallingAction(paymentCreateAction, moveToFailedState()))
                .state(PAYMENT_CANCEL, Actions.errorCallingAction(paymentCancelAction, moveToFailedState()))
                .state(DELIVERY_CREATE, Actions.errorCallingAction(deliveryCreateAction, moveToFailedState()))
                .end(ORDER_COMPLETE)
                .end(ORDER_FAILED);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<States, Events> transitions)
            throws Exception {
        transitions
                .withExternal()
                .source(ORDER_CREATE)
                .target(STOCK_UPDATE)
                .event(Events.STOCK_UPDATE)

                .and().withExternal()
                .source(STOCK_UPDATE)
                .target(PAYMENT_CREATE)
                .event(Events.PAYMENT_CREATE)

                .and().withExternal()
                .source(PAYMENT_CREATE)
                .target(DELIVERY_CREATE)
                .event(Events.DELIVERY_CREATE)

                .and().withExternal()
                .source(DELIVERY_CREATE)
                .target(ORDER_COMPLETE)
                .event(Events.ORDER_COMPLETE)

                .and().withExternal()
                .source(DELIVERY_CREATE)
                .target(PAYMENT_CANCEL)
                .event(Events.PAYMENT_CANCEL)

                .and().withExternal()
                .source(PAYMENT_CANCEL)
                .target(STOCK_ROLLBACK)
                .event(Events.STOCK_ROLLBACK)

                .and().withExternal()
                .source(STOCK_ROLLBACK)
                .target(ORDER_FAILED)
                .event(Events.ORDER_FAILED)

                .and().withExternal()
                .source(PAYMENT_CREATE)
                .target(STOCK_ROLLBACK)
                .event(Events.STOCK_ROLLBACK)

                .and().withExternal()
                .source(STOCK_UPDATE)
                .target(ORDER_FAILED)
                .event(Events.ORDER_FAILED)

                .and().withExternal()
                .source(ORDER_CREATE)
                .target(ORDER_FAILED)
                .event(Events.ORDER_FAILED);
    }

    public Action<States, Events> moveToFailedState() {
        return stateContext -> log.error(stateContext.getException().getMessage());
    }

//    @Bean
//    public StateMachineService<WorkflowStatus, Events> stateMachineService(
//            final StateMachineFactory<WorkflowStatus, Events> stateMachineFactory,
//            final StateMachinePersist<WorkflowStatus, Events, String> stateMachinePersist) {
//        return new DefaultStateMachineService<>(stateMachineFactory, stateMachinePersist);
//    }

//    @Bean
//    public StateMachineListener<WorkflowStatus, Events> listener() {
//        return new StateMachineListenerAdapter<>() {
//            public void stateChanged(State<WorkflowStatus, Events> from, State<WorkflowStatus, Events> to) {
//                System.out.println("State change to " + to.getId());
//            }
//        };
}
