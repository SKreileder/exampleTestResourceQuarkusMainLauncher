package com.github.skreileder.exampleTestResourceQuarkusMainLauncher.integrationtests;

import com.github.skreileder.exampleTestResourceQuarkusMainLauncher.extension.SchemaServer;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.main.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


@QuarkusMainIntegrationTest
@QuarkusTestResource(value = SchemaServer.class)
class Example1
{

  @Test
  void a(QuarkusMainLauncher pLauncher)
  {
    var result = pLauncher.launch("-s", SchemaServer.getSchemaCatalog());
    assertEquals(0, result.exitCode());
  }

  @Test
  @Launch(value = {"-s", SchemaServer.SCHEMA_PATH}, exitCode = 0)
  void b()
  {
  }

  @Test
  void c(QuarkusMainLauncher pLauncher)
  {
    var result = pLauncher.launch("-s", SchemaServer.getSchemaCatalog());
    assertEquals(0, result.exitCode());
  }
}
