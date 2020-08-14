package it.zanotti.poc.vaadinreactive.portal.transformers;

import it.zanotti.poc.vaadinreactive.portal.model.UIEvent;
import it.zanotti.poc.vaadinreactive.portal.model.UIModel;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;

import java.util.function.Function;

/**
 * Transformer that takes as input a Flux of UIEvents and returns a flux of UIModels.
 * <p>
 * Note that the transformer does not know anything about the UI (the UI uses the transformers, but the transformers are
 * completely independent from the UI).
 *
 * @author Michele Zanotti on 14/08/20
 **/
public interface Transformer<E extends UIEvent<?>, M extends UIModel<?>> extends Function<Flux<E>, Publisher<M>> {
}
