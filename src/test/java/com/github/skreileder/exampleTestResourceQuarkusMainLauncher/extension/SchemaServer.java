package com.github.skreileder.exampleTestResourceQuarkusMainLauncher.extension;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import lombok.*;
import org.eclipse.microprofile.config.ConfigProvider;

import java.io.IOException;
import java.net.URL;
import java.nio.file.*;
import java.util.*;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author s.kreileder, 22.01.2025
 */
public class SchemaServer implements QuarkusTestResourceLifecycleManager
{
  public static final String SCHEMA_CATALOG_CONFIG_PARAM = "schema.server.url";
  public static final String SCHEMA_PATH = "http://localhost:33333/schemas/schemas.json";
  private WireMockServer mockServer;

  @Override
  @SneakyThrows
  public Map<String, String> start()
  {
    mockServer = new WireMockServer(options().port(33333));
    mockServer.start();

    Path schemaFolder = Path.of(getFilePath("schemas"));

    Files.walk(schemaFolder)
        .filter(pPath -> !pPath.toFile().isDirectory())
        .forEach(pFile -> {
          String path = "/" + pFile.getParent().getFileName() + "/" + pFile.getFileName();

          try
          {
            String fileContent = Files.readString(pFile);
            fileContent = fileContent.replace("<serverUrl>", mockServer.baseUrl());

            WireMock.create().port(33333).build().register(
                get(urlEqualTo(path))
                    .willReturn(aResponse()
                                    .withHeader("Content-Type", "application/json")
                                    .withBody(
                                        fileContent
                                    )));
          }
          catch (IOException pE)
          {
            throw new RuntimeException(pE);
          }
        });


    return Collections.singletonMap(SCHEMA_CATALOG_CONFIG_PARAM, mockServer.baseUrl() + "/schemas/schemas.json");
  }

  @Override
  public synchronized void stop()
  {
    if (null != mockServer)
    {
      mockServer.stop();
      mockServer = null;
    }
  }

  public static String getSchemaCatalog()
  {
    return ConfigProvider.getConfig().getValue(SchemaServer.SCHEMA_CATALOG_CONFIG_PARAM, String.class);
  }


  /**
   * Returns the given test file or fails the test if it is not there.
   *
   * @param pFileName the name of the Indexfile
   * @return the path
   */
  @SneakyThrows
  @NonNull
  public static String getFilePath(@NonNull String pFileName)
  {
    ClassLoader classLoader = SchemaServer.class.getClassLoader();
    URL indexFileURL = classLoader.getResource("com/github/skreileder/exampleTestResourceQuarkusMainLauncher/" + pFileName);
    assertNotNull(indexFileURL, "file " + pFileName + " is not there");
    return Path.of(indexFileURL.toURI()).toString();
  }

}
