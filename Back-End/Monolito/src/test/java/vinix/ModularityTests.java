package vinix;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;

/** Garante as fronteiras entre módulos (sem ciclos nem acesso a classes internas) e gera a documentação */
class ModularityTests {

  private final ApplicationModules modules = ApplicationModules.of(MonolitoApplication.class);

  @Test
  void verificaEstruturaModular() {
    modules.verify();
  }

  @Test
  void geraDocumentacaoDosModulos() {
    new Documenter(modules).writeModulesAsPlantUml().writeIndividualModulesAsPlantUml();
  }
}
