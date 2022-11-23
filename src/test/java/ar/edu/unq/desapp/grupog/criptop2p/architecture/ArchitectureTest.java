package ar.edu.unq.desapp.grupog.criptop2p.architecture;

import ar.edu.unq.desapp.grupog.criptop2p.utils.aspects.LogExecutionTime;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.library.Architectures.LayeredArchitecture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.RestController;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

class ArchitectureTest {

    private JavaClasses classes;
    private LayeredArchitecture architecture;

    @BeforeEach
    void setup() {
        classes = new ClassFileImporter()
                .withImportOption(new ImportOption.DoNotIncludeTests())
                .importPackages("ar.edu.unq.desapp.grupog.criptop2p");

        architecture = layeredArchitecture()
                .consideringOnlyDependenciesInLayers()
                .layer("webservices").definedBy("ar.edu.unq.desapp.grupog.criptop2p.webservices")
                .layer("service").definedBy("ar.edu.unq.desapp.grupog.criptop2p.service")
                .layer("persistence").definedBy("ar.edu.unq.desapp.grupog.criptop2p.persistence")
                .layer("model").definedBy("ar.edu.unq.desapp.grupog.criptop2p.model");
    }

    @Test
    @DisplayName("The webservices layer should not be accessed by either the service nor persistence layer")
    void webServicesLayerShouldNotBeAccessedByServiceNorPersistenceLayerTest() {
        architecture.whereLayer("webservices").mayNotBeAccessedByAnyLayer();
        architecture.check(classes);
    }

    @Test
    @DisplayName("The webservices layer should only communicate with service layer")
    void webServicesLayerShouldOnlyCommunicateWithServiceLayerTest() {
        architecture.whereLayer("webservices").mayOnlyAccessLayers("service");
        architecture.check(classes);
    }

    @Test
    @DisplayName("The service layer should only be accessed by either the webservices layer or by another member of the service layer")
    void serviceLayerShouldOnlyBeAccessedByWebServicesOrServiceLayerOnlyTest() {
        architecture.whereLayer("service").mayOnlyBeAccessedByLayers("service", "webservices");
        architecture.check(classes);
    }

    @Test
    @DisplayName("The persistence layer should only be accessed by the service layer")
    void persistenceLayerShouldOnlyBeAccessedByServiceLayerTest() {
        architecture.whereLayer("persistence").mayOnlyBeAccessedByLayers("service");
        architecture.check(classes);
    }

    @Test
    @DisplayName("All web services should have the log execution time annotation in it")
    void allWebServicesShouldHaveTheLogExecutionTimeAnnotationTest() {
        classes()
                .that()
                .resideInAPackage("ar.edu.unq.desapp.grupog.criptop2p.webservices")
                .and()
                .areAnnotatedWith(RestController.class)
                .should()
                .beAnnotatedWith(LogExecutionTime.class)
                .check(classes);
    }

}
