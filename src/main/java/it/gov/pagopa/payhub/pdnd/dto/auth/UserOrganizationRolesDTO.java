package it.gov.pagopa.payhub.pdnd.dto.auth;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class UserOrganizationRolesDTO {

  private String operatorId;
  private String organizationIpaCode;
  @ToString.Exclude
  private String email;
  @Builder.Default
  private List<String> roles = new ArrayList<>();

}

