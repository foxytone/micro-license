package org.neat0n.licensingservice.license.repo;

import org.neat0n.licensingservice.license.model.License;
import org.springframework.data.repository.CrudRepository;

import javax.validation.constraints.Size;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LicenseRepository extends CrudRepository<License, Long> {
    List<License> findByOrganizationId(long organizationId);
    
    Optional<License> findByOrganizationIdAndLicenseId(long organizationId, UUID licenseId);
}
