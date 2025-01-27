package com.github.skreileder.exampleTestResourceQuarkusMainLauncher.converters;

import picocli.CommandLine;
import picocli.CommandLine.TypeConversionException;

import java.io.IOException;
import java.net.*;

/**
 * TypeConverter that validates, if the given string points to an <b>existing</b> file.
 * If not, a {@link TypeConversionException} gets thrown.
 *
 * @author s.kreileder, 14.12.2023
 */
public class ExistingURIConverter implements CommandLine.ITypeConverter<URI>
{
  @Override
  public URI convert(String pValue)
  {

    URI uri = URI.create(pValue);
    if (!exists(uri))
      throw new TypeConversionException("Specified file '" + pValue + "' does not exist");
    return uri;
  }

  /**
   * Checks if a given {@link URI} exists
   *
   * @param pURI the {@link URI} to check
   * @return true if it exists
   */
  private boolean exists(URI pURI)
  {
    try
    {
      URL url = pURI.toURL();
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("GET");
      connection.setConnectTimeout(5000);
      connection.setReadTimeout(5000);

      int responseCode = connection.getResponseCode();
      return (responseCode >= 200 && responseCode < 400);
    }
    catch (IOException pE)
    {
      throw new TypeConversionException("Could not verify existence of URL: " + pURI);
    }
  }
}
