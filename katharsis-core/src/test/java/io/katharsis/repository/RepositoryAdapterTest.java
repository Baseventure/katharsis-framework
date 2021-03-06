package io.katharsis.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import io.katharsis.errorhandling.exception.RepositoryAnnotationNotFoundException;
import io.katharsis.errorhandling.exception.RepositoryMethodException;
import io.katharsis.legacy.internal.AnnotatedRepositoryAdapter;
import io.katharsis.legacy.internal.AnnotatedResourceRepositoryAdapter;
import io.katharsis.legacy.internal.ParametersFactory;
import io.katharsis.legacy.internal.QueryParamsAdapter;
import io.katharsis.legacy.queryParams.QueryParams;
import io.katharsis.legacy.repository.annotations.JsonApiLinks;
import io.katharsis.legacy.repository.annotations.JsonApiMeta;
import io.katharsis.legacy.repository.annotations.JsonApiResourceRepository;
import io.katharsis.module.ModuleRegistry;
import io.katharsis.repository.mock.NewInstanceRepositoryMethodParameterProvider;
import io.katharsis.resource.links.LinksInformation;
import io.katharsis.resource.meta.MetaInformation;
import io.katharsis.resource.mock.models.Project;

public class RepositoryAdapterTest {
    private QueryParams queryParams;
    private ParametersFactory parameterFactory;
	private QueryParamsAdapter queryAdapter;
	
	private ModuleRegistry moduleRegistry = new ModuleRegistry();

    @Before
    public void setUp() throws Exception {
        queryParams = new QueryParams();
        queryAdapter = new QueryParamsAdapter(queryParams);
        parameterFactory = new ParametersFactory(moduleRegistry, new NewInstanceRepositoryMethodParameterProvider());
    }

    @Test
    public void onClassWithNoGetLinksInformationShouldReturnFalse() throws Exception {
        // GIVEN
        ResourceRepositoryWithoutAnyMethods repository = new ResourceRepositoryWithoutAnyMethods();
        SimpleRepositoryAdapter sut = new SimpleRepositoryAdapter(repository, parameterFactory);

        // WHEN
        boolean result = sut.linksRepositoryAvailable();

        // THEN
        assertThat(result).isFalse();
    }

    @Test(expected = RepositoryAnnotationNotFoundException.class)
    public void onClassWithNoGetLinksInformationShouldThrowException() throws Exception {
        // GIVEN
        ResourceRepositoryWithoutAnyMethods repository = new ResourceRepositoryWithoutAnyMethods();
        SimpleRepositoryAdapter sut = new SimpleRepositoryAdapter(repository, parameterFactory);

        // WHEN
        sut.getLinksInformation(Collections.singletonList(new Project()), queryAdapter);
    }

    @Test(expected = RepositoryMethodException.class)
    public void onClassWithInvalidGetLinksInformationShouldThrowException() throws Exception {
        // GIVEN
        ResourceRepositoryWithEmptyGetLinksInformation repo = new ResourceRepositoryWithEmptyGetLinksInformation();
        AnnotatedResourceRepositoryAdapter<Project, Long> sut = new AnnotatedResourceRepositoryAdapter<>(repo, parameterFactory);

        // WHEN
        sut.getLinksInformation(Collections.singletonList(new Project()), queryAdapter);
    }

    @Test
    public void onClassWithGetLinksInformationShouldReturnTrue() throws Exception {
        // GIVEN
        ResourceRepositoryWithGetLinksInformation repo = spy(ResourceRepositoryWithGetLinksInformation.class);
        AnnotatedResourceRepositoryAdapter<Project, Long> sut = new AnnotatedResourceRepositoryAdapter<>(repo, parameterFactory);

        // WHEN
        boolean result = sut.linksRepositoryAvailable();

        // THEN
        assertThat(result).isTrue();
    }

    @Test
    public void onClassWithGetLinksInformationShouldInvokeMethod() throws Exception {
        // GIVEN
        ResourceRepositoryWithGetLinksInformation repo = spy(ResourceRepositoryWithGetLinksInformation.class);
        AnnotatedResourceRepositoryAdapter<Project, Long> sut = new AnnotatedResourceRepositoryAdapter<>(repo, parameterFactory);
        List<Project> resources = Collections.singletonList(new Project());

        // WHEN
        sut.getLinksInformation(resources, queryAdapter);

        // THEN
        verify(repo).getLinksInformation(resources, queryParams, "");
    }

