package org.molgenis.datashield.service;

import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toMap;
import static org.molgenis.datashield.r.Formatter.stringVector;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.molgenis.datashield.r.REXPParser;
import org.molgenis.datashield.service.model.Package;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REXPString;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;
import org.springframework.stereotype.Component;

/** Retrieves available {@link Package}s */
@Component
public class PackageService {

  private final REXPParser rexpParser;

  public static final String INSTALLED_PACKAGES =
      format(
          "installed.packages(fields=%s)",
          stringVector("AggregateMethods", "AssignMethods", "Options"));

  public PackageService(REXPParser rexpParser) {
    this.rexpParser = rexpParser;
  }

  public List<Package> getInstalledPackages(RConnection connection)
      throws RserveException, REXPMismatchException {
    REXPString matrix = (REXPString) connection.eval(INSTALLED_PACKAGES);
    List<Map<String, String>> rows = rexpParser.toStringMap(matrix);
    return rows.stream().map(PackageService::toPackage).collect(Collectors.toList());
  }

  public static Package toPackage(Map<String, String> row) {
    Package.Builder builder =
        Package.builder()
            .setName(row.get("Package"))
            .setLibPath(row.get("LibPath"))
            .setVersion(row.get("Version"))
            .setBuilt(row.get("Built"));
    if (row.containsKey("Options")) {
      builder.setOptions(parseOptions(row.get("Options")));
    }
    if (row.containsKey("AssignMethods")) {
      builder.setAssignMethods(parseMethods(row.get("AssignMethods")));
    }
    if (row.containsKey("AggregateMethods")) {
      builder.setAggregateMethods(parseMethods(row.get("AggregateMethods")));
    }
    return builder.build();
  }

  // TODO: check out DataShieldROptionsParser in opal, values can contain commas?
  static ImmutableMap<String, String> parseOptions(String options1) {
    String[] options = options1.split(",");
    Map<String, String> optionsMap =
        stream(options)
            .map(it -> it.split("="))
            .collect(toMap(it -> it[0].trim(), it -> it[1].trim()));
    return ImmutableMap.copyOf(optionsMap);
  }

  static ImmutableSet<String> parseMethods(String aggregateMethods) {
    String[] methods = aggregateMethods.split(",");
    Set<String> methodSet = stream(methods).map(String::trim).collect(Collectors.toSet());
    return ImmutableSet.copyOf(methodSet);
  }
}