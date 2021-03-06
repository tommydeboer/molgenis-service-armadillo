package org.molgenis.armadillo;

import com.google.common.collect.ImmutableMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.annotation.PostConstruct;
import org.molgenis.r.RConnectionFactory;
import org.molgenis.r.model.RPackage;
import org.molgenis.r.service.PackageService;
import org.rosuda.REngine.Rserve.RConnection;
import org.springframework.stereotype.Component;

/**
 * Retrieves and combines armadillo options. These are defined by:
 *
 * <ol>
 *   <li>The Options fields in the * installed R packages
 *   <li>The rserve.options application properties
 * </ol>
 */
@Component
public class DataShieldOptionsImpl implements DataShieldOptions {

  private final DataShieldProperties dataShieldProperties;
  private final PackageService packageService;
  private Map<String, String> options;
  private RConnectionFactory rConnectionFactory;

  public DataShieldOptionsImpl(
      DataShieldProperties dataShieldProperties,
      PackageService packageService,
      RConnectionFactory rConnectionFactory) {
    this.dataShieldProperties = dataShieldProperties;
    this.packageService = packageService;
    this.rConnectionFactory = rConnectionFactory;
  }

  @PostConstruct
  public void init() {
    RConnection connection = null;
    try {
      connection = rConnectionFactory.retryCreateConnection();
      options =
          packageService.getInstalledPackages(connection).stream()
              .map(RPackage::options)
              .filter(Objects::nonNull)
              .collect(HashMap::new, Map::putAll, Map::putAll);
      options.putAll(dataShieldProperties.getOptions());
    } finally {
      if (connection != null) {
        connection.close();
      }
    }
  }

  @Override
  public ImmutableMap<String, String> getValue() {
    return ImmutableMap.copyOf(options);
  }
}
