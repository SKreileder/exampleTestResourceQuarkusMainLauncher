package com.github.skreileder.exampleTestResourceQuarkusMainLauncher;

import com.github.skreileder.exampleTestResourceQuarkusMainLauncher.converters.ExistingURIConverter;
import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.net.URI;
import java.nio.file.Path;
import java.util.concurrent.Callable;

/**
 * Main entry point for our command line interface, provided by PicoCLIs {@link Command}
 *
 * @author s.kreileder, 13.12.2023
 */
@Command(mixinStandardHelpOptions = true)
public class ConversionCommand implements Callable<Integer>
{

  /**
   * Setting for the {@link Path} to the schema
   */
  @CommandLine.Option(names = {"-s", "--schemaCatalog", "--schemacatalog"},
      description = "The URL to the JSON schema catalog with wich the conversion should happen.",
      converter = ExistingURIConverter.class, required = true)
  private URI schemaPath;

  @Override
  public Integer call()
  {
    return 0;
  }


}
