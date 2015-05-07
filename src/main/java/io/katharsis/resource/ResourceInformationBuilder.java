package io.katharsis.resource;

import io.katharsis.resource.annotations.JsonApiId;
import io.katharsis.resource.annotations.JsonApiToMany;
import io.katharsis.resource.annotations.JsonApiToOne;
import io.katharsis.resource.exception.ResourceIdNotFoundException;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

/**
 * A builder which creates ResourceInformation instances of a specific class. It extracts information about a resource
 * from annotations.
 */
public class ResourceInformationBuilder {

    public ResourceInformation build(Class<?> resourceClass) {

        ResourceInformation resourceInformation = new ResourceInformation();
        resourceInformation.setResourceClass(resourceClass);
        Field idField = getIdField(resourceClass);
        resourceInformation.setIdField(idField);
        resourceInformation.setAttributeFields(getAttributes(resourceClass, idField));
        resourceInformation.setRelationshipFields(getRelationshipFields(resourceClass, idField));

        return resourceInformation;
    }

    private <T> Field getIdField(Class<T> resourceClass) {
        for (Field field : resourceClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(JsonApiId.class)) {
                return field;
            }
        }
        throw new ResourceIdNotFoundException("Id field for class not found: " + resourceClass.getCanonicalName());
    }

    private <T> Set<Field> getAttributes(Class<T> resourceClass, Field idField) {
        Set<Field> fields = new HashSet<>();
        for (Field field : resourceClass.getDeclaredFields()) {
            if (!isRelationshipType(field) && !field.equals(idField) && !field.isSynthetic()) {
                fields.add(field);
            }
        }
        return fields;
    }

    private <T> Set<Field> getRelationshipFields(Class<T> resourceClass, Field idField) {
        Set<Field> fields = new HashSet<>();
        for (Field field : resourceClass.getDeclaredFields()) {
            if (isRelationshipType(field) && !field.equals(idField)) {
                fields.add(field);
            }
        }
        return fields;
    }

    private boolean isRelationshipType(Field type) {
        return type.isAnnotationPresent(JsonApiToMany.class) || type.isAnnotationPresent(JsonApiToOne.class);
    }
}