package io.katharsis.resource.annotations;

import java.lang.annotation.*;

/**
 * Defines a field which will be used to provide links information about a resource
 *
 * @see <a href="http://jsonapi.org/format/#document-resource-objects">JSON API - Resource Objects</a>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface JsonApiLinksInformation {
}
