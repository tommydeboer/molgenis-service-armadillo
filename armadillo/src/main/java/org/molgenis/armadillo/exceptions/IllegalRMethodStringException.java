package org.molgenis.armadillo.exceptions;

public class IllegalRMethodStringException extends RuntimeException {

  public IllegalRMethodStringException(String methodString) {
    super(methodString);
  }
}
