package com.bootcamptoprod.serverrequestobservationconvention.config;

import io.micrometer.common.KeyValue;
import io.micrometer.common.KeyValues;
import org.springframework.http.server.observation.ServerHttpObservationDocumentation;
import org.springframework.http.server.observation.ServerRequestObservationContext;
import org.springframework.http.server.observation.ServerRequestObservationConvention;
import org.springframework.stereotype.Component;


/**
 * The type Bootcamp to prod custom server request observation convention.
 * Useful for adding tags in controller metrics in addition to default tags.
 */
@Component
public class BootcampToProdCustomServerRequestObservationConvention implements ServerRequestObservationConvention {

    @Override
    public String getName() {
        // Will be used for the metric name
        // We can customize the metric name as per our own requirement
        return "http.server.requests";
    }

    @Override
    public String getContextualName(ServerRequestObservationContext context) {
        // will be used for the trace name
        return "http " + context.getCarrier().getMethod().toLowerCase();
    }

    @Override
    public KeyValues getLowCardinalityKeyValues(ServerRequestObservationContext context) {
        return KeyValues.of(method(context), status(context), exception(context)).and(additionalTags(context));
    }

    // additional tags that we want to add in metrics
    protected KeyValues additionalTags(ServerRequestObservationContext context) {
        KeyValues keyValues = KeyValues.empty();

        // Optional tag which will be present in metrics only when the condition is evaluated to true
        if (context.getCarrier() != null && context.getCarrier().getParameter("user") != null) {
            keyValues = keyValues.and(KeyValue.of("user", context.getCarrier().getParameter("user")));
        }

        // Custom tag which will be present in all the controller metrics
        keyValues = keyValues.and(KeyValue.of("tag", "value"));

        return keyValues;
    }

    // Adding info related to HTTP Method
    protected KeyValue method(ServerRequestObservationContext context) {
        // You should reuse as much as possible the corresponding ObservationDocumentation for key names
        return KeyValue.of(ServerHttpObservationDocumentation.LowCardinalityKeyNames.METHOD, context.getCarrier().getMethod());
    }

    // Adding info related to HTTP Status
    protected KeyValue status(ServerRequestObservationContext context) {
        // You should reuse as much as possible the corresponding ObservationDocumentation for key names
        return KeyValue.of(ServerHttpObservationDocumentation.LowCardinalityKeyNames.STATUS, Integer.toString(context.getResponse().getStatus()));
    }

    // Adding info related to exception
    protected KeyValue exception(ServerRequestObservationContext context) {
        if (context.getError() != null) {
            // You should reuse as much as possible the corresponding ObservationDocumentation for key names
            return KeyValue.of(ServerHttpObservationDocumentation.LowCardinalityKeyNames.EXCEPTION, context.getError().getClass().getName());
        } else {
            return KeyValue.of(ServerHttpObservationDocumentation.LowCardinalityKeyNames.EXCEPTION, "none");
        }
    }

}
