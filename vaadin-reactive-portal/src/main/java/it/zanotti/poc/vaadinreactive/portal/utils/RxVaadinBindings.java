package it.zanotti.poc.vaadinreactive.portal.utils;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ClickNotifier;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.shared.Registration;
import reactor.core.CoreSubscriber;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;

import java.util.Objects;

/**
 * @author Michele Zanotti on 02/08/20
 **/
public final class RxVaadinBindings {
    private RxVaadinBindings() {
        throw new UnsupportedOperationException("Utility class not supposed to be instantiated");
    }

    public static Flux<Object> onButtonClicks(ClickNotifier<Button> clickNotifier) {
        return new ClickNotifierFlux<>(clickNotifier);
    }

    private static class ClickNotifierFlux<C extends Component> extends Flux<Object> {
        private final ClickNotifier<C> clickNotifier;

        private ClickNotifierFlux(ClickNotifier<C> clickNotifier) {
            this.clickNotifier = clickNotifier;
        }

        @Override
        public void subscribe(CoreSubscriber<? super Object> subscriber) {
            DisposableClickListener<C> disposableClickListener = new DisposableClickListener<>(subscriber);
            Registration registration = clickNotifier.addClickListener(disposableClickListener);
            disposableClickListener.setListenerRegistration(registration);
        }
    }

    private static class DisposableClickListener<C extends Component> implements Disposable, ComponentEventListener<ClickEvent<C>> {
        private final CoreSubscriber subscriber;
        private Registration listenerRegistration;

        private DisposableClickListener(CoreSubscriber<?> subscriber) {
            this.subscriber = subscriber;
        }

        void setListenerRegistration(Registration listenerRegistration) {
            this.listenerRegistration = listenerRegistration;
        }

        @Override
        public void dispose() {
            if (!Objects.isNull(listenerRegistration)) {
                listenerRegistration.remove();
                listenerRegistration = null;
            }
        }

        @Override
        public boolean isDisposed() {
            return Objects.isNull(listenerRegistration);
        }

        @Override
        public void onComponentEvent(ClickEvent<C> event) {
            if (event.isFromClient()) {
                subscriber.onNext(new Object());
            }
        }
    }
}
