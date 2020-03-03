package org.molgenis.datashield;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.molgenis.datashield.exceptions.DataShieldRequestFailedException;
import org.molgenis.datashield.service.DownloadServiceImpl;
import org.molgenis.r.RConnectionConsumer;
import org.molgenis.r.model.Package;
import org.molgenis.r.model.Table;
import org.molgenis.r.service.PackageService;
import org.molgenis.r.service.RExecutorServiceImpl;
import org.rosuda.REngine.REXPDouble;
import org.rosuda.REngine.REXPNull;
import org.rosuda.REngine.Rserve.RConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = DataController.class)
class DataControllerTest {

  public static Package BASE =
      Package.builder()
          .setName("base")
          .setVersion("3.6.1")
          .setBuilt("3.6.1")
          .setLibPath("/usr/local/lib/R/site-library")
          .build();

  public static Package DESC =
      Package.builder()
          .setName("desc")
          .setVersion("1.2.0")
          .setBuilt("3.6.1")
          .setLibPath("/usr/local/lib/R/site-library")
          .build();

  @Autowired private MockMvc mockMvc;
  @MockBean private DownloadServiceImpl downloadService;
  @MockBean private RExecutorServiceImpl executorService;
  @MockBean private DataShieldSession datashieldSession;
  @MockBean private PackageService packageService;

  @SuppressWarnings({"unchecked"})
  @Test
  @WithMockUser
  void testLoad() throws Exception {
    Table table = mock(Table.class);
    ResponseEntity<Resource> response = mock(ResponseEntity.class);
    Resource resource = mock(Resource.class);
    InputStream csvStream = mock(InputStream.class);
    when(response.getBody()).thenReturn(resource);
    when(resource.getInputStream()).thenReturn(csvStream);
    when(downloadService.getMetadata("project.patients")).thenReturn(table);
    when(downloadService.download(table)).thenReturn(response);
    RConnection rConnection = mockDatashieldSessionConsumer();

    mockMvc.perform(get("/load/project.patients")).andExpect(status().isOk());

    assertAll(
        () -> verify(downloadService).getMetadata("project.patients"),
        () -> verify(downloadService).download(table),
        () -> verify(executorService).assign(csvStream, table, rConnection));
  }

  @Test
  @WithMockUser
  void testGetPackages() throws Exception {
    RConnection rConnection = mockDatashieldSessionConsumer();
    when(packageService.getInstalledPackages(rConnection)).thenReturn(List.of(BASE, DESC));
    mockMvc
        .perform(get("/packages"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(content().json("[{\"name\": \"base\"}, {\"name\": \"desc\"}]"));
  }

  @SuppressWarnings({"unchecked"})
  @Test
  @WithMockUser
  void testLoadFailed() throws Exception {
    Table table = mock(Table.class);
    ResponseEntity<Resource> response = mock(ResponseEntity.class);
    Resource resource = mock(Resource.class);
    InputStream csvStream = mock(InputStream.class);
    when(response.getBody()).thenReturn(resource);
    when(resource.getInputStream()).thenReturn(csvStream);
    when(downloadService.getMetadata("project.patients")).thenReturn(table);
    when(downloadService.download(table)).thenReturn(response);
    RConnection rConnection = mockDatashieldSessionConsumer();
    IOException exception = new IOException("test");
    when(executorService.assign(csvStream, table, rConnection)).thenThrow(exception);

    assertThatThrownBy(() -> mockMvc.perform(get("/load/project.patients")))
        .hasCause(new DataShieldRequestFailedException("test", exception));

    assertAll(
        () -> verify(downloadService).getMetadata("project.patients"),
        () -> verify(downloadService).download(table),
        () -> verify(executorService).assign(csvStream, table, rConnection));
  }

  @Test
  @WithMockUser
  void testExecuteIntResult() throws Exception {
    RConnection rConnection = mockDatashieldSessionConsumer();
    when(executorService.execute("dsMean(D$age)", rConnection)).thenReturn(new REXPDouble(36.6));

    mockMvc
        .perform(post("/execute").contentType(MediaType.TEXT_PLAIN).content("dsMean(D$age)"))
        .andExpect(status().isOk())
        .andExpect(content().string("36.6"));
  }

  @Test
  @WithMockUser
  void testExecuteNullResult() throws Exception {
    RConnection rConnection = mockDatashieldSessionConsumer();
    when(executorService.execute("install.package('dsBase')", rConnection))
        .thenReturn(new REXPNull());

    mockMvc
        .perform(
            post("/execute").contentType(MediaType.TEXT_PLAIN).content("install.package('dsBase')"))
        .andExpect(status().isOk())
        .andExpect(content().string("null"));

    verify(executorService).execute("install.package('dsBase')", rConnection);
  }

  @SuppressWarnings("unchecked")
  private RConnection mockDatashieldSessionConsumer()
      throws org.rosuda.REngine.Rserve.RserveException, org.rosuda.REngine.REXPMismatchException {
    RConnection rConnection = mock(RConnection.class);
    doAnswer(
            answer -> {
              RConnectionConsumer<String> consumer = answer.getArgument(0);
              return consumer.accept(rConnection);
            })
        .when(datashieldSession)
        .execute(any(RConnectionConsumer.class));
    return rConnection;
  }
}