    @Test
    public void onClassWithNoGetMetaInformationShouldReturnFalse() throws Exception {
        // GIVEN
        ResourceRepositoryWithoutAnyMethods repository = new ResourceRepositoryWithoutAnyMethods();
        SimpleRepositoryAdapter sut = new SimpleRepositoryAdapter(repository, parameterFactory);

        // WHEN
        boolean result = sut.metaRepositoryAvailable();

        // THEN
        assertThat(result).isFalse();
    }

    @Test(expected = RepositoryAnnotationNotFoundException.class)
    public void onClassWithNoGetMetaInformationShouldThrowException() throws Exception {
        // GIVEN
        ResourceRepositoryWithoutAnyMethods repository = new ResourceRepositoryWithoutAnyMethods();
        SimpleRepositoryAdapter sut = new SimpleRepositoryAdapter(repository, parameterFactory);

        // WHEN
        sut.getMetaInformation(Collections.singletonList(new Project()), queryAdapter);
    }

    @Test(expected = RepositoryMethodException.class)
    public void onClassWithInvalidGetMetaInformationShouldThrowException() throws Exception {
        // GIVEN
        ResourceRepositoryWithEmptyGetMetaInformation repo = new ResourceRepositoryWithEmptyGetMetaInformation();
        AnnotatedResourceRepositoryAdapter<Project, Long> sut = new AnnotatedResourceRepositoryAdapter<>(repo, parameterFactory);

        // WHEN
        sut.getMetaInformation(Collections.singletonList(new Project()), queryAdapter);
    }

    @Test
    public void onClassWithGetMetaInformationShouldReturnTrue() throws Exception {
        // GIVEN
        ResourceRepositoryWithGetMetaInformation repo = spy(ResourceRepositoryWithGetMetaInformation.class);
        AnnotatedResourceRepositoryAdapter<Project, Long> sut = new AnnotatedResourceRepositoryAdapter<>(repo, parameterFactory);

        // WHEN
        boolean result = sut.metaRepositoryAvailable();

        // THEN
        assertThat(result).isTrue();
    }

    @Test
    public void onClassWithGetMetaInformationShouldInvokeMethod() throws Exception {
        // GIVEN
        ResourceRepositoryWithGetMetaInformation repo = spy(ResourceRepositoryWithGetMetaInformation.class);
        AnnotatedResourceRepositoryAdapter<Project, Long> sut = new AnnotatedResourceRepositoryAdapter<>(repo, parameterFactory);
        List<Project> resources = Collections.singletonList(new Project());

        // WHEN
        sut.getMetaInformation(resources, queryAdapter);

        // THEN
        verify(repo).getMetaInformation(resources, queryParams, "");
    }

    @JsonApiResourceRepository(Project.class)
    public static class ResourceRepositoryWithoutAnyMethods {
    }

    @JsonApiResourceRepository(Project.class)
    public static class ResourceRepositoryWithEmptyGetLinksInformation {

        @JsonApiLinks
        public LinksData getLinksInformation() {
            return new LinksData();
        }
    }

    @JsonApiResourceRepository(Project.class)
    public static class ResourceRepositoryWithGetLinksInformation {

        @JsonApiLinks
        public LinksData getLinksInformation(Iterable<Project> entities, QueryParams queryParams, String someString) {
            return new LinksData();
        }
    }

    @JsonApiResourceRepository(Project.class)
    public static class ResourceRepositoryWithEmptyGetMetaInformation {

        @JsonApiMeta
        public MetaData getMetaInformation() {
            return new MetaData();
        }
    }

    @JsonApiResourceRepository(Project.class)
    public static class ResourceRepositoryWithGetMetaInformation {

        @JsonApiMeta
        public MetaData getMetaInformation(Iterable<Project> entities, QueryParams queryParams, String someString) {
            return new MetaData();
        }
    }

    public static class SimpleRepositoryAdapter extends AnnotatedRepositoryAdapter {
        public SimpleRepositoryAdapter(Object implementationObject, ParametersFactory parametersFactory) {
            super(implementationObject, parametersFactory);
        }
    }

    public static class LinksData implements LinksInformation {
    }

    public static class MetaData implements MetaInformation {
    }
}
