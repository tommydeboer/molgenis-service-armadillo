package org.molgenis.datashield.r;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RSession;
import org.rosuda.REngine.Rserve.RserveException;
import org.springframework.boot.test.mock.mockito.MockBean;

@ExtendWith(MockitoExtension.class)
class RDatashieldSessionTest {

  @MockBean private RConnectionFactory rConnectionFactory;

  private RDatashieldSession rDatashieldSession;
  @Mock private RConnection rConnection;
  @Mock private RSession rSession;
  @Mock private RConnectionConsumer rConnectionConsumer;

  @BeforeEach
  public void before() {
    this.rDatashieldSession = new RDatashieldSession(rConnectionFactory);
  }

  @SuppressWarnings("unchecked")
  @Test
  void execute() throws REXPMismatchException, RserveException {
    when(rConnectionFactory.getNewConnection(false)).thenReturn(rConnection);
    when(rConnection.detach()).thenReturn(rSession);
    when(rSession.attach()).thenReturn(rConnection);

    rDatashieldSession.execute(rConnectionConsumer);

    assertAll(
        () -> verify(rConnectionConsumer).accept(rConnection), () -> verify(rConnection).detach());
  }

  @Test
  void sessionCleanup() {}
}
